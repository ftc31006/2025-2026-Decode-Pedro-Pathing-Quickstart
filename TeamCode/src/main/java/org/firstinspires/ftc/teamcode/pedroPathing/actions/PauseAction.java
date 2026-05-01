package org.firstinspires.ftc.teamcode.pedroPathing.actions;

import com.pedropathing.follower.Follower;
import com.pedropathing.util.Timer;

public class PauseAction implements Action {
    private final Timer timer;
    private final long milliseconds;

    public PauseAction(long milliseconds) {
        this.milliseconds = milliseconds;

        timer = new Timer();
    }

    @Override
    public boolean isComplete(Follower follower) {
        return timer.getElapsedTime() > milliseconds;
    }

    @Override
    public void start(Follower follower) {
        timer.resetTimer();
    }
}
