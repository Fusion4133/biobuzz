package org.firstinspires.ftc.teamcode.BioBuzzRobot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.StateMachine.StateMachineCommand;
import org.firstinspires.ftc.teamcode.StateMachine.StateMachineCommands;
import org.firstinspires.ftc.teamcode.StateMachine.StateMachineIntakeCommand;

public class Intake {
    public DcMotor intakeMotor;
    public IntakeState currentIntakeState = IntakeState.STOPPED;
    public StateMachineIntakeCommand currentIntakeCommand;
    public enum IntakeCommand {
        STOP,
        COLLECT,
        EJECT
    }

    public enum IntakeState {
        STOPPED,
        COLLECTING,
        EJECTING
    }


    public void checkIntakeState(StateMachineCommands autoCommands, StateMachineCommand.SystemTypes nextCommandType){

        switch (currentIntakeState) {
            case STOPPED:
                if (nextCommandType == StateMachineCommand.SystemTypes.INTAKE){

                    currentIntakeCommand = autoCommands.getIntakeCommand();
                   // robot.stateMachineOptions.currentIntakeOption++;

                }
                break;

            case EJECTING:

                break;
            case COLLECTING:

                break;
        }





    }

    public void intakeInit(HardwareMap hwMap, Telemetry telem) {
        intakeMotor = hwMap.dcMotor.get("im");


    }


}
