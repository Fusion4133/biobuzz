package org.firstinspires.ftc.teamcode.StateMachine;

import org.firstinspires.ftc.teamcode.BioBuzzRobot.WaitComponent;

public class StateMachineWaitCommand extends StateMachineCommand
{

    public WaitComponent.WaitCommand command;
    public int waitTime;

    // waiting component is the subsystem that is waiting on something else to finish
    // the wait on component is the subsystem that is finishing before waiting component
    public SystemTypes waitingComponent;
    public SystemTypes waitOnComponent;

    public StateMachineWaitCommand(WaitComponent.WaitCommand iCommand, SystemTypes iWaitingComponent, int iTime)
    {
        command = iCommand;
        systemType = SystemTypes.WAIT;
        waitingComponent = iWaitingComponent;
        waitTime = iTime;
    }

    public StateMachineWaitCommand(WaitComponent.WaitCommand iCommand, SystemTypes iWaitingComponent, SystemTypes iWaitOnComponent)
    {
        command = iCommand;
        systemType = SystemTypes.WAIT;
        waitingComponent = iWaitingComponent;
        waitOnComponent = iWaitOnComponent;
    }

}
