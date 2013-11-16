package ruby.bamboo.tileentity.spa;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

public class BoilItem implements IBoilItem {
    private final ItemStack output;
    private int cookTime;

    public BoilItem(ItemStack output) {
        this.output = output;
    }

    public BoilItem(ItemStack output, int cookTime) {
        this(output);
        this.cookTime = cookTime;
    }

    @Override
    public void boil(ITileEntitySpa iTileSpa, EntityItem entity) {
        if (entity.getEntityItem().stackSize-- > 0) {
            entity.worldObj.spawnEntityInWorld(new EntityItem(entity.worldObj, entity.posX, entity.posY, entity.posZ, output.copy()));
        }
    }

    @Override
    public int getBoileTime() {
        return this.cookTime;
    }

}