package com.nickfanelli;

import com.nickfanelli.graphics.Window;
import com.nickfanelli.render.SpriteRenderer;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Application {

    private Window window = null;
    private int currentFps = 0;

    private SpriteRenderer spriteRenderer;

    public void startApplication() {

        this.window = new Window("Terraria Clone", 1280, 720);
        this.window.createWindowContext();

        this.spriteRenderer = new SpriteRenderer();
        this.spriteRenderer.initializeRenderer();
        this.spriteRenderer.registerQuad();

        this.startUpdateLoop();

    }

    private void startUpdateLoop() {

        final long windowPtr = this.window.getWindowPtr();

        float endTime, startTime = (float) glfwGetTime();
        float deltaTime = 0.0f;
        float accumulativeDeltaTime = 0.0f;

        int frameCount = 0;

        while(!glfwWindowShouldClose(windowPtr)) {

            glClearColor(0.1f, 0.1f, 0.2f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            if(deltaTime >= 0) {
                this.update();
            }

            glfwSwapBuffers(windowPtr);
            glfwPollEvents();

            endTime = (float) glfwGetTime();
            deltaTime = endTime - startTime;
            accumulativeDeltaTime += deltaTime;
            startTime = endTime;

            if(accumulativeDeltaTime >= 1.0f) {
                this.currentFps = frameCount;
                frameCount = 0;
                accumulativeDeltaTime = 0.0f;
            }

            frameCount++;

        }

        this.cleanUpApplication();

    }

    private void update() {

//        glBegin(GL_TRIANGLES);
//
//        glColor3f(1.0f, 0.0f, 0.0f); // Red
//        glVertex2f(-0.5f, -0.5f);     // Top vertex
//
//        glColor3f(0.0f, 1.0f, 0.0f); // Green
//        glVertex2f(0.5f, -0.5f);  // Bottom-left vertex
//
//        glColor3f(0.0f, 0.0f, 1.0f); // Blue
//        glVertex2f(0.0f, 0.5f);   // Bottom-right vertex
//
//
//        glEnd();

        this.spriteRenderer.render();

    }

    private void cleanUpApplication() {

        this.window.destroyWindow();

    }
}
