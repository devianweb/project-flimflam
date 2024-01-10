import org.lwjgl.opengl.GL;
import org.lwjgl.glfw.GLFW;

public class Game {

    private final GameWindow gameWindow;
    private final Loader loader;
    private final Renderer renderer;

    private long window;


    private float[] vertices = {
            -0.5f, 0.5f, 0f,
            -0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f,
            0.5f, 0.5f, 0f,
    };

    private int[] indices = {
            0, 1, 3,
            3, 1, 2
    };

    private RawModel model;

    public Game() {
        gameWindow = new GameWindow();
        loader = new Loader();
        renderer = new Renderer();
        run();
    }

    private void update() {
        renderer.prepare();
        renderer.render(model);

        GLFW.glfwSwapBuffers(window); // swap the color buffers

        // Poll for window events. The key callback above will only be
        // invoked during this call.
        GLFW.glfwPollEvents();
    }

    public void run() {
        window = gameWindow.getWindow();

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        //initialise loader and renderer - has to be after we create the context capabilities

        //load rectangle to memory
        model = loader.loadToVAO(vertices, indices);

        //game loop stuff
        double timePerUpdate = 1000000000.0 / 120;

        int updates = 0;
        long lastCheck = System.currentTimeMillis();
        long previousTime = System.nanoTime();

        double deltaU = 0;

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!GLFW.glfwWindowShouldClose(window)) {
            var currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;

            previousTime = currentTime;

            if(deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("UPS: " + updates);
                updates = 0;
            }
        }

        //clean up memory
        loader.cleanUp();
        //kill window
        gameWindow.destoryGameWindow();
    }
}
