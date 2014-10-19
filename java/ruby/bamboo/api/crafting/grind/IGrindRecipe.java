package ruby.bamboo.api.crafting.grind;

import net.minecraft.item.ItemStack;

public interface IGrindRecipe {
    public static final short WILD_CARD = Short.MAX_VALUE;

    //input
    public IGrindInputItem getInput();

    //output:100% obtain
    public ItemStack getOutput();

    //bonus item:random obtain
    public ItemStack getBonus();

    //rand.nextFloat() <= getBonusWeight()
    public float getBonusWeight();
}
