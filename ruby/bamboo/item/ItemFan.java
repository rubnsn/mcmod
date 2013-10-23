package ruby.bamboo.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ruby.bamboo.BambooCore;
import ruby.bamboo.entity.EntityWind;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemFan extends Item {
    public ItemFan(int par1) {
        super(par1);
        setMaxDamage(100);
        setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        par1ItemStack.damageItem(1, par3EntityPlayer);
        EntityWind entity = new EntityWind(par2World, par3EntityPlayer);
        par2World.spawnEntityInWorld(entity);
        return par1ItemStack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister) {
        this.itemIcon = par1IconRegister.registerIcon(BambooCore.resorceDmain + "fan");
    }
}
