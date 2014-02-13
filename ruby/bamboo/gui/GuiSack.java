package ruby.bamboo.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import ruby.bamboo.BambooCore;
import ruby.bamboo.BambooInit;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSack extends GuiContainer {
    private static final ResourceLocation RESORCE = new ResourceLocation(BambooCore.resourceDomain + "textures/guis/guisack.png");

    public GuiSack(InventoryPlayer par1InventoryPlayer, ItemStack par2ItemStack) {
        super(new ContainerSack(par1InventoryPlayer, par2ItemStack));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        String s = StatCollector.translateToLocal(BambooInit.itemSack.getUnlocalizedName());
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(RESORCE);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }
}
