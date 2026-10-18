package org.firstinspires.ftc.teamcode.BioBuzzRobot;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.DriveTrains.OdoMecDrive;

public class BioBuzzRobot {

    OdoMecDrive drivetrain = new OdoMecDrive();
    Intake intake = new Intake();

    Shooter shooter = new Shooter();

    public void robotInit (HardwareMap hwMap, Telemetry telem) {
        drivetrain.driveInit(hwMap, telem);
        intake.intakeInit(hwMap, telem);



    }




}
