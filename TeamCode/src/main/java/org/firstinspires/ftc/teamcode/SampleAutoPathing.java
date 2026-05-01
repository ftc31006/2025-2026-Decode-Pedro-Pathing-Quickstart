package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.pedropathing.util.Timer;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.pedroPathing.PathCollection;
import org.firstinspires.ftc.teamcode.pedroPathing.Paths;
import org.firstinspires.ftc.teamcode.pedroPathing.actions.ActionSequence;

@Autonomous
public class SampleAutoPathing extends OpMode {
    private Follower follower;
//    private PathCollection paths;
    private ActionSequence actionSequence;
//    private Timer pathTimer, opModeTimer;

//    private int currentPathIndex;

//    public void statePathUpdate() {
//        if (follower.isBusy()) {
//            return;
//        }
//
//        if (currentPathIndex >= paths.AllPaths.size()) {
//            return;
//        }
//
//        follower.followPath(paths.AllPaths.get(currentPathIndex));
//        currentPathIndex++;
//    }

//    public void setPathState(int index) {
//        currentPathIndex = index;
//        pathTimer.resetTimer();
//    }

    @Override
    public void init() {
//        currentPathIndex = 0;
//        pathTimer = new Timer();
//        opModeTimer = new Timer();

        follower = Constants.createFollower(hardwareMap);
//        paths = new PathCollection(follower);
        follower.setPose(new Pose(55.526, 8.474, Math.toRadians(90)));

        actionSequence = new ActionSequence(follower);
    }

//    public void start() {
//        opModeTimer.resetTimer();
//        setPathState(0);
//    }

    @Override
    public void loop(){
        actionSequence.update();

//        statePathUpdate();

//        telemetry.addData("Current Index", currentPathIndex);
    }
}
