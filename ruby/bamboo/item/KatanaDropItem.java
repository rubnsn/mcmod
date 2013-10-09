package ruby.bamboo.item;

import net.minecraft.item.ItemStack;

public class KatanaDropItem {
    private ItemStack dropItem;
    private float reality;
    private int randomAmount;

    public KatanaDropItem(ItemStack dropItem, float reality) {
        this.dropItem = dropItem;
        this.reality = reality;
        this.randomAmount = 0;
    }

    public KatanaDropItem setRandomAddAmount(int amount) {
        this.randomAmount = amount;
        return this;
    }

    public float getReality() {
        return reality;
    }

    public ItemStack getDropItem() {
        return dropItem;
    }

    public int getRandomAmount() {
        return randomAmount;
    }
}
