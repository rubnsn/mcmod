package ruby.bamboo;

import static ruby.bamboo.BambooInit.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import ruby.bamboo.entity.EnumSlideDoor;
import ruby.bamboo.grinder.GrindRegistory;
import ruby.bamboo.item.EnumShavedIce;
import cpw.mods.fml.common.registry.GameRegistry;

public class BambooRecipe {
    private static final String BAMBOO = "bamboo";
    private static final String WHEAT_RICE = "wheatRice";
    private static final String TUDURA = "tudura";
    private static final String BUSKET = "bambooBasket";
    private static final String CROP_RICE = "cropRice";

    public BambooRecipe() {
        recipeInit();
    }

    private void recipeInit() {
        // 竹製品
        addShapedOreRecipe(new ItemStack(tudura, 1, 0), " # ", "# #", " # ", '#', BAMBOO);
        addShapedOreRecipe(new ItemStack(Items.paper, 1), "###", '#', BAMBOO);
        addShapedOreRecipe(new ItemStack(Items.stick, 2), "#", "#", '#', BAMBOO);
        addShapedOreRecipe(new ItemStack(singleTexDeco, 2, 0), "##", "##", '#', BAMBOO);
        // きつねび
        addShapedOreRecipe(new ItemStack(kitunebi, 6, 0), "YYY", "#X#", "YYY", 'Y', "dyeBlue", '#', TUDURA, 'X', Blocks.pumpkin_stem);
        addShapedOreRecipe(new ItemStack(kitunebi, 6, 0), "Y#Y", "YXY", "Y#Y", 'Y', "dyeBlue", '#', TUDURA, 'X', Blocks.pumpkin_stem);
        addShapedOreRecipe(new ItemStack(kitunebi, 6, 0), "YYY", "#X#", "YYY", 'Y', "dyeBlue", '#', TUDURA, 'X', Items.ender_pearl);
        addShapedOreRecipe(new ItemStack(kitunebi, 6, 0), "Y#Y", "YXY", "Y#Y", 'Y', "dyeBlue", '#', TUDURA, 'X', Items.ender_pearl);
        // うつわ
        addShapedOreRecipe(new ItemStack(bambooBasket, 1, 0), "# #", " # ", '#', BAMBOO);
        // 竹槍
        GameRegistry.addRecipe(new ItemStack(bambooSpear, 2, 0), "##", '#', new ItemStack(singleTexDeco, 1, 32767));
        // 引き戸類
        addShapedOreRecipe(new ItemStack(slideDoors, 2, EnumSlideDoor.HUSUMA.getId()), "XYX", "X#X", "XYX", 'X', Items.stick, 'Y', Items.paper, '#', TUDURA);
        addShapedOreRecipe(new ItemStack(slideDoors, 2, EnumSlideDoor.SHOZI.getId()), "XYX", "Y#Y", "XYX", '#', TUDURA, 'X', Items.stick, 'Y', Items.paper);
        addShapedOreRecipe(new ItemStack(slideDoors, 2, EnumSlideDoor.GLASS.getId()), "XYX", "X#X", "XYX", '#', TUDURA, 'X', Blocks.iron_bars, 'Y', Blocks.glass_pane);
        addShapedOreRecipe(new ItemStack(slideDoors, 2, EnumSlideDoor.GGLASS.getId()), "XYX", "X#X", "XYX", '#', TUDURA, 'X', Items.stick, 'Y', Blocks.glass_pane);
        addShapedOreRecipe(new ItemStack(slideDoors, 2, EnumSlideDoor.YUKI.getId()), "XYX", "X#X", "XZX", '#', TUDURA, 'X', Items.stick, 'Y', Items.paper, 'Z', Blocks.glass_pane);
        addShapedOreRecipe(new ItemStack(slideDoors, 2, EnumSlideDoor.AMADO.getId()), "XYX", "X#X", "XYX", '#', TUDURA, 'X', Items.stick, 'Y', "plankWood");
        // パネル類
        addShapedOreRecipe(new ItemStack(bamboopane, 8, 0), "###", "###", '#', BAMBOO);
        addShapedOreRecipe(new ItemStack(bamboopane, 8, 1), "#X#", "###", '#', Blocks.planks, 'X', TUDURA);
        addShapedOreRecipe(new ItemStack(bamboopane, 6, 2), "#X#", "#X#", '#', BAMBOO, 'X', new ItemStack(bamboopane, 1, 0));
        addShapedOreRecipe(new ItemStack(bamboopane, 6, 3), "#X#", "#X#", '#', BAMBOO, 'X', new ItemStack(bamboopane, 1, 2));
        addShapedOreRecipe(new ItemStack(bamboopane, 8, 4), "#X#", "###", '#', new ItemStack(Blocks.wool, 1, 11), 'X', TUDURA);
        addShapedOreRecipe(new ItemStack(bamboopane, 8, 5), "#X#", "###", '#', new ItemStack(Blocks.wool, 1, 10), 'X', TUDURA);
        // 爆竹類
        addShapedOreRecipe(new ItemStack(firecracker, 10, 0), " # ", " X ", " # ", '#', BUSKET, 'X', Items.gunpowder);
        addShapedOreRecipe(new ItemStack(firecracker, 1, 1), " # ", "XYX", " # ", '#', BUSKET, 'X', Items.gunpowder, 'Y', new ItemStack(firecracker, 1, 0));
        addShapedOreRecipe(new ItemStack(firecracker, 1, 2), " # ", "XYX", " # ", '#', BUSKET, 'X', Items.gunpowder, 'Y', new ItemStack(firecracker, 1, 1));
        // 飯類
        addShapelessOreRecipe(new ItemStack(foods, 1, 1), Items.cooked_beef, WHEAT_RICE);
        addShapelessOreRecipe(new ItemStack(foods, 1, 2), Items.cooked_porkchop, WHEAT_RICE);
        addShapelessOreRecipe(new ItemStack(foods, 1, 3), Blocks.red_mushroom, WHEAT_RICE);
        addShapelessOreRecipe(new ItemStack(foods, 1, 4), Items.porkchop, BAMBOO);
        addShapelessOreRecipe(new ItemStack(foods, 1, 5), Items.cooked_beef, BAMBOO);
        addShapelessOreRecipe(new ItemStack(foods, 1, 6), takenoko, WHEAT_RICE);
        addShapelessOreRecipe(new ItemStack(foods, 1, 7), Items.egg, WHEAT_RICE);
        addShapelessOreRecipe(new ItemStack(foods, 1, 8), Items.egg, Items.cooked_chicken, WHEAT_RICE);
        addShapelessOreRecipe(new ItemStack(foods, 1, 9), Items.fish, WHEAT_RICE);
        addShapelessOreRecipe(new ItemStack(foods, 1, 10), Items.cooked_chicken, BAMBOO);
        // 箪笥
        addShapedOreRecipe(new ItemStack(jpchest, 1, 0), "YYY", "Y#Y", "YYY", '#', TUDURA, 'Y', "logWood");
        // 畳 茅葺き
        addShapedOreRecipe(new ItemStack(dSquare, 4, 0), " X ", "X#X", " X ", '#', TUDURA, 'X', Items.wheat);
        addShapedOreRecipe(new ItemStack(dSquare, 8, 4), "XXX", "X#X", "XXX", '#', TUDURA, 'X', Items.wheat);
        GameRegistry.addShapelessRecipe(new ItemStack(dSquare, 1, 12), new ItemStack(dSquare, 1, 0));
        GameRegistry.addShapelessRecipe(new ItemStack(dSquare, 1, 0), new ItemStack(dSquare, 1, 12));
        addSlabRecipe(new ItemStack(dHalfSquare, 6, 0), new ItemStack(dSquare, 1, 0));
        addSlabRecipe(new ItemStack(dHalfSquare, 6, 4), new ItemStack(dSquare, 1, 4));
        addSlabRecipe(new ItemStack(dHalfSquare, 6, 8), new ItemStack(dSquare, 1, 8));
        addSlabRecipe(new ItemStack(dHalfSquare, 6, 10), new ItemStack(dSquare, 1, 10));
        addSlabRecipe(new ItemStack(dHalfSquare, 6, 12), new ItemStack(dSquare, 1, 12));
        addCarpetRecipe(new ItemStack(decoCarpet, 3, 0), new ItemStack(dSquare, 1, 0));
        addCarpetRecipe(new ItemStack(decoCarpet, 3, 2), new ItemStack(dSquare, 1, 4));
        addCarpetRecipe(new ItemStack(decoCarpet, 3, 4), new ItemStack(dSquare, 1, 8));
        addCarpetRecipe(new ItemStack(decoCarpet, 3, 6), new ItemStack(dSquare, 1, 10));
        addCarpetRecipe(new ItemStack(decoCarpet, 3, 8), new ItemStack(dSquare, 1, 12));
        // 掛け軸
        addShapedOreRecipe(new ItemStack(kakeziku, 1, 0), "YYY", "X#X", "XXX", '#', TUDURA, 'Y', Items.stick, 'X', Items.paper);
        // 行灯
        addShapedOreRecipe(new ItemStack(andon, 1, 0), "###", "#Y#", "#X#", '#', Items.stick, 'X', Blocks.torch, 'Y', TUDURA);
        // ふとん
        addShapedOreRecipe(new ItemStack(huton, 1, 0), " # ", "XXX", "XXX", '#', TUDURA, 'X', Blocks.wool);
        // 茅葺屋根
        addStairRecipe(new ItemStack(rooftile, 6, 0), new ItemStack(dSquare, 1, 4));
        addStairRecipe(new ItemStack(rooftile, 6, 0), Items.wheat, TUDURA);
        // 温泉ユニット
        addShapedOreRecipe(new ItemStack(spaunit, 1, 0), "#Z#", "#X#", "#Y#", 'Z', TUDURA, '#', Blocks.cobblestone, 'Y', Items.lava_bucket, 'X', Items.water_bucket);
        // かき氷
        EnumShavedIce[] esis = EnumShavedIce.values();

        for (EnumShavedIce esi : esis) {
            if (esi.material != null) {
                addShapelessOreRecipe(new ItemStack(shavedIce, 1, esi.id), esi.material, Blocks.snow, Items.sugar, BUSKET);
                GameRegistry.addShapelessRecipe(new ItemStack(shavedIce, 1, esi.id), esi.material, new ItemStack(shavedIce, 1, 0));
            } else {
                addShapelessOreRecipe(new ItemStack(shavedIce, 1, esi.id), Blocks.snow, Items.sugar, BUSKET);
            }

            GameRegistry.addShapelessRecipe(new ItemStack(shavedIce, 1, esi.id + esis.length), Items.milk_bucket, new ItemStack(shavedIce, 1, esi.id));
        }

        // 風鈴
        addShapedOreRecipe(new ItemStack(windChime, 1, 0), "#", "X", "B", '#', Items.string, 'X', TUDURA, 'B', Items.paper);
        // ふくろ
        addShapedOreRecipe(new ItemStack(itemSack, 1, 0), "###", "WXW", "WWW", 'X', TUDURA, '#', Items.string, 'W', Blocks.wool);
        GameRegistry.addShapelessRecipe(new ItemStack(itemSack, 1, 0), new ItemStack(itemSack, 1, 32767));
        // 雪球
        GameRegistry.addRecipe(new ItemStack(snowBall, 32, 0), "###", "#X#", "###", '#', Blocks.snow, 'X', Blocks.cobblestone);
        GameRegistry.addRecipe(new ItemStack(snowBall, 32, 1), "###", "#X#", "###", '#', Blocks.snow, 'X', Blocks.ice);
        GameRegistry.addRecipe(new ItemStack(snowBall, 32, 2), "###", "#X#", "###", '#', Blocks.snow, 'X', Items.iron_ingot);
        GameRegistry.addRecipe(new ItemStack(snowBall, 01, 3), "###", "#X#", "###", '#', Blocks.snow, 'X', Items.gold_nugget);
        GameRegistry.addRecipe(new ItemStack(snowBall, 32, 4), "###", "#X#", "###", '#', Blocks.snow, 'X', Items.diamond);
        GameRegistry.addRecipe(new ItemStack(snowBall, 32, 5), "###", "#X#", "###", '#', Blocks.snow, 'X', Items.water_bucket);
        GameRegistry.addRecipe(new ItemStack(snowBall, 32, 6), "###", "#X#", "###", '#', Blocks.snow, 'X', Items.ender_pearl);
        GameRegistry.addRecipe(new ItemStack(snowBall, 04, 7), "###", "#X#", "###", '#', Blocks.snow, 'X', Blocks.red_mushroom);
        GameRegistry.addRecipe(new ItemStack(snowBall, 04, 8), "###", "#X#", "###", '#', Blocks.snow, 'X', Items.spider_eye);
        GameRegistry.addRecipe(new ItemStack(snowBall, 01, 9), "###", "#X#", "###", '#', Blocks.snow, 'X', Items.melon);
        // 扇子
        addShapedOreRecipe(new ItemStack(fan, 1, 0), "XYY", "XYY", "XXX", 'X', BAMBOO, 'Y', Items.paper);
        // 武器
        addShapedOreRecipe(new ItemStack(katana, 1, 0), "X", "X", "Y", 'X', Items.iron_ingot, 'Y', TUDURA);
        GameRegistry.addRecipe(new ItemStack(bambooBow, 1, 0), " XY", "X Y", " XY", 'X', singleTexDeco, 'Y', Items.string);
        addShapedOreRecipe(new ItemStack(bambooSword, 1, 0), " X ", " X ", " Y ", 'X', BAMBOO, 'Y', TUDURA);
        // サクラから桜木材
        // カメレオン類
        addShapedOreRecipe(new ItemStack(delude_width, 6, 0), " # ", "YXY", " # ", '#', TUDURA, 'X', Blocks.pumpkin_stem, 'Y', Blocks.soul_sand);
        addShapedOreRecipe(new ItemStack(delude_height, 6, 0), " Y ", "#X#", " Y ", '#', TUDURA, 'X', Blocks.pumpkin_stem, 'Y', Blocks.soul_sand);
        addShapedOreRecipe(new ItemStack(delude_stair, 8, 0), "Y  ", "YX ", "#YY", '#', TUDURA, 'X', Blocks.pumpkin_stem, 'Y', Blocks.soul_sand);
        addShapedOreRecipe(new ItemStack(delude_stair, 8, 0), "  Y", " XY", "YY#", '#', TUDURA, 'X', Blocks.pumpkin_stem, 'Y', Blocks.soul_sand);
        addShapedOreRecipe(new ItemStack(delude_plate, 1, 0), "##", '#', new ItemStack(delude_width, 1, 0));
        // いろり
        addShapedOreRecipe(new ItemStack(campfire, 1, 0), " # ", "ZXZ", "YYY", '#', TUDURA, 'Z', Blocks.iron_bars, 'X', Items.flint_and_steel, 'Y', new ItemStack(Items.coal, 1, 1));
        // 風車水車
        addShapedOreRecipe(new ItemStack(windmill, 1, 0), "YXY", "X#X", "YXY", '#', TUDURA, 'X', BAMBOO, 'Y', singleTexDeco);
        addShapedOreRecipe(new ItemStack(windmill, 1, 1), "YXY", "X#X", "YXY", '#', TUDURA, 'X', BAMBOO, 'Y', Blocks.wool);
        addShapedOreRecipe(new ItemStack(waterWheel, 1, 0), "YXY", "X#X", "YXY", '#', TUDURA, 'X', BAMBOO, 'Y', Blocks.planks);
        // 方向なしデコブロック
        addShapedOreRecipe(new ItemStack(decoration, 8, 0), "###", "#X#", "###", '#', Blocks.sand, 'X', TUDURA);
        addShapedOreRecipe(new ItemStack(decoration, 8, 1), "Y#Y", "#X#", "Y#Y", '#', new ItemStack(decoration, 8, 5), 'X', TUDURA, 'Y', new ItemStack(decoration, 8, 0));
        addShapedOreRecipe(new ItemStack(decoration, 8, 2), "###", "#X#", "###", '#', new ItemStack(Blocks.planks, 1, 0), 'X', TUDURA);
        addShapedOreRecipe(new ItemStack(decoration, 8, 3), "###", "#X#", "###", '#', new ItemStack(Blocks.planks, 1, 1), 'X', TUDURA);
        addShapedOreRecipe(new ItemStack(decoration, 8, 4), "###", "#X#", "###", '#', new ItemStack(Blocks.planks, 1, 2), 'X', TUDURA);
        addShapedOreRecipe(new ItemStack(decoration, 8, 5), "###", "#X#", "###", '#', Items.brick, 'X', TUDURA);
        // デコハーフ
        addSlabRecipe(new ItemStack(decoration_half, 6, 0), new ItemStack(decoration, 8, 0));
        addSlabRecipe(new ItemStack(decoration_half, 6, 1), new ItemStack(decoration, 8, 1));
        addSlabRecipe(new ItemStack(decoration_half, 6, 2), new ItemStack(decoration, 8, 2));
        addSlabRecipe(new ItemStack(decoration_half, 6, 3), new ItemStack(decoration, 8, 3));
        addSlabRecipe(new ItemStack(decoration_half, 6, 4), new ItemStack(decoration, 8, 4));
        addSlabRecipe(new ItemStack(decoration_half, 6, 5), new ItemStack(decoration, 8, 5));
        // 方向付きデコ（桜木材等）
        addShapedOreRecipe(new ItemStack(decoration_dir, 8, 0), "###", "#X#", "###", '#', new ItemStack(decoration_dir, 8, 2), 'X', TUDURA);
        GameRegistry.addShapelessRecipe(new ItemStack(decoration_dir, 4, 2), sakuralog);
        GameRegistry.addShapedRecipe(new ItemStack(decoration_dir, 4, 4), "###", "###", "###", '#', straw);
        // 方向付きデコハーフ
        addSlabRecipe(new ItemStack(decoration_dir_half, 6, 0), new ItemStack(decoration_dir, 1, 0));
        addSlabRecipe(new ItemStack(decoration_dir_half, 6, 2), new ItemStack(decoration_dir, 1, 2));
        addSlabRecipe(new ItemStack(decoration_dir_half, 6, 4), new ItemStack(decoration_dir, 1, 4));
        //藁葺階段
        addStairRecipe(new ItemStack(wara_stair, 4, 0), new ItemStack(decoration_dir, 1, 4));
        // 瓦
        // 瓦階段
        addStairRecipe(new ItemStack(kawara_stair, 4, 0), new ItemStack(decoration, 1, 5));
        // おぼん
        GameRegistry.addRecipe(new ItemStack(obon, 1, 0), "###", "# #", '#', new ItemStack(decoration_dir, 4, 2));
        // 柱
        addShapedOreRecipe(new ItemStack(thickSakuraPillar, 4, 0), "#", "X", "#", '#', new ItemStack(decoration_dir, 1, 2), 'X', TUDURA);
        addShapedOreRecipe(new ItemStack(thinSakuraPillar, 4, 0), "#", "X", "#", '#', new ItemStack(thickSakuraPillar, 1, 0), 'X', TUDURA);
        addShapedOreRecipe(new ItemStack(thickOrcPillar, 4, 0), "#", "X", "#", '#', new ItemStack(Blocks.planks, 1, 0), 'X', TUDURA);
        addShapedOreRecipe(new ItemStack(thinOrcPillar, 4, 0), "#", "X", "#", '#', new ItemStack(thickOrcPillar, 1, 0), 'X', TUDURA);
        addShapedOreRecipe(new ItemStack(thickSprucePillar, 4, 0), "#", "X", "#", '#', new ItemStack(Blocks.log, 1, 1), 'X', TUDURA);
        addShapedOreRecipe(new ItemStack(thinSprucePillar, 4, 0), "#", "X", "#", '#', new ItemStack(thickSprucePillar, 1, 0), 'X', TUDURA);
        //石臼
        addShapedOreRecipe(new ItemStack(millStone, 1, 0), "###", "X#X", "###", '#', new ItemStack(Blocks.cobblestone), 'X', TUDURA);
        // 鉱石辞書
        addOreDictionary();
        // 粉砕レシピ
        addGrindRecipe();
        // やきもの
        GameRegistry.addSmelting(singleTexDeco, new ItemStack(Items.coal, 1, 1), 0.15F);
        GameRegistry.addSmelting(sakuralog, new ItemStack(Items.coal, 1, 1), 0.15F);
        GameRegistry.addSmelting(rawrice, new ItemStack(foods, 1, 0), 0.15F);
    }

    private void addGrindRecipe() {
        GrindRegistory.addRecipe(new ItemStack(Blocks.gravel), new ItemStack(Blocks.sand), new ItemStack(Blocks.stone, 3), 5);
        GrindRegistory.addRecipe(new ItemStack(Blocks.gravel), new ItemStack(Blocks.sand), Blocks.cobblestone, 10);
        GrindRegistory.addRecipe(new ItemStack(Blocks.sand), new ItemStack(Items.flint), Blocks.gravel, 7);
        GrindRegistory.addRecipe(new ItemStack(Items.dye, 3, 15), new ItemStack(Items.dye, 2, 15), Items.bone, 2);
        GrindRegistory.addRecipe(new ItemStack(Items.blaze_powder, 2, 0), new ItemStack(Items.blaze_powder, 1, 0), Items.blaze_rod, 2);
        GrindRegistory.addRecipe(new ItemStack(Items.clay_ball, 4, 0), new ItemStack(Blocks.hardened_clay, 1, GrindRegistory.WILD_CARD));
        GrindRegistory.addRecipe(new ItemStack(Items.clay_ball, 4, 0), new ItemStack(Blocks.stained_hardened_clay, 1, GrindRegistory.WILD_CARD));
        GrindRegistory.addRecipe(new ItemStack(rawrice, 1, 0), new ItemStack(riceSeed, 6, 0));
    }

    private void addOreDictionary() {
        OreDictionary.registerOre(BAMBOO, itembamboo);
        OreDictionary.registerOre(WHEAT_RICE, new ItemStack(foods, 1, 0));
        OreDictionary.registerOre(TUDURA, tudura);
        OreDictionary.registerOre(BUSKET, bambooBasket);
        OreDictionary.registerOre("treeSapling", sakura);
        OreDictionary.registerOre("logWood", sakuralog);
        OreDictionary.registerOre("plankWood", new ItemStack(decoration_dir, 1, 2));
        OreDictionary.registerOre(CROP_RICE, rawrice);
    }

    //1個目は主素材のみ、2個めはサブ素材、だいたいツヅラ
    private void addStairRecipe(ItemStack output, Object... input) {
        if (input.length == 0) {
            throw new IllegalArgumentException();
        }
        if (input.length == 1) {
            addShapedOreRecipe(output, "#  ", "## ", "###", '#', input[0]);
            addShapedOreRecipe(output, "  #", " ##", "###", '#', input[0]);
        } else {
            addShapedOreRecipe(output, "#  ", "#X ", "###", '#', input[0], 'X', input[1]);
            addShapedOreRecipe(output, "  #", " X#", "###", '#', input[0], 'X', input[1]);
        }
    }

    private void addSlabRecipe(ItemStack output, ItemStack input) {
        GameRegistry.addRecipe(output, "###", '#', input);
    }

    private void addCarpetRecipe(ItemStack output, ItemStack input) {
        GameRegistry.addRecipe(output, "##", '#', input);
    }

    private void addShapedOreRecipe(ItemStack itemStack, Object... objects) {
        GameRegistry.addRecipe(new ShapedOreRecipe(itemStack, objects));
    }

    private void addShapelessOreRecipe(ItemStack itemStack, Object... objects) {
        GameRegistry.addRecipe(new ShapelessOreRecipe(itemStack, objects));
    }
}
