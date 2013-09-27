package ruby.bamboo.render;

import java.util.Random;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import ruby.bamboo.entity.EntityKakeziku;
import ruby.bamboo.entity.EnumKakeziku;

public class RenderKakeziku extends Render
{
    private static ResourceLocation RESOUCE = new ResourceLocation("textures/entitys/kakeziku.png");
    public RenderKakeziku()
    {
        rand = new Random();
    }

    public void func_158_a(EntityKakeziku entity, double d, double d1, double d2,
                           float f, float f1)
    {
        rand.setSeed(187L);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1 + 1, (float)d2);
        GL11.glRotatef(f, 0.0F, 1.0F, 0.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        bindEntityTexture(entity);
        EnumKakeziku enumkakeziku = entity.getArt();
        float f2 = 0.0625F;
        GL11.glScalef(f2, f2, f2);
        func_159_a(entity, enumkakeziku.sizeX, enumkakeziku.sizeY, enumkakeziku.offsetX, enumkakeziku.offsetY);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    private void func_159_a(EntityKakeziku entity, int i, int j, int k, int l)
    {
        float f = (-i) / 2.0F;
        float f1 = (-j) / 2.0F;
        float f2 = -0.5F;
        float f3 = 0.5F;

        for (int i1 = 0; i1 < i / 16; i1++)
        {
            for (int j1 = 0; j1 < j / 16; j1++)
            {
                float f4 = f + (i1 + 1) * 16;
                float f5 = f + i1 * 16;
                float f6 = f1 + (j1 + 1) * 16;
                float f7 = f1 + j1 * 16;
                func_160_a(entity, (f4 + f5) / 2.0F, f1 + (j) / 2.0F);
                float f8 = ((k + i) - i1 * 16) / 256F;
                float f9 = ((k + i) - (i1 + 1) * 16) / 256F;
                float f10 = ((l + j) - j1 * 16) / 256F;
                float f11 = ((l + j) - (j1 + 1) * 16) / 256F;
                float f12 = 0.75F + 0.0625F * i1;
                float f13 = 0.8125F + 0.0625F * i1;
                float f14 = 0.0F + 0.0625F * j1;
                float f15 = 0.0625F + 0.0625F * j1;
                float f16 = 0.75F;
                float f17 = 0.8125F;
                float f18 = 0.001953125F;
                float f19 = 0.001953125F;
                float f20 = 0.7519531F;
                float f21 = 0.7519531F;
                float f22 = 0.0F;
                float f23 = 0.0625F;
                Tessellator tessellator = Tessellator.instance;
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 0.0F, -1F);
                tessellator.addVertexWithUV(f4, f7, f2, f9, f10);
                tessellator.addVertexWithUV(f5, f7, f2, f8, f10);
                tessellator.addVertexWithUV(f5, f6, f2, f8, f11);
                tessellator.addVertexWithUV(f4, f6, f2, f9, f11);
                tessellator.setNormal(0.0F, 0.0F, 1.0F);
                tessellator.addVertexWithUV(f4, f6, f3, f12, f14);
                tessellator.addVertexWithUV(f5, f6, f3, f13, f14);
                tessellator.addVertexWithUV(f5, f7, f3, f13, f15);
                tessellator.addVertexWithUV(f4, f7, f3, f12, f15);
                tessellator.setNormal(0.0F, -1F, 0.0F);
                tessellator.addVertexWithUV(f4, f6, f2, f16, f18);
                tessellator.addVertexWithUV(f5, f6, f2, f17, f18);
                tessellator.addVertexWithUV(f5, f6, f3, f17, f19);
                tessellator.addVertexWithUV(f4, f6, f3, f16, f19);
                tessellator.setNormal(0.0F, 1.0F, 0.0F);
                tessellator.addVertexWithUV(f4, f7, f3, f16, f18);
                tessellator.addVertexWithUV(f5, f7, f3, f17, f18);
                tessellator.addVertexWithUV(f5, f7, f2, f17, f19);
                tessellator.addVertexWithUV(f4, f7, f2, f16, f19);
                tessellator.setNormal(-1F, 0.0F, 0.0F);
                tessellator.addVertexWithUV(f4, f6, f3, f21, f22);
                tessellator.addVertexWithUV(f4, f7, f3, f21, f23);
                tessellator.addVertexWithUV(f4, f7, f2, f20, f23);
                tessellator.addVertexWithUV(f4, f6, f2, f20, f22);
                tessellator.setNormal(1.0F, 0.0F, 0.0F);
                tessellator.addVertexWithUV(f5, f6, f2, f21, f22);
                tessellator.addVertexWithUV(f5, f7, f2, f21, f23);
                tessellator.addVertexWithUV(f5, f7, f3, f20, f23);
                tessellator.addVertexWithUV(f5, f6, f3, f20, f22);
                tessellator.draw();
            }
        }
    }

    private void func_160_a(EntityKakeziku entity, float f, float f1)
    {
        int i = MathHelper.floor_double(entity.posX);
        int j = MathHelper.floor_double(entity.posY + f1 / 16F);
        int k = MathHelper.floor_double(entity.posZ);

        if (entity.getDir() == 0)
        {
            i = MathHelper.floor_double(entity.posX + f / 16F);
        }

        if (entity.getDir() == 1)
        {
            k = MathHelper.floor_double(entity.posZ - f / 16F);
        }

        if (entity.getDir() == 2)
        {
            i = MathHelper.floor_double(entity.posX - f / 16F);
        }

        if (entity.getDir() == 3)
        {
            k = MathHelper.floor_double(entity.posZ + f / 16F);
        }

        int l = renderManager.worldObj.getLightBrightnessForSkyBlocks(i, j, k, 0);
        int i1 = l % 0x10000;
        int j1 = l / 0x10000;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i1, j1);
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
    }

    @Override
    public void doRender(Entity entity, double d, double d1, double d2,
                         float f, float f1)
    {
        func_158_a((EntityKakeziku)entity, d, d1, d2, f, f1);
    }

    private Random rand;
    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        // TODO 自動生成されたメソッド・スタブ
        return RESOUCE;
    }
}
