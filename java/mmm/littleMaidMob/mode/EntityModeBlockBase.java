package mmm.littleMaidMob.mode;

import mmm.littleMaidMob.entity.EntityLittleMaidBase;
import net.minecraft.tileentity.TileEntity;

public abstract class EntityModeBlockBase extends EntityModeBase {

    //  protected TileEntity fTile;
    protected double fDistance;

    public EntityModeBlockBase(ModeController pEntity) {
        super(pEntity);
    }

    @Override
    public void updateBlock() {
        // 基準となるTileをセット
        getOwner().getTileContainer().setTilePos(0);
    }

    /**
     * すでに使用中のTileがある場合はshouldBlockへ飛ぶようにする。
     */
    @Override
    public boolean isSearchBlock() {
        boolean lflag = false;
        /*for (int li = 0; li < getOwner().maidTiles.length; li++) {
            if (getOwner().maidTiles[li] != null) {
                TileEntity ltile = getOwner().getTileEntity(li);
                if (ltile != null && !checkWorldMaid(ltile)) {
                    if (!lflag) {
                        getOwner().setTilePos(ltile);
                    }
                    lflag = true;
                }
            }
        }*/
        return !lflag;
    }

    @Override
    public boolean overlooksBlock(int pMode) {
        if (getOwner().getTileContainer().isTilePos()) {
            getOwner().getTileContainer().setTilePos(0);
        }
        return true;
    }

    /**
     * 他のメイドが使用しているかをチェック。
     * 
     * @return
     */
    protected boolean checkWorldMaid(TileEntity pTile) {
        // 世界のメイドから
        for (Object lo : getOwner().worldObj.loadedEntityList) {
            if (lo == getOwner())
                continue;
            if (lo instanceof EntityLittleMaidBase) {
                EntityLittleMaidBase lem = (EntityLittleMaidBase) lo;
                if (lem.getTileContainer().isUsingTile(pTile)) {
                    // 誰かが使用中
                    return true;
                }
            }
        }
        return false;
    }

}
