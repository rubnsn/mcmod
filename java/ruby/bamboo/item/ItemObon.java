package ruby.bamboo.item;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import ruby.bamboo.BambooCore;
import ruby.bamboo.entity.EntityObon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemObon extends Item {

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
        if (!par3World.isRemote && par7 == 1) {
            float yOffset = 0;
            MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(par3World, par2EntityPlayer, true);

            if (movingobjectposition != null && par3World.getBlock(par4, movingobjectposition.blockY, par6).getMaterial().equals(Material.water)) {
                yOffset += 0.8;
            }

            EntityObon e = new EntityObon(par3World);

            if (par2EntityPlayer.isSneaking()) {
                e.setPosition(par4 + 0.5, par5 + par9 + yOffset, par6 + 0.45);
            } else {
                e.setPositionAndRotation(par4 + par8, par5 + par9 + yOffset, par6 + par10, par2EntityPlayer.rotationYaw, par2EntityPlayer.rotationPitch);
            }

            par3World.spawnEntityInWorld(e);

            if (!par2EntityPlayer.capabilities.isCreativeMode) {
                par1ItemStack.stackSize--;
            }
        }

        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        this.itemIcon = par1IconRegister.registerIcon(BambooCore.resourceDomain + "obon");
    }
}
