package org.firstinspires.ftc.teamcode.Tools;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class OdoLogger {
    OdoLog [] log = new OdoLog[0];

    public void addLog (int leftEncoder, int rightEncoder, int auxEncoder, double mx, Vector pos, double vectorStart, double thetaStart){
        OdoLog [] tempArray = new OdoLog[log.length + 1];

        for (int i=0; i< log.length; i++){
            tempArray[i] = log[i];
        }

        tempArray[log.length] = new OdoLog(leftEncoder, rightEncoder, auxEncoder, mx, pos, vectorStart, thetaStart);
        log = tempArray;
    }

    public void showLog (Telemetry telemetry, Gamepad gamepad1, LinearOpMode opMode){
       int index = 0;
       boolean crossPressed = false;
       boolean trianglePressed = false;


       while (index < log.length && opMode.opModeIsActive() && !opMode.isStopRequested()){
           log[index].displayLog(telemetry);
           telemetry.update();
           if (gamepad1.cross && !crossPressed){
               index ++;
           } else if (gamepad1.triangle && !trianglePressed){
               index --;
           }
           crossPressed = gamepad1.cross;
           trianglePressed = gamepad1.triangle;
       }
    }

}
