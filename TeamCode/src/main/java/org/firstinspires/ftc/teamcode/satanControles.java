package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Disabled
@TeleOp(name = "Wack AF", group = "")
public class satanControles extends LinearOpMode {

    private DcMotor motorDriveBackLeft;
    private DcMotor motorDriveBackRight;
    private DcMotor motorDriveFrontLeft;
    private DcMotor motorDriveFrontRight;
    private DcMotor motorDriveLifter;
    private Servo GripServo;
    private Servo trayDragServo;

    @Override
    public void runOpMode () {
        motorDriveBackLeft = hardwareMap.dcMotor.get("motorDriveBackLeft");
        motorDriveBackRight = hardwareMap.dcMotor.get("motorDriveBackRight");
        motorDriveFrontLeft = hardwareMap.dcMotor.get("motorDriveFrontLeft");
        motorDriveFrontRight = hardwareMap.dcMotor.get("motorDriveFrontRight");
        //motorDriveLifter = hardwareMap.dcMotor.get("motorDriveLifter");
        GripServo = hardwareMap.servo.get("GripServo");
        //trayDragServo = hardwareMap.servo.get("trayDragServo");
        waitForStart();
        if (opModeIsActive()) {
            motorDriveBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motorDriveBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motorDriveFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motorDriveFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            while (opModeIsActive()) {

                if (opModeIsActive()) {

                    motorDriveBackLeft.setPower(-gamepad1.right_stick_y * 0.25);
                    motorDriveBackRight.setPower(gamepad1.right_stick_y * 0.25);
                    motorDriveFrontRight.setPower(-gamepad1.right_stick_y * 0.25);
                    motorDriveFrontLeft.setPower(gamepad1.right_stick_y * 0.25);

                    motorDriveBackLeft.setPower(gamepad1.right_stick_x * 0.25);
                    motorDriveBackRight.setPower(gamepad1.right_stick_x * 0.25);
                    motorDriveFrontRight.setPower(gamepad1.right_stick_x * 0.25);
                    motorDriveFrontLeft.setPower(gamepad1.right_stick_x * 0.25);
                }
                if (gamepad1.left_bumper) {
                    GripServo.setPosition(.25);
                } else {
                    GripServo.setPosition(.5);
                }
                telemetry.update();
            }
        }
    }
}
