package org.firstinspires.ftc.teamcode.driveControl;

public class DriveControllerContext {
    private final double forwardBack;
    private final double strafe;
    private final double turn;
    private final double botHeading;

    public DriveControllerContext(double forwardBack, double strafe, double turn, double botHeading) {
        this.forwardBack = forwardBack;
        this.strafe = strafe;
        this.turn = turn;
        this.botHeading = botHeading;
    }

    public double getForwardBack() {
        return forwardBack;
    }

    public double getStrafe() {
        return strafe;
    }

    public double getTurn() {
        return turn;
    }

    public double getBotHeading() {
        return botHeading;
    }
}
