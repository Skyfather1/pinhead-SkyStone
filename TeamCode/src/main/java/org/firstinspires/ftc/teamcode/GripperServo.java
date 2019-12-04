package org.firstinspires.ftc.teamcode;

/**
 * Author: Daan Stevenson
 * Created: 12/2/19
 * Goal: Move the block gripper servo open and closed via a button press
 */

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "GripperServo", group = "")

public class GripperServo extends LinearOpMode {

    // initialize I/O
    Servo   servoGripper;
    double gripper_open = 0.4;
    double gripper_closed = 0.8;
    boolean gripper_press = false;

    // gripper state string
    private String gripper_state = "OPEN";

    @Override
    public void runOpMode() {

        // reference Configuration variables
        servoGripper = hardwareMap.get(Servo.class, "servoGripper");

        // initialize tray servo position
        servoGripper.setPosition(gripper_open);

        // initializing telemetry
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

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
            telemetry.addData("Block Gripper", String.valueOf(gripper_state));
            telemetry.update();
        }
    }
}
