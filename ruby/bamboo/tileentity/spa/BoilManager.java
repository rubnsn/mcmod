package ruby.bamboo.tileentity.spa;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BoilManager {
    private static final HashMap<Item, HashMap<Integer, IBoilItem>> boilMap = new HashMap<Item, HashMap<Integer, IBoilItem>>();
    static {
        ItemStack is = new ItemStack(Item.dyePowder);
        for (int i = 1; i < 16; i++) {
            is.setItemDamage(i);
            addBileItem(new BoilDye(), is);
        }
    }

    public static void boil(ITileEntitySpa iTileSpa, EntityItem entity) {
        IBoilItem bileItem = search(entity.getEntityItem().getItem(), entity.getEntityItem().getItemDamage());
        if (bileItem != null && bileItem.getBoileTime() < entity.age) {
            bileItem.boil(iTileSpa, entity);
        }
    }

    private static IBoilItem search(Item item, int meta) {
        if (boilMap.containsKey(item) && boilMap.get(item).containsKey(meta)) {
            return boilMap.get(item).get(meta);
        }
        return null;
    }

    public static void addBileItem(IBoilItem boilItem, ItemStack input) {
        if (boilItem != null && input != null) {
            if (!boilMap.containsKey(input.getItem())) {
                boilMap.put(input.getItem(), new HashMap<Integer, IBoilItem>());
            }
            if (!boilMap.get(input.getItem()).containsKey(input.getItemDamage())) {
                boilMap.get(input.getItem()).put(input.getItemDamage(), boilItem);
            }
        }
    }

    private static class BoilDye implements IBoilItem {
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

        @Override
        public int getBoileTime() {
            return 0;
        }

    }
}
