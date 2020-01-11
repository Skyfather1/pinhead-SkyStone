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
        //Console.log for java ftc thonk
        telemetry.addData("Say", "Hello World!");

        //init move class.
        Robot robot = new Robot(hardwareMap);

        while(robot.elevator((float)-0.4) == true);

        robot.gripper(false);
        boolean gripper = false;

        boolean slowMode = false;
        boolean slowPress = false;
        float slowSpeed = 1;

        boolean trayPress = false;
        boolean trayDrag = false;

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // OwO Await big red button press.
        waitForStart();
        robot.gripper(gripper);
        while (opModeIsActive()) {
            if (gamepad1.x) {
                if (!slowPress) {
                    if (slowMode){
                        slowSpeed = (float)1.0;
                        slowMode = false;
                    } else {
                        slowSpeed = (float)0.3;
                        slowMode = true;
                    }
                    slowPress = true;
                }
            } else {
                if (slowPress) {
                    slowPress = false;
                }
            }
            if (gamepad1.a) {
                if (!trayPress) {
                    if (trayDrag){
                        trayDrag = false;
                    } else {
                        trayDrag = true;
                    }
                    trayPress = true;
                }
            } else {
                if (trayPress) {
                    trayPress = false;
                }
            }
            robot.tray(trayDrag);
            goPlaces = gamepad1.right_trigger - gamepad1.left_trigger;
            rotate = (float) ((gamepad1.right_bumper ? 1 : 0) - (gamepad1.left_bumper ? 1 : 0));
            sideToSide = gamepad1.left_stick_x;
            robot.drive(goPlaces, rotate, sideToSide, slowSpeed);
            robot.elevator(-gamepad1.right_stick_y);
            if(gamepad1.right_stick_x > (float)0.7) {
                gripper = false;
            } else if (gamepad1.right_stick_x < (float)-0.7) {
                gripper = true;
            }
            robot.gripper(gripper);
        }
    }
}
