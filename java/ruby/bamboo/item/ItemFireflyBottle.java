package ruby.bamboo.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import ruby.bamboo.entity.EntityFirefly;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFireflyBottle extends Item {
    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
        if (!par3World.isRemote) {
            EntityFirefly e = new EntityFirefly(par3World);
            e.setPosition(par4 + par8, par5 + 1 + par9, par6 + par10);
            e.setTamed();
            e.setSpawnPosition((float) e.posX, (float) e.posY, (float) e.posZ);
            par3World.spawnEntityInWorld(e);
            par1ItemStack.stackSize--;
            if (!par2EntityPlayer.capabilities.isCreativeMode) {
                par2EntityPlayer.dropItem(Items.glass_bottle, 1);
            }
            return true;
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int par1, int par2) {
        return par2 == 0 ? this.getIconFromDamage(par1) : Items.glass_bottle.getIconFromDamage(0);
    }

}
