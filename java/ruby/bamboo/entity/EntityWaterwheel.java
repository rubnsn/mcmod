package ruby.bamboo.entity;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import ruby.bamboo.BambooInit;

public class EntityWaterwheel extends EntityMill {
    private static final byte IS_MIRROR_ROTE = 17;
    private static final byte SIZE = 18;

    public EntityWaterwheel(World par1World) {
        super(par1World);
        // setSize(3.5F,3F);
    }

    @Override
    public boolean handleWaterMovement() {
        if (this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0D, -0.4000000059604645D, 0.0D).contract(0.001D, 0.001D, 0.001D), Material.water, this)) {
            this.inWater = true;
        } else {
            this.inWater = false;
        }

        return this.inWater;
    }

    @Override
    public boolean interactFirst(EntityPlayer par1EntityPlayer) {
        ItemStack useItem = par1EntityPlayer.getCurrentEquippedItem();
        if (useItem != null) {
            if (useItem.getItem() == BambooInit.itembamboo) {
                dataWatcher.updateObject(IS_MIRROR_ROTE, (byte) (dataWatcher.getWatchableObjectByte(IS_MIRROR_ROTE) == 0 ? 1 : 0));
            }

            if (useItem.getItem() == BambooInit.tudura) {
                if (par1EntityPlayer.isSneaking()) {
                    if (dataWatcher.getWatchableObjectByte(SIZE) > 1) {
                        dataWatcher.updateObject(SIZE, (byte) (dataWatcher.getWatchableObjectByte(SIZE) - 1));
                    }
                } else {
                    if (dataWatcher.getWatchableObjectByte(SIZE) < 2) {
                        dataWatcher.updateObject(SIZE, (byte) (dataWatcher.getWatchableObjectByte(SIZE) + 1));
                    }

                }
            }
        }

        return false;
    }

    @Override
    public void setBounds(double par1, double par3, double par5) {
        if (getDir() == 0 || getDir() == 2) {
            boundingBox.setBounds(this.posX - (1.5 * getSize()), this.posY - (2 * getSize()), this.posZ, this.posX + (2.5 * getSize()), this.posY + (2 * getSize()), this.posZ + (0.5 * getSize()));
        } else if (getDir() == 1 || getDir() == 3) {
            boundingBox.setBounds(this.posX - (1.5 * getSize()), this.posY - (2 * getSize()), this.posZ - (1.5 * getSize()), this.posX + (0.5 * getSize()), this.posY + (1.5 * getSize()), this.posZ + (2.5 * getSize()));
        }
    }

    public byte getSize() {
        return dataWatcher != null ? dataWatcher.getWatchableObjectByte(SIZE) : 1;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.inWater) {
            if (dataWatcher.getWatchableObjectByte(IS_MIRROR_ROTE) == 0) {
                roll = roll < 360 ? ++roll : 0;
            } else {
                roll = 0 < roll ? --roll : 360;
            }
        }
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(BambooInit.waterWheel, 1, 0);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataWatcher.addObject(IS_MIRROR_ROTE, (byte) 0);
        dataWatcher.addObject(SIZE, (byte) 1);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound var1) {
        super.readEntityFromNBT(var1);
        if (var1.hasKey("mirrorRoll")) {
            dataWatcher.updateObject(IS_MIRROR_ROTE, var1.getBoolean("mirrorRoll") ? (byte) 1 : (byte) 0);
        }
        if (var1.hasKey("wheelSize")) {
            dataWatcher.updateObject(SIZE, var1.getByte("wheelSize"));
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound var1) {
        super.writeEntityToNBT(var1);
        var1.setBoolean("mirrorRoll", dataWatcher.getWatchableObjectByte(IS_MIRROR_ROTE) != (byte) 0);
        var1.setByte("wheelSize", dataWatcher.getWatchableObjectByte(SIZE));
    }
}
