package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private Vector3f pos = new Vector3f(0,0,0);
    private float pitch,yaw,roll;


    public Camera(){

    }

    public void move(){
        if (Keyboard.isKeyDown(Keyboard.KEY_W)){
            pos.z -= 0.2f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)){
            pos.z += 0.2f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)){
            pos.x += 0.2f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)){
            pos.x -= 0.2f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_R)){
            pos.y += 0.2f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_F)){
            pos.y -= 0.2f;
        }


    }

    public Vector3f getPos() {
        return pos;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }
}
