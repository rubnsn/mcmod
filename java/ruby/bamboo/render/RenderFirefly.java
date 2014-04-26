package ruby.bamboo.render;

import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import ruby.bamboo.BambooCore;
import ruby.bamboo.entity.EntityFirefly;

public class RenderFirefly extends Render {
    private static final ResourceLocation RESOURCE = new ResourceLocation(BambooCore.resourceDomain + "textures/entitys/firefly.png");

    @Override
    public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9) {
        GL11.glPushMatrix();
        float f1 = ActiveRenderInfo.rotationX;
        float f2 = ActiveRenderInfo.rotationZ;
        float f3 = ActiveRenderInfo.rotationYZ;
        float f4 = ActiveRenderInfo.rotationXY;
        float f5 = ActiveRenderInfo.rotationXZ;

        bindTexture(getEntityTexture(var1));
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glTranslatef((float) var2, (float) var4 + var1.height / 2, (float) var6);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        //GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();

        renderParticle(tessellator, (EntityFirefly) var1, 1F, f1, f5, f2, f3, f4);

        tessellator.draw();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    private void renderParticle(Tessellator par1Tessellator, EntityFirefly entity, float par2, float par3, float par4, float par5, float par6, float par7) {
        float f6 = 0;
        float f7 = 1F;
        float f8 = 0;
        float f9 = 1F;
        float f10 = 0.1F;

        double interpPosX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) par2;
        double interpPosY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) par2;
        double interpPosZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) par2;

        float f11 = (float) (entity.prevPosX + (entity.posX - entity.prevPosX) * (double) par2 - interpPosX);
        float f12 = (float) (entity.prevPosY + (entity.posY - entity.prevPosY) * (double) par2 - interpPosY);
        float f13 = (float) (entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) par2 - interpPosZ);
        par1Tessellator.setColorRGBA_F(1F, 1F, 1F, entity.getAlpha());
        par1Tessellator.addVertexWithUV((double) (f11 - par3 * f10 - par6 * f10), (double) (f12 - par4 * f10), (double) (f13 - par5 * f10 - par7 * f10), (double) f7, (double) f9);
        par1Tessellator.addVertexWithUV((double) (f11 - par3 * f10 + par6 * f10), (double) (f12 + par4 * f10), (double) (f13 - par5 * f10 + par7 * f10), (double) f7, (double) f8);
        par1Tessellator.addVertexWithUV((double) (f11 + par3 * f10 + par6 * f10), (double) (f12 + par4 * f10), (double) (f13 + par5 * f10 + par7 * f10), (double) f6, (double) f8);
        par1Tessellator.addVertexWithUV((double) (f11 + par3 * f10 - par6 * f10), (double) (f12 - par4 * f10), (double) (f13 + par5 * f10 - par7 * f10), (double) f6, (double) f9);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity var1) {
        return RESOURCE;
    }

}
