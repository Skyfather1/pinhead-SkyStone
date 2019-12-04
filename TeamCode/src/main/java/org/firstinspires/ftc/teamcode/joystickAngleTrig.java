//I promace this will do something usefull please dont delete my code!!!
import Java.lang.Math;

public class joystickAngleTrig {
    private float x;
    private float y;
    public joystickAngleTrig(float stickX, float stickY) {
        this.x = stickX;
        this.y = stickY;
    }
    public float getAngle() {
        return Math.atan(x/y)*(180/(22/7));
    }
    public void updateaAngle(float stickX, float stickY) {
        this.x = stickX;
        this.y = stickY;
    }
}
