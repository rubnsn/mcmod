package ruby.bamboo.nei;

import java.awt.Rectangle;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import ruby.bamboo.BambooCore;
import ruby.bamboo.gui.GuiCampfire;
import ruby.bamboo.item.crafting.CookingManager;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.recipe.ShapedRecipeHandler;

public class CampfireSharpedRecipeHandler extends ShapedRecipeHandler {

    public static final String RECIPE_NAME = "Campfire Recipe";

    @Override
    public String getRecipeName() {
        return RECIPE_NAME;
    }

    @Override
    public String getGuiTexture() {
        return BambooCore.resourceDomain + "textures/guis/campfire.png";
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiCampfire.class;
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(84, 23, 24, 18), RECIPE_NAME));
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(RECIPE_NAME) && getClass() == CampfireSharpedRecipeHandler.class) {
            for (IRecipe irecipe : CookingManager.getInstance().getRecipeList()) {
                CachedShapedRecipe recipe = null;
                if (irecipe instanceof ShapedRecipes)
                    recipe = new CachedShapedRecipe((ShapedRecipes) irecipe);
                else if (irecipe instanceof ShapedOreRecipe)
                    recipe = forgeShapedRecipe((ShapedOreRecipe) irecipe);

                if (recipe == null)
                    continue;

                recipe.computeVisuals();
                arecipes.add(recipe);
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (IRecipe irecipe : CookingManager.getInstance().getRecipeList()) {
            if (NEIServerUtils.areStacksSameTypeCrafting(irecipe.getRecipeOutput(), result)) {
                CachedShapedRecipe recipe = null;
                if (irecipe instanceof ShapedRecipes)
                    recipe = new CachedShapedRecipe((ShapedRecipes) irecipe);
                else if (irecipe instanceof ShapedOreRecipe)
                    recipe = forgeShapedRecipe((ShapedOreRecipe) irecipe);

                if (recipe == null)
                    continue;

                recipe.computeVisuals();
                arecipes.add(recipe);
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {

        for (IRecipe irecipe : CookingManager.getInstance().getRecipeList()) {
            CachedShapedRecipe recipe = null;
            if (irecipe instanceof ShapedRecipes)
                recipe = new CachedShapedRecipe((ShapedRecipes) irecipe);
            else if (irecipe instanceof ShapedOreRecipe)
                recipe = forgeShapedRecipe((ShapedOreRecipe) irecipe);

            if (recipe == null || !recipe.contains(recipe.ingredients, ingredient.getItem()))
                continue;

            recipe.computeVisuals();
            if (recipe.contains(recipe.ingredients, ingredient)) {
                recipe.setIngredientPermutation(recipe.ingredients, ingredient);
                arecipes.add(recipe);
            }
        }
    }

    @Override
    public String getOverlayIdentifier() {
        return RECIPE_NAME;
    }

    @Override
    public boolean isRecipe2x2(int recipe) {
        return false;
    }

    @Override
    public void drawExtras(int recipe) {
        drawProgressBar(85, 24, 176, 0, 24, 16, 48, 0);
        drawProgressBar(5, 6, 176, 17, 12, 35, 48, 1);
    }
}
