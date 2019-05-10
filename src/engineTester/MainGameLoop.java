package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import models.RawModel;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {

    public static void main(String[] args) {
        DisplayManager.createDisplay();
        Loader loader = new Loader();

        RawModel model = OBJLoader.loadObjModel("stall", loader);
        TexturedModel texturedModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("stallTex")));
        ModelTexture texture = texturedModel.getModelTexture();
        texture.setReflectivity(2);
        texture.setShineDamper(1);
        Entity entity = new Entity(texturedModel, new Vector3f(0,0,-25),0,0f,0,1);

        Camera camera = new Camera();
        Light light = new Light(new Vector3f(0,-1,-20), new Vector3f(1,1,1));

        MasterRenderer renderer = new MasterRenderer();
        while (!Display.isCloseRequested()) {
            entity.increaseRotation(0,0.2f,0);
            camera.move();
            renderer.render(light,camera);
            renderer.processEntity(entity);
            DisplayManager.updateDisplay();

        }
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
