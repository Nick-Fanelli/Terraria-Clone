package com.nickfanelli.graphics;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;

public class Window {

    private final String windowTitle;
    private final int windowWidth;
    private final int windowHeight;

    private long windowPtr = MemoryUtil.NULL;

    public Window(String windowTitle, int windowWidth, int windowHeight) {

        this.windowTitle = windowTitle;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;

    }

    private void initializeWindow() {

        StaticGraphics.initializeGraphics();

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        this.windowPtr = glfwCreateWindow(this.windowWidth, this.windowHeight, this.windowTitle, MemoryUtil.NULL, MemoryUtil.NULL);

        if (this.windowPtr == MemoryUtil.NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetKeyCallback(this.windowPtr, (window, key, scancode, action, mods) -> {

            // TODO: MAKE ONLY IN DEBUG MODE
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(this.windowPtr, true);
            }

        });

        // Get the thread stack and push a new frame
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(this.windowPtr, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            if(vidmode == null) {
                throw new RuntimeException("Failed to get primary video mode");
            }

            glfwSetWindowPos(
                    this.windowPtr,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(this.windowPtr);

        glfwWindowHint(GLFW_SAMPLES, 4);

        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(this.windowPtr);
        glfwFocusWindow(this.windowPtr);

    }

    public void createWindowContext() {

        this.initializeWindow();

        GL.createCapabilities();

        glEnable(GL_CULL_FACE);

        glEnable(GL_MULTISAMPLE);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glClearColor(0.2f, 0.2f, 0.2f, 0.0f);

    }

    public void destroyWindow() {

        glfwDestroyWindow(this.windowPtr);

    }

    public long getWindowPtr() { return this.windowPtr; }

}
