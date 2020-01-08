package org.firstinspires.ftc.teamcode;

/**
 * Author: Daan Stevenson
 * Created: 12/2/19
 * Elevator control combined with Gripper
 */

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "ElevatorAndGripper", group = "")
@Disabled

public class ElevatorAndGripper extends LinearOpMode {

    // initialize I/O
    private DcMotorSimple motorElevator;
    private DigitalChannel digElevatorLimit;
    Servo servoGripper;

    // runtime variable
    private ElapsedTime elevatorRuntime = new ElapsedTime();

    // gripper states
    double gripper_open = 0.4;
    double gripper_closed = 0.8;
    boolean gripper_press = false;
    private String gripper_state = "OPEN";

    @Override
    public void runOpMode() {

        // reference Configuration variables
        motorElevator = hardwareMap.get(DcMotorSimple.class, "motorElevator");
        digElevatorLimit = hardwareMap.get(DigitalChannel.class, "digElevatorLimit");
        servoGripper = hardwareMap.get(Servo.class, "servoGripper");

        // set the digital channel to input.
        digElevatorLimit.setMode(DigitalChannel.Mode.INPUT);
        motorElevator.setDirection(DcMotorSimple.Direction.REVERSE);

        // lower elevator until limit switch is depressed
        while (digElevatorLimit.getState() == false) {
            motorElevator.setPower(-0.4);
        }
        motorElevator.setPower(0);
        float elevatorInput = 0;

        // initialize tray servo position
        servoGripper.setPosition(gripper_open);

        // Setup an integer to define block level
        // 0 = lowest (on ground)
        // 1 = up 1 block level
        int elevatorLevel = 0;
        int elevatorDesiredLevel = 0;
        boolean elevator_press = false;
        double level_duration = 600;


        // initializing telemetry
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // elevator set level logic
            if (elevator_press == false) {
                if (gamepad1.dpad_up == true) {
                    elevatorDesiredLevel++;
                    elevator_press = true;
                } else if (gamepad1.dpad_down == true) {
                    elevatorDesiredLevel--;
                    elevator_press = true;
                } else {
                    elevator_press = false;
                }
            } else{
                if (gamepad1.dpad_up == false && gamepad1.dpad_down == false){
                    elevator_press = false;
                }
            }
            elevatorDesiredLevel = (int) Range.clip(elevatorDesiredLevel, 0, 3) ;

            // elevator run logic
            if (elevatorDesiredLevel > elevatorLevel) { // raise
                if (elevatorInput == 0) { // initialize run
                    elevatorRuntime.reset();
                    elevatorInput = 1;
                } else { // was already running
                    if (elevatorRuntime.milliseconds() > level_duration) {
                        if (elevatorInput > 0) { // rising
                            elevatorLevel++;
                        } else { // lowering
                            elevatorLevel--;
                        }
                        elevatorInput = 0;
                    }
                }
            } else if (elevatorDesiredLevel < elevatorLevel) { // lower
                if (elevatorInput == 0) { // initialize run
                    elevatorRuntime.reset();
                    elevatorInput = -1;
                } else { // was already running
                    if (elevatorRuntime.milliseconds() > level_duration) {
                        if (elevatorInput > 0) { // rising
                            elevatorLevel++;
                        } else { // lowering
                            elevatorLevel--;
                        }
                        elevatorInput = 0;
                    }
                }
            } else { // reset to desired level
                if (elevatorInput != 0) { // running
                    if (elevatorRuntime.milliseconds() > level_duration) {
                        if (elevatorInput > 0) { // rising
                            elevatorLevel++;
                        } else { // lowering
                            elevatorLevel--;
                        }
                        elevatorInput = 0;
                    }
                }
            }

            // manual joystick control (disabled)
            // double elevatorInput = -gamepad1.left_stick_y;

            // if limit switch is depressed, elevator can only raise and level is set to 0
            if (digElevatorLimit.getState() == true) {
                elevatorInput = Range.clip(elevatorInput, 0, 1);
                elevatorLevel = 0;
            }

            // set elevator motor speed
            motorElevator.setPower(elevatorInput);


            // gripper servo logic & control
            if (gripper_press == false) {
                if (gamepad1.dpad_left == true) {
                    servoGripper.setPosition(gripper_open);
                    gripper_state = "OPEN";
                    gripper_press = true;
                } else if (gamepad1.dpad_right == true) {
                    servoGripper.setPosition(gripper_closed);
                    gripper_state = "CLOSED";
                    gripper_press = true;
                } else {
                    gripper_press = false;
                }
            } else{
                if (gamepad1.dpad_left == false && gamepad1.dpad_right == false){
                    gripper_press = false;
                }
            }

            // telemetry update
            telemetry.addData("Status", "Running");
            telemetry.addData("Desired elevator Level", String.valueOf(elevatorDesiredLevel));
            telemetry.addData("Elevator Level", String.valueOf(elevatorLevel));
            telemetry.addData("Block Gripper", String.valueOf(gripper_state));
            telemetry.update();
        }
    }
}
