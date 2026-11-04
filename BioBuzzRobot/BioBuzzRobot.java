package org.firstinspires.ftc.teamcode.BioBuzzRobot;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.DriveTrains.OdoMecDrive;
import org.firstinspires.ftc.teamcode.StateMachine.StateMachineCommands;

public class BioBuzzRobot {

    public OdoMecDrive drivetrain = new OdoMecDrive();
   public Intake intake = new Intake();
   public Shooter shooter = new Shooter();

    public void robotInit (HardwareMap hwMap, Telemetry telem) {
        drivetrain.driveInit(hwMap, telem);
        intake.intakeInit(hwMap, telem);
        shooter.shooterInit(hwMap, telem);


    }




}
