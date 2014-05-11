package ruby.bamboo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import net.minecraft.util.ChatComponentText;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class UpdateNotify implements Runnable {

    private boolean notifyUpdate;
    private String newVersionNum;
    private String newVersionMessage;

    public UpdateNotify() {
        notifyUpdate = false;
        newVersionMessage = "";
    }

    @SubscribeEvent
    public void serverStarted(PlayerTickEvent event) {
        if (notifyUpdate && newVersionNum != null) {
            if (event.side.isClient()) {
                if (event.player.worldObj.isRemote) {
                    event.player.addChatMessage(new ChatComponentText("BambooMod update!:" + newVersionNum + (!newVersionMessage.equals("") ? ":" + newVersionMessage : "")));
                    FMLCommonHandler.instance().bus().unregister(this);
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            InputStreamReader in = new InputStreamReader(new URL("https://dl.dropboxusercontent.com/u/37248864/update.json").openStream());
            JsonObject jsono = (JsonObject) new JsonParser().parse(in).getAsJsonObject().get(BambooCore.MC_VER);
            if (jsono != null) {
                newVersionNum = jsono.get("version").getAsString();
                if (!newVersionNum.equals(BambooCore.BAMBOO_VER)) {
                    notifyUpdate = true;
                    newVersionMessage = jsono.get("mes").getAsString();
                    FMLCommonHandler.instance().bus().register(this);
                }
            }
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
    }
}
