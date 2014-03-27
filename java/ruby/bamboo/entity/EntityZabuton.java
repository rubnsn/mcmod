package ruby.bamboo.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityZabuton extends Entity {
    private final static byte PATTERN = 16;

    public enum EnumZabutonColor {
        BLACK(0x312935),
        RED(0xa61920),
        GREEN(0x669259),
        CACAO(0x6b4b41),
        BLUE(0x2a405d),
        PURPLE(0x534362),
        CYAN(0x77b7b7),
        LIGHT_GRAY(0x8b8b99),
        GRAY(0x3f3f46),
        PINK(0xe18f8f),
        LIME(0xb8c826),
        YELLOW(0xb4a700),
        LIGHT_BLUE(0x17728d),
        MAGENTA(0xa15275),
        ORANGE(0xc8870e),
        WHITE(0xffffff);
        EnumZabutonColor(int color) {
            this.red = color >> 16;
            this.green = (color >> 8) & 0xFF;
            this.blue = color & 0xFF;
        }

        public static final EnumZabutonColor[] COLORS = { BLACK, RED, GREEN, CACAO, BLUE, PURPLE, CYAN, LIGHT_GRAY, GRAY, PINK, LIME, YELLOW, LIGHT_BLUE, MAGENTA, ORANGE, WHITE };

        private int red, green, blue;

        public static EnumZabutonColor getColor(int meta) {
            return COLORS[meta < 16 ? meta : 0];
        }

        public int getRed() {
            return red;
        }

        public int getGreen() {
            return green;
        }

        public int getBlue() {
            return blue;
        }

    }

    public EntityZabuton(World par1World) {
        super(par1World);
        setSize(1F, 2 / 16F);
    }

    public int getPatteran() {
        return dataWatcher.getWatchableObjectByte(PATTERN);
    }

    @Override
    public void onUpdate() {
        //this.setDead();
        dataWatcher.updateObject(PATTERN, (byte) 2);
        this.onEntityUpdate();
    }

    @Override
    protected void entityInit() {
        dataWatcher.addObject(PATTERN, (byte) 0);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound var1) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound var1) {

    }

}
