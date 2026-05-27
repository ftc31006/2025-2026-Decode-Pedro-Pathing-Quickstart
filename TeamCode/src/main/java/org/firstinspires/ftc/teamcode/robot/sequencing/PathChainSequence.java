package org.firstinspires.ftc.teamcode.robot.sequencing;

import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.robot.Context;

public class PathChainSequence implements Sequence {
    private final Follower follower;
    private final PathChain pathChain;
    private final double maxPower;

    public PathChainSequence(Follower follower, PathChain pathChain) {
        this.follower = follower;
        this.pathChain = pathChain;
        this.maxPower = 1;
    }

    public PathChainSequence(Follower follower, PathChain pathChain, double maxPower) {
        this.follower = follower;
        this.pathChain = pathChain;
        this.maxPower = maxPower;
    }

    @Override
    public boolean hasCompleted(Context context) {
        return !follower.isBusy();
    }

    @Override
    public void start(Context context)
    {
        follower.setMaxPower(1);
        follower.followPath(pathChain);
    }

    @Override
    public void executeFrame(Context context) {
//        if (follower.getDistanceRemaining() > 5) {
  //          follower.setMaxPower(maxPower);
 //       }
    }
}
