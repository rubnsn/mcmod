package ruby.bamboo.tileentity.spa;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import ruby.bamboo.BambooInit;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BoilManager {
    private static final HashMap<Item, HashMap<Integer, IBoilItem>> boilMap = new HashMap<Item, HashMap<Integer, IBoilItem>>();
    static {
        addSimpleBoilItem(Item.itemsList[BambooInit.boiledEggIID], 3600, Item.egg);
        addSimpleBoilItem(Item.axeGold, 5990, Item.axeIron);
        ItemStack is = new ItemStack(Item.dyePowder);
        for (int i = 0; i < 15; i++) {
            is.setItemDamage(i);
            addBoilItem(new BoilDye(), is);
        }
    }

    public static void boil(ITileEntitySpa iTileSpa, EntityItem entity) {
        IBoilItem bileItem = search(entity.getEntityItem().getItem(), entity.getEntityItem().getItemDamage());
        if (iTileSpa != null && bileItem != null) {
            bileItem.boil(iTileSpa, entity);
        }
    }

    private static IBoilItem search(Item item, int meta) {
        if (boilMap.containsKey(item)) {
            if (boilMap.get(item).containsKey(Short.MAX_VALUE)) {
                boilMap.get(item).get(Short.MAX_VALUE);
            } else if (boilMap.get(item).containsKey(meta)) {
                return boilMap.get(item).get(meta);
            }
        }
        return null;
    }

    /**
     * 単純に煮る
     * 
     * @param outPut 成果物
     * @param boilTime 煮沸時間
     * @param inPut 素材
     */
    public static void addSimpleBoilItem(Item outPut, int boilTime, Item inPut) {
        addSimpleBoilItem(new ItemStack(outPut), boilTime, new ItemStack(inPut));
    }

    public static void addSimpleBoilItem(ItemStack outPut, int boilTime, ItemStack inPut) {
        addBoilItem(new BoilItem(outPut, boilTime), inPut);
    }

    public static void addBoilItem(IBoilItem boilItem, ItemStack input) {
        if (boilItem != null && input != null) {
            if (!boilMap.containsKey(input.getItem())) {
                boilMap.put(input.getItem(), new HashMap<Integer, IBoilItem>());
            }
            if (!boilMap.get(input.getItem()).containsKey(input.getItemDamage())) {
                boilMap.get(input.getItem()).put(input.getItemDamage(), boilItem);
            }
        }
    }
}
