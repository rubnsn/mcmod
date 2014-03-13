package ruby.bamboo;

import java.io.File;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.common.Loader;

public class Config {
    public boolean windPushPlayer;
    public boolean timeAccel;
    public int maxExplosionLv;
    public boolean exDrop;
    public int deludeTexMaxReference;
    public int deludeMaxReference;
    public boolean addMagatama;
    public boolean useMagatama;

    public Config() {
        windPushPlayer = true;
        timeAccel = true;
        maxExplosionLv = 3;
        exDrop = false;
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
        prop = conf.get("BambooSettings", "ReliefItem", false);
        prop.comment = "Weed to drop a Sakura and Takenoko true:false";
        exDrop = prop.getBoolean(false);
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
        if (exDrop) {
            MinecraftForge.addGrassSeed(new ItemStack(BambooInit.takenoko), 10);
            MinecraftForge.addGrassSeed(new ItemStack(BambooInit.sakura), 10);
        }
        // ModLoader.addCommand(this);
    }

}
