package org.firstinspires.ftc.teamcode.pedroPathing.actions;

import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;

public class PathChainAction implements Action {
    private PathChain pathChain;

    public PathChainAction(PathChain pathChain) {
        this.pathChain = pathChain;
    }

    @Override
    public boolean isComplete(Follower follower) {
        return !follower.isBusy();
    }

    @Override
    public void start(Follower follower) {
        follower.followPath(pathChain);
    }
}
