package org.firstinspires.ftc.teamcode.pedroPathing.actions;

import com.pedropathing.follower.Follower;

public interface Action {
    boolean isComplete(Follower follower);
    void start(Follower follower);
}
