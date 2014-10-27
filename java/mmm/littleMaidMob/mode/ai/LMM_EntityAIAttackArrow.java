package mmm.littleMaidMob.mode.ai;

import mmm.littleMaidMob.SwingController.SwingStatus;
import mmm.littleMaidMob.entity.EntityLittleMaidAvatar;
import mmm.littleMaidMob.entity.EntityLittleMaidBase;
import mmm.littleMaidMob.inventory.InventoryLittleMaid;
import mmm.littleMaidMob.sound.EnumSound;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;

public class LMM_EntityAIAttackArrow extends EntityAIBase implements
        LMM_IEntityAI {

    protected boolean fEnable;

    protected EntityLittleMaidBase fMaid;
    protected EntityLittleMaidAvatar fAvatar;
    protected InventoryLittleMaid fInventory;
    protected SwingStatus swingState;
    protected World worldObj;
    protected EntityLivingBase fTarget;
    protected int fForget;

    public LMM_EntityAIAttackArrow(EntityLittleMaidBase pEntityLittleMaid) {
        fMaid = pEntityLittleMaid;
        fAvatar = pEntityLittleMaid.avatar;
        fInventory = pEntityLittleMaid.inventory;
        swingState = pEntityLittleMaid.getSwingStatus().getSwingStatusDominant();
        worldObj = pEntityLittleMaid.worldObj;
        fEnable = false;
        setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        EntityLivingBase entityliving = fMaid.getAttackTarget();

        if (!fEnable || entityliving == null || entityliving.isDead) {
            fMaid.setAttackTarget(null);
            fMaid.setTarget(null);
            if (entityliving != null) {
                fMaid.getNavigator().clearPathEntity();
            }
            fTarget = null;
            return false;
        } else {
            fTarget = entityliving;
            return true;
        }
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        fMaid.playSound(fMaid.isBloodsuck() ? EnumSound.findTarget_B : EnumSound.findTarget_N, false);
        swingState = fMaid.getSwingStatus().getSwingStatusDominant();
    }

    @Override
    public boolean continueExecuting() {
        return shouldExecute() || (fTarget != null && !fMaid.getNavigator().noPath());
    }

    @Override
    public void resetTask() {
        fTarget = null;
    }

    @Override
    public void updateTask() {/*TODO:そのうち何とかする
                              double lrange = 225D;
                              double ldist = fMaid.getDistanceSqToEntity(fTarget);
                              boolean lsee = fMaid.getEntitySenses().canSee(fTarget);

                              if (lsee) {
                              fForget = 0;
                              } else {
                              fForget++;
                              }

                              fMaid.getLookHelper().setLookPositionWithEntity(fTarget, 30F, 30F);

                              if (ldist < lrange) {
                              double atx = fTarget.posX - fMaid.posX;
                              double aty = fTarget.posY - fMaid.posY;
                              double atz = fTarget.posZ - fMaid.posZ;
                              if (fTarget.isEntityAlive()) {
                              ItemStack litemstack = fMaid.getEquipmentInSlot(0);
                              double atl = atx * atx + aty * aty + atz * atz;
                              double il = -1D;
                              double milsq = 10D;
                              Entity masterEntity = fMaid.getMaidMasterEntity();
                              if (masterEntity != null && !fMaid.isPlaying()) {
                              double amx = masterEntity.posX - fMaid.posX;
                              double amy = masterEntity.posY - fMaid.posY;//-2D
                              double amz = masterEntity.posZ - fMaid.posZ;

                              il = (amx * atx + amy * aty + amz * atz) / atl;

                              double mix = (fMaid.posX + il * atx) - masterEntity.posX;
                              double miy = (fMaid.posY + il * aty) - masterEntity.posY;// + 2D;
                              double miz = (fMaid.posZ + il * atz) - masterEntity.posZ;
                              milsq = mix * mix + miy * miy + miz * miz;
                              //					mod_LMM_littleMaidMob.Debug("il:%f, milsq:%f", il, milsq);
                              }

                              if (litemstack != null && !(litemstack.getItem() instanceof ItemFood) && !fMaid.weaponReload) {
                              int lastentityid = worldObj.loadedEntityList.size();
                              int itemcount = litemstack.stackSize;
                              fMaid.mstatAimeBow = true;
                              fAvatar.getValueVectorFire(atx, aty, atz, atl);
                              boolean lcanattack = true;
                              boolean ldotarget = false;
                              double tpr = Math.sqrt(atl);
                              Entity lentity = MMM_Helper.getRayTraceEntity(fMaid.avatar, tpr + 1.0F, 1.0F, 1.0F);
                              Item helmid = !fMaid.isMaskedMaid() ? null : fInventory.armorInventory[3].getItem();
                              if (helmid == Items.diamond_helmet || helmid == Items.iron_helmet) {
                              if (lentity != null && fMaid.getIFF(lentity)) {
                              lcanattack = false;
                              //							mod_LMM_littleMaidMob.Debug("ID:%d-friendly fire to ID:%d.", fMaid.entityId, lentity.entityId);
                              }
                              }
                              if (lentity == fTarget) {
                              ldotarget = true;
                              }
                              lcanattack &= (milsq > 3D || il < 0D);
                              lcanattack &= ldotarget;
                              if (!lcanattack) {
                              double tpx = fMaid.posX;
                              double tpy = fMaid.posY;
                              double tpz = fMaid.posZ;
                              //						double tpr = Math.sqrt(atl) * 0.5D;
                              tpr = tpr * 0.5D;
                              if (fMaid.isBloodsuck()) {
                              tpx += (atz / tpr);
                              tpz -= (atx / tpr);
                              } else {
                              tpx -= (atz / tpr);
                              tpz += (atx / tpr);
                              }
                              fMaid.getNavigator().tryMoveToXYZ(tpx, tpy, tpz, 1.0F);
                              } else if (lsee & ldist < 100) {
                              fMaid.getNavigator().clearPathEntity();
                              //						mod_LMM_littleMaidMob.Debug("Shooting Range.");
                              }

                              lcanattack &= lsee;
                              //            		mod_littleMaidMob.Debug(String.format("id:%d at:%d", entityId, attackTime));
                              if (((fMaid.weaponFullAuto && !lcanattack) || (lcanattack && fMaid.getSwingStatus().getSwingStatusDominant().canAttack())) && fAvatar.isItemTrigger) {
                              littleMaidMob.Debug("id:%d shoot.", fMaid);
                              fAvatar.stopUsingItem();
                              fMaid.getSwingStatus().setSwing(30, EnumSound.shoot);
                              } else {
                              if (litemstack.getMaxItemUseDuration() > 500) {
                              //                			mod_littleMaidMob.Debug(String.format("non reload.%b", isMaskedMaid));
                              if (!fAvatar.isUsingItemLittleMaid()) {
                                if (!fMaid.weaponFullAuto || lcanattack) {
                                    int at = ((helmid == Items.iron_helmet) || (helmid == Items.diamond_helmet)) ? 26 : 16;
                                    if (swingState.attackTime < at) {
                                        fMaid.getSwingStatus().setSwing(at, EnumSound.sighting);
                                        litemstack = litemstack.useItemRightClick(worldObj, fAvatar);
                                        littleMaidMob.Debug("id:%d redygun.", fMaid);
                                    }
                                } else {
                                    littleMaidMob.Debug(String.format("ID:%d-friendly fire FullAuto.", fMaid));
                                }
                              }
                              } else if (litemstack.getMaxItemUseDuration() == 0) {
                              if (swingState.canAttack() && !fAvatar.isUsingItem()) {
                                if (lcanattack) {
                                    litemstack = litemstack.useItemRightClick(worldObj, fAvatar);
                                    fMaid.mstatAimeBow = false;
                                    fMaid.setSwing(10, (litemstack.stackSize == itemcount) ? EnumSound.shoot_burst : EnumSound.Null);
                                    littleMaidMob.Debug(String.format("id:%d throw weapon.(%d:%f:%f)", fMaid, swingState.attackTime, fMaid.rotationYaw, fMaid.rotationYawHead));
                                } else {
                                    LMM_littleMaidMob.Debug(String.format("ID:%d-friendly fire throw weapon.", fMaid));
                                }
                              }
                              } else {
                              if (!fAvatar.isUsingItemLittleMaid()) {
                                litemstack = litemstack.useItemRightClick(worldObj, fAvatar);
                                littleMaidMob.Debug(String.format("%d reload.", fMaid));
                              }
                              swingState.attackTime = 5;
                              }
                              }
                              //            		maidAvatarEntity.setValueRotation();
                              fAvatar.setValueVector();
                              if (litemstack.stackSize <= 0) {
                              fMaid.destroyCurrentEquippedItem();
                              fMaid.getNextEquipItem();
                              } else {
                              fInventory.setInventoryCurrentSlotContents(litemstack);
                              }

                              List<Entity> newentitys = worldObj.loadedEntityList.subList(lastentityid, worldObj.loadedEntityList.size());
                              boolean shootingflag = false;
                              if (newentitys != null && newentitys.size() > 0) {
                              littleMaidMob.Debug(String.format("new FO entity %d", newentitys.size()));
                              for (Entity te : newentitys) {
                              if (te.isDead) {
                                shootingflag = true;
                                continue;
                              }
                              try {
                                Field fd[] = te.getClass().getDeclaredFields();
                                //                				mod_littleMaidMob.Debug(String.format("%s, %d", e.getClass().getName(), fd.length));
                                for (Field ff : fd) {
                                    ff.setAccessible(true);
                                    Object eo = ff.get(te);
                                    if (eo.equals(fAvatar)) {
                                        ff.set(te, this);
                                        littleMaidMob.Debug("Replace FO Owner.");
                                    }
                                }
                              } catch (Exception exception) {
                              }
                              }
                              }
                              if (shootingflag) {
                              for (Object obj : worldObj.loadedEntityList) {
                              if (obj instanceof EntityCreature && !(obj instanceof EntityLittleMaidBase)) {
                                EntityCreature ecr = (EntityCreature) obj;
                                if (ecr.getEntityToAttack() == fAvatar) {
                                    ecr.setTarget(fMaid);
                                }
                              }
                              }
                              }
                              }
                              }
                              } else {
                              if (fMaid.getNavigator().noPath()) {
                              fMaid.getNavigator().tryMoveToEntityLiving(fTarget, 1.0);
                              }
                              if (fMaid.getNavigator().noPath()) {
                              littleMaidMob.Debug("id:%d Target renge out.", fMaid);
                              fMaid.setAttackTarget(null);
                              }
                              if (fMaid.weaponFullAuto && fAvatar.isItemTrigger) {
                              fAvatar.stopUsingItem();
                              } else {
                              fAvatar.clearItemInUse();
                              }

                              }
                              */
    }

    @Override
    public void setEnable(boolean pFlag) {
        fEnable = pFlag;
    }

    @Override
    public boolean getEnable() {
        return fEnable;
    }

}
