package ruby.bamboo.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ruby.bamboo.entity.EntityZabuton;

public class ItemZabuton extends Item {

    public ItemZabuton() {
        super();
        this.setHasSubtypes(true);
    }

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
        if (!par3World.isRemote) {
            EntityZabuton entity = new EntityZabuton(par3World);
            entity.setPositionAndRotation(par4 + par8, par5 + par9, par6 + par10, par2EntityPlayer.rotationYaw, par2EntityPlayer.rotationPitch);
            par3World.spawnEntityInWorld(entity);
        }
        return true;
    }

}
