package com.nickfanelli.graphics;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;

public class Window {

    private final String windowTitle;

    private int windowWidth;
    private int windowHeight;
    private int aspectRatio;

    private GLFWWindowSizeCallback windowSizeCallback;

    private long windowPtr = MemoryUtil.NULL;

    public Window(String windowTitle, int windowWidth, int windowHeight) {

        this.windowTitle = windowTitle;

        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.aspectRatio = windowWidth / windowHeight;

    }

    private void initializeWindow() {

        StaticGraphics.initializeGraphics();

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_SAMPLES, 4);

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

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

        glfwSetWindowSizeCallback(this.windowPtr, (window, width, height) -> {

            this.windowWidth = width;
            this.windowHeight = height;
            this.aspectRatio = width / height;

            glViewport(0, 0, width, height);

        });

        // Make the OpenGL context current
        glfwMakeContextCurrent(this.windowPtr);

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

        glViewport(0, 0, this.windowWidth, this.windowHeight);

    }

    public void destroyWindow() {

        glfwDestroyWindow(this.windowPtr);

    }

    public long getWindowPtr() { return this.windowPtr; }

}
