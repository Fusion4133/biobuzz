package org.firstinspires.ftc.teamcode.Tools;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class OdoLog {
    int leftEncoder;
    int rightEncoder;
    int auxEncoder;
    double mx;
    double x;
    double y;
    double theta;
    double vectorStart;
    double thetaStart;

    public OdoLog (int leftEncoder, int rightEncoder, int auxEncoder, double mx, Vector pos, double vectorStart, double thetaStart){
        this.leftEncoder = leftEncoder;
        this.rightEncoder = rightEncoder;
        this.auxEncoder = auxEncoder;
        this.mx = mx;
        this.x = pos.x;
        this.y = pos.y;
        this.theta = pos.theta;
        this.vectorStart = vectorStart;
        this.thetaStart = thetaStart;
    }

    public OdoLog(){

    }

    public void displayLog (Telemetry telemetry){
        telemetry.addData("Pos: ", x + ", " + y + ", " + theta);
        telemetry.addData("Start (Vec, Theta): ", vectorStart + ", " + thetaStart);
        telemetry.addData("LeftEnc: ", leftEncoder);
        telemetry.addData("RightEnc: ", rightEncoder);
        telemetry.addData("AuxEnc: ", auxEncoder);
        telemetry.addData("mx: ", mx);


    }
}
