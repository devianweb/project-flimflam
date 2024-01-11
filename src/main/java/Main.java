import org.lwjgl.opengl.GL;

public class Main {

    public static void main(String[] args) {
        var gameWindow = new GameWindow();
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
        var game = new Game(gameWindow);
        game.run();
    }
}
