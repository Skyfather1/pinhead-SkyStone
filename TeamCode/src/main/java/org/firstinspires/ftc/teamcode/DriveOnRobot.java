package org.firstinspires.ftc.teamcode;

/**
 * Author: big brother
 * Created: Before Time
 * Goal: Control space and time.
 */


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "DriveOnRobot", group = "")

//Do you even java.
public class DriveOnRobot extends LinearOpMode {
    //OwO whats this??

    //condition ? if true : if fails;
    private float goPlaces;
    private float rotate;
    private float sideToSide;


    @Override
    public void runOpMode() {
        //Console.log for java ftc bull****
        telemetry.addData("Say", "Hello World!");

        //init move class.
        Robot robot = new Robot(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // OwO Await big red buttion press.
        waitForStart();
        while (opModeIsActive()) {
<<<<<<< HEAD
            goPlaces = (float) gamepad1.right_trigger - gamepad1.left_trigger;
=======
            goPlaces = (float) Math.pow(Range.clip((gamepad1.right_trigger - gamepad1.left_trigger), -1, 1), 5);
>>>>>>> 98c24eff9b3b942d99ff113ba47e2ec06c8833cf
            rotate = (float) ((gamepad1.right_bumper ? 1 : 0) - (gamepad1.left_bumper ? 1 : 0));
            sideToSide = (float) gamepad1.left_stick_x;
            robot.drive(goPlaces, rotate, sideToSide, (float)1);
        }
    }
}
