package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "Wack AF", group = "")
public class satanControles extends LinearOpMode {

    // initialize I/O
    private DcMotor motorDrive[];
    private String motors[] = {
      "motorDriveBackLeft",
      "motorDriveBackRight",
      "motorDriveFrontLeft",
      "motorDriveFrontRight"
    };
    // slow mode string
    private String slow_mode = "DISENGAGED";

    @Override
    public void runOpMode() {

        // reference Configuration variables

        for(int i = 0; i < motors.length(); i++) {
            motorDrive[i] = hardwareMap.dcMotor.get(motors[i]);
        }

        // set motor directions
        motorDrive[0].setDirection(DcMotor.Direction.REVERSE);
        motorDrive[1].setDirection(DcMotor.Direction.FORWARD);
        motorDrive[2].setDirection(DcMotor.Direction.FORWARD);
        motorDrive[3].setDirection(DcMotor.Direction.REVERSE);

        // initializing telemetry
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // coefficients
        float slowDownCoeff = 1;
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // logic to engage slow mode (no debounce)
            if (gamepad1.left_bumper == true)
            {
                if (isPressed == false) {
                    if (slowDownCoeff == 1){
                        slowDownCoeff = (float) 0.3;
                        slow_mode = "ENGAGED";
                    }
                    else {
                        slowDownCoeff = (float) 1;
                        slow_mode = "DISENGAGED";
                    }

                }
            }
            else{
                if (isPressed == true){
                    isPressed = false;
                }
            }

            // load inputs from gamepad
            float for_bak = - (gamepad1.right_stick_y + gamepad1.left_stick_y);
            for_bak = Range.clip (for_bak, -1, 1);
            float crab = gamepad1.right_stick_x;
            float rotate = gamepad1.left_stick_x;

            // raise power to improve low level stick response
            for_bak = (float) Math.pow ( for_bak, 5);
            crab = (float) Math.pow ( crab, 5);
            rotate = (float) Math.pow ( rotate , 7);

            // combos for mecanum wheels
            float leftFront = for_bak + crab + rotate;
            float rightFront = for_bak - crab - rotate;
            float leftRear = for_bak - crab + rotate;
            float rightRear = for_bak + crab - rotate;

            // must keep things proportional when the sum for any wheel > 1
            float frontMax = Math.max(Math.abs(leftFront), Math.abs(rightFront));
            float rearMax = Math.max(Math.abs(leftRear), Math.abs(rightRear));
            float maxDrive = Math.max(frontMax, rearMax);
            // maxDrive = Range.clip(maxDrive, 1, 3);
            maxDrive = (maxDrive > 1) ? maxDrive : 1;

            // scale and clip all motors
            leftFront = leftFront/maxDrive;
            rightFront = rightFront/maxDrive;
            leftRear = leftRear/maxDrive;
            rightRear = rightRear/maxDrive;
            // leftFront = Range.clip(leftFront, -1, 1);
            // rightFront = Range.clip(rightFront, -1, 1);
            // leftRear = Range.clip(leftRear, -1, 1);
            // rightRear = Range.clip(rightRear, -1, 1);

            // set motor powers
            motorDriveBackLeft.setPower(leftRear * slowDownCoeff * 1);
            motorDriveBackRight.setPower(rightRear * slowDownCoeff * 1);
            motorDriveFrontLeft.setPower(leftFront * slowDownCoeff * 1);
            motorDriveFrontRight.setPower(rightFront * slowDownCoeff * 1);

            // telemetry update
            telemetry.addData("Status", "Running");
            telemetry.addData("Slow Mode", slow_mode);
            telemetry.update();

        }

    }
}
