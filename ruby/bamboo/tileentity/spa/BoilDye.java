package ruby.bamboo.tileentity.spa;

import net.minecraft.entity.item.EntityItem;

public class BoilDye implements IBoilItem {
    private static final int dyePattern[] = new int[16];
    static {
        dyePattern[1] = 0x000404;
        dyePattern[2] = 0x040004;
        dyePattern[3] = 0x040808;
        dyePattern[4] = 0x040400;
        dyePattern[5] = 0x000400;
        dyePattern[6] = 0x040202;
        dyePattern[7] = 0x010101;
        dyePattern[8] = 0x020202;
        dyePattern[9] = 0x020404;
        dyePattern[10] = 0x040204;
        dyePattern[11] = 0x000002;
        dyePattern[12] = 0x040402;
        dyePattern[13] = 0x040406;
        dyePattern[14] = 0x000004;
        dyePattern[15] = 0x000000;
    }

    @Override
    public void boil(ITileEntitySpa iTileSpa, EntityItem entity) {
        while (entity.getEntityItem().stackSize-- > 0) {
            iTileSpa.addColor(dyePattern[entity.getEntityItem().getItemDamage()]);
        }
        iTileSpa.colorUpdate();
    }
}
