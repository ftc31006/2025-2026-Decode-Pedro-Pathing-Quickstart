package org.firstinspires.ftc.teamcode;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.field.FieldManager;
import com.bylazar.field.PanelsField;
import com.bylazar.field.Style;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.PoseHistory;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.geometry.CartesianPoint;
import org.firstinspires.ftc.teamcode.geometry.PolarPoint;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import java.util.function.Supplier;

@Configurable
@TeleOp
public class ExampleTeleOp extends OpMode {
    private Follower follower;
    public static Pose startingPose = new Pose(45, 98, Math.toRadians(90)); //See ExampleAuto to understand how to use this
    private boolean automatedDrive;
    private Supplier<PathChain> pathChain;
    private TelemetryManager telemetryM;
    private boolean slowMode = false;
    private double slowModeMultiplier = 0.5;

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startingPose == null ? new Pose() : startingPose);
        follower.update();
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();

        pathChain = () -> follower.pathBuilder() //Lazy Curve Generation
                .addPath(new Path(new BezierLine(follower::getPose, new Pose(45, 98))))
                .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(follower::getHeading, Math.toRadians(90), 0.8))
                .addParametricCallback(0.75, () -> follower.setMaxPower(.5))
                .addParametricCallback(0.99999, () -> follower.setMaxPower(1))
                .build();
    }

    @Override
    public void start() {
        Drawing.init();

        //The parameter controls whether the Follower should use break mode on the motors (using it is recommended).
        //In order to use float mode, add .useBrakeModeInTeleOp(true); to your Drivetrain Constants in Constant.java (for Mecanum)
        //If you don't pass anything in, it uses the default (false)
        follower.startTeleopDrive();
    }

    @Override
    public void loop() {
        //Call this once per loop
        follower.update();
        telemetryM.update();

        if (!automatedDrive) {
            //Make the last parameter false for field-centric
            //In case the drivers want to use a "slowMode" you can scale the vectors

            //This is the normal version to use in the TeleOp
            if (!slowMode) follower.setTeleOpDrive(
                    -gamepad1.left_stick_y,
                    -gamepad1.left_stick_x,
                    -gamepad1.right_stick_x,
                    true // Robot Centric
            );

                //This is how it looks with slowMode on
            else follower.setTeleOpDrive(
                    -gamepad1.left_stick_y * slowModeMultiplier,
                    -gamepad1.left_stick_x * slowModeMultiplier,
                    -gamepad1.right_stick_x * slowModeMultiplier,
                    true // Robot Centric
            );
        }

        //Automated PathFollowing
        if (gamepad1.aWasPressed()) {
            follower.followPath(pathChain.get());
            automatedDrive = true;
        }

        //Stop automated following if the follower is done
        if (automatedDrive && (gamepad1.bWasPressed() || !follower.isBusy())) {
            follower.startTeleopDrive();
            automatedDrive = false;
        }

        //Slow Mode
        if (gamepad1.rightBumperWasPressed()) {
            slowMode = !slowMode;
        }

        //Optional way to change slow mode strength
        if (gamepad1.xWasPressed()) {
            slowModeMultiplier += 0.25;
        }

        //Optional way to change slow mode strength
        if (gamepad2.yWasPressed()) {
            slowModeMultiplier -= 0.25;
        }

        telemetryM.debug("position", follower.getPose());
        telemetryM.debug("velocity", follower.getVelocity());
        telemetryM.debug("automatedDrive", automatedDrive);
        Drawing.drawDebug(follower);
        Drawing.sendPacket();
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

//        public static void drawRobot(Pose pose, Style style) {
//            if (pose == null || Double.isNaN(pose.getX()) || Double.isNaN(pose.getY()) || Double.isNaN(pose.getHeading())) {
//                return;
//            }
//
//            panelsField.setStyle(style);
//            panelsField.moveCursor(pose.getX(), pose.getY());
//            panelsField.circle(ROBOT_RADIUS);
//
//            Vector v = pose.getHeadingAsUnitVector();
//            v.setMagnitude(v.getMagnitude() * ROBOT_RADIUS);
//            double x1 = pose.getX() + v.getXComponent() / 2, y1 = pose.getY() + v.getYComponent() / 2;
//            double x2 = pose.getX() + v.getXComponent(), y2 = pose.getY() + v.getYComponent();
//
//            panelsField.setStyle(style);
//            panelsField.moveCursor(x1, y1);
//            panelsField.line(x2, y2);
//        }

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
                    // Halfway between origin and Front Center
                    new CartesianPoint(0, ROBOT_LENGTH / 4),
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
            panelsField.setStyle(style);
            CartesianPoint firstPoint = transformPoint(points[0], pose);
            panelsField.moveCursor(firstPoint.getX(), firstPoint.getY());

            for (int i = 1; i < points.length; i++) {
                CartesianPoint transformedPoint = transformPoint(points[i], pose);
                panelsField.line(transformedPoint.getX(), transformedPoint.getY());
                panelsField.moveCursor(transformedPoint.getX(), transformedPoint.getY());
            }
        }

        private static CartesianPoint transformPoint(CartesianPoint point, Pose pose) {
            double deltaTheta = pose.getHeadingAsUnitVector().getTheta();
            double deltaX = pose.getX();
            double deltaY = pose.getY();

            PolarPoint pp = point.toPolar();
            PolarPoint transformedPoint = pp.move(0, deltaTheta - Math.toRadians(90));

            return transformedPoint.toCartesian().move(deltaX, deltaY);
        }
    }
}


