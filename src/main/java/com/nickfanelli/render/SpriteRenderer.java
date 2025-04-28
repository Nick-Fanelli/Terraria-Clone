package com.nickfanelli.render;

import org.joml.Vector2f;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class SpriteRenderer {

    // Vertex
    // ================================================
    // Position
    // float, float

    public static final int BATCH_SPRITE_COUNT = 20000; // Number of sprites per render batch

    private static final int BATCH_VERTEX_COUNT = BATCH_SPRITE_COUNT * 4; // 4 vertices per sprite
    private static final int BATCH_INDEX_COUNT = BATCH_SPRITE_COUNT * 6; // 6 indices per sprite

    private static final Vector2f[] QUAD_VERTEX_POSITIONS = {
            new Vector2f(-0.5f, -0.5f),
            new Vector2f(0.5f, -0.5f),
            new Vector2f(0.5f, 0.5f),
            new Vector2f(-0.5f, 0.5f)
    };

    // Position
    private static final int POSITION_FLOAT_COUNT = 2; // x and y

    private static final int VERTEX_FLOAT_COUNT = POSITION_FLOAT_COUNT;

    private int vaoId, vboId, iboId;
    private float[] vertices;

    private Shader shader;

    private int vertexCount = 0;
    private int indexCount = 0;

    public void initializeRenderer() {

        this.shader = new Shader("sprite");
        shader.create();

        this.vertices = new float[BATCH_VERTEX_COUNT * VERTEX_FLOAT_COUNT];

        // Create VAO
        this.vaoId = glGenVertexArrays();
        glBindVertexArray(this.vaoId);

        // Create VBO
        this.vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, this.vaoId);

        // Allocate space in VBO
        glBufferData(GL_ARRAY_BUFFER, BATCH_VERTEX_COUNT * VERTEX_FLOAT_COUNT * Float.BYTES, GL_STATIC_DRAW);

        // Setup Attrib Pointers
        glVertexAttribPointer(0, POSITION_FLOAT_COUNT, GL_FLOAT, false, VERTEX_FLOAT_COUNT * Float.BYTES, 0);

        // Unbind VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        // Create the indices
        int[] indices = new int[BATCH_INDEX_COUNT];
        int offset = 0;

        for(int i = 0; i < BATCH_INDEX_COUNT; i += 6) {
            // First Triangle
            indices[i] = offset;
            indices[i + 1] = offset + 1;
            indices[i + 2] = offset + 2;

            // Second Triangle
            indices[i + 3] = offset + 2;
            indices[i + 4] = offset + 3;
            indices[i + 5] = offset;

            offset += 4; // Four Vertices Per Quad
        }

        // Bind the indices
        this.iboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.iboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // Unbind
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

    }

    private void updateBatchVertexData() {

        glBindBuffer(GL_ARRAY_BUFFER, this.vboId);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

    }

    public void render() {

        shader.bind();

        glBindVertexArray(this.vaoId);

        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.iboId);

        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        glDisableVertexAttribArray(0);

        glBindVertexArray(0);

        glUseProgram(0); // Unbind shader

    }

    private void addVertex(Vector2f position) {

        // Position
        this.vertices[this.vertexCount] = position.x;
        this.vertices[this.vertexCount + 1] = position.y;

        this.vertexCount += 2;

    }

    public void registerQuad() {

        for(int i = 0; i < 4; i++) {

            float xPos = QUAD_VERTEX_POSITIONS[i].x;
            float yPos = QUAD_VERTEX_POSITIONS[i].y;

            addVertex(new Vector2f(xPos, yPos));

        }

        indexCount += 6;

        this.updateBatchVertexData();

    }

}
