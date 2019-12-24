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
import com.qualcomm.robotcore.util.Range;

@Autonomous(name = "EncoderDrive", group = "")

public class EncoderDrive extends LinearOpMode {

    // initialize I/O
    private DcMotor motorDriveBackLeft;
    private DcMotor motorDriveBackRight;
    private DcMotor motorDriveFrontLeft;
    private DcMotor motorDriveFrontRight;

    double counts_per_inch = 753.2 / (3.1415 * 4);

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

        // reset encoders
        motorDriveBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorDriveBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorDriveFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorDriveFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        double slowDownCoeff = 0.5;

        // initializing telemetry
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // set target position
            motorDriveBackLeft.setTargetPosition((int)(10 * counts_per_inch));
            motorDriveBackRight.setTargetPosition((int)(10 * counts_per_inch));
            motorDriveFrontLeft.setTargetPosition((int)(10 * counts_per_inch));
            motorDriveFrontRight.setTargetPosition((int)(10 * counts_per_inch));


            // use encoders
            motorDriveBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorDriveBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorDriveFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorDriveFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            //set powers
            motorDriveBackLeft.setPower(slowDownCoeff);
            motorDriveBackRight.setPower(slowDownCoeff);
            motorDriveFrontLeft.setPower(slowDownCoeff);
            motorDriveFrontRight.setPower(slowDownCoeff);

            while(motorDriveBackLeft.isBusy() || motorDriveBackRight.isBusy() || motorDriveFrontLeft.isBusy() || motorDriveFrontRight.isBusy()){

            }

            //set powers
            motorDriveBackLeft.setPower(0);
            motorDriveBackRight.setPower(0);
            motorDriveFrontLeft.setPower(0);
            motorDriveFrontRight.setPower(0);

            // telemetry update
            telemetry.addData("Status", "Running");
            telemetry.update();

        }

    }
}
