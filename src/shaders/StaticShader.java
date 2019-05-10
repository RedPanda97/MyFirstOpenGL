package shaders;

import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import toolbox.Maths;

public class StaticShader extends ShaderProgram {
    private static final String VERTEX_FILE = "src/shaders/vertexShader.glsl";
    private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.glsl";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_lightColour;
    private int location_shineDamper;
    private int location_reflectivity;


    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUnifromLocation("transformationMatrix");
        location_projectionMatrix = super.getUnifromLocation("projectionMatrix");
        location_viewMatrix = super.getUnifromLocation("viewMatrix");
        location_lightPosition = super.getUnifromLocation("lightPosition");
        location_lightColour = super.getUnifromLocation("lightColour");
        location_shineDamper = super.getUnifromLocation("shineDamper");
        location_reflectivity = super.getUnifromLocation("reflectivity");

    }

    @Override
    protected void bindAttributes() {
        super.bindAttributes(0, "position");
        super.bindAttributes(1, "textureCoords");
        super.bindAttributes(2,"normal");
    }


    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix,matrix);
    }
    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix,matrix);
    }
    public void loadViewMatrix(Camera camera){
        Matrix4f matrix4f = Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix,matrix4f);
    }

    public void loadLight(Light light){
        super.loadVector(location_lightPosition, light.getPosition());
        super.loadVector(location_lightColour, light.getColour());
    }

    public void loadShineVariables(float damper, float reflectivity){
        super.loadFloat(location_shineDamper, damper);
        super.loadFloat(location_reflectivity, reflectivity);
    }
}
