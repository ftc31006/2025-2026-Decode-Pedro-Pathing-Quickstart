package org.firstinspires.ftc.teamcode.robot.sequencing;

import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robot.Context;
import org.firstinspires.ftc.teamcode.robot.LEDState;
import org.firstinspires.ftc.teamcode.robot.RampageRobot;

import java.util.Objects;

public class PauseSequence implements Sequence {
    private final ElapsedTime timer = new ElapsedTime();
    private final long milliseconds;

    public PauseSequence(long milliseconds) {
        this.milliseconds = milliseconds;
    }

    @Override
    public boolean hasCompleted(Context context) {
        return timer.milliseconds() > milliseconds;
    }

    @Override
    public void start(Context context) {
        timer.reset();
    }

    @Override
    public void executeFrame(Context context) { }
}
