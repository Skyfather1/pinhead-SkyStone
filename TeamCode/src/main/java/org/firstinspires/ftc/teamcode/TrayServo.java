package org.firstinspires.ftc.teamcode;

/**
 * Author: Daan Stevenson
 * Created: 11/20/19
 * Goal: Move the tray gripper servo up and down via a button press
 */

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "TrayServo", group = "")

public class TrayServo extends LinearOpMode {

    // initialize I/O
    Servo   servoTray;
    double tray_up = 0.5;
    double tray_down = 0.6;
    boolean gamepad_press = false;

    // slow mode string
    private String tray_state = "UP";

    @Override
    public void runOpMode() {

        // reference Configuration variables
        servoTray = hardwareMap.get(Servo.class, "servoTray");

        // initialize tray servo position
        servoTray.setPosition(tray_up);

        // initializing telemetry
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // elevator set level logic
            if (gamepad_press == false) {
                if (gamepad1.y == true) {
                    servoTray.setPosition(tray_up);
                    tray_state = "UP";
                    gamepad_press = true;
                } else if (gamepad1.a == true) {
                    servoTray.setPosition(tray_down);
                    tray_state = "DOWN";
                    gamepad_press = true;
                } else {
                    gamepad_press = false;
                }
            } else{
                if (gamepad1.dpad_up == false && gamepad1.dpad_down == false){
                    gamepad_press = false;
                }
            }

            // telemetry update
            telemetry.addData("Status", "Running");
            telemetry.addData("Tray servo", String.valueOf(tray_state));
            telemetry.update();
        }
    }
}
