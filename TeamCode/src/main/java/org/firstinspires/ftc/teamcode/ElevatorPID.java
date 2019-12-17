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
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Arrays;

@TeleOp(name = "ElevatorPID", group = "")

public class ElevatorPID extends LinearOpMode {

    // initialize I/O
    private DcMotorSimple motorElevator;
    private DigitalChannel digElevatorLimit;
    private DistanceSensor sensorRange;
    Servo servoGripper;
    double gripper_open = 0.4;
    double gripper_closed = 0.8;
    boolean gripper_press = false;

    // runtime variable
    private ElapsedTime loopTime = new ElapsedTime();
    private ElapsedTime gripOpenTime = new ElapsedTime();
    private String gripper_state = "OPEN";

    @Override
    public void runOpMode() {

        // reference Configuration variables
        motorElevator = hardwareMap.get(DcMotorSimple.class, "motorElevator");
        digElevatorLimit = hardwareMap.get(DigitalChannel.class, "digElevatorLimit");
        sensorRange = hardwareMap.get(DistanceSensor.class, "sensor_range");
        servoGripper = hardwareMap.get(Servo.class, "servoGripper");

        // you can also cast this to a Rev2mDistanceSensor if you want to use added
        // methods associated with the Rev2mDistanceSensor class.
        // Rev2mDistanceSensor sensorTimeOfFlight = (Rev2mDistanceSensor)sensorRange;

        // set the digital channel to input.
        digElevatorLimit.setMode(DigitalChannel.Mode.INPUT);
        motorElevator.setDirection(DcMotorSimple.Direction.REVERSE);

        // initialize tray servo position
        servoGripper.setPosition(gripper_open);
        // lower elevator until limit switch is depressed
        while (digElevatorLimit.getState() == false) {
            motorElevator.setPower(-0.4);
        }
        motorElevator.setPower(0);
        float elevatorInput = 0;

        // Setup an integer to define block level
        // 0 = lowest (on ground)
        // 1 = up 1 block level
        int elevatorDesiredLevel = 0;
        boolean gamepad_press = false;

        double desired_distance = 20;
        double measured_distance = sensorRange.getDistance(DistanceUnit.MM);
        double[] distance_array = new double[10]; // this value determines the filtering
        Arrays.fill(distance_array, measured_distance);
        double error = measured_distance - desired_distance;

        double last_error;
        double error_der;
        double error_int = 0;
        double PID_out;
        double loop_time;

        // initializing telemetry
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        loopTime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // gripper servo logic & control
            if (gripper_press == false) {
                if (gamepad1.dpad_left == true) {
                    gripper_state = "OPEN";
                    gripper_press = true;
                    gripOpenTime.reset();
                } else if (gamepad1.dpad_right == true) {
                    gripper_state = "CLOSED";
                    gripper_press = true;
                    servoGripper.setPosition(gripper_closed);
                    sleep(500);
                } else {
                    gripper_press = false;
                }
            } else{
                if (gamepad1.dpad_left == false && gamepad1.dpad_right == false){
                    gripper_press = false;
                }
            }

            if (gripper_state == "OPEN" && gripOpenTime.milliseconds() > 500){
                servoGripper.setPosition(gripper_open);
            }

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
            elevatorDesiredLevel = (int) Range.clip(elevatorDesiredLevel, 0, 2);

            // elevator run logic
            if (gripper_state == "OPEN"){
                if (elevatorDesiredLevel == 0) {
                    desired_distance = 20;
                } else if (elevatorDesiredLevel == 1) {
                    desired_distance = 120;
                } else if (elevatorDesiredLevel == 2) {
                    desired_distance = 220;
                }
            } else {
                if (elevatorDesiredLevel == 0) {
                    desired_distance = 40;
                } else if (elevatorDesiredLevel == 1) {
                    desired_distance = 140;
                } else if (elevatorDesiredLevel == 2) {
                    desired_distance = 240;
                }
            }

            // logic for simple average filtering of signal
            System.arraycopy(distance_array, 0 , distance_array, 1, distance_array.length - 1);
            distance_array[0] = sensorRange.getDistance(DistanceUnit.MM);
            measured_distance = 0;
            for (int i = 0; i < distance_array.length; i++ ) {
                measured_distance += distance_array[i];
            }
            measured_distance /= distance_array.length;

            // PID logic
            loop_time = loopTime.seconds();
            last_error = error;
            error = measured_distance - desired_distance;
            error_der = (error - last_error) / loop_time;
            error_int += last_error * loop_time;
            loopTime.reset();

            PID_out = - (0.02 * error + 0.001 * error_der + 0.002 * error_int);
            elevatorInput =  (float) Range.clip(PID_out, -1, 1);
            //if (Math.abs(elevatorInput) < 0.3) {
            //    elevatorInput = 0;
            //}

            // manual joystick control by bumper
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
            //telemetry.addData("Desired elevator level", String.valueOf(elevatorDesiredLevel));
            telemetry.addData("Last measurement", String.format("%.01f mm", distance_array[0]));
            telemetry.addData("Averaged elevator distance", String.format("%.01f mm", measured_distance));
            telemetry.addData("PID Output", String.valueOf(PID_out));
            telemetry.addData("Loop Time", String.valueOf(loop_time));

            telemetry.update();
        }
    }
}
