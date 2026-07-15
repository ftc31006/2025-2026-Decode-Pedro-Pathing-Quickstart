package org.firstinspires.ftc.teamcode;

import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PIDFController;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.driveControl.DriveController;
import org.firstinspires.ftc.teamcode.driveControl.DriveControllerContext;
import org.firstinspires.ftc.teamcode.driveControl.FieldCentricDriveController;
import org.firstinspires.ftc.teamcode.driveControl.RobotCentricDriveController;
import org.firstinspires.ftc.teamcode.geometry.TargetLocator;
import org.firstinspires.ftc.teamcode.robot.Context;
import org.firstinspires.ftc.teamcode.robot.DriveMotorPower;
import org.firstinspires.ftc.teamcode.robot.sequencing.FeederSequence;
import org.firstinspires.ftc.teamcode.robot.GlobalState;
import org.firstinspires.ftc.teamcode.robot.sequencing.LEDSequence;
import org.firstinspires.ftc.teamcode.robot.LEDState;
import org.firstinspires.ftc.teamcode.robot.RampageRobot;
import org.firstinspires.ftc.teamcode.robot.sequencing.ShootSequence;
import org.firstinspires.ftc.teamcode.robot.motors.FlywheelVelocitySettings;
import org.firstinspires.ftc.teamcode.telemetry.TelemetryWriter;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.List;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp")
public class TeleOp extends RampageOpMode {
    private final Pose startingPose = new Pose(85, 8.5, Math.toRadians(90)); //See ExampleAuto to understand how to use this
    private final TargetLocator targetLocator = new TargetLocator(22);
    private final LEDSequence ledSequence = new LEDSequence();
    private FeederSequence feederSequence = null;
    private String distance;
    private final double autoAimTurnSpeed = .3;
    private final PIDFController autoAimController = new PIDFController(new PIDFCoefficients(.0000001, 0, 0, 0));
    private boolean automatedDrive;
    private boolean robotCentric = false;
    private final List<DriveController> allDriveControllers = List.of(new FieldCentricDriveController(), new RobotCentricDriveController());
    private DriveController driveController = allDriveControllers.get(0);
    private int driveControllerIndex = 0;
    private final double TargetX = 135;
    private final double TargetY = 135;

    @Override
    protected void onStart(Context context) {
        RampageRobot robot = context.getRobot();
        robot.getFollower().setStartingPose(startingPose == null ? new Pose() : startingPose);

        context.registerSequence(ledSequence);
        robot.setFlywheelVelocity(FlywheelVelocitySettings.Default);
        robot.setShotDistanceLEDState(LEDState.GREEN);
        distance = "Near";

        robot.getFollower().startTeleOpDrive();
    }

    @Override
    protected void executingFrame(Context context) {
        RampageRobot robot = context.getRobot();
        Follower follower = robot.getFollower();

        if (gamepad1.startWasPressed()) {
            nextDriveController();
            robotCentric = !robotCentric;
        }

        if (gamepad1.aWasPressed()) {
            autoAimController.reset();
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
        Follower follower = robot.getFollower();
        Pose pose = follower.getPose();

//        writer.write("Feeder State", robot.getFeederState());
//        writer.write("Feeder Home Position", GlobalState.FeederHomePosition);
//        writer.write("Feeder Current Position", robot.getFeederPosition());
//        writer.write("Sequence Count", context.getSequenceCount());
//
//        DriveMotorPower driveMotorPower = robot.getDriveMotorPower();
//        writer.write("Front Left Wheel Power", driveMotorPower.getFrontLeftPower());
//        writer.write("Front Right Wheel Power", driveMotorPower.getFrontRightPower());
//        writer.write("Back Left Wheel Power", driveMotorPower.getBackLeftPower());
//        writer.write("Back Right Wheel Power", driveMotorPower.getBackRightPower());
//        writer.write("Drive Controller Index", driveControllerIndex);

//        writer.write("Distance", distance);
        writer.write("Heading", pose.getHeading());
        writer.write("X", pose.getX());
        writer.write("Y", pose.getY());
        writer.write("a", a);
        writer.write("isAutoAimingWorking",isAutoAimingWorking);
        writer.write("b", b);
        writer.write("targetAngle", TA);
        AprilTagDetection detection = robot.getClosestTagById(20, 24);
        if (detection != null) {
            if (detection.metadata != null) {
                telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (cm)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (cm, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
            } else {
                telemetry.addLine(String.format("\n==== (ID %d) Unknown", detection.id));
                telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
            }
        }
    }

    private String isAutoAimingWorking;
    private String TA;
    private String a;
    private String b;

    private Double updateAutoAimingDetails(Context context) {
        if (!gamepad1.x){
            isAutoAimingWorking = "false";
            return null;
        }

        RampageRobot robot = context.getRobot();
        Pose pose = robot.getFollower().getPose();
        LEDState aprilTagState = LEDState.OFF;
        Double turn = null;
        Integer frequency = null;
        double a =  (TargetX-pose.getX());
        this.a = Double.toString(a);
        double b = (TargetY-pose.getY());
        this.b = Double.toString(b);
        double targetAngle = Math.atan(b/a);
        TA = Double.toString(Math.toDegrees(targetAngle));
        double angleDifference = Math.toDegrees(pose.getHeading()-targetAngle);
        isAutoAimingWorking = Double.toString(angleDifference);
        if(Math.abs(angleDifference)<3){
            return 0.0;
        }

        if(angleDifference < 0) {
            turn = -autoAimTurnSpeed;
        } else {
            turn = autoAimTurnSpeed;
        }

//        AprilTagDetection detection = robot.getClosestTagById(20, 24);
//        if (detection != null && detection.metadata != null) {
//            double angle = targetLocator.getAngle(detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.yaw);
//
//            aprilTagState = LEDState.RED;
//
//            if (Math.abs(angle) < 3) {
//                aprilTagState = LEDState.GREEN;
//                if (gamepad1.x) {
//                    frequency = 100;
//                    turn = 0.0;
//                }
//            } else if (gamepad1.x) {
//                turn = angle < 0 ? -autoAimTurnSpeed : autoAimTurnSpeed;
//                frequency = 100;
//            }
//        }

        ledSequence.setState(aprilTagState, frequency);

        return turn;
    }

    private void updateDriveMotorPower(Context context, Double turnOverride) {
        RampageRobot robot = context.getRobot();
//        IMU imu = robot.getImu();

        double powerMultiplier = gamepad1.right_bumper ? .35 : 1;
        double turn = turnOverride == null ? gamepad1.right_stick_x * powerMultiplier : turnOverride;
        robot.getFollower().setTeleOpDrive(
                -gamepad1.left_stick_y * powerMultiplier,
                -gamepad1.left_stick_x * powerMultiplier,
                -turn,
                robotCentric
        );

//        DriveControllerContext driveControllerContext = new DriveControllerContext(
//                -gamepad1.left_stick_y,
//                gamepad1.left_stick_x,
//                turnOverride == null ? gamepad1.right_stick_x * powerMultiplier : turnOverride,
//                imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS)
//        );

//        DriveMotorPower driveMotorPower = driveController.calculateDriveMotorPower(driveControllerContext);

//        double frontLeftPower = driveMotorPower.getFrontLeftPower() * powerMultiplier;
//        double backLeftPower = driveMotorPower.getBackLeftPower() * powerMultiplier;
//        double frontRightPower = driveMotorPower.getFrontRightPower() * powerMultiplier;
//        double backRightPower = driveMotorPower.getBackRightPower() * powerMultiplier;
//
//        robot.setDriveMotorPower(frontLeftPower, frontRightPower, backLeftPower, backRightPower);
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

    private void nextDriveController() {
        int currentIndex = allDriveControllers.indexOf(driveController);
        driveControllerIndex = (currentIndex + 1) % allDriveControllers.size();
        driveController = allDriveControllers.get(driveControllerIndex);
    }
}
