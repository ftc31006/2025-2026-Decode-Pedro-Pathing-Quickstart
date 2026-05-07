package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.robot.Context;
import org.firstinspires.ftc.teamcode.robot.sequencing.Sequence;

public abstract class RampageSequenceOpMode extends RampageOpMode {
    private Sequence sequence;

    @Override
    protected void executeOpMode(Context context) {
        sequence = getSequence(context);
        context.registerSequence(sequence);
        super.executeOpMode(context);
    }

    @Override
    protected boolean hasFinished(Context context) {
        return sequence.hasCompleted(context);
    }

    protected abstract Sequence getSequence(Context context);
}
