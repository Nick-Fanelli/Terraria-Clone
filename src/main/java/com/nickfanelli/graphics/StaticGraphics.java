package com.nickfanelli.graphics;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.system.linux.Stat;

import static org.lwjgl.glfw.GLFW.*;

public class StaticGraphics {

    private static boolean graphicsInitialized = false;

    public static void initializeGraphics() {

        if(StaticGraphics.graphicsInitialized)
            return;

        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        StaticGraphics.graphicsInitialized = true;

    }

    public static void destroyGraphics() {

        if(!StaticGraphics.graphicsInitialized)
            return;

        glfwTerminate();
        glfwSetErrorCallback(null).free();

        StaticGraphics.graphicsInitialized = false;

    }

    public static boolean graphicsInitialized() { return StaticGraphics.graphicsInitialized; }


}
