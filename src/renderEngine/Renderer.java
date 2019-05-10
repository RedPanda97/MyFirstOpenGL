package renderEngine;

import entities.Camera;
import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.Maths;

import java.util.List;
import java.util.Map;

public class Renderer {

    private static final float FOV = 70;
    private static final float NEAR_PANE = 0.1f;
    private static final float FAR_PANE = 1000;

    private Matrix4f projectionMatrix;
    private StaticShader shader;


    public Renderer(StaticShader staticShader) {
        this.shader = staticShader;
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        createProjectionMatrix();
        staticShader.start();
        staticShader.loadProjectionMatrix(projectionMatrix);
        staticShader.stop();
    }

    public void prepare(){
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0.2f,0.2f,0.2f,0);
    }

    public void render(Map<TexturedModel, List<Entity>> entities){
        for (TexturedModel model: entities.keySet()) {
                prepareTexturedModel(model);
            List<Entity> batch = entities.get(model);
            for (Entity entitiy: batch) {
                prepareInstance(entitiy);
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT,0);
            }
            unbindTexturedModel();
        }
    }

    private void prepareTexturedModel(TexturedModel model){
        RawModel rawModel = model.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        ModelTexture texture = model.getModelTexture();
        shader.loadShineVariables(texture.getShineDamper(),texture.getReflectivity());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getModelTexture().getID());
    }

    private void unbindTexturedModel(){
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glDeleteVertexArrays(0);
    }

    private void prepareInstance(Entity entity){
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotx(), entity.getRoty(), entity.getRotz(), entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
    }

    private void createProjectionMatrix(){
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV/2f))) * aspectRatio);
        float x_scale = y_scale/aspectRatio;
        float frustum_length = FAR_PANE - NEAR_PANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PANE+NEAR_PANE)/frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2*NEAR_PANE * FAR_PANE)/frustum_length);
        projectionMatrix.m33 = 0;
    }
}
