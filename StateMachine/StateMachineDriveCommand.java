package org.firstinspires.ftc.teamcode.StateMachine;

import org.firstinspires.ftc.teamcode.DriveTrains.MecDrive;
import org.firstinspires.ftc.teamcode.Tools.Vector;

public class StateMachineDriveCommand extends StateMachineCommand {

    public MecDrive.DriveCommand command;
    public double power;
    public double distance;
    public Vector startVector;
    public int tag;
    public Vector finalVector;
    public boolean ramp;

    public StateMachineDriveCommand(MecDrive.DriveCommand iCommand, double iPower, double iDistance){
        command = iCommand;
        power = iPower;
        distance = iDistance;
        systemType = SystemTypes.DRIVE;
        ramp = false;
    }

    public StateMachineDriveCommand(MecDrive.DriveCommand iCommand, double iPower, double iDistance, Vector iVector){
        command = iCommand;
        power = iPower;
        distance = iDistance;
        startVector = iVector;
        systemType = SystemTypes.DRIVE;
        ramp = false;
    }

    public StateMachineDriveCommand(MecDrive.DriveCommand iCommand, double iPower, double iDistance, Vector iVector, boolean rampDown){
        command = iCommand;
        power = iPower;
        ramp = rampDown;
        distance = iDistance;
        startVector = iVector;
        systemType = SystemTypes.DRIVE;
    }

    public StateMachineDriveCommand(MecDrive.DriveCommand iCommand, double iPower, double iDistance, int iTag){
        command = iCommand;
        power = iPower;
        distance = iDistance;
        tag = iTag;
        systemType = SystemTypes.DRIVE;
        ramp = false;
    }

    public StateMachineDriveCommand(MecDrive.DriveCommand iCommand, double iPower, Vector iFinalVector){
        command = iCommand;
        power = iPower;
        finalVector = iFinalVector;
        systemType = SystemTypes.DRIVE;
        ramp = false;
    }

}
