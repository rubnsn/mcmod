package ruby.bamboo.asm;

import java.io.File;
import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@TransformerExclusions({ "ruby.bamboo.asm" })
public class CorePlugin implements IFMLLoadingPlugin {
    static File location;

    @Override
    public String[] getASMTransformerClass() {
        return new String[] { "ruby.bamboo.asm.EnumEnchantmentTypeTransformer", "ruby.bamboo.asm.ItemHoeTransformaer" };
    }

    @Override
    public String getModContainerClass() {
        return "ruby.bamboo.asm.CoreModContainer";
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        if (data.containsKey("coremodLocation")) {
            location = (File) data.get("coremodLocation");
        }
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

}
