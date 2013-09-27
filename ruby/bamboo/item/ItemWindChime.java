package ruby.bamboo.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ruby.bamboo.BambooCore;
import ruby.bamboo.entity.EntityWindChime;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemWindChime extends Item
{
    public ItemWindChime(int par1)
    {
        super(par1);
        this.setCreativeTab(BambooCore.tabBamboo);
    }
    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        if (!par3World.isRemote)
        {
            MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(par3World, par2EntityPlayer, true);

            if (movingobjectposition == null)
            {
                return false;
            }

            if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE)
            {
                int i = movingobjectposition.blockX;
                int j = movingobjectposition.blockY;
                int k = movingobjectposition.blockZ;

                /*
                            if(world.getBlockId(i, j, k)==mod_Bamboo.spaID){
                            	world.setBlockMetadataWithNotify(i, j, k, world.getBlockMetadata(i, j, k)-1);
                            	return itemstack;
                            }*/

                if (!par3World.canMineBlock(par2EntityPlayer, i, j, k))
                {
                    return false;
                }

                if (movingobjectposition.sideHit == 0)
                {
                    j--;
                }

                if (movingobjectposition.sideHit == 1)
                {
                    j++;
                }

                if (movingobjectposition.sideHit == 2)
                {
                    k--;
                }

                if (movingobjectposition.sideHit == 3)
                {
                    k++;
                }

                if (movingobjectposition.sideHit == 4)
                {
                    i--;
                }

                if (movingobjectposition.sideHit == 5)
                {
                    i++;
                }

                if (par3World.isAirBlock(i, j, k))
                {
                    //world.setBlockAndMetadataWithNotify(i, j, k, mod_Bamboo.spaID,6);
                    EntityWindChime entity = new EntityWindChime(par3World);
                    entity.setPosition(i + 0.5, j, k + 0.5);
                    par3World.spawnEntityInWorld(entity);
                    par1ItemStack.stackSize--;
                }
            }
        }

        return true;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.itemIcon = par1IconRegister.registerIcon("windchime");
    }
}
