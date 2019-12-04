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
    //OwO whats this??
    @Override
    public void runOpMode() {



        //This is how to set up our motors to run with contollers
        //motorBL.setMode(DcMotorController.RunMode.RESET_ENCODER)
        //motorBL.setMode(DcMotorController.RunMode.RUN_WITH_ENCODERS)

        //This is how you get the current rotations of the encoder
        //motorBL.getCurrentPosition();
        /*
        So, the plan is to take one wheel(maybe the slowest) and set this wheel as a
        baseline to set the speed for all the other wheels using ratios to get acurate numbers
        :)
        */

        //Wow much motor going ways.


        //init move class.
        move mover = new move(motorFL, motorFR, motorBL, motorBR);

        //Console.log for java ftc bull****
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // OwO Await big red buttion press.
        waitForStart();
        while (opModeIsActive()) {
            mover.drive(gamepad1);
        }
    }
}
