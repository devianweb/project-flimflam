import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private final GLFWErrorCallback errorCallback;
    private GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                glfwSetWindowShouldClose(window, true);
            }
        }
    };

    public Window() {
        //create error callback
        errorCallback = GLFWErrorCallback.createPrint(System.err);
        create();
    }

    private void create() {
        //set error callback for glfw so errors dont get garbage collected
        glfwSetErrorCallback(errorCallback);

        //initialise glfw
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        //create window, if 0 (null in GLFW world) throw exception and terminate
        var window = glfwCreateWindow(640, 480, "Simple example", NULL, NULL);
        if (window == NULL) {
            glfwTerminate();
            throw new RuntimeException("Failed to create the GLFW window");
        }

        //escape key callback
        glfwSetKeyCallback(window, keyCallback);

        //create context
        glfwMakeContextCurrent(window);
    }
}
