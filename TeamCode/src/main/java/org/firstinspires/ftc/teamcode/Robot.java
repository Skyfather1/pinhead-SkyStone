package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Robot {
    private DcMotor motorFL; //front left -1 to 1 forward and back
    private DcMotor motorFR; //front right -1 to 1
    private DcMotor motorBL; //back left -1 to 1
    private DcMotor motorBR; //back right -1 to 1
    private DcMotorSimple elevator; //elevator motor -1 to 1 to up or down.
    private DigitalChannel elevatorLimit; //limmit switch at bottom
    //distance sensor on elevator lower numbers at bottom
    private DistanceSensor elevatorSensor;
    Servo gripper;
    Servo trayGrab;

    static final double ticksInch = 753.2 / (3.1415 * 4);
    static final double ticksDegree = ticksInch * 24 * 3.1415 / 360;

    private HardwareMap hardwareMap = null;
    public Robot(HardwareMap hardwareMap_) {
        hardwareMap = hardwareMap_;
        //Ni-chan! Don't forget hardware.
        motorFL = hardwareMap.get(DcMotor.class, "motorDriveFrontLeft");
        motorFR = hardwareMap.get(DcMotor.class, "motorDriveFrontRight");
        motorBL = hardwareMap.get(DcMotor.class, "motorDriveBackLeft");
        motorBR = hardwareMap.get(DcMotor.class, "motorDriveBackRight");

        elevator = hardwareMap.get(DcMotorSimple.class, "motorElevator");
        elevatorLimit = hardwareMap.get(DigitalChannel.class, "digElevatorLimit");
        elevatorSensor = hardwareMap.get(DistanceSensor.class, "sensor_range");

        //
        this.motorFL.setDirection(DcMotor.Direction.FORWARD);
        this.motorFR.setDirection(DcMotor.Direction.REVERSE);
        this.motorBL.setDirection(DcMotor.Direction.REVERSE);
        this.motorBR.setDirection(DcMotor.Direction.FORWARD);

        gripper = hardwareMap.get(Servo.class, "servoGripper");
        trayGrab = hardwareMap.get(Servo.class, "servoTray");

        elevator.setDirection(DcMotorSimple.Direction.REVERSE);
        elevatorLimit.setMode(DigitalChannel.Mode.INPUT);
    }

    //Driver for the mecanum wheels set as a function.
    public void drive(float goPlaces, float rotate, float sideToSide, float motorMove) {
        goPlaces = (float) Math.pow(Range.clip(goPlaces, -1, 1), 5);
        rotate = (float) Range.clip(rotate, -1, 1);
        sideToSide = (float) Math.pow(Range.clip(sideToSide, -1, 1), 5);
        motorMove = (motorMove > 1) ? 1 : motorMove;
        motorMove = (motorMove < 0) ? 0 : motorMove;

        //Combos for mecanum wheels HH→NK
        float toMotorFL = goPlaces + sideToSide + rotate;
        float toMotorFR = goPlaces - sideToSide - rotate;
        float toMotorBL = goPlaces - sideToSide + rotate;
        float toMotorBR = goPlaces + sideToSide - rotate;

        // must keep things proportional when the sum for any wheel > 1
        float frontMax = Math.max(Math.abs(toMotorFL), Math.abs(toMotorFR));
        float rearMax = Math.max(Math.abs(toMotorBL), Math.abs(toMotorBR));
        float maxDrive = Math.max(frontMax, rearMax);

        //must be a thing
        maxDrive = (maxDrive > 1) ? maxDrive : 1;

        //do thong so can drive
        toMotorFL = toMotorFL/maxDrive;
        toMotorFR = toMotorFR/maxDrive;
        toMotorBL = toMotorBL/maxDrive;
        toMotorBR = toMotorBR/maxDrive;

        //drive
        motorFL.setPower(toMotorFL * motorMove);
        motorFR.setPower(toMotorFR * motorMove);
        motorBL.setPower(toMotorBL * motorMove);
        motorBR.setPower(toMotorBR * motorMove);
    }
    public boolean elevator(float upDown) {
        upDown = Range.clip(upDown, -1, 1);
        if(elevatorLimit.getState()) {
            if (upDown >= 0) {
                elevator.setPower(upDown);
                return true;
            } else {
                elevator.setPower(0);
                return false;
            }
        } else if (elevatorSensor.getDistance(DistanceUnit.MM) >= 250) {
            if(upDown <= 0) {
                elevator.setPower(upDown);
                return true;
            } else {
                elevator.setPower(0);
                return false;}
        } else {
            elevator.setPower(upDown);
            return true;
        }
    }
    public void gripper(boolean clicked) {
        gripper.setPosition(clicked ? 0.8 : 0.4);
    }
    public void tray(boolean clicked) {
        trayGrab.setPosition(clicked ? 0.8 : 0.3);
    }
}
