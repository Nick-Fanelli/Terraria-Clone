package com.nickfanelli.render;

import com.nickfanelli.utils.FileUtils;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Shader {

    private final String shaderName;

    private int programID;
    private final ArrayList<Integer> shaders = new ArrayList<>();

    private final HashMap<String, String> replacements;

    public Shader(String shaderName) {
        this.shaderName = shaderName;
        this.replacements = new HashMap<>();
    }

    public Shader(String shaderName, HashMap<String, String> replacements) {
        this.shaderName = shaderName;
        this.replacements = replacements;
    }

    public void create() {
        programID = glCreateProgram();

        String vertShader = FileUtils.ReadAssetFileAsString("assets/shaders/" + shaderName + ".vert.glsl");
        String fragShader = FileUtils.ReadAssetFileAsString("assets/shaders/" + shaderName + ".frag.glsl");

        if(vertShader == null || fragShader == null)
            throw new RuntimeException("Could not initialize Shaders!");

        for(Map.Entry<String, String> entry : replacements.entrySet()) {
            vertShader = vertShader.replaceAll("!" + entry.getKey(), entry.getValue());
            fragShader = fragShader.replaceAll("!" + entry.getKey(), entry.getValue());
        }

        shaders.add(attachShader(GL_VERTEX_SHADER, vertShader));
        shaders.add(attachShader(GL_FRAGMENT_SHADER, fragShader));

        linkProgram();
    }

    private int attachShader(int shaderType, String shaderCode) {
        int shaderID = glCreateShader(shaderType);

        if(shaderID == NULL) {
            throw new RuntimeException("Error creating shader. Type: " + shaderType);
        }

        glShaderSource(shaderID, shaderCode);
        glCompileShader(shaderID);

        if(glGetShaderi(shaderID, GL_COMPILE_STATUS) == 0) {
            throw new RuntimeException("Error compiling Shader code: " + glGetShaderInfoLog(shaderID, 1024));
        }

        glAttachShader(programID, shaderID);

        return shaderID;
    }

    private void linkProgram() {
        glLinkProgram(programID);

        int comp = glGetProgrami(programID, GL_LINK_STATUS);
        int len = glGetProgrami(programID, GL_INFO_LOG_LENGTH);

        String err = glGetProgramInfoLog(programID, len);

        if(comp == GL_FALSE) {
            throw new RuntimeException(err);
        }

        for(int shader : shaders) {
            glDetachShader(programID, shader);
            glDeleteShader(shader);
        }
    }

    public void bind() {
        glUseProgram(programID);
    }

    public void destroy() {
        glUseProgram(0);
        glDeleteProgram(programID);
    }

    public void addUniformMat4(String varName, Matrix4f mat4) {
        int location = glGetUniformLocation(programID, varName);

        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(16);
        floatBuffer = mat4.get(floatBuffer);

        glUniformMatrix4fv(location, false, floatBuffer);
    }

    public void addUniformIntArray(String varName, int[] intArray) {
        int location = glGetUniformLocation(programID, varName);
        glUniform1iv(location, intArray);
    }

}