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

@TeleOp(name = "???", group = "")

//Do you even java.
public class DriveOnRobot extends LinearOpMode {
    //OwO whats this??

    //condition ? if true : if fails;
    float goPlaces;
    float rotate;
    float sideToSide;


    @Override
    public void runOpMode() {
        //init move class.
        Robot robot = new Robot(hardwareMap);

        //Console.log for java ftc bull****
        telemetry.addData("Say", "Hello World!");
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // OwO Await big red buttion press.
        waitForStart();
        while (opModeIsActive()) {
            goPlaces = (float) Math.pow(Range.clip(-(gamepad1.right_trigger - gamepad1.left_trigger), -1, 1), 5);
            rotate = (float) ((gamepad1.right_bumper ? 1 : 0) - (gamepad1.left_bumper ? 1 : 0));
            sideToSide = (float) Math.pow(gamepad1.left_stick_x, 5);
            robot.Drive(goPlaces, rotate, sideToSide, (float)1)
        }
    }
}
