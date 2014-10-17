package ruby.bamboo.nei;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import ruby.bamboo.BambooCore;
import ruby.bamboo.gui.GuiMillStone;
import ruby.bamboo.item.crafting.GrindRegistory;
import ruby.bamboo.item.crafting.IGrindRecipe;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class GrindRecipeHandler extends TemplateRecipeHandler {
    public static final String RECIPE_NAME = "Grind Recipe";

    public class CachedGrindRecipe extends CachedRecipe {
        private ItemStack input;
        private ItemStack result;
        private ItemStack bonus;

        public CachedGrindRecipe(ItemStack input, ItemStack result, ItemStack bonus) {
            this.input = input;
            this.result = result;
            this.bonus = bonus;
        }

        public CachedGrindRecipe(IGrindRecipe recipe) {
            this(recipe.getInput(), recipe.getOutput(), recipe.getBonus());
        }

        @Override
        public PositionedStack getIngredient() {
            return new PositionedStack(input, 33, 24);
        }

        @Override
        public PositionedStack getResult() {
            return new PositionedStack(result, 101, 24);
        }

        @Override
        public List<PositionedStack> getOtherStacks() {
            ArrayList<PositionedStack> stacks = new ArrayList<PositionedStack>();
            if (bonus != null) {
                stacks.add(new PositionedStack(bonus, 132, 24));
            }
            return stacks;
        }

    }

    @Override
    public String getRecipeName() {
        return RECIPE_NAME;
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(64, 23, 24, 18), RECIPE_NAME));
    }

    @Override
    public String getGuiTexture() {
        return BambooCore.resourceDomain + "textures/guis/neiguimillstone.png";
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiMillStone.class;
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(RECIPE_NAME)) {
            for (IGrindRecipe recipe : GrindRegistory.getRecipeList()) {
                arecipes.add(new CachedGrindRecipe(recipe));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (IGrindRecipe recipe : GrindRegistory.getRecipeList()) {
            if (result.isItemEqual(recipe.getOutput()) || (recipe.getBonus() != null && (result.isItemEqual(recipe.getBonus())))) {
                arecipes.add(new CachedGrindRecipe(recipe));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack input) {
        for (IGrindRecipe recipe : GrindRegistory.getRecipeList()) {
            if (input.getItem() == recipe.getInput().getItem()) {
                if (recipe.getInput().getItemDamage() == IGrindRecipe.WILD_CARD || input.getItemDamage() == recipe.getInput().getItemDamage()) {
                    arecipes.add(new CachedGrindRecipe(recipe));
                }
            }
        }
    }

    @Override
    public void drawExtras(int recipe) {
        drawProgressBar(65, 22, 176, 0 + 16 * ((this.cycleticks >> 3) % 4), 16, 16, 1F, 1);
        drawProgressBar(65, 40, 192, 0 + 6 * ((this.cycleticks >> 3) % 4), 16, 6, 1F, 1);
    }
}
