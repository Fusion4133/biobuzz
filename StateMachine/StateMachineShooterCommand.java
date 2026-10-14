package org.firstinspires.ftc.teamcode.StateMachine;

import org.firstinspires.ftc.teamcode.Shooter;

public class StateMachineShooterCommand extends StateMachineCommand
{

    public Shooter.ShooterCommand command;
    public double rpm;

    public StateMachineShooterCommand(Shooter.ShooterCommand iCommand, double iRpm )
    {
        command = iCommand;
        rpm = iRpm;
        systemType = SystemTypes.SHOOTER;
    }

}
