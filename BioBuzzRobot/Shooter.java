package org.firstinspires.ftc.teamcode.BioBuzzRobot;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.StateMachine.StateMachineCommand;
import org.firstinspires.ftc.teamcode.StateMachine.StateMachineCommands;
import org.firstinspires.ftc.teamcode.StateMachine.StateMachineShooterCommand;


public class Shooter {
    public DcMotorEx nectarMotor;
    public DcMotorEx pollenMotor;
    public Servo nectarServo;
    public Servo pollenServo;

    public ShooterState currentShooterState = ShooterState.STOPPED;
    public StateMachineShooterCommand currentShooterCommand;
    public boolean currentlyShooting = false;

    ElapsedTime shootTimer = new ElapsedTime();

    //eventually nectar might be faster
    //TODO run and test motor speeds
    public static final double NECTAR_MOTOR_VELOCITY = 20000.0;
    public static final double POLLEN_MOTOR_VELOCITY = 20000.0;
    public static final double NECTAR_SERVO_OPEN = 1.0;
    public static final double NECTAR_SERVO_CLOSED = 0.0;
    public static final double POLLEN_SERVO_OPEN = 1.0;
    public static final double POLLEN_SERVO_CLOSED = 0.0;
    public static final double TIME_NEEDED_TO_SHOOT = 2000;


    public enum ShooterCommand {
        STOP,
        TURNON,
        SHOOT
    }

    public enum ShooterState {
        STOPPED,
        READYTOSHOOT,
        SHOOTING
    }


    public void checkShooterState(StateMachineCommands commandList) {
        //TODO let the orchestrator (BioBuzzRobot) handle end of file and next commands
        //if (commandList.currentOption >= commandList.stateMachineCommands.size() && currentDriveState == MecDrive.DriveState.STOPPED) {
        // break;
        // }

        switch (currentShooterState) {
            case STOPPED:
                //waitingOnDrive = false;
                if (commandList.currentOption < commandList.stateMachineCommands.size() &&
                        commandList.getCurrentType() == StateMachineCommand.SystemTypes.SHOOTER) {

                    currentShooterCommand = commandList.getShooterCommand();
                    commandList.currentOption++;

                    switch (currentShooterCommand.command) {
                        case TURNON:
                            nectarMotor.setVelocity(NECTAR_MOTOR_VELOCITY);
                            pollenMotor.setVelocity(POLLEN_MOTOR_VELOCITY);
                            currentShooterState = ShooterState.READYTOSHOOT;
                            break;
                        case SHOOT:
                            nectarMotor.setVelocity(NECTAR_MOTOR_VELOCITY);
                            pollenMotor.setVelocity(POLLEN_MOTOR_VELOCITY);
                            currentShooterState = ShooterState.SHOOTING;
                            break;
                    }
                }
                break;


            case READYTOSHOOT:

                if (commandList.currentOption < commandList.stateMachineCommands.size() &&
                        commandList.getCurrentType() == StateMachineCommand.SystemTypes.SHOOTER) {

                    currentShooterCommand = commandList.getShooterCommand();
                    commandList.currentOption++;

                    switch (currentShooterCommand.command) {
                        case STOP:
                            nectarMotor.setPower(0.0);
                            pollenMotor.setPower(0.0);
                            currentShooterState = ShooterState.STOPPED;
                            break;

                        case SHOOT:
                            currentShooterState = ShooterState.SHOOTING;
                            break;
                    }
                }
            break;


            case SHOOTING:
                // TODO check if motors are up to speed, if so, move servos,
            //lift servos and reset timer
                //if statement if our timer is this, then close servos and state back to ready to shoot
                if (nectarMotor.getVelocity() >= NECTAR_MOTOR_VELOCITY && ! currentlyShooting){ //! means not
                    nectarServo.setPosition(NECTAR_SERVO_OPEN);
                    pollenServo.setPosition(POLLEN_SERVO_OPEN);
                    currentlyShooting = true;
                    shootTimer.reset();
                }

                if (shootTimer.milliseconds() >= TIME_NEEDED_TO_SHOOT && currentlyShooting) {
                    nectarServo.setPosition(NECTAR_SERVO_CLOSED);
                    pollenServo.setPosition(POLLEN_SERVO_CLOSED);
                    currentlyShooting = false;
                    currentShooterState = ShooterState.READYTOSHOOT;
                }





        }
    }




    public void shooterInit(HardwareMap hwMap, Telemetry telem) {
        nectarMotor = hwMap.get(DcMotorEx.class, "nsm");
        pollenMotor = hwMap.get(DcMotorEx.class, "psm");
        nectarServo = hwMap.servo.get("nss");
        pollenServo = hwMap.servo.get("pss");
    }

}
