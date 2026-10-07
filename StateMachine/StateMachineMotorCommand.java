package org.firstinspires.ftc.teamcode.StateMachine;

public class StateMachineMotorCommand extends StateMachineCommand {

        public CommandTypes command;
        public double power;
        public double distance;


        public enum CommandTypes {
            MOTORFORWARD,
            MOTORBACKWARD,
            WAIT,
//            WAITSLIDES,
//            WAITSERVO,
//            WAITDRIVE,
            SETPOWER,


        }

        public StateMachineMotorCommand(CommandTypes iCommand, double iPower, double iDistance){
            command = iCommand;
            power = iPower;
            distance = iDistance;
            systemType = SystemTypes.MOTOR;

        }
        public StateMachineMotorCommand(CommandTypes iCommand, double iPower){
            command = iCommand;
            power = iPower;
            systemType = SystemTypes.MOTOR;

        }


}
