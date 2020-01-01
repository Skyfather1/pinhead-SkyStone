package org.firstinspires.ftc.teamcode;

/**
 * Author: Daan Stevenson
 * Created: 12/23/19
 * Goal: Simple autonomous control using encoders
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@Autonomous(name = "EncoderDrive", group = "")

public class EncoderDrive extends LinearOpMode {

    // initialize I/O
    private DcMotor motorDriveBackLeft;
    private DcMotor motorDriveBackRight;
    private DcMotor motorDriveFrontLeft;
    private DcMotor motorDriveFrontRight;

    private ElapsedTime runtime = new ElapsedTime();

    static final double counts_per_inch = 753.2 / (3.1415 * 4);
    static final double counts_per_degree = counts_per_inch * 18 * 3.1415 / 360;
    static final double counts_per_inch_crab = counts_per_inch * 3;

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

        // initializing telemetry
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            driveStraight(0.5,  10,  5.0);  // S1: Forward 10 Inches with 5 Sec timeout
            tankTurn( 0.3, 90, 2.0);  // S2: Rotate 90 degrees right with 2 Sec timeout
            driveCrab(0.7,  10,  8.0);  // S3: Crab right 10 Inches with 5 Sec timeout

            // telemetry update
            telemetry.addData("Path", "Complete");
            telemetry.update();

        }

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
                telemetry.addData("Driving straight",  "%4d inches", inches);
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
                telemetry.addData("Rotating",  "%4d degrees", degrees);
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
                telemetry.addData("Crabbing","%4d inches", inches);
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