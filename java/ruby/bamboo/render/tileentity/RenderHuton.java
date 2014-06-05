package ruby.bamboo.render.tileentity;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import ruby.bamboo.BambooCore;

public class RenderHuton extends TileEntitySpecialRenderer {

    private static final float[][] offset = new float[][] { { 0F, 0.5F }, { 0.5F, 1F }, { 1F, 0.5F }, { 0.5F, 0F } };
    private final ModelHuton modelHuton;
    private static ResourceLocation MAKURA = new ResourceLocation(BambooCore.resourceDomain + "textures/entitys/makura.png");
    private static ResourceLocation HUTON = new ResourceLocation(BambooCore.resourceDomain + "textures/entitys/huton.png");

    public RenderHuton() {
        modelHuton = new ModelHuton();
    }

    public void renderHuton(TileEntity entity, double d, double d1, double d2, float f, boolean b) {
        GL11.glPushMatrix();
        byte dir = (byte) entity.getBlockMetadata();
        switch (dir) {
        case 0:
            dir = 1;
            break;

        case 1:
            dir = 0;
            break;

        case 2:
            dir = 3;
            break;

        case 3:
            dir = 2;
            break;
        }

        GL11.glTranslatef((float) d + offset[dir][0], (float) d1, (float) d2 + offset[dir][1]);
        GL11.glRotatef(dir * 90.0F, 0.0F, 1.0F, 0.0F);

        if (b) {
            func_110777_b(b);
            modelHuton.renderFoot(0.0625F);
        } else {
            func_110777_b(b);
            modelHuton.renderHead(0.0625F);
        }

        GL11.glPopMatrix();
    }

    protected void func_110777_b(boolean b) {
        if (b) {
            this.bindTexture(HUTON);
        } else {
            this.bindTexture(MAKURA);
        }
    }

    protected ResourceLocation getEntityTexture(Entity entity) {
        return MAKURA;
    }

    @Override
    public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8) {
        renderHuton(var1, var2, var4, var6, var8, true);
        renderHuton(var1, var2, var4, var6, var8, false);
    }
}
