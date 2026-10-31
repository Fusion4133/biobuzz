package org.firstinspires.ftc.teamcode.BioBuzzRobot;

import org.firstinspires.ftc.teamcode.AutoOptions.AutonomousIntOption;
import org.firstinspires.ftc.teamcode.AutoOptions.AutonomousOptions;
import org.firstinspires.ftc.teamcode.AutoOptions.AutonomousTextOption;
import org.firstinspires.ftc.teamcode.DriveTrains.MecDrive;
import org.firstinspires.ftc.teamcode.StateMachine.StateMachineCommand;
import org.firstinspires.ftc.teamcode.StateMachine.StateMachineCommands;
import org.firstinspires.ftc.teamcode.StateMachine.StateMachineDriveCommand;
import org.firstinspires.ftc.teamcode.StateMachine.StateMachineIntakeCommand;
import org.firstinspires.ftc.teamcode.StateMachine.StateMachineShooterCommand;
import org.firstinspires.ftc.teamcode.Tools.Vector;

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
        drivetrain.initOdoPos(0.0, 180.3, (3.0*Math.PI)/2.0);
//        autoCommands.addCommand(new StateMachineDriveCommand(MecDrive.DriveCommand.DRIVETOPOS, 0.8,  new Vector(15.2,180.3, (3.0*Math.PI)/2.0)));
        autoCommands.addCommand(new StateMachineDriveCommand(MecDrive.DriveCommand.DRIVETOPOS, 0.8,  new Vector(15.2,231.1, (3.0*Math.PI)/2.0)));


    }
    public void setupUpTilt(){
        drivetrain.initOdoPos(121.9, 0.0, 0.0);
        autoCommands.addCommand(new StateMachineShooterCommand(Shooter.ShooterCommand.TURNON));
        //wait for shooter to be ready to shoot
        autoCommands.addCommand(new StateMachineShooterCommand(Shooter.ShooterCommand.SHOOT));
        // wait 2 seconds for balls to shoot before stopping
        autoCommands.addCommand(new StateMachineShooterCommand(Shooter.ShooterCommand.STOP));
        autoCommands.addCommand(new StateMachineDriveCommand(MecDrive.DriveCommand.DRIVETOPOS, 0.8, new Vector(45.5, 76.5, 0)));
        // wait till we drive to postition to turn on intake
        autoCommands.addCommand(new StateMachineIntakeCommand(Intake.IntakeCommand.COLLECT));
        // wait for 1 second for intake to collect balls
        autoCommands.addCommand(new StateMachineShooterCommand(Shooter.ShooterCommand.TURNON));
        autoCommands.addCommand(new StateMachineDriveCommand(MecDrive.DriveCommand.DRIVETOPOS, 0.8, new Vector(44.7, 243.9, (3.0*Math.PI)/2.0 )));
        autoCommands.addCommand(new StateMachineDriveCommand(MecDrive.DriveCommand.DRIVETOPOS, 0.8, new Vector(144.8, 348.0, (3.0*Math.PI)/2.0)));
        //wait for robot to get to postition before shooting
        autoCommands.addCommand(new StateMachineShooterCommand(Shooter.ShooterCommand.SHOOT));
        // wait 1/2 second to shoot all balls
        autoCommands.addCommand(new StateMachineShooterCommand(Shooter.ShooterCommand.STOP));
        autoCommands.addCommand(new StateMachineDriveCommand(MecDrive.DriveCommand.DRIVETOPOS, 0.8, new Vector( 144.8, 332.5, (3.0*Math.PI)/2.0));
        autoCommands.addCommand(new StateMachineDriveCommand(MecDrive.DriveCommand.DRIVETOPOS, 0.8, new Vector(61.0, 182.9, (3.0*Math.PI)/2.0)));

    }






}
