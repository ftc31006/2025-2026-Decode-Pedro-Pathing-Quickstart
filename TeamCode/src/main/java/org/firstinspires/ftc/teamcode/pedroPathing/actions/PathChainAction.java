package org.firstinspires.ftc.teamcode.pedroPathing.actions;

import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;

public class PathChainAction implements Action {
    private final Follower follower;
    private final PathChain pathChain;

    public PathChainAction(Follower follower, PathChain pathChain)
    {
        this.follower = follower;
        this.pathChain = pathChain;
    }

    @Override
    public boolean isComplete() {
        return !follower.isBusy();
    }

    @Override
    public void start() {
        follower.followPath(pathChain);
    }

    @Override
    public void update() { }
}
