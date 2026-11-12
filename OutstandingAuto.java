package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.BioBuzzRobot.BioBuzzAutoRobot;

@Autonomous
public class OutstandingAuto extends LinearOpMode {
    // have to use LinearOpMode to use AutoOptions


    BioBuzzAutoRobot robot = new BioBuzzAutoRobot();

    @Override
    public void runOpMode() throws InterruptedException {

        robot.robotInit(hardwareMap, telemetry);
        robot.autoOptions.selectOptions(this);

        if (robot.startPosition.getValue().equals("sidewall")){
            robot.setupSidewall();
        } else if (robot.startPosition.getValue().equals("uptilt")){

        }
        waitForStart();

        sleep((long) robot.startDelay.getValue() * 1000);

        while (opModeIsActive() && !isStopRequested() && robot.isRobotOccupied) {
            //line below refreshes the odometry every time the robot begins auto.
            robot.drivetrain.odoLogic();
            robot.checkRobotState();
        }



    }


}
