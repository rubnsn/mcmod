package ruby.bamboo.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ruby.bamboo.BambooCore;
import ruby.bamboo.BambooUtil;
import ruby.bamboo.entity.EntityWindmill;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemWindmill extends Item {
    private static Icon icons[] = new Icon[2];

    public ItemWindmill(int par1) {
        super(par1);
        setHasSubtypes(true);
    }

    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
    }

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
        if (!par3World.isRemote) {
            MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(par3World, par2EntityPlayer, true);

            if (movingobjectposition == null) {
                return false;
            }

            if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE) {
                int i = movingobjectposition.blockX;
                int j = movingobjectposition.blockY;
                int k = movingobjectposition.blockZ;

                if (!par3World.canMineBlock(par2EntityPlayer, i, j, k)) {
                    return false;
                }

                if (movingobjectposition.sideHit == 0) {
                    j--;
                }

                if (movingobjectposition.sideHit == 1) {
                    j++;
                }

                if (movingobjectposition.sideHit == 2) {
                    k--;
                }

                if (movingobjectposition.sideHit == 3) {
                    k++;
                }

                if (movingobjectposition.sideHit == 4) {
                    i--;
                }

                if (movingobjectposition.sideHit == 5) {
                    i++;
                }

                if (par3World.isAirBlock(i, j, k)) {
                    EntityWindmill entity = new EntityWindmill(par3World);
                    entity.setPosition(i + 0.5, j + 0.5, k + 0.5);
                    byte dir = BambooUtil.getPlayerDir(par2EntityPlayer);
                    entity.setDir(dir);
                    entity.setTexNum((byte) par1ItemStack.getItemDamage());
                    par3World.spawnEntityInWorld(entity);
                    par1ItemStack.stackSize--;
                }
            }
        }

        return true;
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister) {
        icons[0] = par1IconRegister.registerIcon(BambooCore.resorceDmain + "windmill");
        icons[1] = par1IconRegister.registerIcon(BambooCore.resorceDmain + "windmill_cloth");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int par1) {
        return icons[par1];
    }
}
