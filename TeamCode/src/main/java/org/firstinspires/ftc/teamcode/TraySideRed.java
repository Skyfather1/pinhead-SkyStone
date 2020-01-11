package org.firstinspires.ftc.teamcode;

/**
 * Author: Pinhead FTC
 * Created: 1/6/19
 * Goal: Autonomous mode to drag tray into scoring zone and park on line
 * Team: RED
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "TraySideRed", group = "")

public class TraySideRed extends LinearOpMode {

    // initialize I/O
    private DcMotor motorDriveBackLeft;
    private DcMotor motorDriveBackRight;
    private DcMotor motorDriveFrontLeft;
    private DcMotor motorDriveFrontRight;
    private DcMotorSimple motorElevator;
    private DigitalChannel digElevatorLimit;
    private DistanceSensor sensorRange;
    private ColorSensor downwardColorSensor;
    Servo servoGripper;
    Servo   servoTray;

    // gripper states
    double gripper_open = 0.4;
    double gripper_closed = 0.8;
    private String gripper_state = "OPEN";

    // initialize I/O
    double tray_up = 0.8;
    double tray_down = 0.3;
    private String tray_state = "UP";

    private ElapsedTime runtime = new ElapsedTime();

    static final double counts_per_inch = 753.2 / (3.1415 * 4);
    static final double counts_per_degree = counts_per_inch * 24 * 3.1415 / 360;
    static final double counts_per_inch_crab = counts_per_inch * 1;

    @Override
    public void runOpMode() {

        // reference Configuration variables
        motorDriveBackLeft = hardwareMap.dcMotor.get("motorDriveBackLeft");
        motorDriveBackRight = hardwareMap.dcMotor.get("motorDriveBackRight");
        motorDriveFrontLeft = hardwareMap.dcMotor.get("motorDriveFrontLeft");
        motorDriveFrontRight = hardwareMap.dcMotor.get("motorDriveFrontRight");

        // set motor directions
        motorDriveBackLeft.setDirection(DcMotor.Direction.REVERSE);
        motorDriveBackRight.setDirection(DcMotor.Direction.FORWARD);
        motorDriveFrontLeft.setDirection(DcMotor.Direction.FORWARD);
        motorDriveFrontRight.setDirection(DcMotor.Direction.REVERSE);

        // reference Configuration variables
        motorElevator = hardwareMap.get(DcMotorSimple.class, "motorElevator");
        digElevatorLimit = hardwareMap.get(DigitalChannel.class, "digElevatorLimit");
        servoGripper = hardwareMap.get(Servo.class, "servoGripper");
        servoTray = hardwareMap.get(Servo.class, "servoTray");
        sensorRange = hardwareMap.get(DistanceSensor.class, "sensor_range");
        downwardColorSensor = hardwareMap.get(ColorSensor.class, "downwardColorSensor");

        // set the digital channel to input.
        digElevatorLimit.setMode(DigitalChannel.Mode.INPUT);
        motorElevator.setDirection(DcMotorSimple.Direction.REVERSE);

        // lower elevator until limit switch is depressed
        while (digElevatorLimit.getState() == false) {
            motorElevator.setPower(-0.6);
        }
        motorElevator.setPower(0);
        double elevatorInput = 0;

        // initialize tray servo position
        servoGripper.setPosition(gripper_open);
        servoTray.setPosition(tray_up);

        // initializing telemetry
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        driveStraight(0.5,  -32,  10.0);
        servoTray.setPosition(tray_down);
        sleep(1500);
        tankTurn( 0.5, -45, 10.0);
        driveStraight(0.5,  40,  10.0);
        servoTray.setPosition(tray_up);
        sleep(1500);
        driveStraight(0.5,  6,  10.0);
        tankTurn( 0.5, 45, 10.0);

        // use encoders
        motorDriveBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorDriveBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorDriveFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorDriveFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        while (opModeIsActive() && downwardColorSensor.red() < 2000) {
            motorDriveBackLeft.setPower(0.25);
            motorDriveBackRight.setPower(0.25);
            motorDriveFrontLeft.setPower(0.25);
            motorDriveFrontRight.setPower(0.25);
        }
        motorDriveBackLeft.setPower(0);
        motorDriveBackRight.setPower(0);
        motorDriveFrontLeft.setPower(0);
        motorDriveFrontRight.setPower(0);

        driveStraight(0.5,  -6,  10.0);  // S1: Forward 10 Inches with 5 Sec timeout

        // telemetry update
        telemetry.addData("Path", "Complete");
        telemetry.update();

    }

    public void driveStraight(double speed,
                             double inches,
                             double timeoutS) {

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // reset encoders
            motorDriveBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motorDriveBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motorDriveFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motorDriveFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            // set target position
            motorDriveBackLeft.setTargetPosition((int)(inches * counts_per_inch));
            motorDriveBackRight.setTargetPosition((int)(inches * counts_per_inch));
            motorDriveFrontLeft.setTargetPosition((int)(inches * counts_per_inch));
            motorDriveFrontRight.setTargetPosition((int)(inches * counts_per_inch));

            // use encoders
            motorDriveBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorDriveBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorDriveFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorDriveFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            motorDriveBackLeft.setPower(Math.abs(speed));
            motorDriveBackRight.setPower(Math.abs(speed));
            motorDriveFrontLeft.setPower(Math.abs(speed));
            motorDriveFrontRight.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: logic is such that it runs until at least one wheel on the right,
            // AND at least one wheel on the left have completed distance
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    ((motorDriveBackLeft.isBusy() && motorDriveFrontLeft.isBusy()) || (motorDriveBackRight.isBusy() && motorDriveFrontRight.isBusy()))) {

                // O X    O O    X O    X O
                // O X    X X    O X    O O

                // Display it for the driver.
                telemetry.addData("Driving straight",  String.valueOf(inches) + " inches");
                telemetry.update();
            }

            // Stop all motion;
            motorDriveBackLeft.setPower(0);
            motorDriveBackRight.setPower(0);
            motorDriveFrontLeft.setPower(0);
            motorDriveFrontRight.setPower(0);

            //  sleep(250);   // optional pause after each move
        }
    }

    public void tankTurn(double speed,
                              double degrees,
                              double timeoutS) {

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // reset encoders
            motorDriveBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motorDriveBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motorDriveFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motorDriveFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            // set target position
            motorDriveBackLeft.setTargetPosition((int)(degrees * counts_per_degree));
            motorDriveBackRight.setTargetPosition(-(int)(degrees * counts_per_degree));
            motorDriveFrontLeft.setTargetPosition((int)(degrees * counts_per_degree));
            motorDriveFrontRight.setTargetPosition(-(int)(degrees * counts_per_degree));

            // use encoders
            motorDriveBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorDriveBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorDriveFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorDriveFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            motorDriveBackLeft.setPower(Math.abs(speed));
            motorDriveBackRight.setPower(Math.abs(speed));
            motorDriveFrontLeft.setPower(Math.abs(speed));
            motorDriveFrontRight.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: logic is such that it runs until at least one wheel on each diagonal have completed distance
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    ((motorDriveBackLeft.isBusy() && motorDriveFrontRight.isBusy()) || (motorDriveBackRight.isBusy() && motorDriveFrontLeft.isBusy()))) {

                // O X    O O    X O    X O
                // O X    X X    O X    O O

                // Display it for the driver.
                telemetry.addData("Rotating",  String.valueOf(degrees) + " degrees");
                telemetry.update();
            }

            // Stop all motion;
            motorDriveBackLeft.setPower(0);
            motorDriveBackRight.setPower(0);
            motorDriveFrontLeft.setPower(0);
            motorDriveFrontRight.setPower(0);

            //  sleep(250);   // optional pause after each move
        }
    }

    public void driveCrab(double speed,
                         double inches,
                         double timeoutS) {

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // reset encoders
            motorDriveBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motorDriveBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motorDriveFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motorDriveFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            // set target position
            motorDriveBackLeft.setTargetPosition(-(int)(inches * counts_per_inch_crab));
            motorDriveBackRight.setTargetPosition((int)(inches * counts_per_inch_crab));
            motorDriveFrontLeft.setTargetPosition((int)(inches * counts_per_inch_crab));
            motorDriveFrontRight.setTargetPosition(-(int)(inches * counts_per_inch_crab));

            // use encoders
            motorDriveBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorDriveBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorDriveFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorDriveFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            motorDriveBackLeft.setPower(Math.abs(speed));
            motorDriveBackRight.setPower(Math.abs(speed));
            motorDriveFrontLeft.setPower(Math.abs(speed));
            motorDriveFrontRight.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: logic is such that it runs until at least one wheel in the front,
            // AND at least one wheel in the back have completed distance
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    ((motorDriveBackLeft.isBusy() && motorDriveBackRight.isBusy()) || (motorDriveFrontLeft.isBusy() && motorDriveFrontRight.isBusy()))) {

                // O X    O O    X O    X O
                // O X    X X    O X    O O

                // Display it for the driver.
                telemetry.addData("Crabbing",String.valueOf(inches) + " inches");
                telemetry.update();
            }

            // Stop all motion;
            motorDriveBackLeft.setPower(0);
            motorDriveBackRight.setPower(0);
            motorDriveFrontLeft.setPower(0);
            motorDriveFrontRight.setPower(0);

            //  sleep(250);   // optional pause after each move
        }
    }
}
