package org.firstinspires.ftc.teamcode.robot.sequencing;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robot.Context;
import org.firstinspires.ftc.teamcode.robot.LEDState;
import org.firstinspires.ftc.teamcode.robot.RampageRobot;

import java.util.Objects;

public class LEDSequence implements Sequence {
    public LEDState state;
    public Integer frequency;
    private boolean hasNewValue = false;
    private boolean isOn = false;
    private final ElapsedTime timer = new ElapsedTime();

    public void setState(LEDState state, Integer frequency) {
        if (this.state == state && Objects.equals(this.frequency, frequency)) {
            return;
        }

        this.hasNewValue = true;
        this.state = state;
        this.frequency = frequency;
    }

    @Override
    public boolean hasCompleted(Context context) {
        return false;
    }

    @Override
    public void start(Context context) { }

    @Override
    public void executeFrame(Context context) {
        RampageRobot robot = context.getRobot();

        if (hasNewValue) {
            hasNewValue = false;
            robot.setAprilTagLEDState(state);
            isOn = true;
            timer.reset();
            return;
        }

        if (frequency == null) {
            return;
        }

        if (timer.milliseconds() > frequency) {
            isOn = !isOn;
            robot.setAprilTagLEDState(isOn ? state : LEDState.OFF);
            timer.reset();
        }
    }
}
