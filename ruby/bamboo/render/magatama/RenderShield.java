package ruby.bamboo.render.magatama;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderShield extends Render {

    @Override
    public void doRender(Entity entity, double posX, double posY, double posZ, float f, float f1) {
        /*またやるきがでたらがんばる
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        //GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        posX-=0.5;
        posY-=0.5;
        GL11.glRotatef(0, 0.0F, 1.0F, 0.0F);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(0.3F, 0.2F, 0.7F, 1F);

        tessellator.addVertex(posX, posY, posZ);
        tessellator.addVertex(posX, posY+1, posZ);
        tessellator.addVertex(posX+1, posY+1, posZ);
        tessellator.addVertex(posX+1, posY, posZ);

        tessellator.draw();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
        /*/
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }

}
