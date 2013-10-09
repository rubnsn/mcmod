package ruby.bamboo.gui;

import org.lwjgl.opengl.GL11;

import ruby.bamboo.tileentity.TileEntityMillStone;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class GuiMillStone extends GuiContainer {
    private static final ResourceLocation RESORCE = new ResourceLocation("textures/guis/guimillstone.png");
    private TileEntityMillStone tile;

    public GuiMillStone(InventoryPlayer par1InventoryPlayer, TileEntity tileEntity) {
        super(new ContainerMillStone(par1InventoryPlayer, (TileEntityMillStone) tileEntity));
        tile = (TileEntityMillStone) tileEntity;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(RESORCE);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        int i1;

        if (tile.isGrind == 1) {
            i1 = this.tile.grindMotion;

            // ぐらいんだー
            if (i1 < 4) {
                this.drawTexturedModalRect(k + 80, l + 28, 176, 16 * i1, 16, 16);
            }

            // バケツ
            this.drawTexturedModalRect(k + 80, l + 46, 192, 6 * tile.progress, 16, 6);
        }
    }
}
