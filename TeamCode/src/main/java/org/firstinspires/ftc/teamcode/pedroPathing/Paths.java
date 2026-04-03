package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;



public class Paths {
    public PathChain Path1;
    public PathChain Path2;

    public Paths(Follower follower) {
        Path1 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(55.526, 8.474),

                                new Pose(57.186, 52.369)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(180))

                .build();

        Path2 = follower.pathBuilder().addPath(
                new BezierLine(
                        new Pose(57.186, 52.369),

                        new Pose(102.815, 51.936)
                )
        ).setTangentHeadingInterpolation()

                .build();
    }
}
