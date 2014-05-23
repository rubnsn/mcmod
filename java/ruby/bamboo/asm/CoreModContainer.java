package ruby.bamboo.asm;

import java.util.Arrays;

import com.google.common.eventbus.EventBus;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;

public class CoreModContainer extends DummyModContainer {
    public CoreModContainer() {
        super(new ModMetadata());
        ModMetadata meta = getMetadata();

        meta.modId = "bambootransformer";
        meta.name = "BambooTransformer";
        meta.version = "1.0.0";
        meta.authorList = Arrays.asList("ruby");
        meta.description = "";
        meta.url = "";
        meta.credits = "";
        this.setEnabledState(true);
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }
}
