package org.firstinspires.ftc.teamcode.StateMachine;

public abstract class StateMachineCommand
{
    public enum SystemTypes
    {
        DRIVE,
        INTAKE,
        SERVO,
        MOTOR,
        SHOOTER,
        WAIT,
        END
    };

    public SystemTypes systemType;
}
