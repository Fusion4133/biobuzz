package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.DriveTrains.OdoMecDrive;

public class BioBuzzRobot {

    OdoMecDrive drivetrain = new OdoMecDrive();



    public void robotInit (HardwareMap hwMap, Telemetry telem) {
        drivetrain.driveInit(hwMap, telem);



    }




}
