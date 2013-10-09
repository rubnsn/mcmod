package ruby.bamboo;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Calendar;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

import ruby.bamboo.block.*;
import ruby.bamboo.item.*;
import ruby.bamboo.item.magatama.*;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;

public class BambooInit {
    private static final int ITEMID_OFFSET = 256;
    // instance
    public static BambooInit instance = new BambooInit();
    public static final String SINGLE_TEX_DECO = "singleTexDeco";
    public static final String BAMBOO = "bamboo";
    public static final String KITUNEBI = "kitunebi";
    public static final String JP_CHEST = "jpChest";
    public static final String DIR_SQUARE = "dirSquare";
    public static final String ANDON = "andon";
    public static final String HALF_DIR_SQUARE = "halfDirSquare";
    public static final String BAMBOO_PANEL = "bambooPanel";
    public static final String SAKURA_LEAVES = "sakuraLeaves";
    public static final String SAKURA_SAPLING = "sakuraSapling";
    public static final String KAYABUKI_ROOF = "kayabukiRoof";
    public static final String SPA_WATER = "spaWater";
    public static final String SPA_UNIT = "spaUnit";
    public static final String SAKURA_LOG = "sakuraLog";
    public static final String DELUDE_WIDTH = "delude_width";
    public static final String DELUDE_HEIGHT = "delude_height";
    public static final String DELUDE_STAIR = "delude_stair";
    public static final String IRORI = "campfire";
    public static final String DECO_SQUARE = "deco";
    public static final String HALF_DECO_SQUARE = "halfDeco";
    public static final String TWO_DIR_DECO_SQUARE = "twoDirDeco";
    public static final String HALF_TWO_DIR_DECO_SQUARE = "halfTwoDirDeco";
    public static final String KAWARA_ROOF = "kawara_stair";
    public static final String RICE_FIELD = "riceField";
    public static final String RICE_PLANT = "ricePlant";
    public static final String MILLSTONE = "millStone";
    public static final String DECO_CARPET = "decoCarpet";
    public static final String THICK_SAKURA_PILLAR = "thickSakuraPillar";
    public static final String THIN_SAKURA_PILLAR = "thinSakuraPillar";
    public static final String THICK_ORC_PILLAR = "thickOrcPillar";
    public static final String THIN_ORC_PILLAR = "thinOrcPillar";
    public static final String THICK_SPRUCE_PILLAR = "thickSprucePillar";
    public static final String THIN_SPRUCE_PILLAR = "thinSprucePillar";
    public static final String DELUDE_PLATE = "delude_plate";
    public static final String MANEKI = "maneki";
    // blocks
    public static int bambooBlockBID;
    public static int bambooBID;
    public static int sakuraLogBID;
    public static int kitunebiBID;
    public static int spaBID;
    public static int jpchestBID;
    public static int dSquareBID;
    public static int andonBID;
    public static int dHalfSquareBID;
    public static int bamboopaneBID;
    public static int sakuraleavsBID;
    public static int sakuraBID;
    public static int rooftileBID;
    public static int spaunitBID;
    public static int delude_widthBID;
    public static int delude_heightBID;
    public static int delude_stairBID;
    public static int campfireBID;
    public static int decoBID;
    public static int halfDecoBID;
    public static int twoDirDecoBID;
    public static int halfTwoDirDecoBID;
    public static int kawara_stairBID;
    public static int halfKawaraBID;
    public static int riceFieldBID;
    public static int ricePlantBID;
    public static int millStoneBID;
    public static int decoCarpetBID;
    public static int thickSakuraPillarBID;
    public static int thinSakuraPillarBID;
    public static int thickOrcPillarBID;
    public static int thinOrcPillarBID;
    public static int thickSprucePillarBID;
    public static int thinSprucePillarBID;
    public static int delude_plateBID;
    public static int manekiBID;
    // items
    public static int bambooBasketIID;
    public static int firecrackerIID;
    public static int foodsIID;
    public static int windChimeIID;
    public static int itemSackIID;
    public static int shavedIceIID;
    public static int bambooSpearIID;
    public static int slideDoorsIID;
    public static int kakezikuIID;
    public static int hutonIID;
    public static int takenokoIID;
    public static int boiledEggIID;
    public static int snowBallIID;
    public static int fanIID;
    public static int katanaIID;
    public static int bambooBowIID;
    public static int windmillIID;
    public static int waterWheelIID;
    public static int bambooIID;
    public static int tuduraIID;
    public static int kaginawaIID;
    public static int bambooSwordIID;
    public static int obonIID;
    public static int bitchuHoeIID;
    public static int seedRiceIID;
    public static int strawIID;
    public static int magatamaIID;

    private static CreativeTabs tabBamboo;

    public static void init() {
        instance.idsInit();
        tabBamboo = new CreativeTabs("Bamboo") {
            @Override
            public int getTabIconItemIndex() {
                return takenokoIID;
            }

            @Override
            public String getTranslatedTabLabel() {
                return StatCollector.translateToLocal(this.getTabLabel());
            }
        };
        instance.blocksInit();
        instance.itemsInit();
    }

    private void blocksInit() {
        try {
            registerBlock(new BlockBamboo(bambooBID, getChiledName()), BAMBOO);
            registerBlock(new BlockCustomRenderSingleTexture(bambooBlockBID), ItemCustomRenderSingleTexture.class, SINGLE_TEX_DECO, tabBamboo);
            registerBlock(new BlockKitunebi(kitunebiBID).setTextureName("kitunebi"), KITUNEBI, tabBamboo);
            registerBlock(new BlockJpchest(jpchestBID), JP_CHEST, tabBamboo);
            registerBlock(new BlockDSquare(dSquareBID, false).addTexName("tatami_x", "tatami_y", "tatami_x", "tatami_y", "kaya_x", "kaya_y", "kaya_x", "kaya_y").addTexName("tatami_tan_x", "tatami_tan_y", "tatami_tan_nsx", "tatami_tan_nsy", "tatami_nsx", "tatami_nsy", "tatami_nsx", "tatami_nsy"), ItemDSquare.class, DIR_SQUARE, tabBamboo);
            registerBlock(new BlockDSquare(dHalfSquareBID, true).addTexName("tatami_x", "tatami_y", "tatami_x", "tatami_y", "kaya_x", "kaya_y", "kaya_x", "kaya_y").addTexName("tatami_tan_x", "tatami_tan_y", "tatami_tan_nsx", "tatami_tan_nsy", "tatami_nsx", "tatami_nsy", "tatami_nsx", "tatami_nsy"), ItemDSquare.class, HALF_DIR_SQUARE, tabBamboo);
            registerBlock(new BlockAndon(andonBID), ANDON, tabBamboo);
            registerBlock(new BlockBambooPane(bamboopaneBID, Material.ground), ItemBambooPane.class, BAMBOO_PANEL, tabBamboo);
            registerBlock(new BlockSakuraLeaves(sakuraleavsBID), ItemSakuraPetal.class, SAKURA_LEAVES, tabBamboo);
            registerBlock(new BlockSakura(sakuraBID).setTextureName("sakura"), SAKURA_SAPLING, tabBamboo);
            registerBlock(new BlockKayabukiRoof(rooftileBID), ItemRooftile.class, KAYABUKI_ROOF, tabBamboo);
            registerBlock(new BlockSpaWater(spaBID, Material.water), SPA_WATER);
            registerBlock(new BlockSpaUnit(spaunitBID), SPA_UNIT, tabBamboo);
            registerBlock(new BlockSakuraLog(sakuraLogBID), SAKURA_LOG, tabBamboo);
            registerBlock(new BlockDelude(delude_widthBID, false), DELUDE_WIDTH, tabBamboo);
            registerBlock(new BlockDelude(delude_heightBID, true), DELUDE_HEIGHT, tabBamboo);
            registerBlock(new BlockDeludeStair(delude_stairBID), DELUDE_STAIR, tabBamboo);
            registerBlock(new BlockCampfire(campfireBID), IRORI, tabBamboo);
            registerBlock(new BlockDecorations(decoBID, Material.ground, false).addTexName("plaster", "namako", "check_oak", "check_pine", "check_birch", "kawara"), ItemDecorationBlocks.class, DECO_SQUARE, tabBamboo);
            registerBlock(new BlockDecorations(halfDecoBID, Material.ground, true).addTexName("plaster", "namako", "check_oak", "check_pine", "check_birch", "kawara"), ItemDecorationBlocks.class, HALF_DECO_SQUARE, tabBamboo);
            registerBlock(new BlockTwoDirections(twoDirDecoBID, Material.wood, false).addTexName("yoroiita", "sakuraplank"), ItemDecorationBlocks.class, TWO_DIR_DECO_SQUARE, tabBamboo);
            registerBlock(new BlockTwoDirections(halfTwoDirDecoBID, Material.wood, true).addTexName("yoroiita", "sakuraplank"), ItemDecorationBlocks.class, HALF_TWO_DIR_DECO_SQUARE, tabBamboo);
            registerBlock(new BlockKawaraStair(kawara_stairBID, getBlockInstance(decoBID), 5), KAWARA_ROOF, tabBamboo);
            registerBlock(new BlockRiceField(riceFieldBID, Material.ground), RICE_FIELD);
            registerBlock(new BlockRicePlant(ricePlantBID), RICE_PLANT);
            registerBlock(new BlockMillStone(millStoneBID), MILLSTONE, tabBamboo);
            registerBlock(new BlockDecoCarpet(decoCarpetBID), ItemDecoCarpet.class, DECO_CARPET, tabBamboo);
            registerBlock(new BlockPillar(thickSakuraPillarBID, getBlockInstance(twoDirDecoBID), 2, 0.3F, 0.7F, 0.2F), ItemPillar.class, THICK_SAKURA_PILLAR, tabBamboo);
            registerBlock(new BlockPillar(thinSakuraPillarBID, getBlockInstance(twoDirDecoBID), 2, 0.4F, 0.6F, 0.15F), ItemPillar.class, THIN_SAKURA_PILLAR, tabBamboo);
            registerBlock(new BlockPillar(thickOrcPillarBID, Block.planks, 0, 0.3F, 0.7F, 0.2F), ItemPillar.class, THICK_ORC_PILLAR, tabBamboo);
            registerBlock(new BlockPillar(thinOrcPillarBID, Block.planks, 0, 0.4F, 0.6F, 0.15F), ItemPillar.class, THIN_ORC_PILLAR, tabBamboo);
            registerBlock(new BlockPillar(thickSprucePillarBID, Block.wood, 1, 0.3F, 0.7F, 0.2F), ItemPillar.class, THICK_SPRUCE_PILLAR, tabBamboo);
            registerBlock(new BlockPillar(thinSprucePillarBID, Block.wood, 1, 0.4F, 0.6F, 0.15F), ItemPillar.class, THIN_SPRUCE_PILLAR, tabBamboo);
            registerBlock(new BlockDeludePressurePlate(delude_plateBID), DELUDE_PLATE, tabBamboo);
            registerBlock(new BlockManeki(manekiBID, Material.ground), MANEKI, tabBamboo);
        } catch (IllegalArgumentException e) {
            slotOccupiedException(e);
        }
    }

    public static Block getBlockInstance(int blockID) {
        return BambooUtil.getBlcokInstance(blockID);
    }

    public static Item getItemInstance(int itemID) {
        return BambooUtil.getItemInstance(itemID);
    }

    private void itemsInit() {
        new ItemBambooBasket(bambooBasketIID - ITEMID_OFFSET).setUnlocalizedName("bamboobasket").setCreativeTab(tabBamboo);
        new ItemFirecracker(firecrackerIID - ITEMID_OFFSET).setUnlocalizedName("firecracker").setCreativeTab(tabBamboo);
        new ItemBambooFood(foodsIID - ITEMID_OFFSET).setUnlocalizedName("bambooFood").setCreativeTab(tabBamboo);
        new ItemBambooSpear(bambooSpearIID - ITEMID_OFFSET).setUnlocalizedName("bamboospear").setCreativeTab(tabBamboo);
        new ItemSlideDoor(slideDoorsIID - ITEMID_OFFSET).setUnlocalizedName("slideDoor").setCreativeTab(tabBamboo);
        new ItemKakeziku(kakezikuIID - ITEMID_OFFSET).setUnlocalizedName("kakeziku").setCreativeTab(tabBamboo);
        new ItemHuton(hutonIID - ITEMID_OFFSET).setUnlocalizedName("huton").setCreativeTab(tabBamboo);
        new ItemBambooshoot(takenokoIID - ITEMID_OFFSET, bambooBID).setUnlocalizedName("bambooshoot").setCreativeTab(tabBamboo);
        new ItemBoiledEgg(boiledEggIID - ITEMID_OFFSET).setUnlocalizedName("boiledEgg");
        new ItemShavedIce(shavedIceIID - ITEMID_OFFSET).setUnlocalizedName("shavedice").setCreativeTab(tabBamboo);
        new ItemWindChime(windChimeIID - ITEMID_OFFSET).setUnlocalizedName("windchime").setCreativeTab(tabBamboo);
        new ItemSack(itemSackIID - ITEMID_OFFSET).setUnlocalizedName("itemsack").setCreativeTab(tabBamboo);
        new ItemDirtySnowball(snowBallIID - ITEMID_OFFSET).setUnlocalizedName("snowball").setCreativeTab(tabBamboo);
        new ItemFan(fanIID - ITEMID_OFFSET).setUnlocalizedName("fan").setCreativeTab(tabBamboo);
        new ItemKatana(katanaIID - ITEMID_OFFSET).setUnlocalizedName("katana").setCreativeTab(tabBamboo);
        new ItemBambooBow(bambooBowIID - ITEMID_OFFSET).setUnlocalizedName("bamboobow").setCreativeTab(tabBamboo);
        new ItemTudura(tuduraIID - ITEMID_OFFSET).setUnlocalizedName("tudura").setCreativeTab(tabBamboo);
        new ItemWindmill(windmillIID - ITEMID_OFFSET).setUnlocalizedName("windmill").setCreativeTab(tabBamboo);
        new ItemWaterwheel(waterWheelIID - ITEMID_OFFSET).setUnlocalizedName("waterwheel").setCreativeTab(tabBamboo);
        new ItemBamboo(bambooIID - ITEMID_OFFSET).setCreativeTab(tabBamboo).setUnlocalizedName("itembamboo").setCreativeTab(tabBamboo);
        new ItemKaginawa(kaginawaIID - ITEMID_OFFSET).setCreativeTab(tabBamboo).setUnlocalizedName("kaginawa").setCreativeTab(tabBamboo);
        new ItemBambooSword(bambooSwordIID - ITEMID_OFFSET).setCreativeTab(tabBamboo).setUnlocalizedName("bamboosword").setCreativeTab(tabBamboo);
        new ItemObon(obonIID - ITEMID_OFFSET).setCreativeTab(tabBamboo).setUnlocalizedName("obon").setCreativeTab(tabBamboo);
        new ItemBitchuHoe(bitchuHoeIID - ITEMID_OFFSET).setUnlocalizedName("bitchuhoe").setCreativeTab(tabBamboo);
        new ItemSeeds(seedRiceIID - ITEMID_OFFSET, ricePlantBID, riceFieldBID).setUnlocalizedName("seedrice").setTextureName("seedrice").setCreativeTab(tabBamboo);
        MinecraftForge.addGrassSeed(new ItemStack(seedRiceIID, 1, 0), 10);
        new Item(strawIID - ITEMID_OFFSET).setUnlocalizedName("straw").setTextureName("straw").setCreativeTab(tabBamboo);
        new ItemMagatama(magatamaIID - ITEMID_OFFSET).setUnlocalizedName("magatama").setTextureName("magatama").setCreativeTab(tabBamboo);
    }

    private void registerBlock(Block block, String name) {
        this.registerBlock(block, ItemBlock.class, name, null);
    }

    private void registerBlock(Block block, String name, CreativeTabs creativeTabs) {
        this.registerBlock(block, ItemBlock.class, name, creativeTabs);
    }

    private void registerBlock(Block block, Class cls, String name) {
        this.registerBlock(block, cls, name, null);
    }

    private void registerBlock(Block block, Class cls, String name, CreativeTabs creativeTabs) {
        if (creativeTabs != null) {
            block.setCreativeTab(creativeTabs);
        }
        block.setUnlocalizedName(name);
        GameRegistry.registerBlock(block, cls, name, BambooCore.MODID);
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
            chiledName = "bambooshoot0214";
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

    private void idsInit() {
        File file = new File(Loader.instance().getConfigDir(), "mod_BambooIDConfig.cfg");
        Configuration conf = new Configuration(file);
        conf.load();
        blockIdInit(conf);
        itemIdInit(conf);
        idShifter(conf);
        conf.save();
    }

    private void blockIdInit(Configuration conf) {
        bambooBlockBID = getBlockId(conf, "bambooBlock", 3238);
        bambooBID = getBlockId(conf, "bamboo", 3239);
        sakuraLogBID = getBlockId(conf, "sakuraLog", 3240);
        kitunebiBID = getBlockId(conf, "kitunebi", 3241);
        spaBID = getBlockId(conf, "spaWater", 3242);
        jpchestBID = getBlockId(conf, "jpchest", 3243);
        dSquareBID = getBlockId(conf, "dSquare", 3244);
        andonBID = getBlockId(conf, "andon", 3245);
        dHalfSquareBID = getBlockId(conf, "dHalfSquare", 3246);
        bamboopaneBID = getBlockId(conf, "bamboopane", 3247);
        sakuraleavsBID = getBlockId(conf, "sakuraleavs", 3248);
        sakuraBID = getBlockId(conf, "sakura", 3249);
        rooftileBID = getBlockId(conf, "rooftile", 3250);
        spaunitBID = getBlockId(conf, "spaunit", 3251);
        delude_widthBID = getBlockId(conf, "delude_width", 3252);
        delude_heightBID = getBlockId(conf, "delude_height", 3253);
        delude_stairBID = getBlockId(conf, "delude_stair", 3254);
        campfireBID = getBlockId(conf, "campfire", 3255);
        decoBID = getBlockId(conf, "deco", 3256);
        halfDecoBID = getBlockId(conf, "halfDeco", 3257);
        twoDirDecoBID = getBlockId(conf, "twoDirDeco", 3258);
        halfTwoDirDecoBID = getBlockId(conf, "halfTwoDirDeco", 3259);
        kawara_stairBID = getBlockId(conf, "kawara_stair", 3260);
        riceFieldBID = getBlockId(conf, "riceField", 3261);
        ricePlantBID = getBlockId(conf, "ricePlant", 3262);
        millStoneBID = getBlockId(conf, "millStone", 3263);
        decoCarpetBID = getBlockId(conf, "decoCarpet", 3264);
        thickSakuraPillarBID = getBlockId(conf, "thickSakuraPillar", 3265);
        thinSakuraPillarBID = getBlockId(conf, "thinSakuraPillar", 3266);
        thickOrcPillarBID = getBlockId(conf, "thickOrcPillar", 3267);
        thinOrcPillarBID = getBlockId(conf, "thinOrcPillar", 3268);
        thickSprucePillarBID = getBlockId(conf, "thickSprucePillar", 3269);
        thinSprucePillarBID = getBlockId(conf, "thinSprucePillar", 3270);
        delude_plateBID = getBlockId(conf, "delude_plate", 3271);
        manekiBID = getBlockId(conf, "maneki", 3272);
    }

    private int getBlockId(Configuration conf, String key, int defaultId) {
        return conf.getBlock(key, defaultId).getInt(defaultId);
    }

    private void itemIdInit(Configuration conf) {
        bambooBasketIID = getItemId(conf, "bambooBasket", 23535);
        firecrackerIID = getItemId(conf, "firecracker", 23536);
        foodsIID = getItemId(conf, "foods", 23539);
        windChimeIID = getItemId(conf, "windChime", 23540);
        itemSackIID = getItemId(conf, "itemSack", 23541);
        shavedIceIID = getItemId(conf, "shavedIce", 23542);
        bambooSpearIID = getItemId(conf, "bambooSpear", 23543);
        slideDoorsIID = getItemId(conf, "slideDoors", 23544);
        kakezikuIID = getItemId(conf, "kakeziku", 23545);
        seedRiceIID = getItemId(conf, "seedRice", 23546);
        hutonIID = getItemId(conf, "huton", 23547);
        takenokoIID = getItemId(conf, "takenoko", 23548);
        strawIID = getItemId(conf, "straw", 23549);
        boiledEggIID = getItemId(conf, "boiledEgg", 23550);
        snowBallIID = getItemId(conf, "snowBall", 23551);
        fanIID = getItemId(conf, "fan", 23552);
        katanaIID = getItemId(conf, "katana", 23553);
        bambooBowIID = getItemId(conf, "bambooBow", 23554);
        windmillIID = getItemId(conf, "windmill", 23555);
        waterWheelIID = getItemId(conf, "waterWheel", 23556);
        bambooIID = getItemId(conf, "bamboo", 23557);
        tuduraIID = getItemId(conf, "tudura", 23558);
        kaginawaIID = getItemId(conf, "kaginawa", 23559);
        bambooSwordIID = getItemId(conf, "bambooSword", 23560);
        obonIID = getItemId(conf, "obon", 23561);
        bitchuHoeIID = getItemId(conf, "bitchuHoe", 23562);
        magatamaIID = getItemId(conf, "magatama", 23563);
    }

    private int getItemId(Configuration conf, String key, int defaultId) {
        return conf.getItem(key, defaultId).getInt(defaultId);
    }

    private void idShifter(Configuration conf) {
        int blockShiftIndex = conf.get("A", "blockShiftIndex", 0, "BlockID bulk movement").getInt(0);
        int itemShiftIndex = conf.get("A", "itemShiftIndex", 0, "ItemID bulk movement").getInt(0);

        for (Field f : this.getClass().getFields()) {
            try {
                if (f.getName().matches(".*BID")) {
                    f.setInt(this, f.getInt(this) + blockShiftIndex);
                } else if (f.getName().matches(".*IID")) {
                    f.setInt(this, f.getInt(this) + itemShiftIndex + ITEMID_OFFSET);
                }
            } catch (Exception e) {
                FMLCommonHandler.instance().raiseException(e, "[mod_bamboo] IDShift exception", true);
            }
        }
    }

    private void slotOccupiedException(IllegalArgumentException e) {
        if (!(FMLCommonHandler.instance().getMinecraftServerInstance() != null && FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer())) {
            if (Minecraft.getMinecraft().gameSettings.language.equals("ja_JP")) {
                String[] message = e.getMessage().split(" ");
                throw new IllegalArgumentException("\n[mod_Bamboo]BlockID " + message[1] + " が被ってます、トピックへ報告しないでください。\n" + "対象Block： " + message[6] + " と " + message[9] + " です。\n" + "BlockID変更方法のサポートは致しかねます、各自検索していただくようお願い申し上げます。\n" + e.getMessage());
            } else {
                throw e;
            }
        } else {
            throw e;
        }
    }
}
