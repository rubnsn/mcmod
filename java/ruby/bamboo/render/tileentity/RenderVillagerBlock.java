package ruby.bamboo.render.tileentity;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import ruby.bamboo.tileentity.TileEntityVillagerBlock;

public class RenderVillagerBlock extends TileEntitySpecialRenderer {
    public final ModelVillagerBlock model = new ModelVillagerBlock();
    public final ResourceLocation res = new ResourceLocation("textures/entity/villager/villager.png");
    public static RenderVillagerBlock instance;

    public RenderVillagerBlock() {
        instance = this;
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double d0, double d1, double d2, float f0) {
        GL11.glPushMatrix();
        this.bindTexture(res);
        GL11.glTranslatef((float) d0 + 0.5F, (float) d1, (float) d2 + 0.5F);
        GL11.glScalef(1.6F, 1.6F, 1.6F);
        GL11.glRotatef(-90 * (tile.getBlockMetadata() & 3) + ((TileEntityVillagerBlock) tile).getOffsetRoteY(), 0, 1F, 0);
        model.render(null, 0, 0, 0, 180, 180, 0.0625F);
        GL11.glPopMatrix();
    }

    public void renderInv() {
        GL11.glPushMatrix();
        this.bindTexture(res);
        GL11.glTranslatef(0, -0.5F, 0);
        GL11.glScalef(1.68F, 1.68F, 1.68F);
        model.render(null, 0, 0, 0, 90, 180, 0.0625F);
        GL11.glPopMatrix();
    }
}
