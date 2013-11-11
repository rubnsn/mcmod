package ruby.bamboo;

import static ruby.bamboo.BambooInit.*;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ruby.bamboo.entity.EnumSlideDoor;
import ruby.bamboo.item.EnumFood;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.ResourceManager;
import net.minecraft.client.resources.ResourceManagerReloadListener;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@SideOnly(Side.CLIENT)
public class BambooLocalize implements ResourceManagerReloadListener {
    private String lang;
    private Properties prop;
    public static BambooLocalize instance = new BambooLocalize();
    static {
        instance = new BambooLocalize();
    }

    public static void init() {
        //デフォルトパッケージより後だと、ゲーム中に言語変更した際正常に適応されないため、無理やり最初に詰めておく
        //うまいやり方思いついたら直す
        /*
        for (Field field : Minecraft.getMinecraft().getResourceManager().getClass().getDeclaredFields()) {
            try {
                if (field.getType() == List.class) {
                    field.setAccessible(true);
                    ((List) field.get(Minecraft.getMinecraft().getResourceManager())).add(0, instance);
                    instance.onResourceManagerReload(Minecraft.getMinecraft().getResourceManager());
                }
            } catch (Exception e) {
                FMLLog.log(Level.WARNING, "BambooMod localize exception");
                ((SimpleReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(instance);
                return;
            }
        }*/
        ((SimpleReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(instance);
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourcemanager) {
        Language lang = Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage();
        //load(lang.getLanguageCode());
        LanguageRegistry.instance().loadLocalization("/ruby/bamboo/lang/en_US.properties", "en_US", false);
        LanguageRegistry.instance().loadLocalization("/ruby/bamboo/lang/ja_JP.properties", "ja_JP", false);
        LanguageRegistry.instance().loadLocalization("/ruby/bamboo/lang/zh_CN.properties", "zh_CN", false);
        LanguageRegistry.instance().loadLocalization("/ruby/bamboo/lang/" + lang.getLanguageCode() + ".properties", lang.getLanguageCode(), false);
    }

}
