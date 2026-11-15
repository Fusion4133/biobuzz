package org.firstinspires.ftc.teamcode.BioBuzzRobot;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.DriveTrains.MecDrive;
import org.firstinspires.ftc.teamcode.StateMachine.StateMachineCommand;
import org.firstinspires.ftc.teamcode.StateMachine.StateMachineCommands;
import org.firstinspires.ftc.teamcode.StateMachine.StateMachineWaitCommand;

public class WaitComponent
{
    public enum WaitCommand {
        WAITTIME,
        WAITCOMPONENT
    }

    public enum WaitState {
        READY,
        WAITINGTIME,
        WAITINGCOMPONENT
    }

    public WaitState currentWaitState = WaitState.READY;
    public StateMachineWaitCommand currentWaitCommand;

    public ElapsedTime waitTime = new ElapsedTime();

    public void setComponentStates(BioBuzzAutoRobot robot, String state, StateMachineWaitCommand waitCommand) {

        if (state.equals("wait")) {

            switch (waitCommand.waitingComponent) {
                case DRIVE:
                    robot.drivetrain.currentDriveState = MecDrive.DriveState.WAITING;
                    break;
                case INTAKE:
                    robot.intake.currentIntakeState = Intake.IntakeState.WAITING;
                    break;
                case SHOOTER:
                    robot.shooter.currentShooterState = Shooter.ShooterState.WAITING;
                    break;
            }

        }
        else {
            switch (waitCommand.waitingComponent) {
                case DRIVE:
                    robot.drivetrain.currentDriveState = MecDrive.DriveState.STOPPED;
                    break;
                case INTAKE:
                    robot.intake.currentIntakeState = Intake.IntakeState.READY;
                    break;
                case SHOOTER:
                    robot.shooter.currentShooterState = Shooter.ShooterState.READYTOSHOOT;
                    break;
                    //Ignoring STOPPED because there is never a case where we are waiting on a STOPPED shooter
            }

        }
    }



    public void checkWaitState(StateMachineCommands commandList, BioBuzzAutoRobot robot) {
        switch (currentWaitState) {
            case READY:
                if (commandList.getCurrentType() == StateMachineCommand.SystemTypes.WAIT) {

                    currentWaitCommand = commandList.getWaitCommand();

                    switch (currentWaitCommand.command) {
                        case WAITTIME:
                            waitTime.reset();
                            currentWaitState = WaitState.WAITINGTIME;
                            setComponentStates(robot, "wait", currentWaitCommand);

                            break;
                        case WAITCOMPONENT:
                            currentWaitState = WaitState.WAITINGCOMPONENT;
                            setComponentStates(robot, "wait", currentWaitCommand);

                            break;
                    }
                }
                break;

            case WAITINGTIME:
                if (waitTime.milliseconds() >= currentWaitCommand.waitTime) {
                    currentWaitState = WaitState.READY;
                    setComponentStates(robot, "ready", currentWaitCommand);

                }
                break;

            case WAITINGCOMPONENT:
                if ((currentWaitCommand.waitOnComponent == StateMachineCommand.SystemTypes.DRIVE && robot.drivetrain.currentDriveState == MecDrive.DriveState.STOPPED) ||
                        (currentWaitCommand.waitOnComponent == StateMachineCommand.SystemTypes.INTAKE && robot.intake.currentIntakeState == Intake.IntakeState.READY) ||
                        (currentWaitCommand.waitOnComponent == StateMachineCommand.SystemTypes.SHOOTER && robot.shooter.currentShooterState == Shooter.ShooterState.STOPPED) ||
                        (currentWaitCommand.waitOnComponent == StateMachineCommand.SystemTypes.SHOOTER && robot.shooter.currentShooterState == Shooter.ShooterState.READYTOSHOOT)){
                    currentWaitState = WaitState.READY;
                    setComponentStates(robot, "ready", currentWaitCommand);
                }
                break;
        }
    }
}
