package org.firstinspires.ftc.teamcode;

/**
 * Author: cave men?
 * Created: Before Common Era
 * Goal: DANCE
 */


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@Autonomous(name = "Dance", group = "")

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

        robot.gripper(false);
        robot.tray(false);
        while(robot.elevator((float)-0.4) == true);
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // OwO Await big red button press.
        waitForStart();
        while (opModeIsActive()) {
            
        }
    }
}
