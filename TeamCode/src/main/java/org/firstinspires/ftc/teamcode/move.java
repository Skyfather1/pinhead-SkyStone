import com.qualcomm.robotcore.hardware.DcMotor;

public class move {
    private DcMotor motorFL;
    private DcMotor motorFR;
    private DcMotor motorBL;
    private DcMotor motorBR;
    public move(DcMotor motorDriveFrontLeft, DcMotor motorDriveFrontRight, DcMotor motorDriveBackLeft, DcMotor motorDriveBackRight) {
        motorDriveFrontLeft = this.motorFL;
        motorDriveFrontRight = this.motorFR;
        motorDriveBackLeft = this.motorBL;
        motorDriveBackRight = this.motorBR;

        //Ni-chan! Don't forget hardware.
        this.motorFL = hardwareMap.dcMotor.get("motorDriveFrontLeft");
        this.motorFR = hardwareMap.dcMotor.get("motorDriveFrontRight");
        this.motorBL = hardwareMap.dcMotor.get("motorDriveBackLeft");
        this.motorBR = hardwareMap.dcMotor.get("motorDriveBackRight");
        //Set DIRECTION@MOVE.net
        this.motorFL.setDirection(DcMotor.Direction.FORWARD);
        this.motorFR.setDirection(DcMotor.Direction.REVERSE);
        this.motorBL.setDirection(DcMotor.Direction.REVERSE);
        this.motorBR.setDirection(DcMotor.Direction.FORWARD);
    }
    public void drive(Gamepad gamepad1) {
        //code soon to come!
    }
}
