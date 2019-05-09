package shaders;

import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import toolbox.Maths;

public class StaticShader extends ShaderProgram {
    private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
    private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;


    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUnifromLocation("transformationMatrix");
        location_projectionMatrix = super.getUnifromLocation("projectionMatrix");
        location_viewMatrix = super.getUnifromLocation("viewMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttributes(0, "position");
        super.bindAttributes(1, "textureCoords");
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
}
