package org.firstinspires.ftc.teamcode.BioBuzzRobot;

import com.qualcomm.robotcore.util.ElapsedTime;

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

    public void checkWaitState(StateMachineCommands commandList) {
        switch (currentWaitState) {
            case READY:
                if (commandList.getCurrentType() == StateMachineCommand.SystemTypes.WAIT) {

                    currentWaitCommand = commandList.getWaitCommand();

                    switch (currentWaitCommand.command) {
                        case WAITTIME:
                            waitTime.reset();
                            currentWaitState = WaitState.WAITINGTIME;
                            break;
                        case WAITCOMPONENT:
                            currentWaitState = WaitState.WAITINGCOMPONENT;
                            break;
                    }
                }
                break;

        }
    }
}
