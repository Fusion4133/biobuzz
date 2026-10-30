package org.firstinspires.ftc.teamcode.StateMachine;


import org.firstinspires.ftc.teamcode.BioBuzzRobot.Shooter;

public class StateMachineShooterCommand extends StateMachineCommand
{

    public Shooter.ShooterCommand command;


    public StateMachineShooterCommand(Shooter.ShooterCommand iCommand)
    {
        command = iCommand;
        systemType = SystemTypes.SHOOTER;
    }

}
