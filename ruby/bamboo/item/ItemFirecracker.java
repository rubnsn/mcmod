package ruby.bamboo.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ruby.bamboo.BambooCore;
import ruby.bamboo.entity.EntityFirecracker;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class ItemFirecracker extends Item {
    private static int LV[] = new int[] { 1, 2, 3 };
    private static Icon icons[] = new Icon[3];
    private int j;

    public ItemFirecracker(int id) {
        super(id);
        setHasSubtypes(true);
    }

    public static int getExplodeLv(int dmg) {
        return dmg < LV.length ? LV[dmg] : 0;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        /*
         * if(par2World.isRemote){ return par1ItemStack; }
         */
        if (!par3EntityPlayer.capabilities.isCreativeMode) {
            --par1ItemStack.stackSize;
        }

        if (!par2World.isRemote) {
            par2World.spawnEntityInWorld(new EntityFirecracker(par2World, par3EntityPlayer, LV[par1ItemStack.getItemDamage()]));
        } else {
            par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        }

        par3EntityPlayer.swingItem();
        return par1ItemStack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int par1) {
        return icons[par1 < 0 ? 0 : par1];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister) {
        icons[0] = par1IconRegister.registerIcon(BambooCore.resorceDmain + "firecracker_s");
        icons[1] = par1IconRegister.registerIcon(BambooCore.resorceDmain + "firecracker_m");
        icons[2] = par1IconRegister.registerIcon(BambooCore.resorceDmain + "firecracker_l");
    }

    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
        for (int i = 0; i < LV.length; i++) {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        return super.getUnlocalizedName(par1ItemStack) + "." + par1ItemStack.getItemDamage();
    }
}
