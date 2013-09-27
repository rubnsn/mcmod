package ruby.bamboo;

import static ruby.bamboo.BambooInit.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ruby.bamboo.entity.EnumSlideDoor;
import ruby.bamboo.item.EnumFood;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.resources.Locale;
import net.minecraft.client.resources.ResourceManager;
import net.minecraft.client.resources.ResourceManagerReloadListener;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.util.StringTranslate;

@SideOnly(Side.CLIENT)
public class BambooLocalize implements ResourceManagerReloadListener
{
    private String lang;
    private Properties prop;
    public static BambooLocalize instance = new BambooLocalize();
    static
    {
        instance = new BambooLocalize();
    }
    public static void init()
    {
        for (Field field: Minecraft.getMinecraft().func_110442_L().getClass().getDeclaredFields())
        {
            try
            {
                if (field.getType() == List.class)
                {
                    field.setAccessible(true);
                    ((List)field.get(Minecraft.getMinecraft().func_110442_L())).add(0, instance);
                    instance.func_110549_a(Minecraft.getMinecraft().func_110442_L());
                }
            }
            catch (Exception e)
            {
                FMLLog.log(Level.WARNING, "BambooMod localize exception");
                ((SimpleReloadableResourceManager)Minecraft.getMinecraft().func_110442_L()).func_110542_a(instance);
                return;
            }
        }
    }

    public void load(String lang)
    {
        try
        {
            this.lang = lang;
            prop = new Properties();
            InputStream stream = this.getClass().getResourceAsStream("/ruby/bamboo/lang/" + lang + ".properties");

            if (stream != null)
            {
                prop.load(new InputStreamReader(stream, "UTF-8"));
            }
            else
            {
                System.out.printf("[mod_bamboo]lang %s not found \n", lang);
                return;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        int i;
        addName(getItemInstance(bambooIID)) ;
        addName(new ItemStack(bambooBlockBID, 1, 0));
        addName(getBlockInstance(kitunebiBID));
        addName(getItemInstance(bambooBasketIID));
        addName(new ItemStack(firecrackerIID, 0, 0));
        addName(new ItemStack(firecrackerIID, 0, 1));
        addName(new ItemStack(firecrackerIID, 0, 2));

        for (i = 0; i < EnumFood.values().length; i++)
        {
            addName(new ItemStack(foodsIID, 1, i));
        }

        addName(new ItemStack(bambooSpearIID, 1, 0));
        addName(new ItemStack(bambooSpearIID, 1, 1));

        for (i = 0; i < EnumSlideDoor.values().length; i++)
        {
            addName(new ItemStack(slideDoorsIID, 1, i));
        }

        addName(getBlockInstance(jpchestBID));
        addName(new ItemStack(dSquareBID, 1, 0));
        addName(new ItemStack(dSquareBID, 1, 4));
        addName(new ItemStack(dSquareBID, 1, 8));
        addName(new ItemStack(dSquareBID, 1, 10));
        addName(new ItemStack(dSquareBID, 1, 12));
        addName(new ItemStack(dHalfSquareBID, 1, 0));
        addName(new ItemStack(dHalfSquareBID, 1, 4));
        addName(new ItemStack(dHalfSquareBID, 1, 8));
        addName(new ItemStack(dHalfSquareBID, 1, 10));
        addName(new ItemStack(dHalfSquareBID, 1, 12));
        addName(getItemInstance(kakezikuIID));
        addName(getBlockInstance(andonBID));
        addName(new ItemStack(bamboopaneBID, 1, 0));
        addName(new ItemStack(bamboopaneBID, 1, 1));
        addName(new ItemStack(bamboopaneBID, 1, 2));
        addName(new ItemStack(bamboopaneBID, 1, 3));
        addName(new ItemStack(bamboopaneBID, 1, 4));
        addName(new ItemStack(bamboopaneBID, 1, 5));
        addName(getBlockInstance(sakuraleavsBID));
        addName(getBlockInstance(sakuraBID));
        addName(getItemInstance(hutonIID));
        addName(getItemInstance(takenokoIID));
        addName(getBlockInstance(rooftileBID));
        addName(getBlockInstance(spaunitBID));
        addName(getItemInstance(boiledEggIID));

        for (i = 0; i < 18; i++)
        {
            addName(new ItemStack(shavedIceIID, 1, i));
        }

        for (i = 0; i < 10; i++)
        {
            addName(new ItemStack(shavedIceIID, 1, i));
        }

        addName(getItemInstance(windChimeIID));
        addName(getItemInstance(itemSackIID));
        addName(getItemInstance(fanIID));
        addName(getItemInstance(katanaIID));
        addName(getItemInstance(bambooBowIID));
        addName(getBlockInstance(sakuraLogBID));
        addName(getBlockInstance(delude_widthBID));
        addName(getBlockInstance(delude_heightBID));
        addName(getBlockInstance(delude_stairBID));
        addName(getBlockInstance(campfireBID));
        addName(getItemInstance(tuduraIID));
        addName(new ItemStack(windmillIID, 1, 0));
        addName(new ItemStack(windmillIID, 1, 1));
        addName(getItemInstance(waterWheelIID));
        addName(getItemInstance(kaginawaIID));
        addName(getItemInstance(bambooSwordIID));
        addName(new ItemStack(decoBID, 1, 0));
        addName(new ItemStack(decoBID, 1, 1));
        addName(new ItemStack(decoBID, 1, 2));
        addName(new ItemStack(decoBID, 1, 3));
        addName(new ItemStack(decoBID, 1, 4));
        addName(new ItemStack(decoBID, 1, 5));
        addName(new ItemStack(halfDecoBID, 1, 0));
        addName(new ItemStack(halfDecoBID, 1, 1));
        addName(new ItemStack(halfDecoBID, 1, 2));
        addName(new ItemStack(halfDecoBID, 1, 3));
        addName(new ItemStack(halfDecoBID, 1, 4));
        addName(new ItemStack(halfDecoBID, 1, 5));
        addName(new ItemStack(twoDirDecoBID, 1, 0));
        addName(new ItemStack(twoDirDecoBID, 1, 2));
        addName(new ItemStack(halfTwoDirDecoBID, 1, 0));
        addName(new ItemStack(halfTwoDirDecoBID, 1, 2));
        addName(getItemInstance(obonIID));
        addName(getBlockInstance(kawara_stairBID));
    }
    private void addName(Block block)
    {
        try
        {
            LanguageRegistry.instance().addNameForObject(block, lang, prop.getProperty(block.getUnlocalizedName()));
        }
        catch (NullPointerException e)
        {
            ModLoader.getLogger().fine(block.getUnlocalizedName() + " translation mistake");
        }
    }
    private void addName(Item item)
    {
        try
        {
            LanguageRegistry.instance().addNameForObject(item, lang, prop.getProperty(item.getUnlocalizedName()));
        }
        catch (NullPointerException e)
        {
            ModLoader.getLogger().fine(item.getUnlocalizedName() + " translation mistake");
        }
    }
    private void addName(ItemStack is)
    {
        try
        {
            LanguageRegistry.instance().addNameForObject(is, lang, prop.getProperty(is.getItem().getUnlocalizedName() + "." + is.getItemDamage()));
        }
        catch (NullPointerException e)
        {
            ModLoader.getLogger().fine(is.getItem().getUnlocalizedName() + "." + is.getItemDamage() + " translation mistake");
        }
    }
    @Override
    public void func_110549_a(ResourceManager resourcemanager)
    {
        Language lang = Minecraft.getMinecraft().func_135016_M().func_135041_c();
        load(lang.func_135034_a());
    }
}
