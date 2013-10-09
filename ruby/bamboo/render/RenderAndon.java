package ruby.bamboo.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class RenderAndon extends TileEntitySpecialRenderer {
    public static RenderAndon instance;
    public static ModelAndon modelAndon;
    private static final ResourceLocation RESOUCE = new ResourceLocation("textures/entitys/andon.png");

    public RenderAndon() {
        instance = this;
        modelAndon = new ModelAndon();
    }

    public void render(TileEntity entity, double d, double d1, double d2, float f) {
        GL11.glPushMatrix();
        int byte0 = entity.getBlockMetadata();
        GL11.glTranslatef((float) d + 0.5F, (float) d1, (float) d2 + 0.5F);
        // bindTextureByName("/textures/entitys/andon.png");
        this.bindTexture(RESOUCE);
        GL11.glRotatef((byte0 & 3) * 90.0F, 0.0F, 1.0F, 0.0F);
        // 光源処理
        /*
         * int i = MathHelper.floor_double(entity.posX); int j =
         * MathHelper.floor_double(entity.posY + (double)(f1 / 16F)); int k =
         * MathHelper.floor_double(entity.posZ); int l =
         * renderManager.worldObj.getLightBrightnessForSkyBlocks(i, j, k, 0);
         * int i1 = l % 0x10000; int j1 = l / 0x10000;
         * OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit,
         * i1, j1);
         */
        // 光源追加ここまで
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        modelAndon.renderAndon();
        GL11.glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) {
        render(tileentity, d0, d1, d2, f);
    }

    public void renderInv() {
        GL11.glPushMatrix();
        // RenderEngine renderengine =
        // ModLoader.getMinecraftInstance().renderEngine;
        // renderengine.bindTexture("/textures/entitys/andon.png");
        this.bindTexture(RESOUCE);
        GL11.glTranslatef(0, -0.7F, 0);
        GL11.glScalef(1.3F, 1.3F, 1.3F);
        RenderAndon.modelAndon.renderAndon();
        GL11.glPopMatrix();
    }
}
