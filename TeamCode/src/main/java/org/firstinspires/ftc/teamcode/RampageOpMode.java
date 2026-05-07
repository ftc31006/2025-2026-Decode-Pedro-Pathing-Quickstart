package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.Context;
import org.firstinspires.ftc.teamcode.robot.RampageRobot;
import org.firstinspires.ftc.teamcode.robot.sequencing.Sequence;
import org.firstinspires.ftc.teamcode.telemetry.TelemetryWriter;
import org.firstinspires.ftc.teamcode.telemetry.TelemetryWriterImpl;

import java.util.ArrayList;
import java.util.List;

public abstract class RampageOpMode extends LinearOpMode {

    @Override
    public void runOpMode() {
        RampageRobot robot = new RampageRobot(this);

        List<Sequence> sequences = new ArrayList<>();
        Context context = new Context() {
            @Override
            public RampageRobot getRobot() {
                return robot;
            }

            @Override
            public void registerSequence(Sequence sequence) {
                sequences.add(sequence);
                sequence.start(this);
            }

            @Override
            public int getSequenceCount() {
                return sequences.size();
            }

            @Override
            public void executeFrame() {
                sequences.removeIf(s -> s.hasCompleted(this));

                for (Sequence sequence: sequences) {
                    sequence.executeFrame(this);
                }
            }
        };
        robot.initialize(context);

        waitForStart();

        executeOpMode(context);
    }

    protected void executeOpMode(Context context) {
        onStart(context);

        while (opModeIsActive() && !hasFinished(context)) {
            context.getRobot().initializeFrame();

            executingFrame(context);

            context.executeFrame();

            processTelemetry(context);
        }
    }

    protected void onStart(Context context) { }

    protected void executingFrame(Context context) { }

    protected boolean hasFinished(Context context) {
        return false;
    }

    protected void writeTelemetry(Context context, TelemetryWriter writer) { }

    private void processTelemetry(Context context) {
        TelemetryWriterImpl writer = new TelemetryWriterImpl(this);
        writeTelemetry(context, writer);
        writer.update();
    }
}
