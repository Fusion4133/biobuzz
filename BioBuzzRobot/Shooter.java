package org.firstinspires.ftc.teamcode.BioBuzzRobot;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Shooter {
    public DcMotorEx nectarMotor;
    public DcMotorEx pollenMotor;
    public Servo nectarServo;
    public Servo pollenServo;

    public void shooterInit(HardwareMap hwMap, Telemetry telem){
        nectarMotor = hwMap.get(DcMotorEx.class, "nsm");
        pollenMotor = hwMap.get(DcMotorEx.class, "psm");
        nectarServo = hwMap.servo.get("nss");
        pollenServo = hwMap.servo.get("pss");

}
