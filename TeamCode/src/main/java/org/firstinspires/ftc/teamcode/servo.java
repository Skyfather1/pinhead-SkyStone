package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "Servo Class (Tray Gripper)", group = "")
public class servo extends LinearOpMode {
    public String servos[] = {
        "GripServo",
        "trayDragServo"
    };

    private Servo x[];

    @Override
    public void runOpMode () {
        for(int i = 0; i < servos.length(); i++) {
            x.add(hardwareMap.get(Servo.class, servos[i]));
        }
        waitForStart();
        while (opModeIsActive()) {
            if(gamepad1.a){
                servos[1].setPosition(1);
            } else {
                servos[1].setPosition(0)
            }
        }
    }
}
