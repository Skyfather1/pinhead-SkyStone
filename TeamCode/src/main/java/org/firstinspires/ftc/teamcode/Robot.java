package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Robot {
    private DcMotor motorFL;
    private DcMotor motorFR;
    private DcMotor motorBL;
    private DcMotor motorBR;

    HardwareMap hardwareMap = null;
    public Robot() {

    }
    public void init(HardwareMap ahwMap) {

        //Ni-chan! Don't forget hardware.
        this.motorFL = hardwareMap.get(DcMotor.class, "motorDriveFrontLeft");
        this.motorFR = hardwareMap.get(DcMotor.class, "motorDriveFrontRight");
        this.motorBL = hardwareMap.get(DcMotor.class, "motorDriveBackLeft");
        this.motorBR = hardwareMap.get(DcMotor.class, "motorDriveBackRight");
        //Set DIRECTION@MOVE.net
        this.motorFL.setDirection(DcMotor.Direction.FORWARD);
        this.motorFR.setDirection(DcMotor.Direction.REVERSE);
        this.motorBL.setDirection(DcMotor.Direction.REVERSE);
        this.motorBR.setDirection(DcMotor.Direction.FORWARD);
    }
}
