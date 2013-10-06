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
public class BambooLocalize implements ResourceManagerReloadListener {
	private String lang;
	private Properties prop;
	public static BambooLocalize instance = new BambooLocalize();
	static {
		instance = new BambooLocalize();
	}

	public static void init() {
		for (Field field : Minecraft.getMinecraft().getResourceManager()
				.getClass().getDeclaredFields()) {
			try {
				if (field.getType() == List.class) {
					field.setAccessible(true);
					((List) field.get(Minecraft.getMinecraft()
							.getResourceManager())).add(0, instance);
					instance.onResourceManagerReload(Minecraft.getMinecraft()
							.getResourceManager());
				}
			} catch (Exception e) {
				FMLLog.log(Level.WARNING, "BambooMod localize exception");
				((SimpleReloadableResourceManager) Minecraft.getMinecraft()
						.getResourceManager()).registerReloadListener(instance);
				return;
			}
		}
	}

	public void load(String lang) {
		try {
			this.lang = lang;
			prop = new Properties();
			InputStream stream = this.getClass().getResourceAsStream(
					"/ruby/bamboo/lang/" + lang + ".properties");

			if (stream != null) {
				prop.load(new InputStreamReader(stream, "UTF-8"));
			} else {
				System.out.printf("[mod_bamboo]lang %s not found \n", lang);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 桜の花どうしようかなー、名前が似たようなの?
		addName(SAKURA_LEAVES);
		addName(itemSackIID);
		int i;
		// サブタイプ無しは一括
		for (Field field : BambooInit.class.getDeclaredFields()) {
			try {
				if (field.getName().matches(".*BID")||field.getName().matches(".*IID")) {
					i = (Integer) field.get(null);
					if (Item.itemsList[i] != null
							&& !Item.itemsList[i].getHasSubtypes()) {
						addName(i);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// サブタイプありは個別
		addName(SINGLE_TEX_DECO, 0);
		addName(new ItemStack(firecrackerIID, 1, 0));
		addName(new ItemStack(firecrackerIID, 1, 1));
		addName(new ItemStack(firecrackerIID, 1, 2));

		for (i = 0; i < EnumFood.values().length; i++) {
			addName(new ItemStack(foodsIID, 1, i));
		}

		addName(new ItemStack(bambooSpearIID, 1, 0));
		addName(new ItemStack(bambooSpearIID, 1, 1));

		for (i = 0; i < EnumSlideDoor.values().length; i++) {
			addName(new ItemStack(slideDoorsIID, 1, i));
		}

		addName(DIR_SQUARE, 0);
		addName(DIR_SQUARE, 4);
		addName(DIR_SQUARE, 8);
		addName(DIR_SQUARE, 10);
		addName(DIR_SQUARE, 12);
		addName(HALF_DIR_SQUARE, 0);
		addName(HALF_DIR_SQUARE, 4);
		addName(HALF_DIR_SQUARE, 8);
		addName(HALF_DIR_SQUARE, 10);
		addName(HALF_DIR_SQUARE, 12);
		addName(BAMBOO_PANEL, 0);
		addName(BAMBOO_PANEL, 1);
		addName(BAMBOO_PANEL, 2);
		addName(BAMBOO_PANEL, 3);
		addName(BAMBOO_PANEL, 4);
		addName(BAMBOO_PANEL, 5);
		for (i = 0; i < 18; i++) {
			addName(new ItemStack(shavedIceIID, 1, i));
		}

		for (i = 0; i < 10; i++) {
			addName(new ItemStack(shavedIceIID, 1, i));
		}
		for (i = 0; i < 10; i++) {
			addName((new ItemStack(snowBallIID, 1, i)));
		}
		addName(new ItemStack(windmillIID, 1, 0));
		addName(new ItemStack(windmillIID, 1, 1));
		addName(DECO_SQUARE, 0);
		addName(DECO_SQUARE, 1);
		addName(DECO_SQUARE, 2);
		addName(DECO_SQUARE, 3);
		addName(DECO_SQUARE, 4);
		addName(DECO_SQUARE, 5);
		addName(HALF_DECO_SQUARE, 0);
		addName(HALF_DECO_SQUARE, 1);
		addName(HALF_DECO_SQUARE, 2);
		addName(HALF_DECO_SQUARE, 3);
		addName(HALF_DECO_SQUARE, 4);
		addName(HALF_DECO_SQUARE, 5);
		addName(TWO_DIR_DECO_SQUARE, 0);
		addName(TWO_DIR_DECO_SQUARE, 2);
		addName(HALF_TWO_DIR_DECO_SQUARE, 0);
		addName(HALF_TWO_DIR_DECO_SQUARE, 2);
		addName(DECO_CARPET, 0);
		addName(DECO_CARPET, 2);
		addName(DECO_CARPET, 4);
		addName(DECO_CARPET, 6);
		addName(DECO_CARPET, 8);
	}

	private void addName(String name) {
		this.addName(name, 0);
	}

	private void addName(String name, int meta) {
		this.addName(new ItemStack(getBlockInstance(name), 1, meta));
	}

	private void addName(int itemId) {
		if (Item.itemsList[itemId] != null) {
			LanguageRegistry.instance().addNameForObject(
					Item.itemsList[itemId],
					lang,
					prop.getProperty(Item.itemsList[itemId]
							.getUnlocalizedName()));
		} else {
			FMLLog.getLogger().fine(
					Item.itemsList[itemId].getUnlocalizedName()
							+ " translation mistake");
		}
	}

	private void addName(ItemStack is) {
		if (is.getItem() != null) {
			LanguageRegistry.instance().addNameForObject(
					is,
					lang,
					prop.getProperty(is.getItem().getUnlocalizedName() + "."
							+ is.getItemDamage()));
		} else {
			FMLLog.getLogger().fine(
					is.getItem().getUnlocalizedName() + "."
							+ is.getItemDamage() + " translation mistake");
		}
	}

	@Override
	public void onResourceManagerReload(ResourceManager resourcemanager) {
		Language lang = Minecraft.getMinecraft().getLanguageManager()
				.getCurrentLanguage();
		load(lang.getLanguageCode());
	}
}
