package ruby.bamboo;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class PlayerDisplayNameHandler {

    public PlayerDisplayNameHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void getPlayerDispayNameEvent(PlayerEvent.NameFormat event) {
        if (Config.nameMap.containsKey(event.displayname)) {
            event.displayname = Config.nameMap.get(event.displayname);
        }
    }

    @SubscribeEvent
    public void onRenderEntitySpecial(RenderLivingEvent.Specials.Pre event) {
        if (event.entity instanceof EntityPlayer) {
            event.setCanceled(((EntityPlayer) event.entity).getDisplayName().equals(""));
        }
    }
}
