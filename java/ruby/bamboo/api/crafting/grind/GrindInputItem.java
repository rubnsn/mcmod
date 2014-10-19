package ruby.bamboo.api.crafting.grind;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class GrindInputItem implements IGrindInputItem {
    public final Item item;
    public final int stackSize;
    public final int itemDmg;
    public final int[] oreIDs;

    public GrindInputItem(ItemStack stack) {
        this(stack.getItem(), stack.stackSize, stack.getItemDamage(), OreDictionary.getOreIDs(stack));
    }

    public GrindInputItem(Item item, int stackSize, int itemDamage) {
        this(new ItemStack(item, stackSize, itemDamage));
    }

    public GrindInputItem(Item item, int stackSize, int itemDamage, int[] oreIDs) {
        this.item = item;
        this.stackSize = stackSize;
        this.itemDmg = itemDamage;
        this.oreIDs = oreIDs;
    }

    @Override
    public Item getItem() {
        return this.item;
    }

    @Override
    public int getStackSize() {
        return this.stackSize;
    }

    @Override
    public int getItemDamage() {
        return this.itemDmg;
    }

    @Override
    public int[] getOreIDs() {
        return this.oreIDs;
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemStack(item, stackSize, itemDmg);
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder().append(this.getItem());
        if (this.getItemDamage() != IGrindRecipe.WILD_CARD) {
            return hcb.append(this.getItemDamage()).toHashCode();
        } else {
            return hcb.toHashCode();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IGrindInputItem)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        IGrindInputItem rhs = (IGrindInputItem) obj;
        EqualsBuilder eb = new EqualsBuilder().append(this.getItem(), rhs.getItem());
        if (this.getItemDamage() != IGrindRecipe.WILD_CARD) {
            return eb.append(this.getItemDamage(), rhs.getItemDamage()).isEquals();
        } else {
            return eb.isEquals();
        }
    }

}
