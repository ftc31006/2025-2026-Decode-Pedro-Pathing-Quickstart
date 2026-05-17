package org.firstinspires.ftc.teamcode.robot;

public class DriveMotorPower {
    private final double frontLeftPower;
    private final double backLeftPower;
    private final double frontRightPower;
    private final double backRightPower;

    public DriveMotorPower(double frontLeftPower, double backLeftPower, double frontRightPower, double backRightPower) {
        this.frontLeftPower = frontLeftPower;
        this.backLeftPower = backLeftPower;
        this.frontRightPower = frontRightPower;
        this.backRightPower = backRightPower;
    }

    public double getFrontLeftPower() {
        return frontLeftPower;
    }

    public double getBackLeftPower() {
        return backLeftPower;
    }

    public double getFrontRightPower() {
        return frontRightPower;
    }

    public double getBackRightPower() {
        return backRightPower;
    }
}
