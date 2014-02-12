package ruby.autotorch;

import net.minecraft.command.CommandHandler;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.relauncher.Side;

//@Mod(modid = "ruby.AutoTorch", name = "AutoTorch", version = "1.0")
//@NetworkMod(clientSideRequired = false, serverSideRequired = false)
public class AutoTorch {
    private TickHandler tick_handler;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        tick_handler = new TickHandler(new Config());
        TickRegistry.registerTickHandler(tick_handler, Side.SERVER);
    }

    @Mod.EventHandler
    public void addCommand(FMLServerStartedEvent event) {
        ((CommandHandler) MinecraftServer.getServer().getCommandManager()).registerCommand(new CommandAutoTorch(tick_handler));
    }
}
