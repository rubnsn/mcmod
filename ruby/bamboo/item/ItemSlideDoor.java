package ruby.bamboo.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import ruby.bamboo.BambooCore;
import ruby.bamboo.BambooUtil;
import ruby.bamboo.entity.EntitySlideDoor;
import ruby.bamboo.entity.EnumSlideDoor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSlideDoor extends Item {
    private final static EnumSlideDoor[] doors = EnumSlideDoor.values();
    private IIcon[] icons;

    public ItemSlideDoor() {
        super();
        this.setHasSubtypes(true);
    }

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
        if (!par3World.isRemote) {
            par1ItemStack.stackSize--;
            byte dir = BambooUtil.getPlayerDir(par2EntityPlayer);
            EntitySlideDoor entity = new EntitySlideDoor(par3World).setDataDoorId((short) par1ItemStack.getItemDamage()).setDataDir(dir);

            if (dir == 0 || dir == 3) {
                entity.setDataMovedir((byte) -1);
            } else {
                entity.setDataMovedir((byte) 1);
            }

            if (par2EntityPlayer.isSneaking()) {
                entity.setMirror(true);
            }

            entity.setPosition(par4 + 0.5F, par5 + 1, par6 + 0.5F);
            par3World.spawnEntityInWorld(entity);
        }

        return true;
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
    }

    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        for (EnumSlideDoor esd : doors) {
            par3List.add(new ItemStack(par1, 1, esd.getId()));
        }
    }

    @Override
    public IIcon getIconFromDamage(int par1) {
        return icons[par1];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        icons = new IIcon[doors.length];

        for (EnumSlideDoor esd : doors) {
            icons[esd.getId()] = par1IconRegister.registerIcon(BambooCore.resourceDomain + esd.getIconName().toLowerCase());
        }

        // this.iconIndex = par1IconRegister.registerIcon(this.unlocalizedName);
    }
}
