package ruby.bamboo.render;

import org.lwjgl.opengl.GL11;

import ruby.bamboo.BambooCore;
import ruby.bamboo.tileentity.TileEntityManeki;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class RenderManeki extends TileEntitySpecialRenderer {
    public static ModelManeki model;
    public static RenderManeki instance;
    private static ResourceLocation RESOURCE = new ResourceLocation(BambooCore.resourceDomain + "textures/entitys/maneki.png");

    public RenderManeki() {
        instance = this;
        model = new ModelManeki();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) {
        doRender((TileEntityManeki) tileentity, d0, d1, d2, 0.0F, 0.0F);
    }

    private void doRender(TileEntityManeki entity, double d, double d1, double d2, float f, float g) {
        GL11.glPushMatrix();
        byte byte0 = (byte) (entity.getBlockMetadata());
        GL11.glTranslatef((float) d + 0.5F, (float) d1 + 0.5F, (float) d2 + 0.5F);
        this.bindTexture(RESOURCE);
        GL11.glRotatef((byte0 & 3) * 90.0F, 0.0F, 1.0F, 0.0F);
        // 光源処理
        /*
         * int i = MathHelper.floor_double(d); int j =
         * MathHelper.floor_double(d1 + (double)(f1 / 16F)); int k =
         * MathHelper.floor_double(d2); int l =
         * entity.worldObj.getLightBrightnessForSkyBlocks(i, j, k, 0); int i1 =
         * l % 0x10000; int j1 = l / 0x10000;
         * OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit,
         * i1, j1);
         */
        // 光源追加ここまで
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        model.render(null, 0, 0, 0, 0, 0, 0.0625F);
        model.renderHand(entity.getRoll());
        GL11.glPopMatrix();
    }

    public void renderInv() {
        GL11.glPushMatrix();
        this.bindTexture(RESOURCE);
        GL11.glTranslatef(0, -0.25F, 0);
        GL11.glScalef(1F, 1F, 1F);
        RenderManeki.model.render(null, 0, 0, 0, 0, 0, 0.0625F);
        model.renderHand(0);
        GL11.glPopMatrix();
    }
}
