package org.firstinspires.ftc.teamcode;

/**
 * Author: Big Brother
 * Created: Before Time
 * Goal: Control space and time.
 */

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "One (thing to rule them all)", group = "")

//Do you even java.
public class one extends LinearOpMode {
    // Create motor thing well do real hardware later ↓
    private DcMotor motorDriveBackLeft;
    private DcMotor motorDriveBackRight;
    private DcMotor motorDriveFrontLeft;
    private DcMotor motorDriveFrontRight;

    //OwO whats this??
    @Override
    public void runOpMode() {
        //Reference hardware configuration variables
        motorDriveBackLeft = hardwareMap.dcMotor.get("motorDriveBackLeft");
        motorDriveBackRight = hardwareMap.dcMotor.get("motorDriveBackRight");
        motorDriveFrontLeft = hardwareMap.dcMotor.get("motorDriveFrontLeft");
        motorDriveFrontRight = hardwareMap.dcMotor.get("motorDriveFrontRight");

        //Wow much motor going ways.
        motorDriveBackLeft.setDirection(DcMotor.Direction.REVERSE);
        motorDriveBackRight.setDirection(DcMotor.Direction.FORWARD);
        motorDriveFrontLeft.setDirection(DcMotor.Direction.FORWARD);
        motorDriveFrontRight.setDirection(DcMotor.Direction.REVERSE);

        //Console.log for java ftc bull****
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // OwO Big red buttion press.
        waitForStart();
        // Wow runner cool funni
        while (opModeIsActive()) {
            //Code.java
        }
    }
}
