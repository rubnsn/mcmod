package ruby.bamboo;

import java.util.Calendar;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;
import ruby.bamboo.block.BlockAlphaMultiBlock;
import ruby.bamboo.block.BlockAndon;
import ruby.bamboo.block.BlockBamboo;
import ruby.bamboo.block.BlockBambooPane;
import ruby.bamboo.block.BlockBambooShoot;
import ruby.bamboo.block.BlockBroom;
import ruby.bamboo.block.BlockCampfire;
import ruby.bamboo.block.BlockCustomRenderSingleTexture;
import ruby.bamboo.block.BlockDSquare;
import ruby.bamboo.block.BlockDecoCarpet;
import ruby.bamboo.block.BlockDecorations;
import ruby.bamboo.block.BlockDelude;
import ruby.bamboo.block.BlockDeludePressurePlate;
import ruby.bamboo.block.BlockDeludeStair;
import ruby.bamboo.block.BlockHuton;
import ruby.bamboo.block.BlockJpchest;
import ruby.bamboo.block.BlockKawaraStair;
import ruby.bamboo.block.BlockKayabukiRoof;
import ruby.bamboo.block.BlockKitunebi;
import ruby.bamboo.block.BlockLiangBamboo;
import ruby.bamboo.block.BlockLiangVanillaLog;
import ruby.bamboo.block.BlockLiangVanillaLog2;
import ruby.bamboo.block.BlockLiangVanillaWood;
import ruby.bamboo.block.BlockManeki;
import ruby.bamboo.block.BlockMillStone;
import ruby.bamboo.block.BlockMoss;
import ruby.bamboo.block.BlockMultiBlock;
import ruby.bamboo.block.BlockMultiPot;
import ruby.bamboo.block.BlockPillar;
import ruby.bamboo.block.BlockRicePlant;
import ruby.bamboo.block.BlockSakura;
import ruby.bamboo.block.BlockSakuraCarpet;
import ruby.bamboo.block.BlockSakuraLeaves;
import ruby.bamboo.block.BlockSakuraLog;
import ruby.bamboo.block.BlockSpaUnit;
import ruby.bamboo.block.BlockSpaWater;
import ruby.bamboo.block.BlockTwoDirections;
import ruby.bamboo.item.ItemBamboo;
import ruby.bamboo.item.ItemBambooBasket;
import ruby.bamboo.item.ItemBambooBow;
import ruby.bamboo.item.ItemBambooFood;
import ruby.bamboo.item.ItemBambooPane;
import ruby.bamboo.item.ItemBambooPickaxe;
import ruby.bamboo.item.ItemBambooSpear;
import ruby.bamboo.item.ItemBambooSword;
import ruby.bamboo.item.ItemBambooshoot;
import ruby.bamboo.item.ItemBoiledEgg;
import ruby.bamboo.item.ItemDecoCarpet;
import ruby.bamboo.item.ItemDecorationBlocks;
import ruby.bamboo.item.ItemDimensionCreater;
import ruby.bamboo.item.ItemDirtySnowball;
import ruby.bamboo.item.ItemDustClay;
import ruby.bamboo.item.ItemFan;
import ruby.bamboo.item.ItemFirecracker;
import ruby.bamboo.item.ItemFireflyBottle;
import ruby.bamboo.item.ItemHuton;
import ruby.bamboo.item.ItemKaginawa;
import ruby.bamboo.item.ItemKakeziku;
import ruby.bamboo.item.ItemKatana;
import ruby.bamboo.item.ItemMultiBlock;
import ruby.bamboo.item.ItemMultiPot;
import ruby.bamboo.item.ItemObon;
import ruby.bamboo.item.ItemPillar;
import ruby.bamboo.item.ItemSack;
import ruby.bamboo.item.ItemShavedIce;
import ruby.bamboo.item.ItemSimpleSubtype;
import ruby.bamboo.item.ItemSingleNameSubtype;
import ruby.bamboo.item.ItemSlideDoor;
import ruby.bamboo.item.ItemTudura;
import ruby.bamboo.item.ItemWaterwheel;
import ruby.bamboo.item.ItemWindChime;
import ruby.bamboo.item.ItemWindmill;
import ruby.bamboo.item.ItemZabuton;
import ruby.bamboo.item.magatama.ItemMagatama;
import cpw.mods.fml.common.registry.GameRegistry;

public class BambooInit {
    // instance
    public static Block bamboo;
    public static Block bamboo2;
    public static Block bambooSingle;
    public static Block bambooShoot;
    public static Block singleTexDeco;
    public static Block kitunebi;
    public static Block campfire;
    public static Block dSquare;
    public static Block sakura;
    public static Block spa_unit;
    public static Block spa_water;
    public static Block decoration;
    public static Block decoration_half;
    public static Block decoration_dir;
    public static Block decoration_dir_half;
    public static Block delude_stair;
    public static Block delude_height;
    public static Block delude_width;
    public static Block delude_plate;
    public static Block dHalfSquare;
    public static Block bamboopane;
    public static Block jpchest;
    public static Block decoCarpet;
    public static Block andon;
    public static Block rooftile;
    public static Block sakuraleavs;
    public static Block sakuralog;
    public static Block kawara_stair;
    public static Block wara_stair;
    public static Block thickSakuraPillar;
    public static Block thinSakuraPillar;
    public static Block thickOrcPillar;
    public static Block thinOrcPillar;
    public static Block thickSprucePillar;
    public static Block thinSprucePillar;
    public static Block thickBirchPillar;
    public static Block thinBirchPillar;
    public static Block millStone;
    public static Block ricePlant;
    public static Block moss;

    public static Block liangBambooThick;
    public static Block liangVanillaLogThick;
    public static Block liangVanillaLog2Thick;
    public static Block liangVanillaWoodThick;

    public static Block liangBambooThin;
    public static Block liangVanillaLogThin;
    public static Block liangVanillaLog2Thin;
    public static Block liangVanillaWoodThin;

    public static Block multiPot;
    public static Block blockBroom;
    public static Block blockHuton;
    public static Block multiBlock;
    public static Block alphaMultiBlock;
    public static Block sakuraCarpet;

    public static Item itembamboo;
    public static Item straw;
    public static Item bambooBasket;
    public static Item foods;
    public static Item shavedIce;
    public static Item bambooSpear;
    public static Item huton;
    public static Item kakeziku;
    public static Item obon;
    public static Item slideDoors;
    public static Item waterWheel;
    public static Item windChime;
    public static Item tudura;
    public static Item windmill;
    public static Item itemSack;
    public static Item bambooBow;
    public static Item firecracker;
    public static Item boiledEgg;
    public static Item snowBall;
    public static Item fan;
    public static Item katana;
    public static Item bambooSword;
    public static Item rawrice;
    public static Item kaginawa;
    public static Item magatama;
    public static Item riceSeed;
    public static Item dimensionCreater;
    public static Item zabuton;
    public static Item fireflyBottle;
    public static Item bambooPickaxe;
    public static Item dustClay;

    public static BambooInit instance = new BambooInit();
    //items
    public static final String TAKENOKO = "takenoko";

    private static CreativeTabs tabBamboo;

    public static void init() {
        tabBamboo = new CreativeTabs("Bamboo") {
            @Override
            public String getTranslatedTabLabel() {
                return StatCollector.translateToLocal(this.getTabLabel());
            }

            @Override
            public Item getTabIconItem() {
                return Item.getItemFromBlock(bambooShoot);
            }
        };
        instance.blocksInit();
        instance.itemsInit();
    }

    private void blocksInit() {
        bamboo = registerBlock(new BlockBamboo(9, 6).setBlockTextureName(BambooCore.resourceDomain + "bamboo"), "bamboo");
        bambooSingle = registerBlock(new BlockBamboo(9, CustomRenderHandler.coordinateCrossUID, 0).setBlockTextureName(BambooCore.resourceDomain + "bamboosingle"), "bamboosingle");
        bamboo2 = registerBlock(new BlockBamboo(9, CustomRenderHandler.coordinateCrossUID, 1).setBlockTextureName(BambooCore.resourceDomain + "bamboo"), "bamboo2");
        bambooShoot = registerBlock(new BlockBambooShoot().setBlockTextureName(BambooCore.resourceDomain + getChiledName()), ItemBambooshoot.class, "blockbambooshoot", tabBamboo);
        singleTexDeco = registerBlock(new BlockCustomRenderSingleTexture(), ItemSimpleSubtype.class, "singleTexDeco", tabBamboo);
        kitunebi = registerBlock(new BlockKitunebi().setBlockTextureName("kitunebi"), "kitunebi", tabBamboo);
        jpchest = registerBlock(new BlockJpchest(), "jpChest", tabBamboo);
        dSquare = registerBlock(new BlockDSquare(false).addTexName("tatami_x", "tatami_y", "tatami_x", "tatami_y", "kaya_x", "kaya_y", "kaya_x", "kaya_y").addTexName("tatami_tan_x", "tatami_tan_y", "tatami_tan_nsx", "tatami_tan_nsy", "tatami_nsx", "tatami_nsy", "tatami_nsx", "tatami_nsy"), ItemSimpleSubtype.class, "dirSquare", tabBamboo);
        dHalfSquare = registerBlock(new BlockDSquare(true).addTexName("tatami_x", "tatami_y", "tatami_x", "tatami_y", "kaya_x", "kaya_y", "kaya_x", "kaya_y").addTexName("tatami_tan_x", "tatami_tan_y", "tatami_tan_nsx", "tatami_tan_nsy", "tatami_nsx", "tatami_nsy", "tatami_nsx", "tatami_nsy"), ItemSimpleSubtype.class, "halfDirSquare", tabBamboo);
        andon = registerBlock(new BlockAndon(), "andon", tabBamboo);
        bamboopane = registerBlock(new BlockBambooPane(Material.ground), ItemBambooPane.class, "bambooPanel", tabBamboo);
        sakuraleavs = registerBlock(new BlockSakuraLeaves(), ItemSingleNameSubtype.class, "sakuraLeaves", tabBamboo);
        sakura = registerBlock(new BlockSakura().setBlockTextureName("sakura"), "sakuraSapling", tabBamboo);
        rooftile = registerBlock(new BlockKayabukiRoof(), "kayabukiRoof", tabBamboo);
        spa_water = registerBlock(new BlockSpaWater(), "spaWater");
        spa_unit = registerBlock(new BlockSpaUnit(), "spaUnit", tabBamboo);
        sakuralog = registerBlock(new BlockSakuraLog(), "sakuraLog", tabBamboo);
        delude_width = registerBlock(new BlockDelude(false), "delude_width", tabBamboo);
        delude_height = registerBlock(new BlockDelude(true), "delude_height", tabBamboo);
        delude_stair = registerBlock(new BlockDeludeStair(), "delude_stair", tabBamboo);
        campfire = registerBlock(new BlockCampfire(), "campfire", tabBamboo);
        decoration = registerBlock(new BlockDecorations(Material.ground, false).addTexName("plaster", "namako", "check_oak", "check_pine", "check_birch", "kawara"), ItemDecorationBlocks.class, "deco", tabBamboo);
        decoration_half = registerBlock(new BlockDecorations(Material.ground, true).addTexName("plaster", "namako", "check_oak", "check_pine", "check_birch", "kawara"), ItemDecorationBlocks.class, "halfDeco", tabBamboo);
        decoration_dir = registerBlock(new BlockTwoDirections(Material.wood, false).addTexName("yoroiita", "sakuraplank", "wara"), ItemDecorationBlocks.class, "twoDirDeco", tabBamboo);
        decoration_dir_half = registerBlock(new BlockTwoDirections(Material.wood, true).addTexName("yoroiita", "sakuraplank", "wara"), ItemDecorationBlocks.class, "halfTwoDirDeco", tabBamboo);
        kawara_stair = registerBlock(new BlockKawaraStair(decoration, 5), "kawara_stair", tabBamboo);
        wara_stair = registerBlock(new BlockKawaraStair(decoration_dir, 5), "wara_stair", tabBamboo);
        decoCarpet = registerBlock(new BlockDecoCarpet(), ItemDecoCarpet.class, "decoCarpet", tabBamboo);
        thickSakuraPillar = registerBlock(new BlockPillar(decoration_dir, 2, 0.3F, 0.7F, 0.2F), ItemPillar.class, "thickSakuraPillar", tabBamboo);
        thinSakuraPillar = registerBlock(new BlockPillar(decoration_dir, 2, 0.4F, 0.6F, 0.15F), ItemPillar.class, "thinSakuraPillar", tabBamboo);
        thickOrcPillar = registerBlock(new BlockPillar(Blocks.planks, 0, 0.3F, 0.7F, 0.2F), ItemPillar.class, "thickOrcPillar", tabBamboo);
        thinOrcPillar = registerBlock(new BlockPillar(Blocks.planks, 0, 0.4F, 0.6F, 0.15F), ItemPillar.class, "thinOrcPillar", tabBamboo);
        thickSprucePillar = registerBlock(new BlockPillar(Blocks.log, 1, 0.3F, 0.7F, 0.2F), ItemPillar.class, "thickSprucePillar", tabBamboo);
        thinSprucePillar = registerBlock(new BlockPillar(Blocks.log, 1, 0.4F, 0.6F, 0.15F), ItemPillar.class, "thinSprucePillar", tabBamboo);
        thickBirchPillar = registerBlock(new BlockPillar(Blocks.log, 2, 0.3F, 0.7F, 0.2F), ItemPillar.class, "thickBirchPillar", tabBamboo);
        thinBirchPillar = registerBlock(new BlockPillar(Blocks.log, 2, 0.4F, 0.6F, 0.15F), ItemPillar.class, "thinBirchPillar", tabBamboo);
        delude_plate = registerBlock(new BlockDeludePressurePlate(), "delude_plate", tabBamboo);
        ricePlant = registerBlock(new BlockRicePlant().setBlockTextureName(BambooCore.resourceDomain + "riceplant"), "ricePlant");
        millStone = registerBlock(new BlockMillStone(), "bambooMillStone", tabBamboo);
        moss = registerBlock(new BlockMoss().setBlockTextureName(BambooCore.resourceDomain + "moss"), "bambooMoss", tabBamboo);
        multiPot = registerBlock(new BlockMultiPot().setBlockTextureName("flower_pot"), ItemMultiPot.class, "bambooMultiPot", tabBamboo);
        blockBroom = registerBlock(new BlockBroom(), ItemSimpleSubtype.class, "blobkbroom", tabBamboo);
        blockHuton = registerBlock(new BlockHuton(), "bamboohuton");
        multiBlock = registerBlock(new BlockMultiBlock(), ItemMultiBlock.class, "bamboomultiblock", tabBamboo);
        alphaMultiBlock = registerBlock(new BlockAlphaMultiBlock(), ItemMultiBlock.class, "alphamultiblock", tabBamboo);
        sakuraCarpet = registerBlock(new BlockSakuraCarpet(), ItemSingleNameSubtype.class, "sakuracarpet", tabBamboo);
        initLiang();
        registerBlock(new BlockManeki(Material.ground), "maneki", tabBamboo);
        if (BambooCore.DEBUGMODE) {
            workingBlock();
        }
    }

    private void initLiang() {
        float thickMaxW = 0.85F;
        float thickMinW = 1 - thickMaxW;
        float thickMaxH = 0.85F;
        float thickMinH = 1 - thickMaxH;

        float thinMaxW = 0.7F;
        float thinMinW = 1 - thinMaxW;
        float thinMaxH = 0.7F;
        float thinMinH = 1 - thinMaxH;

        liangBambooThick = registerBlock(new BlockLiangBamboo(thickMinW, thickMaxW, thickMinH, thickMaxH), ItemSimpleSubtype.class, "bambooLiangThick", tabBamboo);
        liangVanillaLogThick = registerBlock(new BlockLiangVanillaLog(thickMinW, thickMaxW, thickMinH, thickMaxH), ItemSimpleSubtype.class, "bambooLiangVLogThick", tabBamboo);
        liangVanillaLog2Thick = registerBlock(new BlockLiangVanillaLog2(thickMinW, thickMaxW, thickMinH, thickMaxH), ItemSimpleSubtype.class, "bambooLiangVLog2Thick", tabBamboo);
        liangVanillaWoodThick = registerBlock(new BlockLiangVanillaWood(thickMinW, thickMaxW, thickMinH, thickMaxH), ItemSimpleSubtype.class, "bambooLiangVWoodThick", tabBamboo);

        liangBambooThin = registerBlock(new BlockLiangBamboo(thinMinW, thinMaxW, thinMinH, thinMaxH), ItemSimpleSubtype.class, "bambooLiangThin", tabBamboo);
        liangVanillaLogThin = registerBlock(new BlockLiangVanillaLog(thinMinW, thinMaxW, thinMinH, thinMaxH), ItemSimpleSubtype.class, "bambooLiangVLogThin", tabBamboo);
        liangVanillaLog2Thin = registerBlock(new BlockLiangVanillaLog2(thinMinW, thinMaxW, thinMinH, thinMaxH), ItemSimpleSubtype.class, "bambooLiangVLog2Thin", tabBamboo);
        liangVanillaWoodThin = registerBlock(new BlockLiangVanillaWood(thinMinW, thinMaxW, thinMinH, thinMaxH), ItemSimpleSubtype.class, "bambooLiangVWoodThin", tabBamboo);
    }

    private void workingBlock() {
    }

    private void itemsInit() {
        bambooBasket = registerItem(new ItemBambooBasket(), "bamboobasket", tabBamboo);
        firecracker = registerItem(new ItemFirecracker(), "firecracker", tabBamboo);
        foods = registerItem(new ItemBambooFood(), "bambooFood", tabBamboo);
        bambooSpear = registerItem(new ItemBambooSpear(), "bamboospear", tabBamboo);
        slideDoors = registerItem(new ItemSlideDoor(), "slideDoor", tabBamboo);
        kakeziku = registerItem(new ItemKakeziku(), "kakeziku", tabBamboo);
        huton = registerItem(new ItemHuton(), "huton", tabBamboo);
        boiledEgg = registerItem(new ItemBoiledEgg(), "boiledEgg");
        shavedIce = registerItem(new ItemShavedIce(), "shavedice", tabBamboo);
        windChime = registerItem(new ItemWindChime(), "windchime", tabBamboo);
        itemSack = registerItem(new ItemSack(), "itemsack", tabBamboo);
        snowBall = registerItem(new ItemDirtySnowball(), "snowball", tabBamboo);
        fan = registerItem(new ItemFan(), "fan", tabBamboo);
        katana = registerItem(new ItemKatana(), "katana", tabBamboo);
        bambooBow = registerItem(new ItemBambooBow(), "bamboobow", tabBamboo);
        tudura = registerItem(new ItemTudura(), "tudura", tabBamboo);
        windmill = registerItem(new ItemWindmill(), "windmill", tabBamboo);
        waterWheel = registerItem(new ItemWaterwheel(), "waterwheel", tabBamboo);
        itembamboo = registerItem(new ItemBamboo(), "itembamboo", tabBamboo);
        kaginawa = registerItem(new ItemKaginawa(), "kaginawa", tabBamboo);
        bambooSword = registerItem(new ItemBambooSword(), "bamboosword", tabBamboo);
        obon = registerItem(new ItemObon(), "obon", tabBamboo);
        magatama = registerItem(new ItemMagatama().setTextureName("magatama"), "magatama", tabBamboo);
        straw = registerItem(new Item().setTextureName(BambooCore.resourceDomain + "straw"), "straw", tabBamboo);
        riceSeed = registerItem(new ItemSeeds(ricePlant, Blocks.farmland).setTextureName(BambooCore.resourceDomain + "seedrice"), "seedrice", tabBamboo);
        rawrice = registerItem(new ItemFood(1, false).setTextureName(BambooCore.resourceDomain + "rawrice"), "rawrice", tabBamboo);
        zabuton = registerItem(new ItemZabuton().setTextureName(BambooCore.resourceDomain + "zabuton"), "zabuton", tabBamboo);
        fireflyBottle = registerItem(new ItemFireflyBottle().setTextureName(BambooCore.resourceDomain + "firefly"), "itemFireflyBottle", tabBamboo);
        dustClay = registerItem(new ItemDustClay().setTextureName("sugar"), "bambooDustCray", tabBamboo);
        MinecraftForge.addGrassSeed(new ItemStack(riceSeed, 1, 0), 10);
        bambooPickaxe = registerItem(new ItemBambooPickaxe(), "bamboopickaxe", tabBamboo);
        if (BambooCore.DEBUGMODE) {
            workingItem();
        }
    }

    private void workingItem() {
        dimensionCreater = registerItem(new ItemDimensionCreater(), "dimensioncreater", tabBamboo);
    }

    private Block registerBlock(Block block, String name) {
        return this.registerBlock(block, ItemBlock.class, name, null);
    }

    private Block registerBlock(Block block, String name, CreativeTabs creativeTabs) {
        return this.registerBlock(block, ItemBlock.class, name, creativeTabs);
    }

    private Block registerBlock(Block block, Class cls, String name) {
        return this.registerBlock(block, cls, name, null);
    }

    private Block registerBlock(Block block, Class cls, String name, CreativeTabs creativeTabs) {
        if (creativeTabs != null) {
            block.setCreativeTab(creativeTabs);
        }
        block.setBlockName(name);
        return GameRegistry.registerBlock(block, cls, name);
    }

    private Item registerItem(Item item, String name) {
        return registerItem(item, name, null);
    }

    private Item registerItem(Item item, String name, CreativeTabs creativeTabs) {
        if (creativeTabs != null) {
            item.setCreativeTab(creativeTabs);
        }
        item.setUnlocalizedName(name);
        return GameRegistry.registerItem(item, name, BambooCore.MODID);
    }

    private String getChiledName() {
        String chiledName = "bambooshoot";
        Calendar ci = Calendar.getInstance();
        int date = ci.get(Calendar.DATE);

        switch (ci.get(Calendar.MONTH)) {
        case Calendar.JANUARY:
            if (date == 1 || date == 2 || date == 3) {
                chiledName = "bambooshoot0101";
            }

            break;

        case Calendar.DECEMBER:
            if (date == 24 || date == 25) {
                chiledName = "bambooshoot1224";
            }

            break;

        case Calendar.FEBRUARY:
            if (date == 14) {
                chiledName = "bambooshoot0214";
            }
            break;

        case Calendar.MARCH:
            if (date == 03) {
                chiledName = "bambooshoot0303";
            } else if (date == 14) {
                chiledName = "bambooshoot0314";
            }

            break;

        case Calendar.APRIL:
            if (date == 01) {
                chiledName = "bambooshoot0401";
            }

            break;

        case Calendar.MAY:
            if (date == 05) {
                chiledName = "bambooshoot0505";
            }

            ci.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            ci.set(Calendar.DAY_OF_WEEK_IN_MONTH, 2);

            if (ci.get(Calendar.DATE) == date) {
                chiledName = "bambooshoot_mother";
            }

            break;

        case Calendar.JUNE:
            ci.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            ci.set(Calendar.DAY_OF_WEEK_IN_MONTH, 3);

            if (ci.get(Calendar.DATE) == date) {
                chiledName = "bambooshoot_father";
            }

            break;
        }

        return chiledName;
    }

}
