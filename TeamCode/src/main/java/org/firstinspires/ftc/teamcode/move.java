public class move {
    private DcMotor motorFL;
    private DcMotor motorFR;
    private DcMotor motorBL;
    private DcMotor motorBR;
    public move(DcMotor motorDriveFrontLeft, DcMotor motorDriveFrontRight, DcMotor motorDriveBackLeft, DcMotor motorDriveBackRight) {
        motorDriveFrontLeft = motorFL;
        motorDriveFrontRight = motorFR;
        motorDriveBackLeft = motorBL;
        motorDriveBackRight = motorBR;
    }
    public void drive(Gamepad gamepad1) {
        //code soon to come!
    }
}
