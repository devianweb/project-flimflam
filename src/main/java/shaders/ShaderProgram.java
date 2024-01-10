package shaders;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL46;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public abstract class ShaderProgram {

    private int programID;
    private int vertexShaderId;
    private int fragmentShaderId;

    public ShaderProgram(String vertexFilePath, String fragmentFilePath) {
        vertexShaderId = loadShader(vertexFilePath, GL46.GL_VERTEX_SHADER);
        fragmentShaderId = loadShader(fragmentFilePath, GL46.GL_FRAGMENT_SHADER);
        programID = GL46.glCreateProgram();
        GL46.glAttachShader(programID, vertexShaderId);
        GL46.glAttachShader(programID, fragmentShaderId);
        GL46.glLinkProgram(programID);
        GL46.glValidateProgram(programID);
        bindAttributes();
    }

    public void start() {
        GL46.glUseProgram(programID);
    }

    public void stop(){
        GL46.glUseProgram(0);
    }

    public void cleanUp() {
        stop();
        GL46.glDetachShader(programID, vertexShaderId);
        GL46.glDetachShader(programID, fragmentShaderId);
        GL46.glDeleteShader(vertexShaderId);
        GL46.glDeleteShader(fragmentShaderId);
        GL46.glDeleteProgram(programID);
    }

    protected abstract void bindAttributes();

    protected void bindAttribute(int attribute, String varibaleName) {
        GL20.glBindAttribLocation(programID, attribute, varibaleName);
    }

    private static int loadShader(String file, int type) {
        StringBuilder shaderSource = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("//\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        int shaderID = GL46.glCreateShader(type);
        GL46.glShaderSource(shaderID, shaderSource);
        GL46.glCompileShader(shaderID);
        if (GL46.glGetShaderi(shaderID, GL46.GL_COMPILE_STATUS) == GL46.GL_FALSE) {
            System.out.println(GL46.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader!");
            System.exit(-1);
        }
        return shaderID;
    }
}
