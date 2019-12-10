package org.firstinspires.ftc.teamcode;

/**
 * Author: Daan Stevenson
 * Created: 11/16/19
 * Goal: Move the elevator in discrete steps
 * Motor is controlled by REV Spark Mini
 * Limit switch is used to locate lowest position
 */

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name = "ElevatorPID", group = "")

public class ElevatorPID extends LinearOpMode {

    // initialize I/O
    private DcMotorSimple motorElevator;
    private DigitalChannel digElevatorLimit;
    private DistanceSensor sensorRange;
    // runtime variable
    private ElapsedTime elevatorRuntime = new ElapsedTime();

    @Override
    public void runOpMode() {

        // reference Configuration variables
        motorElevator = hardwareMap.get(DcMotorSimple.class, "motorElevator");
        digElevatorLimit = hardwareMap.get(DigitalChannel.class, "digElevatorLimit");
        sensorRange = hardwareMap.get(DistanceSensor.class, "sensor_range");

        // you can also cast this to a Rev2mDistanceSensor if you want to use added
        // methods associated with the Rev2mDistanceSensor class.
        Rev2mDistanceSensor sensorTimeOfFlight = (Rev2mDistanceSensor)sensorRange;

        // set the digital channel to input.
        digElevatorLimit.setMode(DigitalChannel.Mode.INPUT);
        motorElevator.setDirection(DcMotorSimple.Direction.REVERSE);

        // lower elevator until limit switch is depressed
        while (digElevatorLimit.getState() == false) {
            motorElevator.setPower(-0.4);
        }
        motorElevator.setPower(0);
        float elevatorInput = 0;

        // Setup an integer to define block level
        // 0 = lowest (on ground)
        // 1 = up 1 block level
        int elevatorLevel = 0;
        int elevatorDesiredLevel = 0;
        boolean gamepad_press = false;
        double level_duration = 600;

        double desired_distance = 20;
        double measured_distance = sensorRange.getDistance(DistanceUnit.MM);
        double[] distance_array = new double[]{measured_distance, measured_distance, measured_distance, measured_distance, measured_distance};
        double error = 0;
        double last_error = 0;
        double error_der = 0;
        double error_int = 0;
        double PID_out = 0;

        // initializing telemetry
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // elevator set level logic
            if (gamepad_press == false) {
                if (gamepad1.dpad_up == true) {
                    elevatorDesiredLevel++;
                    gamepad_press = true;
                } else if (gamepad1.dpad_down == true) {
                    elevatorDesiredLevel--;
                    gamepad_press = true;
                } else {
                    gamepad_press = false;
                }
            } else {
                if (gamepad1.dpad_up == false && gamepad1.dpad_down == false) {
                    gamepad_press = false;
                }
            }
            elevatorDesiredLevel = (int) Range.clip(elevatorDesiredLevel, 0, 3);

            // elevator run logic

            if (elevatorDesiredLevel == 0) {
                desired_distance = 20;
            } else if (elevatorDesiredLevel == 1) {
                desired_distance = 120;
            } else if (elevatorDesiredLevel == 2) {
                desired_distance = 220;
            } else if (elevatorDesiredLevel == 3) {
                desired_distance = 320;
            }

            for (int i = 3; i >= 0; i--) {
                distance_array[i+1] = distance_array[i];
            }
            distance_array[0] = sensorRange.getDistance(DistanceUnit.MM);
            measured_distance = ( distance_array[0] + distance_array[1] + distance_array[2] + distance_array[3] + distance_array[4] ) / 5;

            last_error = error;
            error = measured_distance - desired_distance;
            error_der = (error - last_error) / 0.01;
            error_int += last_error * 0.01;

            PID_out = - (0.04 * error + 0.001 * error_der + 0.005 * error_int);
            elevatorInput =  (float) Range.clip(PID_out, -1, 1);
            if (Math.abs(elevatorInput) < 0.3) {
                elevatorInput = 0;
            }

            // manual joystick control (disabled)
            if (gamepad1.right_bumper == true) {
                elevatorInput = -gamepad1.left_stick_y;
            }

            // if limit switch is depressed, elevator can only raise and level is set to 0
            if (digElevatorLimit.getState() == true) {
                elevatorInput = Range.clip(elevatorInput, 0, 1);
            }

            // set elevator motor speed
            motorElevator.setPower(elevatorInput);

            // telemetry update
            telemetry.addData("Status", "Running");
            telemetry.addData("Desired elevator Level", String.valueOf(elevatorDesiredLevel));
            telemetry.addData("range", String.format("%.01f mm", measured_distance));
            telemetry.addData("PID Output", String.valueOf(PID_out));
            telemetry.update();
        }
    }
}
