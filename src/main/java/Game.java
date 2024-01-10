import org.lwjgl.opengl.GL;
import org.lwjgl.glfw.GLFW;

public class Game implements Runnable {

    private Thread thread;
    private GameWindow gameWindow;
    private long window;

    private Loader loader;
    private Renderer renderer;

    private float[] vertices = {
            -0.5f, 0.5f, 0f,
            -0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f,
            0.5f, 0.5f, 0f,
            -0.5f, 0.5f, 0f
    };
    private RawModel model;

    public Game() {
        startGameLoop();
    }

    private void startGameLoop() {
        thread = new Thread(this);
        thread.start();
    }

    private void update() {
        renderer.prepare();
        renderer.render(model);

        GLFW.glfwSwapBuffers(window); // swap the color buffers

        // Poll for window events. The key callback above will only be
        // invoked during this call.
        GLFW.glfwPollEvents();
    }

    @Override
    public void run() {
        gameWindow = new GameWindow();
        window = gameWindow.getWindow();

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        //initialise loader and renderer - has to be after we create the context capabilities
        loader = new Loader();
        renderer = new Renderer();

        //load rectangle to memory
        model = loader.loadToVAO(vertices);

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
