package ruby.bamboo.entity;

import ruby.bamboo.BambooInit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityWindmill extends EntityMill {
    private final static int BLADE = 17;
    private final static int TEXNUM = 18;
    private final static int SIZE = 19;

    public EntityWindmill(World par1World) {
        super(par1World);
    }

    @Override
    public boolean interactFirst(EntityPlayer par1EntityPlayer) {
        ItemStack useItem = par1EntityPlayer.getCurrentEquippedItem();

        if (useItem != null) {
            if (par1EntityPlayer.isSneaking()) {
                if (useItem.getItem().itemID == BambooInit.bambooIID) {
                    if (dataWatcher.getWatchableObjectByte(BLADE) > 0) {
                        dataWatcher.updateObject(BLADE, (byte) (dataWatcher.getWatchableObjectByte(BLADE) - 1));
                    }
                } else if (useItem.getItem().itemID == BambooInit.tuduraIID) {
                    if (dataWatcher.getWatchableObjectByte(SIZE) > 1) {
                        dataWatcher.updateObject(SIZE, (byte) (dataWatcher.getWatchableObjectByte(SIZE) - 1));
                    }
                }
            } else {
                if (useItem.getItem().itemID == BambooInit.bambooIID) {
                    if (dataWatcher.getWatchableObjectByte(BLADE) < 4) {
                        dataWatcher.updateObject(BLADE, (byte) (dataWatcher.getWatchableObjectByte(BLADE) + 1));
                    }
                } else if (useItem.getItem().itemID == BambooInit.tuduraIID) {
                    if (dataWatcher.getWatchableObjectByte(SIZE) < 5) {
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
            boundingBox.setBounds(this.posX - (1.5 * getSize()), this.posY - (1.5 * getSize()), this.posZ, this.posX + (2.5 * getSize()), this.posY + (2 * getSize()), this.posZ + (0.5 * getSize()));
        } else if (getDir() == 1 || getDir() == 3) {
            boundingBox.setBounds(this.posX, this.posY - (1.5 * getSize()), this.posZ - (1.5 * getSize()), this.posX + (0.5 * getSize()), this.posY + (1.5 * getSize()), this.posZ + (2.5 * getSize()));
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if ((roll += 1) > 360) {
            roll = 0;
        }
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(BambooInit.windmillIID, 1, getTexNum());
    }

    // render呼び出し
    public int getBladeNum() {
        return 4 + (dataWatcher.getIsBlank() ? 0 : dataWatcher.getWatchableObjectByte(BLADE));
    }

    public byte getTexNum() {
        return dataWatcher.getIsBlank() ? 0 : dataWatcher.getWatchableObjectByte(TEXNUM);
    }

    public byte getSize() {
        return dataWatcher.getIsBlank() ? 0 : dataWatcher.getWatchableObjectByte(SIZE);
    }

    public void setBlade(byte i) {
        dataWatcher.updateObject(BLADE, i);
    }

    public void setTexNum(byte i) {
        dataWatcher.updateObject(TEXNUM, i);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataWatcher.addObject(BLADE, (byte) 0);
        dataWatcher.addObject(TEXNUM, (byte) 0);
        dataWatcher.addObject(SIZE, (byte) 1);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound var1) {
        super.readEntityFromNBT(var1);
        dataWatcher.updateObject(BLADE, var1.getByte("blade"));
        dataWatcher.updateObject(TEXNUM, var1.getByte("texnum"));

        if (var1.hasKey("size")) {
            dataWatcher.updateObject(SIZE, var1.getByte("size"));
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound var1) {
        super.writeEntityToNBT(var1);
        var1.setByte("blade", dataWatcher.getWatchableObjectByte(BLADE));
        var1.setByte("texnum", dataWatcher.getWatchableObjectByte(TEXNUM));
        var1.setByte("size", dataWatcher.getWatchableObjectByte(SIZE));
    }
}
