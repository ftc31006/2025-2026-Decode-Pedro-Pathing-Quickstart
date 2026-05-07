package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pedroPathing.Paths;
import org.firstinspires.ftc.teamcode.robot.Context;
import org.firstinspires.ftc.teamcode.robot.RampageRobot;
import org.firstinspires.ftc.teamcode.robot.sequencing.CompositeSequence;
import org.firstinspires.ftc.teamcode.robot.sequencing.PathChainSequence;
import org.firstinspires.ftc.teamcode.robot.sequencing.Sequence;
import org.firstinspires.ftc.teamcode.robot.sequencing.ShootSequence;
import org.firstinspires.ftc.teamcode.robot.motors.FlywheelVelocitySettings;

@Autonomous(name = "Pedro Pathing Red")
public class PedroPathingRedAuto extends RampageSequenceOpMode {
    @Override
    protected void onStart(Context context) {
        RampageRobot robot = context.getRobot();
        robot.setFlywheelVelocity(FlywheelVelocitySettings.Auto);

        Follower follower = robot.getFollower();
        follower.setPose(new Pose(47.446, 8.066, Math.toRadians(88)));
    }

    @Override
    protected Sequence getSequence(Context context) {
        RampageRobot robot = context.getRobot();
        Follower follower = robot.getFollower();
        Paths paths = new Paths(follower);

        return new CompositeSequence(
            new PathChainSequence(follower, paths.Path1),
            new ShootSequence(3),
            new PathChainSequence(follower, paths.Path1)
        );
    }
}
