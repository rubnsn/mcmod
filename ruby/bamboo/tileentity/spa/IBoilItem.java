package ruby.bamboo.tileentity.spa;

import net.minecraft.entity.item.EntityItem;

public interface IBoilItem {
    public void boil(ITileEntitySpa iTileSpa, EntityItem entity);

    public int getBoileTime();
}
