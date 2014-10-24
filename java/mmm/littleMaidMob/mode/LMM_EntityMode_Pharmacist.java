package net.minecraft.src;

import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBrewingStand;

public class LMM_EntityMode_Pharmacist extends LMM_EntityModeBlockBase {

	public static final int mmode_Pharmacist = 0x0022;

	protected int inventryPos;


	public LMM_EntityMode_Pharmacist(LMM_EntityLittleMaid pEntity) {
		super(pEntity);
	}

	@Override
	public int priority() {
		return 6100;
	}

	@Override
	public void init() {
		ModLoader.addLocalization("littleMaidMob.mode.Pharmacist", "Pharmacist");
		ModLoader.addLocalization("littleMaidMob.mode.T-Pharmacist", "T-Pharmacist");
		ModLoader.addLocalization("littleMaidMob.mode.F-Pharmacist", "F-Pharmacist");
		ModLoader.addLocalization("littleMaidMob.mode.F-Pharmacist", "D-Pharmacist");
	}

	@Override
	public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
		// Pharmacist:0x0022
		EntityAITasks[] ltasks = new EntityAITasks[2];
		ltasks[0] = pDefaultMove;
		ltasks[1] = pDefaultTargeting;
		
		owner.addMaidMode(ltasks, "Pharmacist", mmode_Pharmacist);
	}

	@Override
	public boolean changeMode(EntityPlayer pentityplayer) {
		ItemStack litemstack = owner.maidInventory.getStackInSlot(0);
		if (litemstack != null) {
			if (litemstack.getItem() instanceof ItemPotion && !MMM_Helper.hasEffect(litemstack)) {
				owner.setMaidMode("Pharmacist");
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean setMode(int pMode) {
		switch (pMode) {
		case mmode_Pharmacist :
			owner.setBloodsuck(false);
			owner.aiJumpTo.setEnable(false);
			owner.aiFollow.setEnable(false);
			owner.aiAttack.setEnable(false);
			owner.aiShooting.setEnable(false);
			inventryPos = 0;
			return true;
		}
		
		return false;
	}

	@Override
	public int getNextEquipItem(int pMode) {
		int li;
		ItemStack litemstack;
		
		// ���[�h�ɉ��������ʔ���A���x�D��
		switch (pMode) {
		case mmode_Pharmacist :
			litemstack = owner.getCurrentEquippedItem();
			if (!(inventryPos > 0 && litemstack != null && !litemstack.getItem().isPotionIngredient())) {
				for (li = 0; li < owner.maidInventory.maxInventorySize; li++) {
					litemstack = owner.maidInventory.getStackInSlot(li);
					if (litemstack != null) {
						// �Ώۂ͐��|�[�V����
						if (litemstack.getItem() instanceof ItemPotion && !MMM_Helper.hasEffect(litemstack)) {
							return li;
						}
					}
				}
			}
			break;
		}
		
		return -1;
	}

	@Override
	public boolean checkItemStack(ItemStack pItemStack) {
		return false;
	}

	@Override
	public boolean isSearchBlock() {
		if (!super.isSearchBlock()) return false;
		
		if (owner.getCurrentEquippedItem() != null) {
			fDistance = Double.MAX_VALUE;
			owner.clearTilePos();
			owner.setSneaking(false);
			return true;
		}
		return false;
	}

	@Override
	public boolean shouldBlock(int pMode) {
		// ���s������
		return owner.maidTileEntity instanceof TileEntityBrewingStand &&
				(((TileEntityBrewingStand)owner.maidTileEntity).getBrewTime() > 0 ||
						(owner.getCurrentEquippedItem() != null) || inventryPos > 0);
	}

	@Override
	public boolean checkBlock(int pMode, int px, int py, int pz) {
		if (owner.getCurrentEquippedItem() == null) {
			return false;
		}
		TileEntity ltile = owner.worldObj.getBlockTileEntity(px, py, pz);
		if (!(ltile instanceof TileEntityBrewingStand)) {
			return false;
		}
		
		// ���E�̃��C�h����
		checkWorldMaid(ltile);
		// �g�p���Ă���������Ȃ炻���ŏI��
		if (owner.isUsingTile(ltile)) return true;
		
		double ldis = owner.getDistanceTilePosSq(ltile);
		if (fDistance > ldis) {
			owner.setTilePos(ltile);
			fDistance = ldis;
		}
		
		return false;
	}

	@Override
	public boolean executeBlock(int pMode, int px, int py, int pz) {
		TileEntityBrewingStand ltile = (TileEntityBrewingStand)owner.maidTileEntity;
		if (owner.worldObj.getBlockTileEntity(px, py, pz) != ltile) {
			return false;
		}		
		
		ItemStack litemstack1;
		boolean lflag = false;
		LMM_SwingStatus lswing = owner.getSwingStatusDominant();
		
		// �����ҋ@
//    	isMaidChaseWait = true;
		if (ltile.getStackInSlot(0) != null || ltile.getStackInSlot(1) != null || ltile.getStackInSlot(2) != null || ltile.getStackInSlot(3) != null || !lswing.canAttack()) {
			// ���d����
			owner.setWorking(true);
		}
		
		if (lswing.canAttack()) {
			ItemStack litemstack2 = ltile.getStackInSlot(3);
			
			if (litemstack2 != null && ltile.getBrewTime() <= 0) {
				// �����s�\�Ȃ̂ŉ��
				if (py <= owner.posY) {
					owner.setSneaking(true);
				}
				lflag = true;
				if (owner.maidInventory.addItemStackToInventory(litemstack2)) {
					ltile.setInventorySlotContents(3, null);
					owner.playSound("random.pop");
					owner.setSwing(5, LMM_EnumSound.Null);
				}
			}
			// �����i
			if (!lflag && inventryPos > owner.maidInventory.mainInventory.length) {
				// �|�[�V�����̉��
				for (int li = 0; li < 3 && !lflag; li ++) {
					litemstack1 = ltile.getStackInSlot(li);
					if (litemstack1 != null && owner.maidInventory.addItemStackToInventory(litemstack1)) {
						ltile.setInventorySlotContents(li, null);
						owner.playSound("random.pop");
						owner.setSwing(5, LMM_EnumSound.Null);
						lflag = true;
					}
				}
				if (!lflag) {
					inventryPos = 0;
					owner.getNextEquipItem();
					lflag = true;
				}
			}
			
			litemstack1 = owner.maidInventory.getCurrentItem();
			if (!lflag && (litemstack1 != null && litemstack1.getItem() instanceof ItemPotion && !MMM_Helper.hasEffect(litemstack1))) {
				// ���r�������Ƃ�ł�
				int li = 0;
				for (li = 0; li < 3 && !lflag; li++) {
					if (ltile.getStackInSlot(li) == null) {
						ltile.setInventorySlotContents(li, litemstack1);
						owner.maidInventory.setInventoryCurrentSlotContents(null);
						owner.playSound("random.pop");
						owner.setSwing(5, LMM_EnumSound.Null);
						owner.getNextEquipItem();
						lflag = true;
					}
				}
			}
			if (!lflag && (ltile.getStackInSlot(0) != null || ltile.getStackInSlot(1) != null || ltile.getStackInSlot(2) != null)
					&& (owner.maidInventory.currentItem == -1 || (litemstack1 != null && litemstack1.getItem() instanceof ItemPotion && !MMM_Helper.hasEffect(litemstack1)))) {
				// �|�[�V�����ȊO������
//				for (inventryPos = 0; inventryPos < owner.maidInventory.mainInventory.length; inventryPos++) {
				for (; inventryPos < owner.maidInventory.mainInventory.length; inventryPos++) {
					litemstack1 = owner.maidInventory.getStackInSlot(inventryPos);
					if (litemstack1 != null && !(litemstack1.getItem() instanceof ItemPotion)) {
						owner.setEquipItem(inventryPos);
						lflag = true;
						break;
					}
				}
			}
			
			if (!lflag && litemstack2 == null && (ltile.getStackInSlot(0) != null || ltile.getStackInSlot(1) != null || ltile.getStackInSlot(2) != null)) {
				// �莝���̃A�C�e�����ہ[��
				if (litemstack1 != null && !(litemstack1.getItem() instanceof ItemPotion) && litemstack1.getItem().isPotionIngredient()) {
					ltile.setInventorySlotContents(3, litemstack1);
					owner.maidInventory.setInventorySlotContents(inventryPos, null);
					owner.playSound("random.pop");
					owner.setSwing(15, LMM_EnumSound.Null);
					lflag = true;
				} 
				else if (litemstack1 == null || (litemstack1.getItem() instanceof ItemPotion && MMM_Helper.hasEffect(litemstack1)) || !litemstack1.getItem().isPotionIngredient()) {
					// �ΏۊO�A�C�e���𔭌��������ɏI��
					inventryPos = owner.maidInventory.mainInventory.length;
					lflag = true;
				}
				inventryPos++;
//				owner.maidInventory.currentItem = maidSearchCount;
				owner.setEquipItem(inventryPos);
			}
			
			
			// �I����Ԃ̃L�����Z��
			if (owner.getSwingStatusDominant().index == -1 && litemstack2 == null) {
				owner.getNextEquipItem();
			}
		} else {
			lflag = true;
		}
		if (ltile.getBrewTime() > 0 || inventryPos > 0) {
			owner.setWorking(true);
			lflag = true;
		}
		return lflag;
	}

	@Override
	public void startBlock(int pMode) {
		inventryPos = 0;
	}

	@Override
	public void resetBlock(int pMode) {
		owner.setSneaking(false);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) {
		inventryPos = par1nbtTagCompound.getInteger("InventryPos");
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound) {
		par1nbtTagCompound.setInteger("InventryPos", inventryPos);
	}

}
