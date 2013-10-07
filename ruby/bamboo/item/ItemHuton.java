package ruby.bamboo.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ruby.bamboo.BambooUtil;
import ruby.bamboo.entity.EntityHuton;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemHuton extends Item
{
    public ItemHuton(int i)
    {
        super(i);
    }
    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        if (!par3World.isRemote)
        {
            par1ItemStack.stackSize--;
            double d = par4;
            double d1 = par5;
            double d2 = par6;

            switch (BambooUtil.getPlayerDir(par2EntityPlayer))
            {
                case 0:
                    d += 0.5;
                    d2 += 1;
                    break;

                case 1:
                    d2 += 0.5;
                    break;

                case 2:
                    d += 0.5;
                    break;

                case 3:
                    d += 1;
                    d2 += 0.5;
                    break;
            }

            Block block = Block.blocksList[par3World.getBlockId(par4, par5, par6)];

            if (block != null)
            {
                d1 += block.getBlockBoundsMaxY();
            }

            EntityHuton entity = new EntityHuton(par3World);
            entity.setPosition(d, d1, d2);
            entity.setDir(BambooUtil.getPlayerDir(par2EntityPlayer));
            par3World.spawnEntityInWorld(entity);
            //entity.sendThisEntity(EntityRespawnHelper.PACKET_CHANNEL);
            /*EntityKaguya ka=new EntityKaguya(world);
            ka.setPosition(d, d1, d2);
            world.entityJoinedWorld(ka);*/
        }

        return true;
        //HollowSquare(x,y,z,size,false);
        //wldObj=world;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.itemIcon = par1IconRegister.registerIcon("huton");
    }
}
