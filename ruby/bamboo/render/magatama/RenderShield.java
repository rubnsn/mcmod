package ruby.bamboo.render.magatama;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderShield extends Render {

    @Override
    public void doRender(Entity entity, double d0, double d1, double d2, float f, float f1) {
        GL11.glPushMatrix();

        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }

}
