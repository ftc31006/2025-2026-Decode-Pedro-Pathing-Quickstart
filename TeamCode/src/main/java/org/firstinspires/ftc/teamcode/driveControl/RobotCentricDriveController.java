package org.firstinspires.ftc.teamcode.driveControl;

import org.firstinspires.ftc.teamcode.robot.DriveMotorPower;

public class RobotCentricDriveController implements DriveController {
    public DriveMotorPower calculateDriveMotorPower(DriveControllerContext context) {
        double forwardBack = context.getForwardBack();
        double strafe = context.getStrafe();
        double turn = context.getTurn();

        double frontLeftPower = forwardBack + strafe + turn;
        double frontRightPower = forwardBack - strafe - turn;
        double backLeftPower = forwardBack - strafe + turn;
        double backRightPower = forwardBack + strafe - turn;

        return new DriveMotorPower(frontLeftPower, backLeftPower, frontRightPower, backRightPower);
    }
}
