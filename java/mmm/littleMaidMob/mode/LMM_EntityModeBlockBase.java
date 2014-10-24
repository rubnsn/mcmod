package net.minecraft.src;

import net.minecraft.tileentity.TileEntity;

public abstract class LMM_EntityModeBlockBase extends LMM_EntityModeBase {

//	protected TileEntity fTile;
	protected double fDistance;


	public LMM_EntityModeBlockBase(LMM_EntityLittleMaid pEntity) {
		super(pEntity);
	}

	@Override
	public void updateBlock() {
		// ��ƂȂ�Tile���Z�b�g
		owner.setTilePos(0);
	}

	/**
	 * ���łɎg�p����Tile������ꍇ��shouldBlock�֔�Ԃ悤�ɂ���B
	 */
	@Override
	public boolean isSearchBlock() {
		boolean lflag = false;
		for (int li = 0; li < owner.maidTiles.length; li++) {
			if (owner.maidTiles[li] != null) {
				TileEntity ltile = owner.getTileEntity(li);
				if (ltile != null && !checkWorldMaid(ltile)) {
					if (!lflag) {
						owner.setTilePos(ltile);
					}
					lflag = true;
				}
			}
		}
		return !lflag;
	}

	@Override
	public boolean overlooksBlock(int pMode) {
		if (owner.isTilePos()) {
			owner.setTilePos(0);
		}
		return true;
	}


	/**
	 * ���̃��C�h���g�p���Ă��邩���`�F�b�N�B
	 * @return
	 */
	protected boolean checkWorldMaid(TileEntity pTile) {
		// ���E�̃��C�h����
		for (Object lo : owner.worldObj.loadedEntityList) {
			if (lo == owner) continue;
			if (lo instanceof LMM_EntityLittleMaid) {
				LMM_EntityLittleMaid lem = (LMM_EntityLittleMaid)lo;
				if (lem.isUsingTile(pTile)) {
					// �N�����g�p��
					return true;
				}
			}
		}
		return false;
	}

}
