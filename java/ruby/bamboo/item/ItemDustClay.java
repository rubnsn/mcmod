package ruby.bamboo.item;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemDustClay extends Item {
    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
        return 0xC0C0C0;
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        if ((!entityItem.worldObj.isRemote)) {
            if (entityItem.handleWaterMovement() && entityItem.age > 3600) {
                ItemStack is = entityItem.getEntityItem();
                if (is.stackSize > 1) {
                    int i = is.stackSize / 2;
                    entityItem.worldObj.spawnEntityInWorld(new EntityItem(entityItem.worldObj, entityItem.posX, entityItem.posY, entityItem.posZ, new ItemStack(Items.clay_ball, i)));
                    is.stackSize -= i * 2;
                    if (is.stackSize == 0) {
                        entityItem.setDead();
                    }
                }
            }
        }
        return false;
    }
}
