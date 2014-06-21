package ruby.bamboo.nei;

import ruby.bamboo.BambooCore;
import ruby.bamboo.gui.GuiCampfire;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.recipe.DefaultOverlayHandler;

public class NEIBambooConfig implements IConfigureNEI {

    @Override
    public void loadConfig() {
        CampfireSharpedRecipeHandler campfireSharpedRecipe = new CampfireSharpedRecipeHandler();
        API.registerRecipeHandler(campfireSharpedRecipe);
        API.registerUsageHandler(campfireSharpedRecipe);
        API.registerGuiOverlay(GuiCampfire.class, campfireSharpedRecipe.getOverlayIdentifier(), 0, 0);
        API.registerGuiOverlay(GuiCampfire.class, campfireSharpedRecipe.RECIPE_NAME);
        API.registerGuiOverlayHandler(GuiCampfire.class, new DefaultOverlayHandler(), campfireSharpedRecipe.RECIPE_NAME);

        CampfireSharpelessRecipeHandler campfireSharplessRecipe = new CampfireSharpelessRecipeHandler();
        API.registerRecipeHandler(campfireSharplessRecipe);
        API.registerUsageHandler(campfireSharplessRecipe);
        API.registerGuiOverlay(GuiCampfire.class, campfireSharplessRecipe.getOverlayIdentifier(), 0, 0);
        API.registerGuiOverlay(GuiCampfire.class, campfireSharplessRecipe.RECIPE_NAME);
        API.registerGuiOverlayHandler(GuiCampfire.class, new DefaultOverlayHandler(), campfireSharplessRecipe.RECIPE_NAME);
    }

    @Override
    public String getName() {
        return "NEIBamboo";
    }

    @Override
    public String getVersion() {
        return BambooCore.BAMBOO_VER;
    }

}
