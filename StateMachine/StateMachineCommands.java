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
        return stateMachineCommands.get(currentOption);
    }

    public StateMachineDriveCommand getDriveCommand ()
    {
       return (StateMachineDriveCommand) stateMachineCommands.get(currentOption);
    }

    public StateMachineServoCommand getServoCommand()
    {
       return (StateMachineServoCommand) stateMachineCommands.get(currentOption);
    }

    public StateMachineMotorCommand getMotorCommand()
    {
        return (StateMachineMotorCommand) stateMachineCommands.get(currentOption);

    }

    public StateMachineIntakeCommand getIntakeCommand() {
        return (StateMachineIntakeCommand) stateMachineCommands.get(currentOption);

    }



   // public StateMachineShooterCommand getShooterCommand()
    {
  //     return (StateMachineShooterCommand) stateMachineCommands.get(currentOption);
    }

}
