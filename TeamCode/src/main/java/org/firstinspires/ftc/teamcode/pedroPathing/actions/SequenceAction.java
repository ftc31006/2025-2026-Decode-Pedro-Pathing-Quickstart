package org.firstinspires.ftc.teamcode.pedroPathing.actions;

import com.pedropathing.follower.Follower;

import org.firstinspires.ftc.teamcode.robot.Context;
import org.firstinspires.ftc.teamcode.robot.Sequence;
import org.firstinspires.ftc.teamcode.robot.ShootSequence;

public class SequenceAction implements Action {
    private final Context context;
    private final Sequence sequence;

    public SequenceAction(Context context, Sequence sequence) {
        this.context = context;
        this.sequence = sequence;
    }

    @Override
    public boolean isComplete() {
        return sequence.hasCompleted();
    }

    @Override
    public void start() {
        context.registerSequence(sequence);
    }

    @Override
    public void update() { }
}
