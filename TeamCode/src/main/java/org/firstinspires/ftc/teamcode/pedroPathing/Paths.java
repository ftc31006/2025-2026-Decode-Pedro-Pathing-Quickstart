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
    public PathChain Path2;

    public Paths(Follower follower) {
        Path1 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(77.575, 8.303),
                                new Pose(79.249, 89.339),
                                new Pose(98.702, 108.119)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(39))

                .build();

        Path2 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(98.702, 108.119),
                                new Pose(94.045, 69.730),
                                new Pose(126.937, 70.043)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(39), Math.toRadians(0))

                .build();
    }
}











//        Path2 = follower.pathBuilder().addPath(
//                        new BezierLine(
//                                new Pose(103.209, 114.287),
//
//                                new Pose(102.722, 68.560)
//                        )
//                ).setLinearHeadingInterpolation(Math.toRadians(39), Math.toRadians(90))
//
//                .build();



