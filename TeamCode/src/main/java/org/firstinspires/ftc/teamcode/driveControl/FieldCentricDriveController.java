package org.firstinspires.ftc.teamcode.driveControl;

import org.firstinspires.ftc.teamcode.robot.DriveMotorPower;

public class FieldCentricDriveController implements DriveController {
    public DriveMotorPower calculateDriveMotorPower(DriveControllerContext context) {
        double forwardBack = context.getForwardBack();
        double strafe = context.getStrafe();
        double turn = context.getTurn();
        double botHeading = context.getBotHeading();

        double rotX = strafe * Math.cos(-botHeading) - forwardBack * Math.sin(-botHeading);
        double rotY = strafe * Math.sin(-botHeading) + forwardBack * Math.cos(-botHeading);
        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(turn), 1.0);

        double frontLeftPower = (rotY + rotX + turn) / denominator;
        double backLeftPower = (rotY - rotX + turn) / denominator;
        double frontRightPower = (rotY - rotX - turn) / denominator;
        double backRightPower = (rotY + rotX - turn) / denominator;

        return new DriveMotorPower(frontLeftPower, backLeftPower, frontRightPower, backRightPower);
    }
}
