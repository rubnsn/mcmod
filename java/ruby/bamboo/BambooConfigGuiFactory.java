package ruby.bamboo;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.IModGuiFactory;

public class BambooConfigGuiFactory implements IModGuiFactory {
    public static class BambooConfigGuiScreen extends GuiScreen {
        private GuiScreen parent;
        private final static byte DONE = 1;
        private static int button;
        private ConfigCategory category;

        public BambooConfigGuiScreen(GuiScreen parent) {
            this.parent = parent;
            category = Config.configuration.getCategory(Config.CATEGORY_BAMBOO);
        }

        @Override
        public void initGui() {
            this.buttonList.add(new GuiButton(DONE, this.width / 2 - 75, this.height - 38, I18n.format("gui.done")));
            int count = 0;
            int height;
            int width = 35;
            for (Property m : category.values()) {
                height = count > 7 ? 195 : 20;
                width = count > 7 ? 35 + (count - 8) * 18 : 35 + count * 18;
                this.buttonList.add(new GuiButton(count + 10, height, width, 175, 17, m.getName() + ": " + m.getString()) {
                    private BambooConfigGuiScreen parent;

                    public GuiButton setParent(BambooConfigGuiScreen parent) {
                        this.parent = parent;
                        return this;
                    }

                    @Override
                    protected void mouseDragged(Minecraft mc, int posX, int posY) {
                        if (func_146115_a()) {
                            mc.fontRenderer.drawStringWithShadow(((Property) category.values().toArray()[this.id - 10]).comment, 35, parent.height - 55, 0xFFFF00);
                        }
                    }
                }.setParent(this));

                count++;
            }
        }

        @Override
        protected void actionPerformed(GuiButton par1GuiButton) {
            if (par1GuiButton.enabled) {
                if (par1GuiButton.id == DONE) {
                    FMLClientHandler.instance().showGuiScreen(parent);
                    Config.configuration.save();
                    Config.reloadConfig();
                } else {
                    Property prop = ((Property) category.values().toArray()[par1GuiButton.id - 10]);
                    if (prop.getType() == Type.BOOLEAN) {
                        prop.set(!prop.getBoolean(false));
                    } else if (prop.getType() == Type.INTEGER) {
                        int i = prop.getInt();
                        i = i < Config.maxValue.get(prop.getName()) ? i + 1 : 0;
                        prop.set(i);
                    }
                    par1GuiButton.displayString = prop.getName() + ": " + prop.getString();
                }
            }
        }

        @Override
        public void drawScreen(int par1, int par2, float par3) {
            this.drawDefaultBackground();
            this.drawCenteredString(this.fontRendererObj, "BambooMod Settings", this.width / 2, 20, 0xFFFFFF);
            super.drawScreen(par1, par2, par3);
        }
    }

    private Minecraft minecraft;

    @Override
    public void initialize(Minecraft minecraftInstance) {
        this.minecraft = minecraftInstance;
    }

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return BambooConfigGuiScreen.class;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
        return null;
    }

}
