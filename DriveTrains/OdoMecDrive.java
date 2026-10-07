package org.firstinspires.ftc.teamcode.DriveTrains;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.UnnormalizedAngleUnit;
import org.firstinspires.ftc.teamcode.Tools.OdoLogger;
import org.firstinspires.ftc.teamcode.Tools.Vector;


public class OdoMecDrive extends MecDrive {
    public DcMotor odoPodLeft, odoPodRight, odoPodMid;

    GoBildaPinpointDriver pinPoint;

    Pose2D pose2D;
    public enum VectorFollow {
        xVector,
        yVector
    }


    //constants for odometry, measurements in centimeters
    static double L = 35.50 ; //length between left and right pod
    static double B = 0.2 ;// how far back the horizontal pod is from the middle of robot 0.635
    static double R = 2.4 ; //radius of the odometry wheels
    static double N  = 2000 ; // encoder ticks

    static double odoTicksPerCm = N / (2.0 * Math.PI * R);
    static double odoCmPerTick = (2.0 * Math.PI * R) / N;

    double vectorStart;
    double thetaStart;
    public Vector START_POS = new Vector (); //xy theta
    public Vector pos = new Vector();


    double currentRightPosition = 0;
    double currentLeftPosition = 0;
    double currentMidPosition = 0;
    double oldRightPosition = 0;
    double oldLeftPosition = 0;
    double oldMidPosition = 0;

    public double driveOdoTargetPos;

    double powerAdjust = 0.1;
    double angularAdjust = 0.81; //.51
    double adjustFactor = 1.0;
    VectorFollow vectorFollow;


    public boolean turnIsComplete;
    public boolean xIsComplete;
    public boolean yIsComplete;

    public double mValue;

    public double originalX = 0;
    public double originalY = 0;
    public double originalTheta = 0;
    public double adjustedTheta;


    //This is to display telemetry for odometry values in autonomous
    public OdoLogger odoLogger = new OdoLogger();

    public void driveInit(HardwareMap hwMap, Telemetry telem) {
        super.driveInit(hwMap, telem);


        pinPoint = hwMap.get(GoBildaPinpointDriver.class, "pinpoint");

        //TODO get numbers
        pinPoint.setOffsets(-10.795, 6.36, DistanceUnit.CM);
        pinPoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        pinPoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.REVERSED, GoBildaPinpointDriver.EncoderDirection.REVERSED);
        pinPoint.resetPosAndIMU();

        pinPoint.getHeading(AngleUnit.RADIANS);

        pinPoint.setPosition(new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0));

    }

    public void initOdoPos(double startx, double starty, double startTheta){
        pinPoint.update();
        pose2D = pinPoint.getPosition();

        originalX = startx;
        originalY = starty;
        originalTheta = startTheta;

        pinPoint.setHeading(originalTheta, AngleUnit.RADIANS);
        pinPoint.setPosX(originalX, DistanceUnit.CM);
        pinPoint.setPosY(originalY, DistanceUnit.CM);
        pos.x = pose2D.getX(DistanceUnit.CM);
        pos.y = pose2D.getY(DistanceUnit.CM);
        pos.theta = pinPoint.getHeading(UnnormalizedAngleUnit.DEGREES);
    }

    public void turnStartOdo(double iPower, double iHeading, TurnDirection iDir){
        //Heading negative if turning left, positive if turning right
        if (iDir == TurnDirection.RIGHTSPIN){
            setDrivePowers(iPower, iPower, -iPower, -iPower);
            currentDriveState = DriveState.TURNINGRIGHT;
        } else if (iDir == TurnDirection.LEFTSPIN){
            setDrivePowers(-iPower, -iPower, iPower, iPower);
            currentDriveState = DriveState.TURNINGLEFT;
        }

        driveOdoTargetPos = iHeading;
    }

    public void odoLogic(){

        pinPoint.update();
        pose2D = pinPoint.getPosition();

        pos.x = pose2D.getX(DistanceUnit.CM);
        pos.y = pose2D.getY(DistanceUnit.CM);
        pos.theta = pinPoint.getHeading(UnnormalizedAngleUnit.RADIANS);
        adjustedTheta = pinPoint.getHeading(AngleUnit.DEGREES);



    }

    public void driveOdoAdjust (double iPower){
        double mx = 0.0;

        telem.addData("Theta", pos.theta);
        telem.addData("Theta Start", thetaStart);
        telem.addData("Y Vector", pos.y);
        telem.addData("Vector Start", vectorStart);
        // telem.addData("power adjust", powerAdjust);
        // telem.addData("angular adjust", angularAdjust);
        telem.addData("leftOdo", -odoPodLeft.getCurrentPosition());
        telem.addData("rightOdo", odoPodRight.getCurrentPosition());

        if (Math.abs(pos.theta - thetaStart) > 0.05){
            mx = angularAdjust * (pos.theta - thetaStart);
            setDrivePowers(iPower + mx, iPower+ mx, iPower - mx, iPower - mx);
            telem.addData("mx:", mx);
        } else if (vectorFollow == VectorFollow.yVector && Math.abs(pos.x - vectorStart) > 1){
            mx = powerAdjust * (pos.x - vectorStart);
            setDrivePowers(iPower - mx, iPower + mx,  iPower + mx, iPower - mx);
            telem.addData("mx:", mx);
        } else if (vectorFollow == VectorFollow.xVector && Math.abs(pos.y - vectorStart) > 1){
            mx = powerAdjust * (pos.y - vectorStart)*(-1.0);
            setDrivePowers(iPower - mx, iPower + mx,  iPower + mx, iPower - mx);
            telem.addData("mx:", mx);
        }

        odoLogger.addLog(-odoPodLeft.getCurrentPosition(), odoPodRight.getCurrentPosition(), -odoPodMid.getCurrentPosition(), mx, pos, vectorStart, thetaStart);
        telem.update();
    }

    public void driveStartOdo(double iPower, double iDist, DriveDirection iDir, Vector startVector) {
        driveStartOdo(iPower, iDist, iDir, startVector,false);
    }

    public void driveStartOdo(double iPower, double iDist, DriveDirection iDir, Vector startVector, boolean isRampingDown) {
        int startPos = primaryDriveEncoder.getCurrentPosition();
        thetaStart = startVector.theta;
        double adjustedTheta = startVector.theta;

        adjustedTheta = adjustedTheta % (2.0 * Math.PI);
        if (adjustedTheta < 0.0){
            adjustedTheta += (2.0 * Math.PI);
        }

        if (adjustedTheta <= .17 || adjustedTheta >= 6.11){
            adjustFactor = 1.0;
            vectorStart = startVector.y;
            vectorFollow = VectorFollow.xVector;
        } else if (adjustedTheta <= 3.31 && adjustedTheta >=2.97){
            adjustFactor = -1.0;
            vectorStart = startVector.y;
            vectorFollow = VectorFollow.xVector;
        } else if (adjustedTheta <= 1.74 && adjustedTheta >= 1.4){
            adjustFactor = 1.0;
            vectorStart = startVector.x;
            vectorFollow = VectorFollow.yVector;
        } else if (adjustedTheta <= 4.88 && adjustedTheta >= 4.54){
            adjustFactor = -1.0;
            vectorStart = startVector.x;
            vectorFollow = VectorFollow.yVector;
        }

        powerAdjust = adjustFactor * 0.08;

        if (iDir == DriveDirection.FORWARD) {
            setDrivePowers(iPower,iPower,iPower,iPower);
            driveEncTargetPos = (int) (startPos - (iDist * odoTicksPerCm));
            //driveEncTargetPos = (int) (startPos - (iDist * odoTicksPerCm));
            currentDriveState = DriveState.DRIVINGFORWARD;
            if (isRampingDown){
                rampDownTargetPos = (int) (startPos - ((iDist - rampDownDist) * odoTicksPerCm));
                mValue = finalDrivePower - iPower / (double) (driveEncTargetPos - rampDownTargetPos);
            }else {
                rampDownTargetPos = driveEncTargetPos;
            }

        } else if (iDir == DriveDirection.BACKWARD) {
            setDrivePowers(-iPower,-iPower,-iPower,-iPower);
            driveEncTargetPos = (int) (startPos + (iDist * odoTicksPerCm));
            //driveEncTargetPos = (int) (startPos + (iDist * odoTicksPerCm));
            currentDriveState = DriveState.DRIVINGBACKWARD;
            if (isRampingDown){
                rampDownTargetPos = (int) (startPos + ((iDist - rampDownDist) * odoTicksPerCm));
                mValue = finalDrivePower - iPower / (double) (driveEncTargetPos - rampDownTargetPos);
            }else {
                rampDownTargetPos = driveEncTargetPos;
            }
        }

    }

    public void driveOdo (double iPower, double iDist, DriveDirection iDir, Vector startVector) throws InterruptedException {
        driveStartOdo(iPower, iDist, iDir, startVector);

        if (iDir == DriveDirection.FORWARD) {
            while (primaryDriveEncoder.getCurrentPosition() < driveEncTargetPos) {
                driveOdoAdjust(iPower);
            }
        } else {
            while (primaryDriveEncoder.getCurrentPosition() > driveEncTargetPos){
                driveOdoAdjust(-iPower);
            }
        }

        stopDriveMotors();
    }

    public void strafeStartOdo(double iPower, double iDist, StrafeDirection iDir, Vector startVector){
        int startPos = odoPodMid.getCurrentPosition();
        thetaStart = startVector.theta;
        double adjustedTheta = startVector.theta;

        adjustedTheta = adjustedTheta % (2.0 * Math.PI);
        if (adjustedTheta < 0.0){
            adjustedTheta += (2.0 * Math.PI);
        }

        if (adjustedTheta <= .17 || adjustedTheta >= 6.11){
            adjustFactor = 1.0;
            vectorStart = startVector.x;
            vectorFollow = VectorFollow.yVector;
        } else if (adjustedTheta <= 3.31 && adjustedTheta >=2.97){
            adjustFactor = -1.0;
            vectorStart = startVector.x;
            vectorFollow = VectorFollow.yVector;
        } else if (adjustedTheta <= 1.74 && adjustedTheta >= 1.4){
            adjustFactor = 1.0;
            vectorStart = startVector.y;
            vectorFollow = VectorFollow.xVector;
        } else if (adjustedTheta <= 4.88 && adjustedTheta >= 4.54){
            adjustFactor = -1.0;
            vectorStart = startVector.y;
            vectorFollow = VectorFollow.xVector;
        }

        powerAdjust = adjustFactor * 0.08;

        if (iDir == StrafeDirection.LEFTSTRAFE) {

            driveEncTargetPos = (startPos - (int) (iDist * odoTicksPerCm));
            setDrivePowers(-iPower, iPower, iPower, -iPower);
            currentDriveState = DriveState.STRAFINGLEFT;
        }else if (iDir == StrafeDirection.RIGHTSTRAFE) {

            driveEncTargetPos = (startPos + (int) (iDist * odoTicksPerCm));
            setDrivePowers(iPower, -iPower, -iPower, iPower);
            currentDriveState = DriveState.STRAFINGRIGHT;
        }
    }

    //TODO Strafe
    public void strafeOdoAdjust(double iPower){
        double mx = 0.0;

        //tolerance was 0.05
        if (Math.abs(pos.theta - thetaStart) > 0.1){
            mx = angularAdjust * (pos.theta - thetaStart);
            //setDrivePowers(-iPower + mx, iPower+ mx, iPower - mx, -iPower - mx);
            telem.addData("mx:", mx);
        } else if (vectorFollow == VectorFollow.yVector && Math.abs(pos.x - vectorStart) > 3){
            mx = powerAdjust * (pos.x - vectorStart);
            //setDrivePowers(-iPower - mx, iPower - mx,  iPower - mx, -iPower - mx);
            telem.addData("mx:", mx);
        } else if (vectorFollow == VectorFollow.xVector && Math.abs(pos.y - vectorStart) > 3){
            mx = powerAdjust * (pos.y - vectorStart);//*(-1.0);
            //setDrivePowers(-iPower - mx, iPower - mx,  iPower - mx, -iPower - mx);
            telem.addData("mx:", mx);
        }

    }

    public void strafeOdo (double iPower, double iDist, StrafeDirection iDir, Vector startVector) {
        strafeStartOdo(iPower, iDist, iDir, startVector);

        if (iDir == StrafeDirection.LEFTSTRAFE) {

            while (odoPodMid.getCurrentPosition() > driveEncTargetPos) {
                strafeOdoAdjust(iPower);
            }
        } else {
            while (odoPodMid.getCurrentPosition() < driveEncTargetPos) {
               strafeOdoAdjust(iPower);
            }
        }

        stopDriveMotors();
    }

    public void driveToOdo (double iPower, Vector finalVector){

        double dX = finalVector.x - pos.x;
        double dY = finalVector.y - pos.y;
        double driveTheta = Math.atan(dY/dX);
        double dTheta = pos.theta - finalVector.theta;
        double turnPower = -0.15 * iPower;


        if (dY < 0 ){
            driveTheta += Math.PI;

        }

        if (dTheta < 0){
            turnPower *= -1;

        }

        if (Math.abs(dTheta) <= 0.1){
            turnPower = 0;
            turnIsComplete = true;

        } else {
            turnIsComplete = false;

        }
        if (Math.abs(dX) <= 3){
            xIsComplete = true;

        } else {
            xIsComplete = false;

        }
        if (Math.abs(dY) <= 3){
            yIsComplete = true;

        } else {
            yIsComplete = false;

        }


//        if (dY > dX){
//            vectorFollow = VectorFollow.yVector;
//
//        }else {
//            vectorFollow = VectorFollow.xVector;
//
//        }
        //TODO name the powers properly
        double lfPower = Math.cos(driveTheta + pos.theta) + Math.sin(driveTheta - pos.theta) + turnPower;
        double rfPower = Math.cos(driveTheta + pos.theta) - Math.sin(driveTheta - pos.theta) + turnPower;
        double lbPower = Math.cos(driveTheta + pos.theta) - Math.sin(driveTheta - pos.theta) - turnPower;
        double rbPower = Math.cos(driveTheta + pos.theta) + Math.sin(driveTheta - pos.theta) - turnPower;


        double maxPower;

        if (Math.abs(lfPower) > Math.abs(rfPower) && Math.abs(lfPower) > Math.abs(lbPower) && Math.abs(lfPower) > Math.abs(rbPower)){
            maxPower = Math.abs(lfPower);

        } else if (Math.abs(lbPower) > Math.abs(rfPower) && Math.abs(lbPower) > Math.abs(rbPower)) {
            maxPower = Math.abs(lbPower);

        } else if (Math.abs(rbPower) > Math.abs(rfPower)) {
            maxPower = Math.abs(rbPower);

        } else {
            maxPower = Math.abs(rbPower);

        }

        currentDriveState = DriveState.DRIVINGT0POS;
        setDrivePowers((lfPower * iPower / maxPower), (rfPower * iPower / maxPower), (lbPower * iPower / maxPower), (rbPower * iPower / maxPower));

    }


    public void driveToOdoAdjustToTarget (double iPower, Vector finalVector){

        double dX = finalVector.x - pos.x;
        double dY = finalVector.y - pos.y;

        if (dX == 0){
            dX = 0.0001;
        }
        if (dY == 0){
            dY = 0.0001;
        }

       // double driveTheta = Math.atan(dY/dX);
        //TODO check the MATH.abs
        double dTheta = finalVector.theta - pos.theta;
        double turnPower = -1;

        if (dTheta > 0){
            turnPower *= -1;
        }

        double turnMx = 0.0;


        if (xIsComplete){
            if (Math.abs(dX) > 0){

            } else {
                //dX = 0.00001;
            }

        }


        if (Math.abs(dTheta) <= 0.025 || turnIsComplete){
            turnPower = 0;
            turnIsComplete = true;
        }
        //TODO check this value
        if (Math.abs(dY) < 1){
            yIsComplete = true;

        }
        if (Math.abs(dX) < 1){
            xIsComplete = true;
        }



        double xyAdjust;
        boolean xyDone;

        if (xIsComplete && yIsComplete){
            if (dX < 0.5 && dY < 0.5){
                xyAdjust = 0;
            }else {
                xyAdjust = 0.4;
            }

            xyDone = true;

        }else {
            xyAdjust = 1;
            xyDone = false;
        }

        telem.addData("turn:", turnIsComplete);
        telem.addData("y:", yIsComplete);
        telem.addData("x:", xIsComplete);

        if (Math.abs(pos.theta - thetaStart) > 0.01 &&  turnIsComplete){
            turnMx = -angularAdjust * (pos.theta - finalVector.theta);
            if (Math.abs(turnMx) > 0.30 ){
                turnMx = 0.30;
            }

            telem.addData("turnMx:", turnMx);
        }

        double driveTheta = Math.atan(dY/dX);

        if (dX < 0 ){
            driveTheta += Math.PI;
        }

        if (Math.abs(dTheta) < Math.PI / 4){
            turnPower *= 0.4;
        }

        double rampNum = dY * dY + dX * dX;
        boolean readyRamp = false;

        if (Math.sqrt(rampNum) < 25.0 && !xyDone && iPower > 0.5 ){
            xyAdjust *= 1.0;
            readyRamp = true;
//            if (xyAdjust * iPower < 0.15){
//                xyAdjust = 1;
//            }
        }

        //TODO name the powers properly

        double lfPower = Math.cos(driveTheta - pos.theta) * xyAdjust + Math.sin(driveTheta - pos.theta) * xyAdjust + turnPower + turnMx;
        double rfPower = Math.cos(driveTheta - pos.theta)* xyAdjust - Math.sin(driveTheta - pos.theta)* xyAdjust + turnPower + turnMx;
        double lbPower = Math.cos(driveTheta - pos.theta)* xyAdjust - Math.sin(driveTheta - pos.theta)* xyAdjust - turnPower - turnMx;
        double rbPower = Math.cos(driveTheta - pos.theta)* xyAdjust + Math.sin(driveTheta - pos.theta)* xyAdjust - turnPower - turnMx;

        if (driveTheta == 0) {

            lfPower = Math.cos(driveTheta + pos.theta) + Math.sin(driveTheta - pos.theta) + turnPower;
            rfPower = Math.cos(driveTheta + pos.theta) - Math.sin(driveTheta - pos.theta) + turnPower;
            lbPower = Math.cos(driveTheta + pos.theta) - Math.sin(driveTheta - pos.theta) - turnPower;
            rbPower = Math.cos(driveTheta + pos.theta) + Math.sin(driveTheta - pos.theta) - turnPower;
        }

        double maxPower;

        if (Math.abs(lfPower) > Math.abs(rfPower) && Math.abs(lfPower) > Math.abs(lbPower) && Math.abs(lfPower) > Math.abs(rbPower)){
            maxPower = Math.abs(lfPower);

        } else if (Math.abs(lbPower) > Math.abs(rfPower) && Math.abs(lbPower) > Math.abs(rbPower)) {
            maxPower = Math.abs(lbPower);

        } else if (Math.abs(rbPower) > Math.abs(rfPower)) {
            maxPower = Math.abs(rbPower);

        } else {
            maxPower = Math.abs(rfPower);

        }

        if((xyDone || readyRamp)){

            maxPower = 1;
        }


        telem.addData("lf:", lfPower);
        telem.addData("lb:", lbPower);
        telem.addData("rf:", rfPower);
        telem.addData("rb:", rbPower);
        telem.addData("maxPower:", maxPower);
        telem.addData("turnPower:", turnPower);
        telem.addData("xycomplete:", xyAdjust);
        telem.addData("y:", pos.y);
        telem.addData("x:", pos.x);
        telem.addData("theta:", pos.theta);
        telem.addData("dY:", dY);
        telem.addData("dTheta:", dTheta);
        telem.addData("DX:", dX);
        setDrivePowers((lfPower * iPower / maxPower), (rfPower * iPower / maxPower), (lbPower * iPower / maxPower), (rbPower * iPower / maxPower));
        telem.update();
    }

    public void driveAboutOdoAdjustToTarget (double iPower, Vector finalVector){

        double dX = finalVector.x - pos.x;
        double dY = finalVector.y - pos.y;

        if (dX == 0){
            dX = 0.0001;
        }
        if (dY == 0){
            dY = 0.0001;
        }

        // double driveTheta = Math.atan(dY/dX);
        //TODO check the MATH.abs
        double dTheta = finalVector.theta - pos.theta;
        double turnPower = -1;

        if (dTheta > 0){
            turnPower *= -1;
        }

        if (Math.abs(dTheta) <= 0.1){
            turnPower = 0;
            turnIsComplete = true;

        } else {
            turnIsComplete = false;

        }
        if (Math.abs(dX) <= 3){
            xIsComplete = true;

        } else {
            xIsComplete = false;

        }
        if (Math.abs(dY) <= 3){
            yIsComplete = true;

        } else {
            yIsComplete = false;

        }

        double turnMx = 0.0;

        if (Math.abs(dTheta) <= 0.025 || turnIsComplete){
            turnPower = 0;
            turnIsComplete = true;
        }
        //TODO check this value
        if (Math.abs(dY) < 10){
            yIsComplete = true;

        }
        if (Math.abs(dX) < 10){
            xIsComplete = true;
        }



        double xyAdjust;
        boolean xyDone;

        if (xIsComplete && yIsComplete){
            if (dX < 0.5 && dY < 0.5){
                xyAdjust = 0;
            }else {
                xyAdjust = 0.4;
            }

            xyDone = true;

        }else {
            xyAdjust = 1;
            xyDone = false;
        }

        telem.addData("turn:", turnIsComplete);
        telem.addData("y:", yIsComplete);
        telem.addData("x:", xIsComplete);

        if (Math.abs(pos.theta - thetaStart) > 0.01 &&  turnIsComplete){
            turnMx = -angularAdjust * (pos.theta - finalVector.theta);
            if (Math.abs(turnMx) > 0.10 ){
                turnMx = 0.10;
            }

            telem.addData("turnMx:", turnMx);
        }

        double driveTheta = Math.atan(dY/dX);

        if (dX < 0 ){
            driveTheta += Math.PI;
        }


        //TODO name the powers properly

        double lfPower = Math.cos(driveTheta - pos.theta) * xyAdjust + Math.sin(driveTheta - pos.theta) * xyAdjust + turnPower + turnMx;
        double rfPower = Math.cos(driveTheta - pos.theta)* xyAdjust - Math.sin(driveTheta - pos.theta)* xyAdjust + turnPower + turnMx;
        double lbPower = Math.cos(driveTheta - pos.theta)* xyAdjust - Math.sin(driveTheta - pos.theta)* xyAdjust - turnPower - turnMx;
        double rbPower = Math.cos(driveTheta - pos.theta)* xyAdjust + Math.sin(driveTheta - pos.theta)* xyAdjust - turnPower - turnMx;

        if (driveTheta == 0) {

            lfPower = Math.cos(driveTheta + pos.theta) + Math.sin(driveTheta - pos.theta) + turnPower;
            rfPower = Math.cos(driveTheta + pos.theta) - Math.sin(driveTheta - pos.theta) + turnPower;
            lbPower = Math.cos(driveTheta + pos.theta) - Math.sin(driveTheta - pos.theta) - turnPower;
            rbPower = Math.cos(driveTheta + pos.theta) + Math.sin(driveTheta - pos.theta) - turnPower;
        }

        double maxPower;

        if (Math.abs(lfPower) > Math.abs(rfPower) && Math.abs(lfPower) > Math.abs(lbPower) && Math.abs(lfPower) > Math.abs(rbPower)){
            maxPower = Math.abs(lfPower);

        } else if (Math.abs(lbPower) > Math.abs(rfPower) && Math.abs(lbPower) > Math.abs(rbPower)) {
            maxPower = Math.abs(lbPower);

        } else if (Math.abs(rbPower) > Math.abs(rfPower)) {
            maxPower = Math.abs(rbPower);

        } else {
            maxPower = Math.abs(rfPower);

        }



        telem.addData("lf:", lfPower);
        telem.addData("lb:", lbPower);
        telem.addData("rf:", rfPower);
        telem.addData("rb:", rbPower);
        telem.addData("maxPower:", maxPower);
        telem.addData("turnPower:", turnPower);
        telem.addData("xycomplete:", xyAdjust);
        telem.addData("y:", pos.y);
        telem.addData("x:", pos.x);
        telem.addData("theta:", pos.theta);
        telem.addData("dY:", dY);
        telem.addData("dTheta:", dTheta);
        telem.addData("DX:", dX);
        setDrivePowers((lfPower * iPower / maxPower), (rfPower * iPower / maxPower), (lbPower * iPower / maxPower), (rbPower * iPower / maxPower));
        telem.update();
    }



}
