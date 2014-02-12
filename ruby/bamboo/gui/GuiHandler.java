package ruby.bamboo.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import ruby.bamboo.tileentity.TileEntityMillStone;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    public static final int GUI_SACK = 0;
    public static final int GUI_MILLSTONE = 1;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
        case GUI_SACK:
            return new ContainerSack(player.inventory, player.getCurrentEquippedItem());

        case GUI_MILLSTONE:
            return new ContainerMillStone(player.inventory, (TileEntityMillStone) world.getTileEntity(x, y, z));

        default:
            return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
        case GUI_SACK:
            return new GuiSack(player.inventory, player.getCurrentEquippedItem());

        case GUI_MILLSTONE:
            return new GuiMillStone(player.inventory, world.getTileEntity(x, y, z));

        default:
            return null;
        }
    }
}
