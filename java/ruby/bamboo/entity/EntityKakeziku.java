package ruby.bamboo.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import ruby.bamboo.BambooInit;

public class EntityKakeziku extends Entity {
    private int tickCounter1;
    /** the direction the painting faces */
    public int direction;
    public int xPosition;
    public int yPosition;
    public int zPosition;
    public EnumKakeziku art;

    public EntityKakeziku(World par1World) {
        super(par1World);
        this.tickCounter1 = 0;
        this.direction = 0;
        this.yOffset = 0.0F;
        this.setSize(1F, 2F);
        dataWatcher.addObject(17, (byte) direction);
        dataWatcher.addObject(18, "");
    }

    public EntityKakeziku(World par1World, int par2, int par3, int par4, int par5) {
        this(par1World);
        this.xPosition = par2;
        this.yPosition = par3 - 1;
        this.zPosition = par4;
        ArrayList var6 = new ArrayList();
        EnumKakeziku[] var7 = EnumKakeziku.values();
        int var8 = var7.length;

        for (int var9 = 0; var9 < var8; ++var9) {
            EnumKakeziku var10 = var7[var9];
            this.art = var10;
            this.setDirection(par5);

            if (this.onValidSurface()) {
                var6.add(var10);
            }
        }

        if (!var6.isEmpty()) {
            this.art = (EnumKakeziku) var6.get(this.rand.nextInt(var6.size()));
        }

        this.setDirection(par5);
        dataWatcher.updateObject(17, (byte) direction);
        dataWatcher.updateObject(18, art.title);
    }

    @Override
    protected void entityInit() {
    }

    public EnumKakeziku getArt() {
        EnumKakeziku art = null;

        for (EnumKakeziku e : EnumKakeziku.values()) {
            if (e.title.equals(dataWatcher.getWatchableObjectString(18))) {
                art = e;
                break;
            }
        }

        if (art == null) {
            setDead();
            art = EnumKakeziku.Tatu;
        }

        return art;
    }

    public byte getDir() {
        byte dir = dataWatcher.getWatchableObjectByte(17);
        this.prevRotationYaw = this.rotationYaw = dir * 90;
        return dir;
    }

    // 1.3.2用少し時間が経つとYが1.5ほど上がるバグの対策
    @Override
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
    }

    public void setDirection(int par1) {
        this.direction = par1;
        this.prevRotationYaw = this.rotationYaw = par1 * 90;
        float var2 = this.art.sizeX;
        float var3 = this.art.sizeY;
        float var4 = this.art.sizeX;

        if (par1 != 0 && par1 != 2) {
            var2 = 0.5F;
        } else {
            var4 = 0.5F;
        }

        var2 /= 32.0F;
        var3 /= 32.0F;
        var4 /= 32.0F;
        float var5 = this.xPosition + 0.5F;
        float var6 = this.yPosition + 0.5F;
        float var7 = this.zPosition + 0.5F;
        float var8 = 0.5625F;

        if (par1 == 0) {
            var7 -= var8;
        }

        if (par1 == 1) {
            var5 -= var8;
        }

        if (par1 == 2) {
            var7 += var8;
        }

        if (par1 == 3) {
            var5 += var8;
        }

        if (par1 == 0) {
            var5 -= this.dirOffset(this.art.sizeX);
        }

        if (par1 == 1) {
            var7 += this.dirOffset(this.art.sizeX);
        }

        if (par1 == 2) {
            var5 += this.dirOffset(this.art.sizeX);
        }

        if (par1 == 3) {
            var7 -= this.dirOffset(this.art.sizeX);
        }

        var6 += this.dirOffset(this.art.sizeY);
        this.setPosition(var5, (double) var6 - 1, var7);
        float var9 = -0.00625F;
        this.setSize(var2, var3 - 0.1F);
        this.boundingBox.setBounds(var5 - var2 - var9, var6 - var3 - var9, var7 - var4 - var9, var5 + var2 + var9, var6 + var3 + var9, var7 + var4 + var9);
    }

    private float dirOffset(int par1) {
        return par1 == 32 ? 0.5F : (par1 == 64 ? 0.5F : 0.0F);
    }

    @Override
    public void onUpdate() {
        /*
         * if (!this.worldObj.isRemote) { if (!this.isDead) { this.setDead();
         * this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj,
         * this.posX, this.posY, this.posZ, new
         * ItemStack(mod_Bamboo.kakeziku))); } }
         */
    }

    public boolean onValidSurface() {
        if (!this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty()) {
            return false;
        } else {
            int var1 = this.art.sizeX / 16;
            int var2 = this.art.sizeY / 16;
            int var3 = this.xPosition;
            int var4 = this.yPosition;
            int var5 = this.zPosition;

            if (getDir() == 0) {
                var3 = MathHelper.floor_double(this.posX - this.art.sizeX / 32.0F);
            }

            if (getDir() == 1) {
                var5 = MathHelper.floor_double(this.posZ - this.art.sizeX / 32.0F);
            }

            if (getDir() == 2) {
                var3 = MathHelper.floor_double(this.posX - this.art.sizeX / 32.0F);
            }

            if (getDir() == 3) {
                var5 = MathHelper.floor_double(this.posZ - this.art.sizeX / 32.0F);
            }

            var4 = MathHelper.floor_double(this.posY - this.art.sizeY / 32.0F) + 1;

            for (int var6 = 0; var6 < var1; ++var6) {
                for (int var7 = 0; var7 < var2; ++var7) {
                    Material var8;

                    if (this.direction != 0 && this.direction != 2) {
                        var8 = this.worldObj.getBlock(this.xPosition, var4 + var7, var5 + var6).getMaterial();
                    } else {
                        var8 = this.worldObj.getBlock(var3 + var6, var4 + var7, this.zPosition).getMaterial();
                    }

                    if (!var8.isSolid()) {
                        return false;
                    }
                }
            }

            List var9 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox);
            Iterator var10 = var9.iterator();
            Entity var11;

            do {
                if (!var10.hasNext()) {
                    return true;
                }

                var11 = (Entity) var10.next();
            } while (!(var11 instanceof EntityKakeziku));

            return false;
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
        if (!this.isDead && !this.worldObj.isRemote) {
            this.setBeenAttacked();
            EntityPlayer var3 = null;

            if (par1DamageSource.damageType == "player") {
                var3 = (EntityPlayer) par1DamageSource.getEntity();
                this.setDead();
            } else {
                return false;
            }

            if (var3 != null && var3.capabilities.isCreativeMode) {
                return true;
            }

            this.dropItem(BambooInit.kakeziku, 1);
        }

        return true;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
        par1NBTTagCompound.setByte("Dir", (byte) this.direction);
        par1NBTTagCompound.setString("Motive", this.art.title);
        par1NBTTagCompound.setInteger("TileX", this.xPosition);
        par1NBTTagCompound.setInteger("TileY", this.yPosition);
        par1NBTTagCompound.setInteger("TileZ", this.zPosition);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
        this.direction = par1NBTTagCompound.getByte("Dir");
        this.xPosition = par1NBTTagCompound.getInteger("TileX");
        this.yPosition = par1NBTTagCompound.getInteger("TileY");
        this.zPosition = par1NBTTagCompound.getInteger("TileZ");
        String var2 = par1NBTTagCompound.getString("Motive");
        EnumKakeziku[] var3 = EnumKakeziku.values();
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            EnumKakeziku var6 = var3[var5];

            if (var6.title.equals(var2)) {
                this.art = var6;
            }
        }

        if (this.art == null) {
            this.art = EnumKakeziku.Tatu;
        }

        this.setDirection(this.direction);
        dataWatcher.updateObject(17, (byte) direction);
        dataWatcher.updateObject(18, var2);
    }

    @Override
    public void moveEntity(double par1, double par3, double par5) {
        if (!this.worldObj.isRemote && !this.isDead && par1 * par1 + par3 * par3 + par5 * par5 > 0.0D) {
            this.setDead();
            this.dropItem(BambooInit.kakeziku, 1);
        }
    }

    @Override
    public void addVelocity(double par1, double par3, double par5) {
        if (!this.worldObj.isRemote && !this.isDead && par1 * par1 + par3 * par3 + par5 * par5 > 0.0D) {
            this.setDead();
            this.dropItem(BambooInit.kakeziku, 1);
        }
    }

    @Override
    public boolean isBurning() {
        return false;
    }
}
