package org.firstinspires.ftc.teamcode.DriveTrains;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;


public class MecDrive {

    public enum DriveCommand {
        DRIVEFORWARD,
        DRIVEBACKWARD,
        TURNRIGHT,
        TURNLEFT,
        STRAFERIGHT,
        STRAFELEFT,
        DRIVETOTAG,
        WAIT,
        WAITSLIDES,
        WAITSHOOTER,
        WAITSERVO,
        DRIVETOPOS,
        DRIVEABOUTPOS,
        DRIVETIME

    }

    public DcMotor leftFront, leftBack, rightFront, rightBack;

    public DcMotor primaryDriveEncoder;

    public Telemetry telem;


    public double leftX = 0;
    public double leftY = 0;
    public  double rightX = 0;


    public int driveEncTargetPos;
    public int rampDownTargetPos;
    public int rampDownDist = 50;
    double finalDrivePower = 0.2;

    public BNO055IMU imu;

    double ticksPerInch = 51; //560(encoder ticks) / (Math.PI * 3.5);

    public DriveState currentDriveState = DriveState.STOPPED;


    public enum DriveDirection {
        FORWARD,
        BACKWARD
    }

    public enum TurnDirection {
        RIGHTSPIN,
        LEFTSPIN,
        RIGHTPOINT,
        LEFTPOINT
    }

    public enum DriveState {
        STOPPED,
        DRIVINGFORWARD,
        DRIVINGBACKWARD,
        TURNINGLEFT,
        TURNINGRIGHT,
        STRAFINGRIGHT,
        STRAFINGLEFT,
        DRIVINGTOTAG,
        WAITING,
        WAITINGSERVO,
        WAITINGSLIDES,
        WAITINGSHOOTER,
        DRIVINGT0POS,
        DRIVINGABOUTPOS
    }

    public enum StrafeDirection {
        RIGHTSTRAFE,
        LEFTSTRAFE
    }

    public void driveInit(HardwareMap hwMap, Telemetry telem) {
        this.telem = telem;

        leftBack = hwMap.dcMotor.get("lb");
        leftFront = hwMap.dcMotor.get("lf");
        rightBack = hwMap.dcMotor.get("rb");
        rightFront = hwMap.dcMotor.get("rf");

        primaryDriveEncoder = rightFront; //set to whatever you want to check

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);

        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;
    }

    public void setDrivePowers (double lFront,double lBack,double rFront, double rBack) {
        leftFront.setPower(lFront);
        rightFront.setPower(rFront);
        leftBack.setPower(lBack);
        rightBack.setPower(rBack);
    }

    public void stopDriveMotors(){
        setDrivePowers(0.0, 0.0, 0.0, 0.0);
        currentDriveState = DriveState.STOPPED;
    }

    public float getTurnDegrees(float startHeading, float currentHeading){
        float traveledDegrees = currentHeading - startHeading;
        if (traveledDegrees > 180){
            traveledDegrees= traveledDegrees -360;
        }
        else if (traveledDegrees < -180){
            traveledDegrees = traveledDegrees+ 360;
        }
        return Math.abs(traveledDegrees);
    }

    public void robotCentricDrive(double xIn, double yIn, double hIn, boolean driveModeHigh) {
        double powerFactor = 1;
        if (driveModeHigh == false){
            powerFactor = 0.5;
        }

        yIn = yIn*1.1;
        double denominator = Math.max(Math.abs(xIn)+ Math.abs(yIn) + Math.abs(hIn), 1.0);
        leftFront.setPower((hIn + (yIn + xIn))*(powerFactor)/denominator);
        leftBack.setPower((hIn + (yIn - xIn))*(powerFactor)/denominator);
        rightFront.setPower((-hIn + (yIn - xIn))*(powerFactor)/denominator);
        rightBack.setPower((-hIn + (yIn + xIn))*(powerFactor)/denominator);
    }
    public void fieldCentricDrive(double xImput, double yImput, double turnImput, double thetaImput, boolean driveModeHigh){

        double x;
        if (xImput == 0){
            x = 0.0001;
        }else {
            x = xImput;
        }

        double driveTheta = Math.atan(yImput / x);

        if (xImput < 0 ){
            driveTheta += Math.PI;
        }

        double drivePower = Math.sqrt(Math.pow(xImput, 2) + Math.pow(yImput, 2));

        double power = Math.sqrt(Math.pow(drivePower, 2) + Math.pow(turnImput, 2));

        double lfPower = drivePower * Math.cos(driveTheta - thetaImput) + drivePower * Math.sin(driveTheta - thetaImput) + turnImput;
        double rfPower = drivePower * Math.cos(driveTheta - thetaImput) - drivePower * Math.sin(driveTheta - thetaImput) + turnImput;
        double lbPower = drivePower * Math.cos(driveTheta - thetaImput) - drivePower * Math.sin(driveTheta - thetaImput) - turnImput;
        double rbPower = drivePower * Math.cos(driveTheta - thetaImput) + drivePower * Math.sin(driveTheta - thetaImput) - turnImput;

        double maxPower = 1.0;


        maxPower = Math.max(maxPower, Math.abs(lbPower));
        maxPower = Math.max(maxPower, Math.abs(rfPower));
        maxPower = Math.max(maxPower, Math.abs(lbPower));
        maxPower = Math.max(maxPower, Math.abs(rbPower));

        double totalPower = power / maxPower;

        setDrivePowers((lfPower * totalPower), (rfPower * totalPower), (lbPower * totalPower), (rbPower * totalPower));
    }

    public void turnGyro (double iPower, long iDegrees, TurnDirection iDir) throws InterruptedException{
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        Float startHeading = angles.firstAngle;
        Float curentHeading = angles.firstAngle;

        if (iDir == TurnDirection.LEFTPOINT){
            setDrivePowers(0.0, 0.0, iPower, iPower);
        }else if (iDir == TurnDirection.RIGHTPOINT){
            setDrivePowers(iPower, iPower, 0.0, 0.0);
        }else if (iDir== TurnDirection.LEFTSPIN){
            setDrivePowers(-iPower, -iPower, iPower, iPower);
        }else if (iDir== TurnDirection.RIGHTSPIN) {
            setDrivePowers(iPower, iPower, -iPower, -iPower);
        }

        while (getTurnDegrees(startHeading, curentHeading) < iDegrees){
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            curentHeading = angles.firstAngle;
        }

        stopDriveMotors();
    }

    public void driveStartEnc(double iPower, double iDist, DriveDirection iDir) {
        int startPos = primaryDriveEncoder.getCurrentPosition();

        if (iDir == DriveDirection.FORWARD) {
            setDrivePowers(iPower,iPower,iPower,iPower);

            driveEncTargetPos = (int) (startPos - (iDist * ticksPerInch));

            currentDriveState = DriveState.DRIVINGFORWARD;
        } else if (iDir == DriveDirection.BACKWARD) {
            setDrivePowers(-iPower,-iPower,-iPower,-iPower);
            driveEncTargetPos = (int) (startPos + (iDist * ticksPerInch));
            currentDriveState = DriveState.DRIVINGBACKWARD;
        }
    }

    public void driveEnc(double iPower, double iDist, DriveDirection iDir) {
        driveStartEnc(iPower, iDist, iDir);

        if (iDir == DriveDirection.FORWARD) {
            while (primaryDriveEncoder.getCurrentPosition() > driveEncTargetPos) {

            }
        } else if (iDir == DriveDirection.BACKWARD){
            while (primaryDriveEncoder.getCurrentPosition() < driveEncTargetPos) {

            }
        }

        stopDriveMotors();
    }

    public void strafeEncoder (double iPower, double iDist, StrafeDirection iDir) throws InterruptedException{

        int startPos = primaryDriveEncoder.getCurrentPosition();
        int targetPos;

        if (iDir == StrafeDirection.LEFTSTRAFE) {
            targetPos =  (startPos + (int)(iDist * ticksPerInch));

            setDrivePowers(-iPower, iPower, iPower, -iPower);
            currentDriveState = DriveState.STRAFINGLEFT;

            while (primaryDriveEncoder.getCurrentPosition() < targetPos) {

            }
        } else {
            targetPos =  (startPos - (int)(iDist * ticksPerInch));

            setDrivePowers(iPower, -iPower, -iPower, iPower);
            currentDriveState = DriveState.STRAFINGRIGHT;

            while (primaryDriveEncoder.getCurrentPosition() > targetPos) {

            }
        }

        stopDriveMotors();
    }

    public void strafeTime(double iPower, long iTime, StrafeDirection iDir) throws InterruptedException {

        if (iDir == StrafeDirection.LEFTSTRAFE) {
            setDrivePowers(-iPower, iPower, iPower, -iPower);
            currentDriveState = DriveState.STRAFINGLEFT;
        } else {
            setDrivePowers(iPower, -iPower, -iPower, iPower);
            currentDriveState = DriveState.STRAFINGRIGHT;
        }

        Thread.sleep(iTime);

        stopDriveMotors();
    }
}
