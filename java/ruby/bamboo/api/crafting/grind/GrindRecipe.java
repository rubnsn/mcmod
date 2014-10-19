package ruby.bamboo.api.crafting.grind;

import net.minecraft.item.ItemStack;

public class GrindRecipe implements IGrindRecipe {
    private IGrindInputItem input;
    private ItemStack output;
    private ItemStack bonus;
    private float weight;

    public GrindRecipe(IGrindInputItem input, ItemStack output, ItemStack bonus, float weight) {
        this.input = input;
        this.output = output;
        this.bonus = bonus;
        this.weight = weight;
    }

    @Override
    public IGrindInputItem getInput() {
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
