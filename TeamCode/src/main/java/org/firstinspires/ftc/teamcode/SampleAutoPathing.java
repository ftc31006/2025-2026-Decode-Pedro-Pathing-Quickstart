package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.pedropathing.util.Timer;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.pedroPathing.Paths;

@Autonomous
public class SampleAutoPathing extends OpMode {
    private Follower follower;
    private Paths paths;
    private Timer pathTimer, opModeTimer;

    public enum PathState{
        PATH_1,
        PATH_2,
        DONE
    }

    private PathState pathState;

    public void statePathUpdate() {
        switch (pathState) {
            case PATH_1:
                follower.followPath(paths.Path1);
                setPathState(PathState.PATH_2);
                break;
            case PATH_2:
                if (!follower.isBusy()) {
                    telemetry.addLine("Done with Path 1");
                }
                break;
            default:
                telemetry.addLine("No state commanded");
                break;
        }
    }

    public void setPathState(PathState newState) {
        pathState = newState;
        pathTimer.resetTimer();
    }

    @Override
    public void init() {
        pathState = PathState.PATH_1;
        pathTimer = new Timer();
        opModeTimer = new Timer();

        follower = Constants.createFollower(hardwareMap);
        paths = new Paths(follower);
        follower.setPose(new Pose(55.526, 8.474, Math.toRadians(90)));
    }

    public void start() {
        opModeTimer.resetTimer();
        setPathState(pathState);
    }

    @Override
    public void loop(){
        follower.update();
        statePathUpdate();

        telemetry.addData("Path State", pathState.toString());
    }
}
