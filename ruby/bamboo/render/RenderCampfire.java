package ruby.bamboo.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import ruby.bamboo.BambooCore;
import ruby.bamboo.tileentity.TileEntityCampfire;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderCampfire extends TileEntitySpecialRenderer {
    public static ModelCampfire model;
    public static RenderCampfire instance;
    private static ResourceLocation RESOURCE = new ResourceLocation(BambooCore.resorceDmain + "textures/entitys/campfire.png");

    public RenderCampfire() {
        instance = this;
        model = new ModelCampfire();
    }

    @Override
    public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8) {
        doRender((TileEntityCampfire) var1, var2, var4, var6, 0.0F, 0.0F);
    }

    public void doRender(TileEntityCampfire entity, double d, double d1, double d2, float f, float f1) {
        GL11.glPushMatrix();
        byte byte0 = (byte) (entity.getBlockMetadata());
        GL11.glTranslatef((float) d + 0.5F, (float) d1, (float) d2 + 0.5F);
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
        model.renderWood();

        // System.out.println(entity.isMeat);
        if (byte0 >> 2 == 1) {
            model.renderFish();
        } else if (byte0 >> 2 == 2) {
            model.renderRods();
            model.renderMeat(entity.getMeatroll());
        } else if (byte0 >> 2 == 3) {
            model.renderRods();
            model.renderPot();
        }

        GL11.glPopMatrix();
    }

    public void renderInv() {
        GL11.glPushMatrix();
        this.bindTexture(RESOURCE);
        GL11.glTranslatef(0, -0.25F, 0);
        GL11.glScalef(1.68F, 1.68F, 1.68F);
        RenderCampfire.model.renderWood();
        GL11.glPopMatrix();
    }
}
