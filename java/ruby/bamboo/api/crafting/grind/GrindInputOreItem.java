package ruby.bamboo.api.crafting.grind;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class GrindInputOreItem implements IGrindInputItem {
    public final String oreName;
    public final int stackSize;
    public final int[] oreIDs;

    public GrindInputOreItem(String oreName, int stackSize) {
        this(oreName, stackSize, new int[] { OreDictionary.getOreID(oreName) });
    }

    public GrindInputOreItem(String oreName, int stackSize, int[] oreIDs) {
        this.oreName = oreName;
        this.stackSize = stackSize;
        this.oreIDs = oreIDs;
    }

    @Override
    public Item getItem() {
        return OreDictionary.getOres(oreName).get(0).getItem();
    }

    @Override
    public int getStackSize() {
        return this.stackSize;
    }

    @Override
    public int getItemDamage() {
        return 0;
    }

    @Override
    public int[] getOreIDs() {
        return this.oreIDs;
    }

    @Override
    public ItemStack getItemStack() {
        return OreDictionary.getOres(oreName).get(0).copy();
    }

    @Override
    public int hashCode() {
        return oreName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GrindInputOreItem)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        GrindInputOreItem rhs = (GrindInputOreItem) obj;
        return oreName.equals(rhs.oreName);
    }
}
