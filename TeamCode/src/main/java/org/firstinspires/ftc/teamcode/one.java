package org.firstinspires.ftc.teamcode;

/**
 * Author: Big Brother
 * Created: Before Time
 * Goal: Control space and time.
 */


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "One (thing to rule them all)", group = "")

//Do you even java.
public class one extends LinearOpMode {
    //OwO whats this??
    @Override
    public void runOpMode() {
        //init move class.
        Robot robot = new Robot(hardwareMap, gamepad1);

        //Console.log for java ftc bull****
        telemetry.addData("Say", "Hello World!");
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // OwO Await big red buttion press.
        waitForStart();
        while (opModeIsActive()) {
            robot.drive((float)1);
        }
    }
}
