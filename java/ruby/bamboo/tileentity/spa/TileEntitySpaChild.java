package ruby.bamboo.tileentity.spa;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class TileEntitySpaChild extends TileEntity implements ITileEntitySpa {
    private int parentX, parentY, parentZ;
    private boolean isScheduled = false;
    private int lastTickMeta;

    @Override
    public void addColor(int deycolor) {
        if (this.isParentExist()) {
            this.getParent().addColor(deycolor);
        }
    }

    @Override
    public int getColor() {
        if (this.isParentExist()) {
            return this.getParent().getColor();
        }
        return 0xFFFFFF;
    }

    @Override
    public void colorUpdate() {
        if (this.isParentExist()) {
            this.getParent().colorUpdate();
        }
    }

    @Override
    public boolean isStay() {
        if (this.isParentExist()) {
            return this.getParent().isStay();
        }
        return false;
    }

    public TileEntitySpaChild setParentPosition(int[] position) {
        this.parentX = position[0];
        this.parentY = position[1];
        this.parentZ = position[2];
        this.worldObj.markTileEntityChunkModified(this.xCoord, this.yCoord, this.zCoord, this);
        return this;
    }

    @Override
    public int[] getParentPosition() {
        return new int[] { parentX, parentY, parentZ };
    }

    private boolean isParentExist() {
        TileEntity tileEntity = worldObj.getBlockTileEntity(parentX, parentY, parentZ);
        if (tileEntity != null && tileEntity instanceof ITileEntitySpa) {
            return true;
        }
        return false;
    }

    private ITileEntitySpa getParent() {
        TileEntity tileEntity = worldObj.getBlockTileEntity(parentX, parentY, parentZ);
        return (ITileEntitySpa) tileEntity;
    }

    @Override
    public void onEntityLivingCollision(EntityLivingBase entity) {
        if (this.isParentExist()) {
            this.getParent().onEntityLivingCollision(entity);
        }
    }

    @Override
    public void onEntityItemCollision(EntityItem entity) {
        if (this.isParentExist()) {
            this.getParent().onEntityItemCollision(entity);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("parentX", parentX);
        nbttagcompound.setInteger("parentY", parentY);
        nbttagcompound.setInteger("parentZ", parentZ);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        parentX = nbttagcompound.getInteger("parentX");
        parentY = nbttagcompound.getInteger("parentY");
        parentZ = nbttagcompound.getInteger("parentZ");
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound var1 = new NBTTagCompound();
        this.writeToNBT(var1);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, var1);
    }

    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
        this.readFromNBT(pkt.data);
    }

    @Override
    public boolean canUpdate() {
        return false;
    }

    @Override
    public boolean isTickScheduled() {
        return isScheduled;
    }

    @Override
    public void setTickSchedule(boolean flg) {
        isScheduled = flg;
    }

    @Override
    public int getLastTickMeta() {
        return lastTickMeta;
    }

    @Override
    public void setLastTickMeta(int meta) {
        this.lastTickMeta = meta;
    }

}
