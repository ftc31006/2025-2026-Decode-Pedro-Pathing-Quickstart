package org.firstinspires.ftc.teamcode.robot.sequencing;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robot.Context;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class CompositeSequence implements Sequence {
    private final Iterator<Sequence> sequences;
    private Sequence currentSequence;

    public CompositeSequence(Sequence... sequences) {
        this.sequences = Arrays.stream(sequences).iterator();
    }

    public CompositeSequence(Iterable<Sequence> sequences) {
        this.sequences = sequences.iterator();
    }

    @Override
    public boolean hasCompleted(Context context) {
        return getActiveSequence(context) == null;
    }

    @Override
    public void start(Context context) {
        // Getting the active sequence forces the first sequence to start.
        getActiveSequence(context);
    }

    @Override
    public void executeFrame(Context context) {
        Sequence activeSequence = getActiveSequence(context);
        if (activeSequence == null) {
            return;
        }
        activeSequence.executeFrame(context);
    }

    private Sequence getActiveSequence(Context context) {
        if (currentSequence == null || currentSequence.hasCompleted(context)) {
            if (!sequences.hasNext()) {
                currentSequence = null;
                return null;
            }
            currentSequence = sequences.next();
            currentSequence.start(context);
        }
        return currentSequence;
    }
}
