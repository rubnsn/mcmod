package ruby.bamboo;

import static ruby.bamboo.Config.*;
import static ruby.bamboo.BambooInit.*;
import cpw.mods.fml.common.registry.GameRegistry;
import ruby.bamboo.entity.EnumSlideDoor;
import ruby.bamboo.item.EnumShavedIce;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class BambooRecipe
{
    private static final String BAMBOO = "bamboo";
    private static final String WHEAT_RICE = "wheatRice";
    private static final String TUDURA = "tudura";
    private static final String BUSKET = "bambooBasket";
    public BambooRecipe()
    {
        recipeInit();
    }

    private void recipeInit()
    {
        //竹製品
        addShapedOreRecipe(new ItemStack(tuduraIID, 1, 0), " # ", "# #", " # ", '#', BAMBOO);
        addShapedOreRecipe(new ItemStack(Item.paper, 1), "###", '#', BAMBOO);
        addShapedOreRecipe(new ItemStack(Item.stick, 2), "#", "#", '#', BAMBOO);
        addShapedOreRecipe(getItemStack(SINGLE_TEX_DECO, 2, 0), "##", "##", '#', BAMBOO);
        //きつねび
        addShapedOreRecipe(getItemStack(KITUNEBI, 6, 0), "YYY", "#X#", "YYY", 'Y', "dyeBlue", '#', TUDURA, 'X', Block.pumpkinLantern);
        addShapedOreRecipe(getItemStack(KITUNEBI, 6, 0), "Y#Y", "YXY", "Y#Y", 'Y', "dyeBlue", '#', TUDURA, 'X', Block.pumpkinLantern);
        /*GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(kitunebi, 6),
        			 "YYY", "YXY", "YYY", 'X',
        					Block.pumpkinLantern, 'Y',"oreNikolite"));*/
        //うつわ
        addShapedOreRecipe(new ItemStack(bambooBasketIID, 1, 0), "# #", " # ", '#', BAMBOO);
        //竹槍
        GameRegistry.addRecipe(new ItemStack(bambooSpearIID, 2, 0), "##", '#', getItemStack(SINGLE_TEX_DECO, 1, 0));
        //引き戸類
        addShapedOreRecipe(new ItemStack(slideDoorsIID, 2, EnumSlideDoor.HUSUMA.getId()), "XYX", "X#X", "XYX", 'X', Item.stick, 'Y', Item.paper, '#', TUDURA);
        addShapedOreRecipe(new ItemStack(slideDoorsIID, 2, EnumSlideDoor.SHOZI.getId()), "XYX", "Y#Y", "XYX", '#', TUDURA, 'X', Item.stick, 'Y', Item.paper);
        addShapedOreRecipe(new ItemStack(slideDoorsIID, 2, EnumSlideDoor.GLASS.getId()), "XYX", "X#X", "XYX", '#', TUDURA, 'X', Block.fenceIron, 'Y', Block.thinGlass);
        addShapedOreRecipe(new ItemStack(slideDoorsIID, 2, EnumSlideDoor.GGLASS.getId()), "XYX", "X#X", "XYX", '#', TUDURA, 'X', Item.stick, 'Y', Block.thinGlass);
        addShapedOreRecipe(new ItemStack(slideDoorsIID, 2, EnumSlideDoor.YUKI.getId()), "XYX", "X#X", "XZX", '#', TUDURA, 'X', Item.stick, 'Y', Item.paper , 'Z', Block.thinGlass);
        //パネル類
        addShapedOreRecipe(getItemStack(BAMBOO_PANEL, 8, 0), "###", "###", '#', BAMBOO);
        addShapedOreRecipe(getItemStack(BAMBOO_PANEL, 8, 1), "#X#", "###", '#', Block.planks , 'X', TUDURA);
        addShapedOreRecipe(getItemStack(BAMBOO_PANEL, 6, 2), "#X#", "#X#", '#', BAMBOO , 'X', getItemStack(BAMBOO_PANEL, 1, 0));
        addShapedOreRecipe(getItemStack(BAMBOO_PANEL, 6, 3), "#X#", "#X#", '#', BAMBOO , 'X', getItemStack(BAMBOO_PANEL, 1, 2));
        addShapedOreRecipe(getItemStack(BAMBOO_PANEL, 8, 4), "#X#", "###", '#', new ItemStack(Block.cloth, 1, 11), 'X', TUDURA);
        addShapedOreRecipe(getItemStack(BAMBOO_PANEL, 8, 5), "#X#", "###", '#', new ItemStack(Block.cloth, 1, 10) , 'X', TUDURA);
        //爆竹類
        addShapedOreRecipe(new ItemStack(firecrackerIID, 10, 0), " # ", " X ", " # ", '#', BUSKET, 'X', Item.gunpowder);
        addShapedOreRecipe(new ItemStack(firecrackerIID, 1, 1), " # ", "XYX", " # ", '#', BUSKET, 'X', Item.gunpowder, 'Y', new ItemStack(firecrackerIID, 1, 0));
        addShapedOreRecipe(new ItemStack(firecrackerIID, 1, 2), " # ", "XYX", " # ", '#', BUSKET, 'X', Item.gunpowder, 'Y', new ItemStack(firecrackerIID, 1, 1));
        //飯類
        addShapelessOreRecipe(new ItemStack(foodsIID, 1, 0), Item.wheat, BUSKET);
        addShapelessOreRecipe(new ItemStack(foodsIID, 1, 1), Item.beefCooked, WHEAT_RICE);
        addShapelessOreRecipe(new ItemStack(foodsIID, 1, 2), Item.porkCooked, WHEAT_RICE);
        addShapelessOreRecipe(new ItemStack(foodsIID, 1, 3), Block.mushroomRed, WHEAT_RICE);
        addShapelessOreRecipe(new ItemStack(foodsIID, 1, 4), Item.porkCooked, BAMBOO);
        addShapelessOreRecipe(new ItemStack(foodsIID, 1, 5), Item.beefCooked, BAMBOO);
        addShapelessOreRecipe(new ItemStack(foodsIID, 1, 6), getItemInstance(takenokoIID), WHEAT_RICE);
        addShapelessOreRecipe(new ItemStack(foodsIID, 1, 7), Item.egg, WHEAT_RICE);
        addShapelessOreRecipe(new ItemStack(foodsIID, 1, 8), Item.egg, Item.chickenCooked, WHEAT_RICE);
        addShapelessOreRecipe(new ItemStack(foodsIID, 1, 9), Item.fishRaw, WHEAT_RICE);
        addShapelessOreRecipe(new ItemStack(foodsIID, 1, 10), Item.chickenCooked, BAMBOO);
        //箪笥
        addShapedOreRecipe(getItemStack(JP_CHEST, 1, 0), "YYY", "Y#Y", "YYY", '#', TUDURA, 'Y', "logWood");
        //畳 茅葺き
        addShapedOreRecipe(getItemStack(DIR_SQUARE, 4, 0),  " X ", "X#X", " X ", '#', TUDURA, 'X', Item.wheat);
        addShapedOreRecipe(getItemStack(DIR_SQUARE, 8, 4), "XXX", "X#X", "XXX", '#', TUDURA, 'X', Item.wheat);
        GameRegistry.addShapelessRecipe(getItemStack(DIR_SQUARE, 1, 12), getItemStack(DIR_SQUARE, 1, 0));
        GameRegistry.addShapelessRecipe(getItemStack(DIR_SQUARE, 1, 0), getItemStack(DIR_SQUARE, 1, 12));
        addSlabRecipe(getItemStack(HALF_DIR_SQUARE, 6, 0), getItemStack(DIR_SQUARE, 1, 0));
        addSlabRecipe(getItemStack(HALF_DIR_SQUARE, 6, 4), getItemStack(DIR_SQUARE, 1, 4));
        addSlabRecipe(getItemStack(HALF_DIR_SQUARE, 6, 8), getItemStack(DIR_SQUARE, 1, 8));
        addSlabRecipe(getItemStack(HALF_DIR_SQUARE, 6, 10), getItemStack(DIR_SQUARE, 1, 10));
        addSlabRecipe(getItemStack(HALF_DIR_SQUARE, 6, 12), getItemStack(DIR_SQUARE, 1, 12));
        addCarpetRecipe(getItemStack(DECO_CARPET, 3, 0), getItemStack(DIR_SQUARE, 1, 0));
        addCarpetRecipe(getItemStack(DECO_CARPET, 3, 2), getItemStack(DIR_SQUARE, 1, 4));
        addCarpetRecipe(getItemStack(DECO_CARPET, 3, 4), getItemStack(DIR_SQUARE, 1, 8));
        addCarpetRecipe(getItemStack(DECO_CARPET, 3, 6), getItemStack(DIR_SQUARE, 1, 10));
        addCarpetRecipe(getItemStack(DECO_CARPET, 3, 8), getItemStack(DIR_SQUARE, 1, 12));
        //掛け軸
        addShapedOreRecipe(new ItemStack(kakezikuIID, 1, 0), "YYY", "X#X", "XXX", '#', TUDURA, 'Y', Item.stick, 'X', Item.paper);
        //行灯
        addShapedOreRecipe(getItemStack(ANDON, 1, 0), "###", "#Y#", "#X#", '#', Item.stick, 'X', Block.torchWood, 'Y', TUDURA);
        //ふとん
        addShapedOreRecipe(new ItemStack(hutonIID, 1, 0), " # ", "XXX", "XXX", '#', TUDURA, 'X', Block.cloth);
        //茅葺屋根
        addShapedOreRecipe(getItemStack(KAYABUKI_ROOF, 6, 0), "#  ", "#X ", "###", 'X', TUDURA, '#', Item.wheat);
        //温泉ユニット
        addShapedOreRecipe(getItemStack(SPA_UNIT, 1, 0), "#Z#", "#X#", "#Y#", 'Z', TUDURA, '#', Block.cobblestone, 'Y', Item.bucketLava, 'X', Item.bucketWater);
        //かき氷
        EnumShavedIce[] esis = EnumShavedIce.values();

        for (EnumShavedIce esi: esis)
        {
            if (esi.material != null)
            {
                addShapelessOreRecipe(new ItemStack(shavedIceIID, 1, esi.id), esi.material, Block.blockSnow, Item.sugar, BUSKET);
                GameRegistry.addShapelessRecipe(new ItemStack(shavedIceIID, 1, esi.id), esi.material, new ItemStack(shavedIceIID, 1, 0));
            }
            else
            {
                addShapelessOreRecipe(new ItemStack(shavedIceIID, 1, esi.id), Block.blockSnow, Item.sugar, BUSKET);
            }

            GameRegistry.addShapelessRecipe(new ItemStack(shavedIceIID, 1, esi.id + esis.length), Item.bucketMilk, new ItemStack(shavedIceIID, 1, esi.id));
        }

        //風鈴
        addShapedOreRecipe(new ItemStack(windChimeIID, 1, 0), "#", "X", "B", '#', Item.silk, 'X', TUDURA, 'B', Item.paper);
        //ふくろ
        addShapedOreRecipe(new ItemStack(itemSackIID, 1, 0), "###", "WXW", "WWW", 'X', TUDURA, '#', Item.silk, 'W', Block.cloth);
        GameRegistry.addShapelessRecipe(new ItemStack(itemSackIID, 1, 0), new ItemStack(itemSackIID, 1, 32767));
        //雪球
        GameRegistry.addRecipe(new ItemStack(snowBallIID, 32, 0), "###", "#X#", "###", '#', Block.blockSnow, 'X', Block.cobblestone);
        GameRegistry.addRecipe(new ItemStack(snowBallIID, 32, 1), "###", "#X#", "###", '#', Block.blockSnow, 'X', Block.ice);
        GameRegistry.addRecipe(new ItemStack(snowBallIID, 32, 2), "###", "#X#", "###", '#', Block.blockSnow, 'X', Item.ingotIron);
        GameRegistry.addRecipe(new ItemStack(snowBallIID, 01, 3), "###", "#X#", "###", '#', Block.blockSnow, 'X', Item.goldNugget);
        GameRegistry.addRecipe(new ItemStack(snowBallIID, 32, 4), "###", "#X#", "###", '#', Block.blockSnow, 'X', Item.diamond);
        GameRegistry.addRecipe(new ItemStack(snowBallIID, 32, 5), "###", "#X#", "###", '#', Block.blockSnow, 'X', Item.bucketWater);
        GameRegistry.addRecipe(new ItemStack(snowBallIID, 32, 6), "###", "#X#", "###", '#', Block.blockSnow, 'X', Item.enderPearl);
        GameRegistry.addRecipe(new ItemStack(snowBallIID, 04, 7), "###", "#X#", "###", '#', Block.blockSnow, 'X', Block.mushroomRed);
        GameRegistry.addRecipe(new ItemStack(snowBallIID, 04, 8), "###", "#X#", "###", '#', Block.blockSnow, 'X', Item.spiderEye);
        GameRegistry.addRecipe(new ItemStack(snowBallIID, 01, 9), "###", "#X#", "###", '#', Block.blockSnow, 'X', Item.melon);
        //扇子
        addShapedOreRecipe(new ItemStack(fanIID, 1, 0), "XYY", "XYY", "XXX", 'X', BAMBOO, 'Y', Item.paper);
        //武器
        addShapedOreRecipe(new ItemStack(katanaIID, 1, 0), "X", "X", "Y", 'X', Item.ingotIron, 'Y', TUDURA);
        GameRegistry.addRecipe(new ItemStack(bambooBowIID, 1, 0), " XY", "X Y", " XY", 'X', getItemStack(SINGLE_TEX_DECO,1,0), 'Y', Item.silk);
        addShapedOreRecipe(new ItemStack(bambooSwordIID, 1, 0), " X ", " X ", " Y ", 'X', BAMBOO, 'Y', TUDURA);
        //サクラから桜木材
        GameRegistry.addShapelessRecipe(getItemStack(TWO_DIR_DECO_SQUARE, 4, 2), getBlockInstance(SAKURA_LOG));
        //カメレオン類
        addShapedOreRecipe(getItemStack(DELUDE_WIDTH, 6, 0), " # ", "YXY", " # ", '#', TUDURA, 'X', Block.pumpkinLantern, 'Y', Block.slowSand);
        addShapedOreRecipe(getItemStack(DELUDE_HEIGHT, 6, 0),  " Y ", "#X#", " Y ", '#', TUDURA, 'X', Block.pumpkinLantern, 'Y', Block.slowSand);
        addShapedOreRecipe(getItemStack(DELUDE_STAIR, 8, 0), "Y  ", "YX ", "#YY", '#', TUDURA, 'X', Block.pumpkinLantern, 'Y', Block.slowSand);
        addShapedOreRecipe(getItemStack(DELUDE_STAIR, 8, 0), "  Y", " XY", "YY#", '#', TUDURA, 'X', Block.pumpkinLantern, 'Y', Block.slowSand);
        addShapedOreRecipe(getItemStack(DELUDE_PLATE,1,0), "##", '#', getItemStack(DELUDE_WIDTH,1,0));
        //いろり
        addShapedOreRecipe(getItemStack(IRORI, 1, 0), " # ", "ZXZ", "YYY", '#', TUDURA, 'Z', Block.fenceIron, 'X', Item.flintAndSteel, 'Y', new ItemStack(Item.coal, 1, 1));
        //風車水車
        addShapedOreRecipe(new ItemStack(windmillIID, 1, 0), "YXY", "X#X", "YXY", '#', TUDURA, 'X', BAMBOO, 'Y', getItemStack(SINGLE_TEX_DECO,1,0));
        addShapedOreRecipe(new ItemStack(windmillIID, 1, 1), "YXY", "X#X", "YXY", '#', TUDURA, 'X', BAMBOO, 'Y', Block.cloth);
        addShapedOreRecipe(new ItemStack(waterWheelIID, 1, 0), "YXY", "X#X", "YXY", '#', TUDURA, 'X', BAMBOO, 'Y', Block.planks);
        //方向なしデコブロック
        addShapedOreRecipe(getItemStack(DECO_SQUARE, 8, 0), "###", "#X#", "###", '#', Block.sand, 'X', TUDURA);
        addShapedOreRecipe(getItemStack(DECO_SQUARE, 8, 1), "Y#Y", "#X#", "Y#Y", '#', getItemStack(DECO_SQUARE, 8, 5), 'X', TUDURA, 'Y', getItemStack(DECO_SQUARE, 8, 0));
        addShapedOreRecipe(getItemStack(DECO_SQUARE, 8, 2), "###", "#X#", "###", '#', new ItemStack(Block.planks, 1, 0), 'X', TUDURA);
        addShapedOreRecipe(getItemStack(DECO_SQUARE, 8, 3), "###", "#X#", "###", '#', new ItemStack(Block.planks, 1, 1), 'X', TUDURA);
        addShapedOreRecipe(getItemStack(DECO_SQUARE, 8, 4), "###", "#X#", "###", '#', new ItemStack(Block.planks, 1, 2), 'X', TUDURA);
        addShapedOreRecipe(getItemStack(DECO_SQUARE, 8, 5), "###", "#X#", "###", '#', Item.brick, 'X', TUDURA);
        //デコハーフ
        addSlabRecipe(getItemStack(HALF_DECO_SQUARE, 6, 0), getItemStack(DECO_SQUARE, 8, 0));
        addSlabRecipe(getItemStack(HALF_DECO_SQUARE, 6, 1), getItemStack(DECO_SQUARE, 8, 1));
        addSlabRecipe(getItemStack(HALF_DECO_SQUARE, 6, 2), getItemStack(DECO_SQUARE, 8, 2));
        addSlabRecipe(getItemStack(HALF_DECO_SQUARE, 6, 3), getItemStack(DECO_SQUARE, 8, 3));
        addSlabRecipe(getItemStack(HALF_DECO_SQUARE, 6, 4), getItemStack(DECO_SQUARE, 8, 4));
        addSlabRecipe(getItemStack(HALF_DECO_SQUARE, 6, 5), getItemStack(DECO_SQUARE, 8, 5));
        //方向付きデコ（桜木材）
        addShapedOreRecipe(getItemStack(TWO_DIR_DECO_SQUARE, 8, 0), "###", "#X#", "###", '#', getItemStack(TWO_DIR_DECO_SQUARE, 8, 2), 'X', TUDURA);
        //方向付きデコハーフ
        addSlabRecipe(getItemStack(HALF_TWO_DIR_DECO_SQUARE, 6, 0), getItemStack(TWO_DIR_DECO_SQUARE, 4, 0));
        addSlabRecipe(getItemStack(HALF_TWO_DIR_DECO_SQUARE, 6, 2), getItemStack(TWO_DIR_DECO_SQUARE, 4, 2));
        //瓦
        //瓦階段
        GameRegistry.addRecipe(getItemStack(KAWARA_ROOF, 4, 0), "#  ", "## ", "###", '#', getItemStack(DECO_SQUARE, 1, 5));
        GameRegistry.addRecipe(getItemStack(KAWARA_ROOF, 4, 0), "  #", " ##", "###", '#', getItemStack(DECO_SQUARE, 1, 5));
        //おぼん
        GameRegistry.addRecipe(new ItemStack(obonIID, 1, 0), "###", "# #", '#', getItemStack(TWO_DIR_DECO_SQUARE, 4, 2));
        //柱
        addShapedOreRecipe(getItemStack(THICK_SAKURA_PILLAR,4,0), "#","X","#",'#',getItemStack(TWO_DIR_DECO_SQUARE,1,2),'X',TUDURA);
        addShapedOreRecipe(getItemStack(THIN_SAKURA_PILLAR,4,0), "#","X","#",'#',getItemStack(THICK_SAKURA_PILLAR,1,0),'X',TUDURA);
        addShapedOreRecipe(getItemStack(THICK_ORC_PILLAR,4,0), "#","X","#",'#',new ItemStack(Block.planks,1,0),'X',TUDURA);
        addShapedOreRecipe(getItemStack(THIN_ORC_PILLAR,4,0), "#","X","#",'#',getItemStack(THICK_ORC_PILLAR,1,0),'X',TUDURA);
        addShapedOreRecipe(getItemStack(THICK_SPRUCE_PILLAR,4,0), "#","X","#",'#',new ItemStack(Block.wood,1,1),'X',TUDURA);
        addShapedOreRecipe(getItemStack(THIN_SPRUCE_PILLAR,4,0), "#","X","#",'#',getItemStack(THICK_SPRUCE_PILLAR,1,0),'X',TUDURA);
        //鉱石辞書
        addOreDictionary();
        //粉砕レシピ
        addGrindRecipe();
        //やきもの
        GameRegistry.addSmelting(bambooBlockBID, new ItemStack(Item.coal, 1, 1), 0.15F);
        GameRegistry.addSmelting(sakuraLogBID, new ItemStack(Item.coal, 1, 1), 0.15F);
    }

    private void addGrindRecipe()
    {
        GrindRegistory.addRecipe(new ItemStack(Block.gravel), new ItemStack(Block.sand), Block.stone , 5);
        GrindRegistory.addRecipe(new ItemStack(Block.gravel), new ItemStack(Block.sand), Block.cobblestone, 10);
        GrindRegistory.addRecipe(new ItemStack(Block.sand), new ItemStack(Item.flint), Block.gravel, 7);
        GrindRegistory.addRecipe(new ItemStack(Item.dyePowder, 3, 15), new ItemStack(Item.dyePowder, 2, 15), Item.bone, 2);
        GrindRegistory.addRecipe(new ItemStack(Item.blazePowder, 2, 0), new ItemStack(Item.blazePowder, 1, 0), Item.blazeRod, 2);
        GrindRegistory.addRecipe(new ItemStack(Item.clay, 4, 0), new ItemStack(Block.hardenedClay, 1, GrindRegistory.WILD_CARD));
        GrindRegistory.addRecipe(new ItemStack(Item.clay, 4, 0), new ItemStack(Block.stainedClay, 1, GrindRegistory.WILD_CARD));
    }

    private void addOreDictionary()
    {
        OreDictionary.registerOre(BAMBOO, getBlockInstance(BAMBOO));
        OreDictionary.registerOre(BAMBOO, getItemInstance(bambooIID));
        OreDictionary.registerOre(WHEAT_RICE, new ItemStack(foodsIID, 1, 0));
        OreDictionary.registerOre(TUDURA, getItemInstance(tuduraIID));
        OreDictionary.registerOre(BUSKET, getItemInstance(bambooBasketIID));
        OreDictionary.registerOre("treeSapling", getBlockInstance(SAKURA_SAPLING));
        OreDictionary.registerOre("logWood", getBlockInstance(SAKURA_LOG));
        OreDictionary.registerOre("plankWood", getItemStack(TWO_DIR_DECO_SQUARE, 1, 2));
    }
    private void addSlabRecipe(ItemStack output, ItemStack input)
    {
        GameRegistry.addRecipe(output, "###", '#', input);
    }
    private void addCarpetRecipe(ItemStack output, ItemStack input)
    {
        GameRegistry.addRecipe(output, "##", '#', input);
    }
    private ItemStack getItemStack(String blockName,int amount,int meta){
    	return new ItemStack(getBlockInstance(blockName),amount,meta);
    }
    private void addShapedOreRecipe(ItemStack itemStack, Object... objects)
    {
        GameRegistry.addRecipe(new ShapedOreRecipe(itemStack, objects));
    }
    private void addShapelessOreRecipe(ItemStack itemStack, Object... objects)
    {
        GameRegistry.addRecipe(new ShapelessOreRecipe(itemStack, objects));
    }
}
