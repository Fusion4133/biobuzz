package org.firstinspires.ftc.teamcode.BioBuzzRobot;

import org.firstinspires.ftc.teamcode.AutoOptions.AutonomousIntOption;
import org.firstinspires.ftc.teamcode.AutoOptions.AutonomousOptions;
import org.firstinspires.ftc.teamcode.AutoOptions.AutonomousTextOption;
import org.firstinspires.ftc.teamcode.StateMachine.StateMachineCommand;
import org.firstinspires.ftc.teamcode.StateMachine.StateMachineCommands;

public class BioBuzzAutoRobot extends BioBuzzRobot {

    public AutonomousOptions autoOptions = new AutonomousOptions();
    public AutonomousTextOption allianceColor = autoOptions.addTextOption("Alliance", "red", new String[]{"red", "blue"});
    public AutonomousTextOption startPosition = autoOptions.addTextOption("StartPostion", "uptilt", new String[]{"uptilt", "downtilt", "sidewall"});

    public AutonomousIntOption startDelay = autoOptions.addIntOption("Start Delay", 0, 0, 20);

    //TODO Add the rest of autonomous options(ball collect)


    public StateMachineCommands autoCommands;

    public void checkRobotStates(){
        // getCurrentType means to figure out what "subsystem" of  the robot the next command is
        StateMachineCommand.SystemTypes currentType = autoCommands.getCurrentType();

        intake.checkIntakeState(autoCommands, currentType);
    }

    public void setupSidewall(){
        autoCommands.addCommand();


    }






}
