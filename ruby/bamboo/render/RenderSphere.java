package ruby.bamboo.render;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import net.minecraft.entity.Entity;

import org.lwjgl.util.glu.Sphere;

import ruby.bamboo.entity.EntityFirecracker;

public class RenderSphere// extends Render
{
    public RenderSphere() {
        s.setTextureFlag(true);
    }

    private Sphere s = new Sphere();
    private float t;

    private void render(Entity var1, double var2, double var4, double var6, float var8, float var9) {
        t = t < 360 ? t + 1 : 0;
        // glMaterial(GL_FRONT, GL_SPECULAR, matSpecular);
        glPushMatrix();
        // loadTexture("/textures/entitys/bamboobasketb.png");
        // glMatrixMode(GL_MODELVIEW);
        // glColor3f(0.1f, 0.4f, 0.9f);
        glTranslatef((float) var2, (float) var4, (float) var6);
        int paw = ((EntityFirecracker) var1).getExplodeLv();
        float scale = paw * paw / 4F;
        glScalef(scale, scale, scale);
        glRotatef(90.0f, 1f, 0, 0);
        glRotatef(t, 0, 0, 1);
        s.draw(0.5f, 4 + paw, 2 + paw);
        glPopMatrix();
    }

    // @Override
    public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9) {
        render(var1, var2, var4, var6, var8, var9);
    }
}
