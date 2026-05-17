package org.firstinspires.ftc.teamcode.driveControl;

import org.firstinspires.ftc.teamcode.robot.DriveMotorPower;

public interface DriveController {
    DriveMotorPower calculateDriveMotorPower(DriveControllerContext context);
}
