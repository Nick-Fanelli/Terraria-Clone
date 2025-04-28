package com.nickfanelli;

import com.nickfanelli.graphics.Window;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Application {

    private Window window = null;
    private int currentFps = 0;

    public void startApplication() {

        this.window = new Window("Terraria Clone", 1200, 800);
        this.window.createWindowContext();

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
                // Update Here
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

    private void cleanUpApplication() {

        this.window.destroyWindow();

    }
}
