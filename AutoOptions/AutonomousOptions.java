package org.firstinspires.ftc.teamcode.AutoOptions;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.ArrayList;
import java.util.List;

public class AutonomousOptions {
    public List<AutonomousOption> autoOptions = new ArrayList<>();
    public int currentOption = 0;

    private void addOption(AutonomousOption op) {
        autoOptions.add(op);
    }

    public AutonomousBooleanOption addBooleanOption(String opName, Boolean startVal){
        AutonomousBooleanOption tempOption = new AutonomousBooleanOption(opName, startVal);

        addOption(tempOption);

        return tempOption;
    }

    public AutonomousIntOption addIntOption(String opName, int startVal, int lowVal, int highVal){
        AutonomousIntOption tempOption = new AutonomousIntOption(opName, startVal, lowVal, highVal);

        addOption(tempOption);

        return tempOption;
    }

    public AutonomousTextOption addTextOption(String opName, String startVal, String[] allowedVals){
        AutonomousTextOption tempOption = new AutonomousTextOption(opName, startVal, allowedVals);

        addOption(tempOption);

        return tempOption;
    }

    public void showOptions (LinearOpMode op){
        int index = 0;
        String str = "";

        while (index < autoOptions.size() && !op.opModeIsActive() && !op.isStopRequested()){
            switch (autoOptions.get(index).optionType){
                case STRING:
                    str = ((AutonomousTextOption)autoOptions.get(index)).getValue();
                    break;
                case INT:
                    str = Integer.toString(((AutonomousIntOption)autoOptions.get(index)).getValue());
                    break;
                case BOOLEAN:
                    str = String.valueOf(((AutonomousBooleanOption)autoOptions.get(index)).getValue());
                    break;
            }

            if (index == currentOption){
                op.telemetry.addData(Integer.toString(index) + ") ==> " + autoOptions.get(index).name,str);
            } else {
                op.telemetry.addData(Integer.toString(index) + ")     " + autoOptions.get(index).name, str);
            }

            index = index + 1;
        }
        op.telemetry.update();
    }

    public void selectOptions (LinearOpMode op) {
        boolean aPressed = false;
        boolean yPressed = false;
        boolean bPressed = false;
        boolean xPressed = false;

        while (currentOption < autoOptions.size()){
            showOptions(op);

            if (op.gamepad1.a && !aPressed) {
                currentOption = currentOption + 1;
            }
            aPressed = op.gamepad1.a;

            if (op.gamepad1.y && !yPressed) {
                currentOption = currentOption - 1;
            }
            yPressed = op.gamepad1.y;

            if (op.gamepad1.b && !bPressed) {
                autoOptions.get(currentOption).nextValue();
            }
            bPressed = op.gamepad1.b;

            if (op.gamepad1.x && !xPressed) {
                autoOptions.get(currentOption).previousValue();
            }
            xPressed = op.gamepad1.x;

            op.telemetry.update();
            Thread.yield();
        }

        op.telemetry.addData("Robot","READY!!");
        op.telemetry.update();
    }
}
