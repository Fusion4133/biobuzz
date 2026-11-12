package org.firstinspires.ftc.teamcode.StateMachine;

import org.firstinspires.ftc.teamcode.Tools.Vector;

import java.util.ArrayList;
import java.util.List;

public class StateMachineCommands
{
    public List<StateMachineCommand> stateMachineCommands = new ArrayList<>();
    public int currentOption = 0;

    public StateMachineCommand.SystemTypes getCurrentType ()
    {
        if (currentOption >= stateMachineCommands.size())
        {
            return StateMachineCommand.SystemTypes.END;
        }
        else
        {
            return stateMachineCommands.get(currentOption).systemType;
        }
    }

    public void addCommand(StateMachineCommand command)
    {
        stateMachineCommands.add(command);
    }

    public StateMachineCommand getCommand()
    {
        StateMachineCommand tempCommand = stateMachineCommands.get(currentOption);
        currentOption++;
        return tempCommand;
    }

    public StateMachineDriveCommand getDriveCommand ()
    {
       return (StateMachineDriveCommand) getCommand();
    }

    public StateMachineServoCommand getServoCommand()
    {
       return (StateMachineServoCommand) getCommand();
    }

    public StateMachineMotorCommand getMotorCommand()
    {
        return (StateMachineMotorCommand) getCommand();

    }

    public StateMachineIntakeCommand getIntakeCommand() {
        return (StateMachineIntakeCommand) getCommand();

    }

    public StateMachineShooterCommand getShooterCommand()
    {
        return (StateMachineShooterCommand) getCommand();
    }

    public StateMachineWaitCommand getWaitCommand()
    {
        return (StateMachineWaitCommand) getCommand();
    }

}
