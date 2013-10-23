package ruby.bamboo.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ruby.bamboo.BambooCore;
import ruby.bamboo.BambooUtil;
import ruby.bamboo.entity.EntitySlideDoor;
import ruby.bamboo.entity.EnumSlideDoor;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class ItemSlideDoor extends Item {
    private final static EnumSlideDoor[] doors = EnumSlideDoor.values();
    private Icon[] icons;

    public ItemSlideDoor(int i) {
        super(i);
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
        return super.getUnlocalizedName() + "." + doors[par1ItemStack.getItemDamage()].toString();
    }

    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
        for (EnumSlideDoor esd : doors) {
            par3List.add(new ItemStack(par1, 1, esd.getId()));
        }
    }

    @Override
    public Icon getIconFromDamage(int par1) {
        return icons[par1];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister) {
        icons = new Icon[doors.length];

        for (EnumSlideDoor esd : doors) {
            icons[esd.getId()] = par1IconRegister.registerIcon(BambooCore.resorceDmain + esd.getIconName().toLowerCase());
        }

        // this.iconIndex = par1IconRegister.registerIcon(this.unlocalizedName);
    }
}
