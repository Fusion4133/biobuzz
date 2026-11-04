package org.firstinspires.ftc.teamcode.BioBuzzRobot;

import org.firstinspires.ftc.teamcode.AutoOptions.AutonomousIntOption;
import org.firstinspires.ftc.teamcode.AutoOptions.AutonomousOptions;
import org.firstinspires.ftc.teamcode.AutoOptions.AutonomousTextOption;
import org.firstinspires.ftc.teamcode.DriveTrains.MecDrive;
import org.firstinspires.ftc.teamcode.StateMachine.StateMachineCommand;
import org.firstinspires.ftc.teamcode.StateMachine.StateMachineCommands;
import org.firstinspires.ftc.teamcode.StateMachine.StateMachineDriveCommand;
import org.firstinspires.ftc.teamcode.Tools.Vector;

public class BioBuzzAutoRobot extends BioBuzzRobot {

    public AutonomousOptions autoOptions = new AutonomousOptions();
    public AutonomousTextOption allianceColor = autoOptions.addTextOption("Alliance", "red", new String[]{"red", "blue"});
    public AutonomousTextOption startPosition = autoOptions.addTextOption("StartPostion", "uptilt", new String[]{"uptilt", "downtilt", "sidewall"});
    public AutonomousIntOption startDelay = autoOptions.addIntOption("Start Delay", 0, 0, 20);
    public AutonomousTextOption firstCollection = autoOptions.addTextOption("First Collection", "garden", new String[]{"garden", "flower 1", "side wall", "flower 2", "none"});
    public AutonomousTextOption secondCollection = autoOptions.addTextOption("Second Collection", "flower 1", new String[]{"garden", "flower 1", "side wall", "flower 2", "none"});
    public AutonomousTextOption thirdCollection = autoOptions.addTextOption("Third Collection", "side wall", new String[]{"garden", "flower 1", "side wall", "flower 2", "none"});
    public AutonomousTextOption fourthCollection = autoOptions.addTextOption("Fourth Collection", "flower 2", new String[]{"garden", "flower 1", "side wall", "flower 2", "none"});

    //TODO Add the rest of autonomous options(ball collect)


    public StateMachineCommands autoCommands;


    public void setupSidewall(){
        drivetrain.initOdoPos(0.0, 180.3, (3.0*Math.PI)/2.0);
//        autoCommands.addCommand(new StateMachineDriveCommand(MecDrive.DriveCommand.DRIVETOPOS, 0.8,  new Vector(15.2,180.3, (3.0*Math.PI)/2.0)));
        autoCommands.addCommand(new StateMachineDriveCommand(MecDrive.DriveCommand.DRIVETOPOS, 0.8,  new Vector(15.2,231.1, (3.0*Math.PI)/2.0)));


    }
    public void setupUpTilt(){
        drivetrain.initOdoPos(121.9, 0.0, 0.0);
    }

    public void checkRobotState(){
        drivetrain.checkDriveState(autoCommands);
        intake.checkIntakeState(autoCommands);
        shooter.checkShooterState(autoCommands);
    }




}
