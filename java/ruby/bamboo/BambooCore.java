package ruby.bamboo;

import java.util.Collections;

import net.minecraft.block.BlockDispenser;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.oredict.RecipeSorter;
import ruby.bamboo.dispenser.DispenserBehaviorBambooSpear;
import ruby.bamboo.dispenser.DispenserBehaviorDirtySnowball;
import ruby.bamboo.dispenser.DispenserBehaviorFireCracker;
import ruby.bamboo.gui.GuiHandler;
import ruby.bamboo.item.crafting.CookingManager;
import ruby.bamboo.proxy.CommonProxy;
import ruby.bamboo.world.WorldProviderBamboo;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.toposort.ModSortingException;
import cpw.mods.fml.relauncher.CoreModManager;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.ReflectionHelper.UnableToAccessFieldException;

@Mod(
        modid = BambooCore.MODID,
        name = BambooCore.MODID,
        version = "Minecraft" + BambooCore.MC_VER + " var" + BambooCore.BAMBOO_VER,
        guiFactory = "ruby.bamboo.BambooConfigGuiFactory")
/*@NetworkMod(channels = { "B_Entity", "bamboo", "bamboo2" },
        packetHandler = NetworkHandler.class,
        connectionHandler = NetworkHandler.class)*/
public class BambooCore {
    public static final String MODID = "BambooMod";
    public static final String MC_VER = "@MC_VERSION@";
    public static final String BAMBOO_VER = "@VERSION@";
    public static final String resourceDomain = "bamboo:";
    public static final boolean DEBUGMODE = isDevelopment();

    @SidedProxy(serverSide = "ruby.bamboo.proxy.CommonProxy",
            clientSide = "ruby.bamboo.proxy.ClientProxy")
    public static CommonProxy proxy;

    @Instance(MODID)
    public static BambooCore instance;

    @Mod.EventHandler
    public void preLoad(FMLPreInitializationEvent e) {
        proxy.preInit();
        if (Config.updateNotify) {
            new Thread(new UpdateNotify()).run();
        }
        new EnchantHandler();
    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent e) {
        proxy.init();
        proxy.registerTESTileEntity();
        registDispencer();
        // debug
        if (DEBUGMODE) {
            System.out.println("DEBUG MODE Enable");
        }

    }

    @Mod.EventHandler
    public void postLoad(FMLPostInitializationEvent e) {
        proxy.postInit();
    }

    private int provideId;

    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        Config.reloadWorldConfig();
        if (DEBUGMODE) {
            provideId = 0;
            while (!DimensionManager.registerProviderType(++provideId, WorldProviderBamboo.class, false)) {
            }
            DimensionManager.registerDimension(Config.dimensionId, provideId);
        }
    }

    @Mod.EventHandler
    public void serverStoped(FMLServerStoppedEvent event) {
        if (DEBUGMODE) {
            DimensionManager.unregisterDimension(Config.dimensionId);
            DimensionManager.unregisterProviderType(provideId);
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(BambooCore.getInstance(), new GuiHandler());
    }

    @Mod.EventHandler
    public void onAvalible(FMLLoadCompleteEvent event) {
        try {
            Collections.sort(CookingManager.getInstance().getRecipeList(), RecipeSorter.INSTANCE);
        } catch (ModSortingException e) {
            FMLLog.warning("OreDict sort exception!!", e);
        }
    }

    private void registDispencer() {
        BlockDispenser.dispenseBehaviorRegistry.putObject(BambooInit.snowBall, new DispenserBehaviorDirtySnowball());
        BlockDispenser.dispenseBehaviorRegistry.putObject(BambooInit.firecracker, new DispenserBehaviorFireCracker());
        BlockDispenser.dispenseBehaviorRegistry.putObject(BambooInit.bambooSpear, new DispenserBehaviorBambooSpear());
    }

    public static BambooCore getInstance() {
        return instance;
    }

    private static boolean isDevelopment() {
        boolean result;
        try {
            result = ReflectionHelper.getPrivateValue(CoreModManager.class, null, "deobfuscatedEnvironment");
        } catch (UnableToAccessFieldException e) {
            FMLLog.warning("Debug mode forced false!");
            result = false;
        }
        return result;
    }
}