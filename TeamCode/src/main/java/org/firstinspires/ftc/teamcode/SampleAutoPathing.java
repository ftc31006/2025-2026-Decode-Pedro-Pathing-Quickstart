package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.pedroPathing.Paths;
import org.firstinspires.ftc.teamcode.robot.Context;
import org.firstinspires.ftc.teamcode.robot.sequencing.CompositeSequence;
import org.firstinspires.ftc.teamcode.robot.sequencing.PathChainSequence;
import org.firstinspires.ftc.teamcode.robot.sequencing.PauseSequence;
import org.firstinspires.ftc.teamcode.robot.sequencing.Sequence;

import java.util.ArrayList;
import java.util.List;

@Autonomous
public class SampleAutoPathing extends RampageSequenceOpMode {
    @Override
    protected Sequence getSequence(Context context) {
        Follower follower = context.getRobot().getFollower();
        Paths paths = new Paths(follower);
        follower.setPose(new Pose(47.446, 8.066, Math.toRadians(88)));

        List<Sequence> sequences = new ArrayList<>();
        sequences.add(new PathChainSequence(follower, paths.Path1));
//        sequences.add(new PauseSequence(1000));
//        sequences.add(new PathChainSequence(follower, paths.Path2));
//        sequences.add(new PauseSequence(1000));
//        sequences.add(new PathChainSequence(follower, paths.Path3));
//        sequences.add(new PauseSequence(3000));
//        sequences.add(new PathChainSequence(follower, paths.Path4));

        return new CompositeSequence(sequences);
    }
}
