package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.robotcore.util.ElapsedTime;


public class RobotAuto extends Robot {
    //pass the hardware map back so I can use to extend the Robot class
    public RobotAuto (HardwareMap hardwareMap_) { super(hardwareMap_); }
    static final double countsInch = 753.2 / (3.1415 * 4);
    static final double countsDegree = countsInch * 24 * 3.1415 / 360;

    /**
    *@prams forward-backward, rotation degrees, side-side, speed, timeout
    */
    public void drive(float y, float rotate, float x, float speed, float timeoutSeconds) {
        //make sure nothig exceeds whats going here
        y = Range.clip(y, -1, 1);
        x = Range.clip(x, -1, 1);
        rotate = Range.clip(rotate, -1, 1);

        speed = (float) Range.clip(Math.abs(speed), 0, 1);
        timeoutSeconds = (timeoutSeconds < 0) ? 0 : timeoutSeconds;

        //reset encoders to for the drive
        motorFL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //Combos for mecanum wheels HH→NK
        int toMotorFL = (int)((y * countsInch) + (x * countsInch) + (rotate * countsDegree));
        int toMotorFR = (int)((y * countsInch) - (x * countsInch) - (rotate * countsDegree));
        int toMotorBL = (int)((y * countsInch) - (x * countsInch) + (rotate * countsDegree));
        int toMotorBR = (int)((y * countsInch) + (x * countsInch) - (rotate * countsDegree));

        //use encoders
        motorFL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //reset timeout time and start drive
        runtime.reset();
        motorFL.setPower(speed);
        motorFR.setPower(speed);
        motorBL.setPower(speed);
        motorBR.setPower(speed);

        boolean motorsBusy = motorBL.isBusy() ||
                             motorFL.isBusy() ||
                             motorBR.isBusy() ||
                             motorFR.isBusy();

        int motorGoto = (toMotorFL * toMotorFR * toMotorBL * toMotorBR) / 4;
        while (runtime.seconds() < timeoutSeconds && motorsBusy) {
            int motorPos = (
                motorFL.getCurrentPosition()*
                motorFR.getCurrentPosition()*
                motorBL.getCurrentPosition()*
                motorBR.getCurrentPosition()
            ) / 4;

            // telemetry.addData("Driving:",  String.valueOf(motorGoto/motorPos*100)+"% Complete!");
            // telemetry.update();
        }
    }
}
