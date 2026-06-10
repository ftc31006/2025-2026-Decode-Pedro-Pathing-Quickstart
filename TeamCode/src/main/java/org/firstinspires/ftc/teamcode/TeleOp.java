package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.geometry.TargetLocator;
import org.firstinspires.ftc.teamcode.robot.Context;
import org.firstinspires.ftc.teamcode.robot.sequencing.FeederSequence;
import org.firstinspires.ftc.teamcode.robot.GlobalState;
import org.firstinspires.ftc.teamcode.robot.sequencing.LEDSequence;
import org.firstinspires.ftc.teamcode.robot.LEDState;
import org.firstinspires.ftc.teamcode.robot.RampageRobot;
import org.firstinspires.ftc.teamcode.robot.sequencing.ShootSequence;
import org.firstinspires.ftc.teamcode.robot.motors.FlywheelVelocitySettings;
import org.firstinspires.ftc.teamcode.telemetry.TelemetryWriter;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.Locale;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp")
public class TeleOp extends RampageOpMode {
    private final Pose startingPose = new Pose(45, 98, Math.toRadians(90)); //See ExampleAuto to understand how to use this
    private final TargetLocator targetLocator = new TargetLocator(22);
    private final LEDSequence ledSequence = new LEDSequence();
    private FeederSequence feederSequence = null;
    private String distance;
    private final double autoAimTurnSpeed = .2;
    private boolean automatedDrive = false;
    private boolean robotCentric = false;

    @Override
    protected void init(Context context) {
        RampageRobot robot = context.getRobot();
        robot.getFollower().setStartingPose(startingPose == null ? new Pose() : startingPose);
    }

    @Override
    protected void onStart(Context context) {
        RampageRobot robot = context.getRobot();

        context.registerSequence(ledSequence);
        robot.setFlywheelVelocity(FlywheelVelocitySettings.Default);
        robot.setShotDistanceLEDState(LEDState.GREEN);
        distance = "Near";
    }

    @Override
    protected void executingFrame(Context context) {
        RampageRobot robot = context.getRobot();
        Follower follower = robot.getFollower();

        if (gamepad1.startWasPressed()) {
            robotCentric = !robotCentric;
        }

        if (!automatedDrive) {
            Double turnOverride = updateAutoAimingDetails(context);
            updateDriveMotorPower(context, turnOverride);
        }

        if (gamepad1.aWasPressed()) {
            PathChain pathChain = buildPath(follower);
            follower.followPath(pathChain);
            automatedDrive = true;
        }

        if (automatedDrive && (gamepad1.bWasPressed() || !follower.isBusy())) {
            follower.startTeleopDrive();
            automatedDrive = false;
        }

        if (gamepad2.leftBumperWasPressed() ){
            robot.setFlywheelVelocity(FlywheelVelocitySettings.Default);
            robot.setShotDistanceLEDState(LEDState.GREEN);
            distance = "Near";
        }

        if (gamepad2.rightBumperWasPressed()) {
            robot.setFlywheelVelocity(FlywheelVelocitySettings.Far);
            robot.setShotDistanceLEDState(LEDState.RED);
            distance = "Far";
        }

        if (gamepad2.yWasPressed() || gamepad1.yWasPressed()) {
            cancelFeederSequence();
        }

        if (gamepad2.aWasPressed()) {
            initiateShootSequence(context, 1);
        }

        if (gamepad2.bWasPressed()) {
            initiateShootSequence(context, 3);
        }
    }

    @Override
    protected void writeTelemetry(Context context, TelemetryWriter writer) {
        RampageRobot robot = context.getRobot();

        writer.write("Feeder State", robot.getFeederState());
        writer.write("Feeder Home Position", GlobalState.FeederHomePosition);
        writer.write("Feeder Current Position", robot.getFeederPosition());
        writer.write("Sequence Count", context.getSequenceCount());
        writer.write("Automated Drive", automatedDrive);

        writer.write("Distance", distance);

        AprilTagDetection detection = robot.getClosestTagById(20, 24);
        if (detection != null) {
            if (detection.metadata != null) {
                telemetry.addLine(String.format(Locale.US, "\n==== (ID %d) %s", detection.id, detection.metadata.name));
                telemetry.addLine(String.format(Locale.US, "XYZ %6.1f %6.1f %6.1f  (cm)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                telemetry.addLine(String.format(Locale.US, "PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                telemetry.addLine(String.format(Locale.US, "RBE %6.1f %6.1f %6.1f  (cm, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
            } else {
                telemetry.addLine(String.format(Locale.US, "\n==== (ID %d) Unknown", detection.id));
                telemetry.addLine(String.format(Locale.US, "Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
            }
        }
    }

    private Double updateAutoAimingDetails(Context context) {
        RampageRobot robot = context.getRobot();

        LEDState aprilTagState = LEDState.OFF;
        Double turn = null;
        Integer frequency = null;

        AprilTagDetection detection = robot.getClosestTagById(20, 24);
        if (detection != null && detection.metadata != null) {
            double angle = targetLocator.getAngle(detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.yaw);

            aprilTagState = LEDState.RED;

            if (Math.abs(angle) < 3) {
                aprilTagState = LEDState.GREEN;
                if (gamepad1.x) {
                    frequency = 100;
                    turn = 0.0;
                }
            } else if (gamepad1.x) {
                turn = angle < 0 ? -autoAimTurnSpeed : autoAimTurnSpeed;
                frequency = 100;
            }
        }

        ledSequence.setState(aprilTagState, frequency);

        return turn;
    }

    private void updateDriveMotorPower(Context context, Double turnOverride) {
        RampageRobot robot = context.getRobot();

        double powerMultiplier = gamepad1.right_bumper ? .35 : 1;
        robot.getFollower().setTeleOpDrive(
                -gamepad1.left_stick_y * powerMultiplier,
                -gamepad1.left_stick_x * powerMultiplier,
                turnOverride == null ? -gamepad1.right_stick_x * powerMultiplier : turnOverride,
                robotCentric
        );
    }

    private PathChain buildPath(Follower follower) {
        return follower.pathBuilder() //Lazy Curve Generation
                .addPath(new Path(new BezierLine(follower::getPose, new Pose(45, 98))))
                .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(follower::getHeading, Math.toRadians(90), 0.8))
                .build();
    }

    private void initiateShootSequence(Context context, int count) {
        cancelFeederSequence();
        feederSequence = new ShootSequence(count);
        context.registerSequence(feederSequence);
    }

    private void cancelFeederSequence() {
        if (feederSequence == null) {
            return;
        }

        feederSequence.cancel();
        feederSequence = null;
    }
}
