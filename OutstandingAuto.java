package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.BioBuzzRobot.BioBuzzAutoRobot;

@Autonomous
public class OutstandingAuto extends LinearOpMode {
    // have to use LinearOpMode to use AutoOptions


    BioBuzzAutoRobot robot = new BioBuzzAutoRobot();

    @Override
    public void runOpMode() throws InterruptedException {

        robot.robotInit(hardwareMap, telemetry);
        robot.autoOptions.selectOptions(this);

        waitForStart();

        sleep(robot.startDelay.getValue()*1000);



    }


}
