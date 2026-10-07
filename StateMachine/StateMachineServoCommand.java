package org.firstinspires.ftc.teamcode.StateMachine;

public class StateMachineServoCommand extends StateMachineCommand {
    public ServoCommandOptions command;
    public double position;
    public int time;


    public enum ServoCommandOptions{
        LEFTSORTER,
        RIGHTSORTER,
        BALLSTOPPER,
        WAIT
    }

    public StateMachineServoCommand(ServoCommandOptions iCommand, double iPosition){
        command = iCommand;
        position = iPosition;
        systemType = SystemTypes.SERVO;
    }
    public StateMachineServoCommand(ServoCommandOptions iCommand, int iTime){
        command = iCommand;
        time = iTime;
        systemType = SystemTypes.SERVO;
    }


}
