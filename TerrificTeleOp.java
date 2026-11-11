package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.BioBuzzRobot.BioBuzzRobot;
import org.firstinspires.ftc.teamcode.BioBuzzRobot.Intake;
import org.firstinspires.ftc.teamcode.BioBuzzRobot.Shooter;

@TeleOp
public class TerrificTeleOp extends OpMode {


    BioBuzzRobot robot = new BioBuzzRobot();

    public double tempPollenVelocity;
    public double tempNectarVelocity;
    public double inc = 100;


    @Override
    public void init() {
        robot.robotInit(hardwareMap, telemetry);
        //robot.drivetrain.initOdoPos(15.2,221.0,Math.PI/2); //TODO figure out how to pass ending position from auto into this
        robot.drivetrain.initOdoPos(0.0,0.0,0.0); //TODO figure out how to pass ending position from auto into this

        tempNectarVelocity = Shooter.NECTAR_MOTOR_VELOCITY;
        tempPollenVelocity = Shooter.POLLEN_MOTOR_VELOCITY;

    }


    @Override
    public void start() {
        super.start();


    }


    @Override
    public void loop() {
        robot.drivetrain.odoLogic();
        robot.drivetrain.fieldCentricDrive(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x, robot.drivetrain.pos.theta, true);

        telemetry.addData("X Pod", robot.drivetrain.pinPoint.getPosX(DistanceUnit.CM));
        telemetry.addData("y Pod", robot.drivetrain.pinPoint.getPosY(DistanceUnit.CM));
        telemetry.addData("heading", robot.drivetrain.pinPoint.getHeading(AngleUnit.RADIANS));

        telemetry.addData("nectar shooter velocity", robot.shooter.nectarMotor.getVelocity());
        telemetry.addData("pollen shooter velocity", robot.shooter.pollenMotor.getVelocity());

        if (gamepad1.right_trigger > 0.4) {
            robot.intake.intakeMotor.setPower(Intake.COLLECT_MOTOR_SPEED);
        }
        else if (gamepad1.left_trigger > 0.4) {
            robot.intake.intakeMotor.setPower(Intake.EJECT_MOTOR_SPEED);
        }
        else {
            robot.intake.intakeMotor.setPower(0.0);
        }

        if (gamepad2.left_bumper){
            robot.shooter.pollenMotor.setVelocity(tempPollenVelocity);
          //  robot.shooter.pollenMotor.setVelocity(Shooter.POLLEN_MOTOR_VELOCITY);
        }
        else {
            robot.shooter.pollenMotor.setPower(0.0);
        }

        if (gamepad2.right_bumper){
            robot.shooter.nectarMotor.setVelocity(tempNectarVelocity);
           // robot.shooter.nectarMotor.setVelocity(Shooter.NECTAR_MOTOR_VELOCITY);

        }
        else {
            robot.shooter.nectarMotor.setPower(0.0);
        }

        if (gamepad2.left_trigger > 0.4) {
            robot.shooter.pollenServo.setPosition(Shooter.POLLEN_SERVO_OPEN);
        }
        else {
            robot.shooter.pollenServo.setPosition(Shooter.POLLEN_SERVO_CLOSED);
        }

        if (gamepad2.right_trigger < 0.4) {
            robot.shooter.nectarServo.setPosition(Shooter.NECTAR_SERVO_OPEN);
        }
        else {
            robot.shooter.nectarServo.setPosition(Shooter.NECTAR_SERVO_CLOSED);
        }

        if (gamepad1.triangleWasPressed()) {
            robot.drivetrain.pinPoint.setHeading(0.0,AngleUnit.RADIANS);
        }

        if (gamepad2.dpadUpWasPressed()) {
            // += is shorthand for tempPollenVelocity = tempPollenVelocity + inc
            tempPollenVelocity += inc;
            tempNectarVelocity += inc;
        }

        if (gamepad2.dpadDownWasPressed()) {
            tempPollenVelocity -= inc;
            tempNectarVelocity -= inc;

        }

        if (gamepad2.dpadLeftWasPressed()) {
            inc /= 10.0;

        }

        if (gamepad2.dpadRightWasPressed()) {
            inc *= 10.0;
        }



    }


}
