package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.pedroPathing.Paths;
import org.firstinspires.ftc.teamcode.pedroPathing.actions.ActionSequence;
import org.firstinspires.ftc.teamcode.pedroPathing.actions.PathChainAction;
import org.firstinspires.ftc.teamcode.pedroPathing.actions.SequenceAction;
import org.firstinspires.ftc.teamcode.robot.Context;
import org.firstinspires.ftc.teamcode.robot.RampageRobot;
import org.firstinspires.ftc.teamcode.robot.ShootSequence;
import org.firstinspires.ftc.teamcode.robot.motors.FlywheelVelocitySettings;

import java.util.List;


@Autonomous(name = "Pedro Pathing Red")
public class PedroPathingRedAuto extends RampageOpMode {
    @Override
    protected void executeOpMode(Context context) {
        RampageRobot robot = context.getRobot();
        robot.setFlywheelVelocity(FlywheelVelocitySettings.Auto);

        Follower follower = Constants.createFollower(hardwareMap);
        Paths paths = new Paths(follower);
        follower.setPose(new Pose(47.446, 8.066, Math.toRadians(88)));

        ActionSequence sequence = new ActionSequence(
            List.of(
                // Drive to shooting position
                new PathChainAction(follower, paths.Path1),
                // Shoot
                new SequenceAction(context, new ShootSequence(3)),
                // Drive to parking position
                new PathChainAction(follower, paths.Path1)
            ));

        while (opModeIsActive() && !sequence.hasCompleted()) {
            follower.update();
            sequence.update();
            context.executeFrame();
        }

        telemetry.update();
    }
}
