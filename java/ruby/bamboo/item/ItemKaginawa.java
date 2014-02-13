package ruby.bamboo.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ruby.bamboo.KaginawaHandler;
import ruby.bamboo.entity.EntityKaginawa;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemKaginawa extends Item {
    public ItemKaginawa() {
        super();
        setMaxDamage(0);
        setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        EntityThrowable et = new EntityKaginawa(par2World, par3EntityPlayer);

        if (!par2World.isRemote) {
            if (!KaginawaHandler.getUsageState(par3EntityPlayer)) {
                KaginawaHandler.setUsageState(par3EntityPlayer, true);
                par2World.spawnEntityInWorld(et);
            } else if (par3EntityPlayer.onGround) {
                KaginawaHandler.setUsageState(par3EntityPlayer, false);
            }
        } else {
            if (!KaginawaHandler.isUsed()) {
                par2World.joinEntityInSurroundings(et);
                KaginawaHandler.setUsageState(true);
            } else if (par3EntityPlayer.onGround) {
                KaginawaHandler.setUsageState(false);
            }
        }

        return par1ItemStack;
    }

    // LeftClick
    /*
     * @Override public boolean onEntitySwing(EntityLiving entityLiving,
     * ItemStack stack) { return false; }
     */

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        this.itemIcon = Items.string.getIconFromDamage(0);
    }
}
