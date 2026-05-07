package org.firstinspires.ftc.teamcode.robot.sequencing;

import org.firstinspires.ftc.teamcode.robot.Context;
import org.firstinspires.ftc.teamcode.robot.RampageRobot;

public class ShootSequence implements FeederSequence {
    private int count;
    private boolean requiresInitialization = true;
    private boolean isCancelled = false;

    public ShootSequence(int count) {
        this.count = count + 1;
    }

    public void cancel() {
        isCancelled = true;
    }

    @Override
    public boolean hasCompleted(Context context) {
        return count <= 0;
    }

    @Override
    public void start(Context context) { }

    @Override
    public void executeFrame(Context context) {
        if (hasCompleted(context)) {
            return;
        }

        RampageRobot robot = context.getRobot();

        if (isCancelled) {
            robot.openFeeder();
            count = 0;
            return;
        }

        if (requiresInitialization) {
            robot.openFeeder();
            requiresInitialization = false;
        }

        switch (robot.getFeederState()) {
            case OPEN:
                count--;
                if (!hasCompleted(context)) {
                    robot.closeFeeder();
                }
                break;
            case CLOSED:
                robot.openFeeder();
                break;
        }
    }
}
