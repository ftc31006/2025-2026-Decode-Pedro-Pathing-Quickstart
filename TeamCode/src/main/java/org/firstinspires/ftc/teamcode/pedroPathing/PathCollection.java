package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;

import java.util.ArrayList;
import java.util.List;

public class PathCollection {
    public List<PathChain> AllPaths;

    public PathCollection(Follower follower)
    {
        Paths paths=new Paths(follower);
        AllPaths=new ArrayList<>();
        AllPaths.add(paths.Path1);
        AllPaths.add(paths.Path2);
//        AllPaths.add(paths.Path3);
//        AllPaths.add(paths.Path4);
//        AllPaths.add(paths.Path5);
//        AllPaths.add(paths.Path6);
//        AllPaths.add(paths.Path7);
//        AllPaths.add(paths.Path8);
//        AllPaths.add(paths.Path9);
//        AllPaths.add(paths.Path10);
    }
}

