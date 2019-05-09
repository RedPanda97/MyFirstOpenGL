package renderEngine;

import models.RawModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class OBJLoader {

    public static RawModel loadObjModel(String fileName, Loader loader){
        FileReader fileReader = null;
        try {
            fileReader= new FileReader(new File("res/"+fileName+".obj"));
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't load OBJ file");
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(fileReader);
        List<Vector3f> vertList = new ArrayList<>();
        List<Vector2f> texList = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer>  indices = new ArrayList<>();
        float[] verticesArray = null;
        float[] normalsArray = null;
        float[] texturesArray = null;
        int[]   indicesArray = null;

        try {
            String line;
            while (true){
                line = reader.readLine();
                String[] currentLine = line.split(" ");
                if(line.startsWith("v ")){
                    Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]),Float.parseFloat(currentLine[2]),Float.parseFloat(currentLine[3]));
                    vertList.add(vertex);
                }else if (line.startsWith("vt ")){
                    Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
                    texList.add(texture);
                }else if (line.startsWith("vn ")){
                    Vector3f norm = new Vector3f(Float.parseFloat(currentLine[1]),Float.parseFloat(currentLine[2]),Float.parseFloat(currentLine[3]));
                    normals.add(norm);
                }else if (line.startsWith("f ")){
                    texturesArray = new float[vertList.size()*2];
                    normalsArray = new float[vertList.size()*3];
                    break;
                }
            }
         while (line!=null){
             if (!line.startsWith("f ")){
                 line = reader.readLine();
                 continue;
             }
             String[] currentLine = line.split(" ");
             String[] vertex1 = currentLine[1].split("/");
             String[] vertex2 = currentLine[2].split("/");
             String[] vertex3 = currentLine[3].split("/");

             processVertex(vertex1, indices, texList,normals,texturesArray,normalsArray);
             processVertex(vertex2, indices, texList,normals,texturesArray,normalsArray);
             processVertex(vertex3, indices, texList,normals,texturesArray,normalsArray);
             line = reader.readLine();
         }

        }catch (Exception e){
            e.printStackTrace();
        }

        verticesArray = new float[vertList.size()*3];
        indicesArray = new int[indices.size()];


        int vertexPointer = 0;
        for (Vector3f vertex:vertList){
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }

        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }
        return loader.loadtoVAO(verticesArray,indicesArray,texturesArray);
    }


    private static  void  processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures,
                                        List<Vector3f> normals,float[]  texturesArray, float[] normalArray){

        int currentVertexPointer = Integer.parseInt(vertexData[0])-1;
        indices.add(currentVertexPointer);

        Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1])-1);
        texturesArray[currentVertexPointer*2] =  currentTex.x;
        texturesArray[currentVertexPointer*2+1] = 1 - currentTex.y;

        Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2])-1);
        normalArray[currentVertexPointer*3] = currentNorm.x;
        normalArray[currentVertexPointer*3+1] = currentNorm.y;
        normalArray[currentVertexPointer*3+2] = currentNorm.z;


    }
}
