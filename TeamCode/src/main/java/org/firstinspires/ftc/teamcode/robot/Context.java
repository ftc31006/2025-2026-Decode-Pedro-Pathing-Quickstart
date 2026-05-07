package org.firstinspires.ftc.teamcode.robot;

import org.firstinspires.ftc.teamcode.robot.sequencing.Sequence;

public interface Context {
    RampageRobot getRobot();
    void registerSequence(Sequence sequence);
    int getSequenceCount();
    void executeFrame();
}
