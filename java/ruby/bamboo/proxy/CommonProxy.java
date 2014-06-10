package ruby.bamboo.proxy;

import java.util.WeakHashMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;
import ruby.bamboo.BambooCore;
import ruby.bamboo.BambooInit;
import ruby.bamboo.BambooRecipe;
import ruby.bamboo.CraftingHandler;
import ruby.bamboo.entity.EntityBambooSpear;
import ruby.bamboo.entity.EntityDirtySnowball;
import ruby.bamboo.entity.EntityDummyChair;
import ruby.bamboo.entity.EntityFirecracker;
import ruby.bamboo.entity.EntityFirefly;
import ruby.bamboo.entity.EntityKaginawa;
import ruby.bamboo.entity.EntityKakeziku;
import ruby.bamboo.entity.EntityObon;
import ruby.bamboo.entity.EntitySlideDoor;
import ruby.bamboo.entity.EntityThrowZabuton;
import ruby.bamboo.entity.EntityWaterwheel;
import ruby.bamboo.entity.EntityWind;
import ruby.bamboo.entity.EntityWindChime;
import ruby.bamboo.entity.EntityWindmill;
import ruby.bamboo.entity.EntityZabuton;
import ruby.bamboo.entity.magatama.EntityClock;
import ruby.bamboo.entity.magatama.EntityDummy;
import ruby.bamboo.entity.magatama.EntityFlareEffect;
import ruby.bamboo.entity.magatama.EntityGravityHole;
import ruby.bamboo.entity.magatama.EntityMagatama;
import ruby.bamboo.entity.magatama.EntityShield;
import ruby.bamboo.tileentity.TileEntityAndon;
import ruby.bamboo.tileentity.TileEntityCampfire;
import ruby.bamboo.tileentity.TileEntityHuton;
import ruby.bamboo.tileentity.TileEntityJPChest;
import ruby.bamboo.tileentity.TileEntityManeki;
import ruby.bamboo.tileentity.TileEntityMillStone;
import ruby.bamboo.tileentity.TileEntityMultiBlock;
import ruby.bamboo.tileentity.TileEntityMultiPot;
import ruby.bamboo.tileentity.spa.TileEntitySpaChild;
import ruby.bamboo.tileentity.spa.TileEntitySpaParent;
import ruby.bamboo.worldgen.GeneraterHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy {
    public WeakHashMap<Entity, EntityKaginawa> kagimap = new WeakHashMap<Entity, EntityKaginawa>();
    private static final int kakezikuEID = 0;
    // private static int andonEID=1;
    private static final int slidedoorEID = 2;
    // 3;
    private static final int bspearEID = 4;
    private static final int windbellEID = 5;
    private static final int fireCrackerEID = 6;
    private static final int snowBallEID = 7;
    private static final int windMillEID = 8;
    private static final int waterMillEID = 9;
    private static final int windEID = 10;
    private static final int kaginawaEID = 11;
    private static final int obonEID = 12;
    private static final int magatamaEID = 13;
    private static final int flareEID = 14;
    private static final int gravityHoleEID = 15;
    private static final int goldenClockEID = 16;
    private static final int shieldEID = 17;
    private static final int dummyEID = 18;
    private static final int zabutonEID = 19;
    private static final int throwZabutonEID = 20;
    //21
    private static final int fireflyEID = 22;
    private static final int dummyChairEID = 23;

    public void preInit() {
        if (BambooCore.DEBUGMODE) {
            // わーるどじぇねれーとはんどら
            GeneraterHandler.init();
        }
        // くらふとはんどら
        FMLCommonHandler.instance().bus().register(new CraftingHandler());
        // block & item init 分割1
        BambooInit.init();
        // name init 分割2
        registerEntity(EntityKakeziku.class, "Kakeziku", kakezikuEID, 80, 10, false);
        registerEntity(EntitySlideDoor.class, "Syouzi", slidedoorEID, 80, 1, true);
        registerEntity(EntityBambooSpear.class, "BSpear", bspearEID, 80, 3, true);
        registerEntity(EntityWindChime.class, "Wind bell", windbellEID, 80, 10, false);
        registerEntity(EntityFirecracker.class, "FileCracker", fireCrackerEID, 80, 3, true);
        registerEntity(EntityDirtySnowball.class, "DirtySnowball", snowBallEID, 80, 3, true);
        registerEntity(EntityWind.class, "wind", windEID, 80, 3, true);
        registerEntity(EntityWindmill.class, "WindMill", windMillEID, 80, 10, false);
        registerEntity(EntityWaterwheel.class, "WaterMill", waterMillEID, 80, 10, false);
        registerEntity(EntityKaginawa.class, "Kaginawa", kaginawaEID, 80, 3, true);
        registerEntity(EntityObon.class, "Obon", obonEID, 80, 3, false);
        registerEntity(EntityMagatama.class, "Magatama", magatamaEID, 80, 3, true);
        registerEntity(EntityFlareEffect.class, "FlareEffect", flareEID, 80, 3, true);
        registerEntity(EntityGravityHole.class, "GravityHole", gravityHoleEID, 304, 3, true);
        registerEntity(EntityClock.class, "GoldenClock", goldenClockEID, 304, 3, true);
        registerEntity(EntityShield.class, "Shield", shieldEID, 304, 3, true);
        registerEntity(EntityDummy.class, "Dummy", dummyEID, 304, 3, true);
        registerEntity(EntityZabuton.class, "Zabuton", zabutonEID, 80, 3, true);
        registerEntity(EntityThrowZabuton.class, "ThrowZabuton", throwZabutonEID, 80, 1, true);
        registerEntity(EntityFirefly.class, "Firefly", fireflyEID, 80, 1, true);
        registerEntity(EntityDummyChair.class, "DummyChair", dummyChairEID, 80, 3, true);
        GameRegistry.registerTileEntity(TileEntityJPChest.class, "JP Chest");
        GameRegistry.registerTileEntity(TileEntitySpaParent.class, "Tile Spa");
        GameRegistry.registerTileEntity(TileEntitySpaChild.class, "Tile SpaChild");
        GameRegistry.registerTileEntity(TileEntityMultiPot.class, "BambooMultiPot");
        GameRegistry.registerTileEntity(TileEntityMultiBlock.class, "BambooMultiBlock");

        EntityRegistry.addSpawn(EntityFirefly.class, 5, 5, 10, EnumCreatureType.ambient, BiomeGenBase.river, BiomeGenBase.plains);
    }

    public void registerTESTileEntity() {
        GameRegistry.registerTileEntity(TileEntityCampfire.class, "Camp fire");
        GameRegistry.registerTileEntity(TileEntityAndon.class, "Andon");
        GameRegistry.registerTileEntity(TileEntityMillStone.class, "MillStone");
        GameRegistry.registerTileEntity(TileEntityManeki.class, "Maneki");
        GameRegistry.registerTileEntity(TileEntityHuton.class, "Huton");
    }

    public void init() {
        new BambooRecipe();
    }

    private void registerEntity(Class entityClass, String entityName, int id, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
        /*
         * try{
         * Class.forName("cpw.mods.fml.common.registry.EntityRegistry").getMethod
         * (
         * "registerModEntity",Class.class,String.class,int.class,Object.class,int
         * .class,int.class,boolean.class).invoke(null,entityClass,
         * entityName,id
         * ,mod,trackingRange,updateFrequency,sendsVelocityUpdates);
         * }catch(Exception e){ e.printStackTrace(); }
         */
        EntityRegistry.registerModEntity(entityClass, entityName, id, BambooCore.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
        // EntityRegistry.instance().lookupModSpawn(entityClass,false).setCustomSpawning(ClientSpawnHandler.getInstance(),
        // false);
    }
}
