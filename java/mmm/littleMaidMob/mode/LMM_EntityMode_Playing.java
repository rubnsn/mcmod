package net.minecraft.src;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.DamageSource;

public class LMM_EntityMode_Playing extends LMM_EntityModeBase {

	public static final int mmode_Playing	= 0x00ff;

	public static final int mpr_NULL = 0;
	public static final int mpr_QuickShooter = 0x0010;
	public static final int mpr_StockShooter = 0x0020;
	
	public int fcounter;

	public LMM_EntityMode_Playing(LMM_EntityLittleMaid pEntity) {
		super(pEntity);
		fcounter = 0;
	}

	@Override
	public int priority() {
		return 900;
	}

	@Override
	public void init() {
		ModLoader.addLocalization("littleMaidMob.mode.Playing", "Playing");
		// ModLoader.addLocalization("littleMaidMob.mode.T-Playing", "Playing");
		// ModLoader.addLocalization("littleMaidMob.mode.F-Playing", "Playing");
		// ModLoader.addLocalization("littleMaidMob.mode.D-Playing", "Playing");
	}

	@Override
	public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
		// Playing:0x00ff
		EntityAITasks[] ltasks = new EntityAITasks[2];
		ltasks[0] = pDefaultMove;
		ltasks[1] = pDefaultTargeting;
//		ltasks[1] = new EntityAITasks(owner.aiProfiler);
		
//		ltasks[1].addTask(3, new LMM_EntityAIHurtByTarget(owner, true));
//		ltasks[1].addTask(4, new LMM_EntityAINearestAttackableTarget(owner, EntityLiving.class, 16F, 0, true));
		
		owner.addMaidMode(ltasks, "Playing", mmode_Playing);
		
	}

	protected boolean checkSnows(int x, int y, int z) {
		// ��肪�Ⴉ�H
		boolean f = true;
		f &= owner.worldObj.getBlockId(x, y, z) == Block.snow.blockID;
		f &= owner.worldObj.getBlockId(x + 1, y, z) == Block.snow.blockID;
		f &= owner.worldObj.getBlockId(x - 1, y, z) == Block.snow.blockID;
		f &= owner.worldObj.getBlockId(x, y, z + 1) == Block.snow.blockID;
		f &= owner.worldObj.getBlockId(x, y, z - 1) == Block.snow.blockID;
		
		return f;
	}

	protected boolean movePlaying() {
		//
		int x = MathHelper.floor_double(owner.posX);
		int y = MathHelper.floor_double(owner.posY);
		int z = MathHelper.floor_double(owner.posZ);
		PathEntity pe = null;
		
		// CW���Ɍ����̈���L���� 
		loop_search:
			for (int a = 2; a < 18 && pe == null; a += 2) {
				x--;
				z--;
				for (int b = 0; b < a; b++) {
					// N
					for (int c = 0; c < 4; c++) {
						if (checkSnows(x, y, z)) {
							pe = owner.worldObj.getEntityPathToXYZ(owner, x, y - 1, z, 10F, true, false, false, true);
							if (pe != null) {
								break loop_search;
							}
						}
						if (c == 0) x++;
						if (c == 1) z++;
						if (c == 2) x--;
						if (c == 3) z--;
					}
				}
			}
		
		if (pe != null) {
			owner.getNavigator().setPath(pe, 1.0F);
			mod_LMM_littleMaidMob.Debug("Find Snow Area-%d:%d, %d, %d.", owner.entityId, x, y, z);
			return true;
		} else {
			return false;
		}
	}

	protected void playingSnowWar() {
		switch (fcounter) {
		case 0:
			// �L��ʑS��������
			owner.setSitting(false);
			owner.setSneaking(false);
			if (!owner.getNextEquipItem()) {
				owner.setAttackTarget(null);
				
				owner.getNavigator().clearPathEntity();
				fcounter = 1;
			} else if (owner.getAttackTarget() == null) {
				// ���C�h�ƃv���[���[�i�����ʁj���^�[�Q�b�g��
				List<Entity> list = owner.worldObj.getEntitiesWithinAABBExcludingEntity(owner, owner.boundingBox.expand(16D, 4D, 16D));
				for (Entity e : list) {
					if (e != null && (e instanceof EntityPlayer || e instanceof LMM_EntityLittleMaid)) {
						if (owner.rand.nextBoolean()) {
							owner.setAttackTarget((EntityLivingBase)e);
							break;
						}
					}
				}
			}
			break;
		case 1:
			// ��������
			owner.setAttackTarget(null);
			if (owner.getNavigator().noPath()) {
				fcounter = 2;
			}
			break;
		
		case 2:
			// �ጴ��T��
			if (owner.getAttackTarget() == null && owner.getNavigator().noPath()) {
				if (movePlaying()) {
					fcounter = 3;
				} else {
					owner.setPlayingRole(mpr_NULL);
					fcounter = 0;
				}
			} else {
				owner.setAttackTarget(null);
			}
//			isMaidChaseWait = true;
			break;
		case 3:
			// �ጴ�֓���
			if (owner.getNavigator().noPath()) {
				if (checkSnows(
						MathHelper.floor_double(owner.posX),
						MathHelper.floor_double(owner.posY),
						MathHelper.floor_double(owner.posZ))) {
//					owner.isMaidChaseWait = true;
					owner.attackTime = 30;
					if (owner.getPlayingRole() == mpr_QuickShooter) {
						fcounter = 8;
					} else {
						fcounter = 4;
					}
				} else {
					// �Č���
					fcounter = 2;
				}
			}
			break;
		case 4:
		case 5:
		case 6:
		case 7:
			// �����[�h
			if (owner.attackTime <= 0) {
				if (owner.maidInventory.addItemStackToInventory(new ItemStack(Item.snowball))) {
					owner.playSound("random.pop");
					if (owner.getPlayingRole() == mpr_StockShooter) {
						owner.setSwing(5, LMM_EnumSound.collect_snow);
						fcounter = 0;
					} else {
						owner.setSwing(30, LMM_EnumSound.collect_snow);
						fcounter++;
					}
				} else {
					owner.setPlayingRole(mpr_NULL);
					fcounter = 0;
				}
			}
//			owner.isMaidChaseWait = true;
			owner.isJumping = false;
			owner.getNavigator().clearPathEntity();
			owner.getLookHelper().setLookPosition(
					MathHelper.floor_double(owner.posX), 
					MathHelper.floor_double(owner.posY - 1D), 
					MathHelper.floor_double(owner.posZ), 
					30F, 40F);
			owner.setSitting(true);
			break;
		case 8:
			// �����[�h
//			isMaidChaseWait = true;
			if (owner.attackTime <= 0) {
				if (owner.maidInventory.addItemStackToInventory(new ItemStack(Item.snowball))) {
					owner.setSwing(5, LMM_EnumSound.collect_snow);
					owner.playSound("random.pop");
					fcounter = 0;
				} else {
					owner.setPlayingRole(mpr_NULL);
					fcounter = 0;
				}
			}
//			isMaidChaseWait = true;
			owner.setSneaking(true);
			owner.getLookHelper().setLookPosition(
					MathHelper.floor_double(owner.posX), 
					MathHelper.floor_double(owner.posY - 1D), 
					MathHelper.floor_double(owner.posZ), 
					30F, 40F);
			break;
		}
		
	}


	@Override
	public void updateAITick(int pMode) {
		if (owner.isFreedom()) {
			// ���R�s�����̌ő̂͌Վ�ἁX�ƌ������������B
			if (owner.worldObj.isDaytime()) {
				// ���Ԃ̂��V��
				
				// �ጴ����
				if (!owner.isPlaying()) {
					// TODO:���V�є���
					int xx = MathHelper.floor_double(owner.posX);
					int yy = MathHelper.floor_double(owner.posY);
					int zz = MathHelper.floor_double(owner.posZ);
					
					// 3x3����̕����Ȃ炨�V�є��肪����
					boolean f = true;
					for (int z = -1; z < 2; z++) {
						for (int x = -1; x < 2; x++) {
							f &= owner.worldObj.getBlockId(xx + x, yy, zz + z) == Block.snow.blockID;
						}
					}
					int lpr = owner.rand.nextInt(100) - 97;
					lpr = (f && lpr > 0) ? (lpr == 1 ? mpr_QuickShooter : mpr_StockShooter) : 0;
					owner.setPlayingRole(lpr);
					fcounter = 0;
					if (f) {
						// mod_littleMaidMob.Debug(String.format("playRole-%d:%d", entityId, playingRole));
					}
					
				} else if (owner.getPlayingRole() >= 0x8000) {
					// ��̕��I��
					owner.setPlayingRole(mpr_NULL);
					fcounter = 0;
				} else {
					// ���V�т̎��s�������ɏ����H
					if (owner.getPlayingRole() == mpr_QuickShooter || 
							owner.getPlayingRole() == mpr_StockShooter) {
						playingSnowWar();
					}
					
				}
				
			} else {
				// ��̂��V��
				if (!owner.isPlaying()) {
					// ��������
					
				} else if (owner.getPlayingRole() < 0x8000) {
					// ���̕��I��
					owner.setPlayingRole(mpr_NULL);
					fcounter = 0;
					
				} else {
					// ���V�т̎��s�������ɏ����H
					
				}
			}
			
			// �`�F�X�g����
			if (owner.getAttackTarget() == null
					&& owner.maidInventory.getFirstEmptyStack() == -1) {
				
			}
		}
	}

	@Override
	public float attackEntityFrom(DamageSource par1DamageSource, float par2) {
		if (par1DamageSource.getSourceOfDamage() instanceof EntitySnowball) {
			// ���V�є���p�A��ʂ��ǂ�������
			owner.maidDamegeSound = LMM_EnumSound.hurt_snow;
			if (!owner.isContract() || owner.isFreedom()) {
				owner.setPlayingRole(mpr_QuickShooter);
				owner.setMaidWait(false);
				owner.setMaidWaitCount(0);
				mod_LMM_littleMaidMob.Debug("playingMode Enable.");
			}
		}
		return 0F;
	}

	@Override
	public boolean setMode(int pMode) {
		switch (pMode) {
		case mmode_Playing :
			owner.aiAttack.setEnable(false);
			owner.aiShooting.setEnable(true);
			owner.setBloodsuck(false);
			return true;
		}
		
		return false;
	}

	@Override
	public int getNextEquipItem(int pMode) {
		ItemStack litemstack = null;
		if (owner.getPlayingRole() != 0) {
			for (int li = 0; li < owner.maidInventory.maxInventorySize; li++) {
				litemstack = owner.maidInventory.getStackInSlot(li);
				if (litemstack == null) continue;
				
				// �ዅ
				if (litemstack.getItem() instanceof ItemSnowball) {
					return li;
				}
			}
		}
		return -1;
	}

}
