package org.firstinspires.ftc.teamcode.robot.sequencing;

import org.firstinspires.ftc.teamcode.robot.Context;

public interface Sequence {
    boolean hasCompleted(Context context);

    void start(Context context);

    void executeFrame(Context context);
}
