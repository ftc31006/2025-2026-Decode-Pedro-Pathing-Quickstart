package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

import java.util.ArrayList;
import java.util.List;






public class Paths {
    public PathChain Path1;

    public Paths(Follower follower) {
        Path1 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(47.446, 8.066),
                                new Pose(46.898, 15.453),
                                new Pose(100.000, 44.125),
                                new Pose(-10.000, 77.727),
                                new Pose(56.461, 90.386)
                        )
                ).setTangentHeadingInterpolation()

                .build();
    }
}


