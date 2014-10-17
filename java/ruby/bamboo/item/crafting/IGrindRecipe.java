package ruby.bamboo.item.crafting;

import net.minecraft.item.ItemStack;

public interface IGrindRecipe {
    public static final short WILD_CARD = Short.MAX_VALUE;

    //input
    public ItemStack getInput();

    //output:100% obtain
    public ItemStack getOutput();

    //bonus item:random obtain
    public ItemStack getBonus();

    //rand.nextFloat() <= getBonusWeight()
    public float getBonusWeight();
}
