package org.firstinspires.ftc.teamcode.pedroPathing.actions;

import com.pedropathing.follower.Follower;

import org.firstinspires.ftc.teamcode.pedroPathing.Paths;

import java.util.ArrayList;
import java.util.List;

public class ActionSequence {
    private final Follower follower;
    private final List<Action> actions;
    private int currentIndex = -1;

    public ActionSequence(Follower follower) {
        this.follower = follower;
        Paths paths = new Paths(follower);

        actions = new ArrayList<>();
        actions.add(new PathChainAction((paths.Path1)));
        actions.add(new PauseAction(3000));
        actions.add(new PathChainAction((paths.Path2)));
        actions.add(new PauseAction(3000));
        actions.add(new PathChainAction((paths.Path3)));
        actions.add(new PauseAction(3000));
        actions.add(new PathChainAction((paths.Path4)));
    }

    public void update() {
        follower.update();

        if (currentIndex < 0) {
            currentIndex = 0;
        } else {
            if (!actions.get(currentIndex).isComplete(follower)) {
                return;
            }

            currentIndex++;
        }

        if (currentIndex >= actions.size()) {
            return;
        }

        actions.get(currentIndex).start(follower);
    }
}
