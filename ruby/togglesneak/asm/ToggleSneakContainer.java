package ruby.togglesneak.asm;

import java.util.Arrays;

import com.google.common.eventbus.EventBus;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;

public class ToggleSneakContainer extends DummyModContainer {
    public ToggleSneakContainer() {
        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId = "toggleSneak";
        meta.name = "ToggleSneak";
        meta.version = "1.0";
        meta.authorList = Arrays.asList("ruby");
        meta.description = "Toggle Sneak!";
        meta.url = "http://forum.minecraftuser.jp/viewtopic.php?f=13&t=172";
        meta.credits = "";
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        return true;
    }
}
