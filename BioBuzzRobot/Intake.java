package org.firstinspires.ftc.teamcode.BioBuzzRobot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.DriveTrains.MecDrive;
import org.firstinspires.ftc.teamcode.StateMachine.StateMachineCommand;
import org.firstinspires.ftc.teamcode.StateMachine.StateMachineCommands;
import org.firstinspires.ftc.teamcode.StateMachine.StateMachineIntakeCommand;

public class Intake {
    public DcMotor intakeMotor;
    public IntakeState currentIntakeState = IntakeState.READY;
    public StateMachineIntakeCommand currentIntakeCommand;

    public static final double COLLECT_MOTOR_SPEED = 1.0;

    public static final double EJECT_MOTOR_SPEED = -1.0;
    public enum IntakeCommand {
        STOP,
        COLLECT,
        EJECT
    }

    public enum IntakeState {
        READY,
        COLLECTING,
        EJECTING
    }


    public void checkIntakeState(StateMachineCommands commandList) {
        //TODO let the orchestrator (BioBuzzRobot) handle end of file and next commands
        //if (commandList.currentOption >= commandList.stateMachineCommands.size() && currentDriveState == MecDrive.DriveState.STOPPED) {
        // break;
        // }

        switch (currentIntakeState) {
            case READY:
                //waitingOnDrive = false;
                if (commandList.currentOption < commandList.stateMachineCommands.size() &&
                        commandList.getCurrentType() == StateMachineCommand.SystemTypes.INTAKE) {

                    currentIntakeCommand = commandList.getIntakeCommand();
                    commandList.currentOption++;

                    switch (currentIntakeCommand.command) {
                        case COLLECT:
                            intakeMotor.setPower(COLLECT_MOTOR_SPEED);
                            break;
                        case EJECT:
                            intakeMotor.setPower(EJECT_MOTOR_SPEED);
                            break;
                        case STOP:
                            intakeMotor.setPower(0.0);
                            break;

                    }
                }
        }
    }



    public void intakeInit(HardwareMap hwMap, Telemetry telem) {
        intakeMotor = hwMap.dcMotor.get("im");


    }


}
