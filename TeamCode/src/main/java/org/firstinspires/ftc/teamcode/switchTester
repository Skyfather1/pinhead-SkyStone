package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;

@TeleOp(name = "ResetSERVOPosition", group = "")
public class ResetSERVOPosition extends LinearOpMode {
    private DcMotor lifterMotor;
    private Servo GripServo;
    private TouchSensor limitSwitch;
    /**
     * This function is executed when this Op Mode is selected from the Driver Station.
     */
    @Override
    public void runOpMode() {
        limitSwitch = hardwareMap.touchSensor.get("limitSwitch");
        GripServo = hardwareMap.servo.get("GripServo");
        lifterMotor = hardwareMap.dcMotor.get("lifterMotor");
        // Put initialization blocks here.
        waitForStart();
        if (opModeIsActive()) {
            // Put run blocks here.
            while (opModeIsActive()) {
                // Put loop blocks here.
                if (limitSwitch.isPressed()) {
                    lifterMotor.setPower(1);
                }
                telemetry.update();

            }
        }
    }
