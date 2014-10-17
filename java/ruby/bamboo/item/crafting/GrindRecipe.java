package ruby.bamboo.item.crafting;

import net.minecraft.item.ItemStack;

public class GrindRecipe implements IGrindRecipe {
    private ItemStack input;
    private ItemStack output;
    private ItemStack bonus;
    private float weight;

    public GrindRecipe(ItemStack input, ItemStack output, ItemStack bonus, float weight) {
        this.input = input;
        this.output = output;
        this.bonus = bonus;
        this.weight = weight;
    }

    @Override
    public ItemStack getInput() {
        return input;
    }

    @Override
    public ItemStack getOutput() {
        return output;
    }

    @Override
    public ItemStack getBonus() {
        return bonus;
    }

    @Override
    public float getBonusWeight() {
        return weight;
    }
}
