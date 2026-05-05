package org.firstinspires.ftc.teamcode.pedroPathing.actions;

import com.pedropathing.follower.Follower;

import org.firstinspires.ftc.teamcode.pedroPathing.Paths;

import java.util.ArrayList;
import java.util.List;

public class ActionSequence {
    private final List<Action> actions;
    private int currentIndex = -1;

    public ActionSequence(List<Action> actions) {
        this.actions = actions;
    }

    public boolean hasCompleted() {
        return currentIndex >= actions.size();
    }

    public void update() {
        if (hasCompleted()) {
            return;
        }

        if (currentIndex < 0) {
            currentIndex = 0;
        } else {
            Action currentAction = actions.get(currentIndex);
            if (!currentAction.isComplete()) {
                currentAction.update();
                return;
            }

            currentIndex++;
        }

        if (hasCompleted()) {
            return;
        }

        actions.get(currentIndex).start();
    }
}
