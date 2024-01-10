import org.lwjgl.opengl.GL46;

import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class Renderer {

    public void prepare() {
        GL46.glClearColor(1, 0, 0 ,1);
        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(RawModel model) {
        GL46.glBindVertexArray(model.getVaoID());
        GL46.glEnableVertexAttribArray(0);
        GL46.glDrawElements(GL46.GL_TRIANGLES, model.getVertexCount(), GL46.GL_UNSIGNED_INT, 0);
        GL46.glDisableVertexAttribArray(0);
        GL46.glBindVertexArray(0);
    }
}
