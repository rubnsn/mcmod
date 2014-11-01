package mmm.littleMaidMob;

import mmm.littleMaidMob.entity.EntityLittleMaidBase;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class TileContainer implements IExtendedEntityProperties {
    private EntityLittleMaidBase maid;
    private int[] maidTile = new int[3];
    private int maidTiles[][] = new int[9][3];
    private TileEntity maidTileEntity;

    public TileContainer(EntityLittleMaidBase entity) {
        this.maid = entity;
    }

    public int getX() {
        return this.maidTile[0];
    }

    public int getY() {
        return this.maidTile[0];
    }

    public int getZ() {
        return this.maidTile[0];
    }

    public int getSize() {
        return maidTiles.length;
    }

    public boolean isUsingTile(TileEntity pTile) {
        if (this.maid.modeController.isActiveModeClass()) {
            return this.maid.modeController.getActiveModeClass().isUsingTile(pTile);
        }
        for (int li = 0; li < maidTiles.length; li++) {
            if (maidTiles[li] != null && pTile.xCoord == maidTiles[li][0] && pTile.yCoord == maidTiles[li][1] && pTile.zCoord == maidTiles[li][2]) {
                return true;
            }
        }
        return false;
    }

    public boolean isEqualTile() {
        return this.maid.worldObj.getTileEntity(maidTile[0], maidTile[1], maidTile[2]) == maidTileEntity;
    }

    public boolean isTilePos() {
        return maidTileEntity != null;
    }

    public boolean isTilePos(int pIndex) {
        if (pIndex < maidTiles.length) {
            return maidTiles[pIndex] != null;
        }
        return false;
    }

    /**
     * ローカル変数にTileの位置を入れる。
     */
    public boolean getTilePos(int pIndex) {
        if (pIndex < maidTiles.length && maidTiles[pIndex] != null) {
            maidTile[0] = maidTiles[pIndex][0];
            maidTile[1] = maidTiles[pIndex][1];
            maidTile[2] = maidTiles[pIndex][2];
            return true;
        }
        return false;
    }

    public void setTilePos(int pX, int pY, int pZ) {
        maidTile[0] = pX;
        maidTile[1] = pY;
        maidTile[2] = pZ;
    }

    public void setTilePos(TileEntity pEntity) {
        maidTile[0] = pEntity.xCoord;
        maidTile[1] = pEntity.yCoord;
        maidTile[2] = pEntity.zCoord;
        maidTileEntity = pEntity;
    }

    public void setTilePos(int pIndex) {
        if (pIndex < maidTiles.length) {
            if (maidTiles[pIndex] == null) {
                maidTiles[pIndex] = new int[3];
            }
            maidTiles[pIndex][0] = maidTile[0];
            maidTiles[pIndex][1] = maidTile[1];
            maidTiles[pIndex][2] = maidTile[2];
        }
    }

    public void setTilePos(int pIndex, int pX, int pY, int pZ) {
        if (pIndex < maidTiles.length) {
            if (maidTiles[pIndex] == null) {
                maidTiles[pIndex] = new int[3];
            }
            maidTiles[pIndex][0] = pX;
            maidTiles[pIndex][1] = pY;
            maidTiles[pIndex][2] = pZ;
        }
    }

    public TileEntity getTileEntity() {
        return maidTileEntity = this.maid.worldObj.getTileEntity(maidTile[0], maidTile[1], maidTile[2]);
    }

    public TileEntity getTileEntity(int pIndex) {
        if (pIndex < maidTiles.length && maidTiles[pIndex] != null) {
            TileEntity ltile = this.maid.worldObj.getTileEntity(maidTiles[pIndex][0], maidTiles[pIndex][1], maidTiles[pIndex][2]);
            if (ltile == null) {
                clearTilePos(pIndex);
            }
            return ltile;
        }
        return null;
    }

    public void clearTilePos() {
        maidTileEntity = null;
    }

    public void clearTilePos(int pIndex) {
        if (pIndex < maidTiles.length) {
            maidTiles[pIndex] = null;
        }
    }

    public void clearTilePosAll() {
        for (int li = 0; li < maidTiles.length; li++) {
            maidTiles[li] = null;
        }
    }

    public double getDistanceTilePos() {
        return this.maid.getDistance((double) maidTile[0] + 0.5D, (double) maidTile[1] + 0.5D, (double) maidTile[2] + 0.5D);
    }

    public double getDistanceTilePosSq() {
        return this.maid.getDistanceSq((double) maidTile[0] + 0.5D, (double) maidTile[1] + 0.5D, (double) maidTile[2] + 0.5D);
    }

    public double getDistanceTilePos(int pIndex) {
        if (maidTiles.length > pIndex && maidTiles[pIndex] != null) {
            return this.maid.getDistance((double) maidTiles[pIndex][0] + 0.5D, (double) maidTiles[pIndex][1] + 0.5D, (double) maidTiles[pIndex][2] + 0.5D);
        }
        return -1D;
    }

    public double getDistanceTilePosSq(int pIndex) {
        if (maidTiles.length > pIndex && maidTiles[pIndex] != null) {
            return this.maid.getDistanceSq((double) maidTiles[pIndex][0] + 0.5D, (double) maidTiles[pIndex][1] + 0.5D, (double) maidTiles[pIndex][2] + 0.5D);
        }
        return -1D;
    }

    public double getDistanceTilePos(TileEntity pTile) {
        if (pTile != null) {
            return this.maid.getDistance((double) pTile.xCoord + 0.5D, (double) pTile.yCoord + 0.5D, (double) pTile.zCoord + 0.5D);
        }
        return -1D;
    }

    public double getDistanceTilePosSq(TileEntity pTile) {
        if (pTile != null) {
            return this.maid.getDistanceSq((double) pTile.xCoord + 0.5D, (double) pTile.yCoord + 0.5D, (double) pTile.zCoord + 0.5D);
        }
        return -1D;
    }

    public void looksTilePos() {
        this.maid.getLookHelper().setLookPosition(maidTile[0] + 0.5D, maidTile[1] + 0.5D, maidTile[2] + 0.5D, 10F, this.maid.getVerticalFaceSpeed());
    }

    public void looksTilePos(int pIndex) {
        if (maidTiles.length > pIndex && maidTiles[pIndex] != null) {
            this.maid.getLookHelper().setLookPosition(maidTiles[pIndex][0] + 0.5D, maidTiles[pIndex][1] + 0.5D, maidTiles[pIndex][2] + 0.5D, 10F, this.maid.getVerticalFaceSpeed());
        }
    }

    public int[] getPositon() {
        return this.maidTile;
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTTagCompound lnbt = new NBTTagCompound();
        for (int li = 0; li < maidTiles.length; li++) {
            if (maidTiles[li] != null) {
                lnbt.setIntArray(String.valueOf(li), maidTiles[li]);
            }
        }
        compound.setTag("Tiles", lnbt);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        if (compound.hasKey("Tiles")) {
            NBTTagCompound lnbt = compound.getCompoundTag("Tiles");
            for (int li = 0; li < maidTiles.length; li++) {
                int ltile[] = lnbt.getIntArray(String.valueOf(li));
                maidTiles[li] = ltile.length > 0 ? ltile : null;
            }
        }
    }

    @Override
    public void init(Entity entity, World world) {
    }
}
