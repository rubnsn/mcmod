package ruby.bamboo.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import ruby.bamboo.BambooInit;

public class EntityZabuton extends Entity implements IZabuton {
    private final static byte COLOR = 16;

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
        LIME(0x8d9734),
        YELLOW(0xd8c90e),
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

        public int getColor() {
            return (red << 16) + (green << 8) + blue;
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

    public EntityZabuton(World par1World, int color) {
        this(par1World);
        dataWatcher.updateObject(COLOR, (byte) color);
    }

    @Override
    public byte getColor() {
        return dataWatcher.getWatchableObjectByte(COLOR);
    }

    @Override
    public void setDead() {
        if (this.riddenByEntity != null) {
            this.riddenByEntity.mountEntity(null);
        }
        super.setDead();
    }

    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
        // server用
        if (!isDead && !this.worldObj.isRemote) {
            EntityPlayer entityplayer = null;

            if (par1DamageSource.damageType == "player") {
                entityplayer = (EntityPlayer) par1DamageSource.getEntity();
                setDead();
            } else {
                return false;
            }

            if (entityplayer != null && entityplayer.capabilities.isCreativeMode) {
                return true;
            }

            this.entityDropItem(new ItemStack(BambooInit.zabuton, 1, (int) getColor()), 0.5F);
        }

        return true;
    }

    @Override
    public boolean interactFirst(EntityPlayer entityPlayer) {
        if (!worldObj.isRemote && !entityPlayer.isSneaking()) {
            if (this.riddenByEntity == null) {
                entityPlayer.mountEntity(this);
            } else {
                entityPlayer.mountEntity(this);
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return !isDead;
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return boundingBox;
    }

    @Override
    public boolean isBurning() {
        return false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        if (Math.abs(this.motionY) < 0.005D) {
            this.motionY = 0.0D;
        }

        if (this.worldObj.isRemote && (!this.worldObj.blockExists((int) this.posX, 0, (int) this.posZ) || !this.worldObj.getChunkFromBlockCoords((int) this.posX, (int) this.posZ).isChunkLoaded)) {
            if (this.posY > 0.0D) {
                this.motionY = -0.1D;
            } else {
                this.motionY = 0.0D;
            }
        } else {
            this.motionY -= 0.08D;
        }

        this.motionY *= 0.9800000190734863D;
        this.motionX *= 0.91;
        this.motionZ *= 0.91;
    }

    @Override
    protected void entityInit() {
        dataWatcher.addObject(COLOR, (byte) 15);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound var1) {
        dataWatcher.updateObject(COLOR, var1.getByte("color"));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound var1) {
        var1.setByte("color", getColor());
    }

}
