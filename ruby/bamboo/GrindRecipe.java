package ruby.bamboo;

import net.minecraft.item.ItemStack;

public class GrindRecipe {
    private ItemStack input;
    private ItemStack output;
    private ItemStack bonus;
    private int weight;

    public GrindRecipe(ItemStack input, ItemStack output, ItemStack bonus, int weight) {
        this.input = input;
        this.output = output;
        this.bonus = bonus;
        this.weight = weight;
    }

    public ItemStack getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }

    public ItemStack getBonus() {
        return bonus;
    }

    public int getBonusWeight() {
        return weight;
    }
}
