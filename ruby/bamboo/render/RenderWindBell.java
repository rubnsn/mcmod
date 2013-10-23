package ruby.bamboo.render;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import ruby.bamboo.BambooCore;
import ruby.bamboo.entity.EntityWindChime;

public class RenderWindBell extends Render {
    private final ModelWindBell modelwindbell;
    private static final ResourceLocation RESOURCE = new ResourceLocation(BambooCore.resourceDomain + "textures/entitys/windbell.png");

    public RenderWindBell() {
        modelwindbell = new ModelWindBell();
    }

    @Override
    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) d, (float) d1, (float) d2);
        bindEntityTexture(entity);
        // 光源処理
        int i = MathHelper.floor_double(entity.posX);
        int j = MathHelper.floor_double(entity.posY + f1 / 16F);
        int k = MathHelper.floor_double(entity.posZ);
        int l = renderManager.worldObj.getLightBrightnessForSkyBlocks(i, j, k, 0);
        int i1 = l % 0x10000;
        int j1 = l / 0x10000;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i1, j1);
        // 光源追加ここまで
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        modelwindbell.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
        // ひも
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glPushMatrix();
        GL11.glLineWidth(5);
        GL11.glColor3f(1, 1, 1);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(d, d1 + 0.55, d2);
        GL11.glVertex3d(d, d1 + 1, d2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        modelwindbell.windAct(((EntityWindChime) entity).rotx, ((EntityWindChime) entity).roty, ((EntityWindChime) entity).rotz);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        // TODO 自動生成されたメソッド・スタブ
        return RESOURCE;
    }
}
