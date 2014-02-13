package ruby.bamboo.render.magatama;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import ruby.bamboo.entity.magatama.EntityThunderEffect;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderThunderEffect extends Render {

    @Override
    public void doRender(Entity entity, double d0, double d1, double d2, float f, float f1) {
        render((EntityThunderEffect) entity, d0, d1, d2);
    }

    private void render(EntityThunderEffect entity, double posX, double posY, double posZ) {

        posY += (entity.height / 2);
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        double tposX = posX - (entity.posX - entity.tposX);
        double tposY = posY - (entity.posY - entity.tposY);
        double tposZ = posZ - (entity.posZ - entity.tposZ);
        int segment = 6;
        double[] xdouble = new double[segment];
        double[] ydouble = new double[segment];
        double[] zdouble = new double[segment];
        Random random = new Random(entity.getBoltVertex());
        double d1 = 0.0D;
        double d2 = 0.0D;
        double d3 = 0.0D;
        for (int i = segment - 1; i >= 0; --i) {
            d1 += (double) (random.nextInt(20) - 10) * 2 / (random.nextInt(8) + 4);
            d2 += (double) (random.nextInt(20) - 10) * 2 / (random.nextInt(8) + 4);
            d3 += (double) (random.nextInt(20) - 10) * 2 / (random.nextInt(8) + 4);
            xdouble[i] = d1;
            ydouble[i] = d2;
            zdouble[i] = d3;
        }
        double prePosX = posX;
        double prePosY = posY;
        double prePosZ = posZ;
        double preTPosX = posX;
        double preTPosY = posY;
        double preTPosZ = posZ;

        for (int i = 0; i < segment; i++) {
            renderParts(prePosX, prePosY, prePosZ, prePosX - xdouble[i], prePosY - ydouble[i], prePosZ - zdouble[i], 0.2);

            prePosX -= xdouble[i];
            prePosY -= ydouble[i];
            prePosZ -= zdouble[i];
        }
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }

    private void renderParts(double posX, double posY, double posZ, double tposX, double tposY, double tposZ, double size) {
        double offset = size / 2;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(5);
        tessellator.setColorRGBA_F(0.3F, 0.2F, 0.7F, 1F);
        // 上面
        tessellator.addVertex(posX + size - offset, posY + offset, posZ);
        tessellator.addVertex(posX - offset, posY + offset, posZ);
        tessellator.addVertex(tposX + size - offset, tposY + offset, tposZ);
        tessellator.addVertex(tposX - offset, tposY + offset, tposZ);
        // 下面
        tessellator.addVertex(tposX + size - offset, tposY + offset, tposZ);
        tessellator.addVertex(tposX - offset, tposY + offset, tposZ);
        tessellator.addVertex(posX + size - offset, posY + offset, posZ);
        tessellator.addVertex(posX - offset, posY + offset, posZ);
        // 側面
        tessellator.addVertex(posX, posY + size, posZ);
        tessellator.addVertex(posX, posY, posZ);
        tessellator.addVertex(tposX, tposY + size, tposZ);
        tessellator.addVertex(tposX, tposY, tposZ);

        tessellator.addVertex(posX, posY + size, posZ);
        tessellator.addVertex(posX, posY, posZ);
        tessellator.addVertex(tposX, tposY + size, tposZ);
        tessellator.addVertex(tposX, tposY, tposZ);

        tessellator.draw();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity par1Entity) {
        return this.func_110805_a(par1Entity);
    }

    private ResourceLocation func_110805_a(Entity par1Entity) {
        return null;
    }
}
