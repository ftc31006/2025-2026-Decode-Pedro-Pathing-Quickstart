package org.firstinspires.ftc.teamcode;

import com.bylazar.field.FieldManager;
import com.bylazar.field.PanelsField;
import com.bylazar.field.Style;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.math.Vector;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.PoseHistory;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.geometry.CartesianPoint;
import org.firstinspires.ftc.teamcode.geometry.PolarPoint;
import org.firstinspires.ftc.teamcode.robot.Context;
import org.firstinspires.ftc.teamcode.robot.RampageRobot;
import org.firstinspires.ftc.teamcode.robot.sequencing.Sequence;
import org.firstinspires.ftc.teamcode.telemetry.TelemetryWriter;
import org.firstinspires.ftc.teamcode.telemetry.TelemetryWriterImpl;

import java.util.ArrayList;
import java.util.List;

public abstract class RampageOpMode extends LinearOpMode {

    @Override
    public void runOpMode() {
        RampageRobot robot = new RampageRobot(this);

        List<Sequence> sequences = new ArrayList<>();
        Context context = new Context() {
            @Override
            public RampageRobot getRobot() {
                return robot;
            }

            @Override
            public void registerSequence(Sequence sequence) {
                sequences.add(sequence);
                sequence.start(this);
            }

            @Override
            public int getSequenceCount() {
                return sequences.size();
            }

            @Override
            public void executeFrame() {
                sequences.removeIf(s -> s.hasCompleted(this));

                for (Sequence sequence : sequences) {
                    sequence.executeFrame(this);
                }
            }
        };
        robot.initialize(context);

        waitForStart();

        executeOpMode(context);
    }

    protected void executeOpMode(Context context) {
        onStart(context);
        Drawing.init();

        while (opModeIsActive() && !hasFinished(context)) {
            context.getRobot().initializeFrame();

            executingFrame(context);

            context.executeFrame();

            processTelemetry(context);

//            Drawing.drawRobot(context.getRobot().getFollower().getPose());
            Drawing.drawDebug(context.getRobot().getFollower());
            Drawing.sendPacket();
        }
    }

    protected void onStart(Context context) {
    }

    protected void executingFrame(Context context) {
    }

    protected boolean hasFinished(Context context) {
        return false;
    }

    protected void writeTelemetry(Context context, TelemetryWriter writer) {
    }

    private void processTelemetry(Context context) {
        TelemetryWriterImpl writer = new TelemetryWriterImpl(this);
        writeTelemetry(context, writer);
        writer.update();
    }


    private static class Drawing {
        public static final double ROBOT_RADIUS = 9; // woah
        public static final double ROBOT_LENGTH = 17.5;
        public static final double ROBOT_WIDTH = 13;
        private static final FieldManager panelsField = PanelsField.INSTANCE.getField();

        private static final Style robotLook = new Style(
                "", "#3F51B5", 0.75
        );
        private static final Style historyLook = new Style(
                "", "#4CAF50", 0.75
        );

        /**
         * This prepares Panels Field for using Pedro Offsets
         */
        public static void init() {
            panelsField.setOffsets(PanelsField.INSTANCE.getPresets().getPEDRO_PATHING());
        }

        /**
         * This draws everything that will be used in the Follower's telemetryDebug() method. This takes
         * a Follower as an input, so an instance of the DashbaordDrawingHandler class is not needed.
         *
         * @param follower Pedro Follower instance.
         */
        public static void drawDebug(Follower follower) {
            if (follower.getCurrentPath() != null) {
                drawPath(follower.getCurrentPath(), robotLook);
                Pose closestPoint = follower.getPointFromPath(follower.getCurrentPath().getClosestPointTValue());
                drawRobot(new Pose(closestPoint.getX(), closestPoint.getY(), follower.getCurrentPath().getHeadingGoal(follower.getCurrentPath().getClosestPointTValue())), robotLook);
            }
            drawPoseHistory(follower.getPoseHistory(), historyLook);
            drawRobot(follower.getPose(), historyLook);

            sendPacket();
        }

        /**
         * This draws a robot at a specified Pose with a specified
         * look. The heading is represented as a line.
         *
         * @param pose  the Pose to draw the robot at
         * @param style the parameters used to draw the robot with
         */
        public static void drawRobot(Pose pose, Style style) {
            if (pose == null || Double.isNaN(pose.getX()) || Double.isNaN(pose.getY()) || Double.isNaN(pose.getHeading())) {
                return;
            }

            CartesianPoint[] cornerPoints = new CartesianPoint[] {
                    // Front Left
                    new CartesianPoint(-ROBOT_WIDTH / 2, ROBOT_LENGTH / 2),
                    // Front Right
                    new CartesianPoint(ROBOT_WIDTH / 2, ROBOT_LENGTH / 2),
                    // Back Right
                    new CartesianPoint(ROBOT_WIDTH / 2, -ROBOT_LENGTH / 2),
                    // Back Left
                    new CartesianPoint(-ROBOT_WIDTH / 2, -ROBOT_LENGTH / 2),
                    // Return to Front Left to Close the Loop
                    new CartesianPoint(-ROBOT_WIDTH / 2, ROBOT_LENGTH / 2)
            };

            transformAndDrawLines(pose, style, cornerPoints);

            CartesianPoint[] headingPoints = new CartesianPoint[] {
                    // Origin
                    new CartesianPoint(0, 0),
                    // Front Center
                    new CartesianPoint(0, ROBOT_LENGTH / 2)
            };

            transformAndDrawLines(pose, style, headingPoints);
        }

        /**
         * This draws a robot at a specified Pose. The heading is represented as a line.
         *
         * @param pose the Pose to draw the robot at
         */
        public static void drawRobot(Pose pose) {
            drawRobot(pose, robotLook);
        }

        /**
         * This draws a Path with a specified look.
         *
         * @param path  the Path to draw
         * @param style the parameters used to draw the Path with
         */
        public static void drawPath(Path path, Style style) {
            double[][] points = path.getPanelsDrawingPoints();

            for (int i = 0; i < points[0].length; i++) {
                for (int j = 0; j < points.length; j++) {
                    if (Double.isNaN(points[j][i])) {
                        points[j][i] = 0;
                    }
                }
            }

            panelsField.setStyle(style);
            panelsField.moveCursor(points[0][0], points[0][1]);
            panelsField.line(points[1][0], points[1][1]);
        }

        /**
         * This draws all the Paths in a PathChain with a
         * specified look.
         *
         * @param pathChain the PathChain to draw
         * @param style     the parameters used to draw the PathChain with
         */
        public static void drawPath(PathChain pathChain, Style style) {
            for (int i = 0; i < pathChain.size(); i++) {
                drawPath(pathChain.getPath(i), style);
            }
        }

        /**
         * This draws the pose history of the robot.
         *
         * @param poseTracker the PoseHistory to get the pose history from
         * @param style       the parameters used to draw the pose history with
         */
        public static void drawPoseHistory(PoseHistory poseTracker, Style style) {
            panelsField.setStyle(style);

            int size = poseTracker.getXPositionsArray().length;
            for (int i = 0; i < size - 1; i++) {

                panelsField.moveCursor(poseTracker.getXPositionsArray()[i], poseTracker.getYPositionsArray()[i]);
                panelsField.line(poseTracker.getXPositionsArray()[i + 1], poseTracker.getYPositionsArray()[i + 1]);
            }
        }

        /**
         * This draws the pose history of the robot.
         *
         * @param poseTracker the PoseHistory to get the pose history from
         */
        public static void drawPoseHistory(PoseHistory poseTracker) {
            drawPoseHistory(poseTracker, historyLook);
        }

        /**
         * This tries to send the current packet to FTControl Panels.
         */
        public static void sendPacket() {
            panelsField.update();
        }

        private static void transformAndDrawLines(Pose pose, Style style, CartesianPoint[] points) {
            double deltaTheta = pose.getHeadingAsUnitVector().getTheta();
            double deltaX = pose.getX();
            double deltaY = pose.getY();

            panelsField.setStyle(style);
            CartesianPoint firstPoint = points[points.length - 1];
            panelsField.moveCursor(firstPoint.X, firstPoint.Y);

            for (int i = 1; i < points.length; i++) {
                CartesianPoint cp = points[i];
                PolarPoint pp = cp.toPolar();
                pp.move(0, deltaTheta);

                CartesianPoint transformedPoint = pp.toCartesian().move(deltaX, deltaY);
                panelsField.line(transformedPoint.X, transformedPoint.Y);
            }
        }
    }
}