package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

import java.util.ArrayList;
import java.util.List;


public class Paths {
    public PathChain Path1;
    public PathChain Path2;
    public PathChain Path3;
    public PathChain Path4;

    public Paths(Follower follower) {
        Path1 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(56.000, 8.000),

                                new Pose(96.567, 36.000)
                        )
                ).setConstantHeadingInterpolation(Math.toRadians(90))

                .build();

        Path2 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(96.567, 36.000),

                                new Pose(62.209, 57.656)
                        )
                ).setConstantHeadingInterpolation(Math.toRadians(90))
                .build();

        Path3 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(62.209, 57.656),

                                new Pose(87.000, 15.000)
                        )
                ).setTangentHeadingInterpolation()

                .build();

        Path4 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(87.000, 15.000),

                                new Pose(72.387, 71.850)
                        )
                ).setConstantHeadingInterpolation(Math.toRadians(90))

                .build();
    }
}
