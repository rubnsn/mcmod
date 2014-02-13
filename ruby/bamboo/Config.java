package ruby.bamboo;

import java.io.File;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class Config {
    public boolean windPushPlayer;
    public boolean timeAccel;
    public int maxExplosionLv;
    public boolean exRecipe;
    public int deludeTexMaxReference;
    public int deludeMaxReference;
    public boolean addMagatama;
    public boolean useMagatama;

    public Config() {
        windPushPlayer = true;
        timeAccel = true;
        maxExplosionLv = 3;
        exRecipe = false;
        deludeTexMaxReference = 2;
        deludeMaxReference = 2;
        useMagatama = false;
    }

    public void serverInit() {
        File file = new File(Loader.instance().getConfigDir(), "mod_BambooServerConfig.cfg");
        Configuration conf = new Configuration(file);
        conf.load();
        Property prop;
        // 風えんてて
        prop = conf.get("BambooSettings", "WindPushPlayer", true);
        prop.comment = "EntityWind knock back for EntityPlayer true:false";
        windPushPlayer = prop.getBoolean(true);
        // ふとんかそく
        prop = conf.get("BambooSettings", "TimeAccel", true);
        prop.comment = "Acceleration time by futon true:false";
        timeAccel = prop.getBoolean(true);
        // ばくちくれべる
        prop = conf.get("BambooSettings", "MaxExplosionLv", 3);
        prop.comment = "Possible level firecracker explosion 0~3";
        maxExplosionLv = prop.getInt();
        // exレシピ
        prop = conf.get("BambooSettings", "ExRecipe", false);
        prop.comment = "Extra recipes true:false";
        exRecipe = prop.getBoolean(false);
        // 雪を溶かさない
        prop = conf.get("BambooSettings", "UpdateStopSnow", false);
        prop.comment = "Not melt snow with light true:false";
        Blocks.snow.setTickRandomly(!prop.getBoolean(false));
        // 氷溶かさない
        prop = conf.get("BambooSettings", "UpdateStopIce", false);
        prop.comment = "Not melt ice with light true:false";
        Blocks.ice.setTickRandomly(!prop.getBoolean(false));
        // テクスチャ最大参照
        prop = conf.get("BambooSettings", "DeludeMaxTexReference", 2);
        prop.comment = "delude texture max reference";
        deludeTexMaxReference = prop.getInt();
        // 右クリック最大参照
        prop = conf.get("BambooSettings", "DeludeMaxRightClickReference", 2);
        prop.comment = "delude right click max reference";
        deludeMaxReference = prop.getInt();
        // 勾玉をダンジョンチェストへ生成するか
        prop = conf.get("BambooSettings", "addMagatama", true);
        prop.comment = "add magatama to DunsionChests";
        addMagatama = prop.getBoolean(true);
        // 勾玉を使用できるか
        prop = conf.get("BambooSettings", "useMagatama", false);
        prop.comment = "This item is to erode the terrain greatly";
        useMagatama = prop.getBoolean(false);
        conf.save();
        reSetExRecipes();
        // ModLoader.addCommand(this);
    }

    public void reSetExRecipes() {
        if (exRecipe) {
            GameRegistry.addRecipe(new ItemStack(BambooInit.takenoko, 1, 0), new Object[] { "YYY", "YXY", "YYY", 'Y', Items.glowstone_dust, 'X', Items.reeds });
            GameRegistry.addRecipe(new ItemStack(BambooInit.sakura, 1, 0), new Object[] { "YYY", "YXY", "YYY", 'Y', Blocks.netherrack, 'X', Blocks.sapling });
        } else {
            for (int j = 0; j < CraftingManager.getInstance().getRecipeList().size(); ++j) {
                IRecipe ir = (IRecipe) CraftingManager.getInstance().getRecipeList().get(j);

                if (ir.getRecipeOutput() != null) {
                    if (ir.getRecipeOutput().getItem() == BambooInit.takenoko || ir.getRecipeOutput().getItem() == Item.getItemFromBlock(BambooInit.sakura)) {
                        CraftingManager.getInstance().getRecipeList().remove(j);
                    }
                }
            }
        }
    }
}
