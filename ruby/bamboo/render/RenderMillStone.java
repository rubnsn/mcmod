package ruby.bamboo.render;

import org.lwjgl.opengl.GL11;

import ruby.bamboo.BambooCore;
import ruby.bamboo.tileentity.TileEntityMillStone;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class RenderMillStone extends TileEntitySpecialRenderer {
    private final ModelMillStone model;
    public static RenderMillStone instance;
    private static final ResourceLocation RESOUCE = new ResourceLocation(BambooCore.resorceDmain + "textures/entitys/millstone.png");

    public RenderMillStone() {
        instance = this;
        model = new ModelMillStone();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) {
        GL11.glPushMatrix();
        this.bindTexture(RESOUCE);
        GL11.glTranslatef((float) d0 + 0.5F, (float) d1 + 0.5F, (float) d2 + 0.5F);
        model.render((TileEntityMillStone) tileentity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }

    public void renderInv() {
        GL11.glPushMatrix();
        this.bindTexture(RESOUCE);
        model.renderInv();
        GL11.glPopMatrix();
    }
}
