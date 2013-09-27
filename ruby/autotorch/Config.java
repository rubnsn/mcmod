package ruby.autotorch;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import cpw.mods.fml.common.Loader;

public class Config
{
    private int[] skipNo;
    private boolean useInventry;
    private int range;
    private int light;
    private int block;
    Config()
    {
        File file = new File(Loader.instance().getConfigDir(), "AutoTorch.cfg");
        Configuration conf = new Configuration(file);
        //conf.defaultEncoding="MS932";
        conf.load();
        Property prop;/*
		//設置スキップ
		prop=conf.get(Configuration.CATEGORY_GENERAL,"Skip_blockID", new int[]{Block.mobSpawner.blockID});
		prop.comment="設置しないブロックID";
		skipNo=prop.getIntList();*/
        //手持から消費する
        /*
        prop=conf.get(Configuration.CATEGORY_GENERAL,"UseInventry", false);
        prop.comment="インベントリに存在する松明を消費する";
        useInventry=prop.getBoolean(false);*/
        //効果範囲
        prop = conf.get(Configuration.CATEGORY_GENERAL, "Range", 8);
        prop.comment = "効果範囲(チャンク単位)";
        range = prop.getInt(8);
        //光量しきい値
        prop = conf.get(Configuration.CATEGORY_GENERAL, "Lighting_level", 8);
        prop.comment = "設置を行う上限光量";
        light = prop.getInt(8);
        //設置物
        prop = conf.get(Configuration.CATEGORY_GENERAL, "blockID", Block.torchWood.blockID);
        prop.comment = "設置を行うブロック,松明等";
        block = prop.getInt(Block.torchWood.blockID);
        conf.save();
    }
    public int[] getSkipNo()
    {
        return skipNo;
    }
    public boolean isUseInventry()
    {
        return useInventry;
    }
    public int getRange()
    {
        return range;
    }
    public int getLight()
    {
        return light;
    }
    public int getBlock()
    {
        return block;
    }
}
