package ruby.bamboo.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ruby.bamboo.entity.EntityObon;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemObon extends Item
{
    public ItemObon(int par1)
    {
        super(par1);
    }
    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        if (!par3World.isRemote && par7 == 1)
        {
            float yOffset = 0;
            MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(par3World, par2EntityPlayer, true);

            if (movingobjectposition != null && par3World.getBlockMaterial(par4, movingobjectposition.blockY, par6).equals(Material.water))
            {
                yOffset += 0.8;
            }

            EntityObon e = new EntityObon(par3World);

            if (par2EntityPlayer.isSneaking())
            {
                e.setPosition(par4 + 0.5, par5 + par9 + yOffset, par6 + 0.57);
            }
            else
            {
                e.setPosition(par4 + par8, par5 + par9 + yOffset, par6 + par10);
            }

            par3World.spawnEntityInWorld(e);

            if (!par2EntityPlayer.capabilities.isCreativeMode)
            {
                par1ItemStack.stackSize--;
            }
        }

        return true;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.itemIcon = par1IconRegister.registerIcon("obon");
    }
}
