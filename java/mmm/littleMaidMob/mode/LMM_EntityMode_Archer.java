package mmm.littleMaidMob.mode;

import java.util.List;

import mmm.littleMaidMob.mode.ai.LMM_EntityAIHurtByTarget;
import mmm.littleMaidMob.mode.ai.LMM_EntityAINearestAttackableTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LMM_EntityMode_Archer extends EntityModeBase {

    public static final int mmode_Archer = 0x0083;
    public static final int mmode_Blazingstar = 0x00c3;

    @Override
    public int priority() {
        return 3200;
    }

    public LMM_EntityMode_Archer(ModeController pEntity) {
        super(pEntity);

    }

    @Override
    public void init() {
        /*
        ModLoader.addLocalization("littleMaidMob.mode.Archer", "Archer");
        ModLoader.addLocalization("littleMaidMob.mode.F-Archer", "F-Archer");
        ModLoader.addLocalization("littleMaidMob.mode.T-Archer", "T-Archer");
        ModLoader.addLocalization("littleMaidMob.mode.D-Archer", "D-Archer");
        ModLoader.addLocalization("littleMaidMob.mode.Blazingstar", "Blazingstar");
        ModLoader.addLocalization("littleMaidMob.mode.F-Blazingstar", "F-Blazingstar");
        ModLoader.addLocalization("littleMaidMob.mode.T-Blazingstar", "T-Blazingstar");
        ModLoader.addLocalization("littleMaidMob.mode.D-Blazingstar", "D-Blazingstar");
        LMM_TriggerSelect.appendTriggerItem(null, "Bow", "");
        LMM_TriggerSelect.appendTriggerItem(null, "Arrow", "");
        */
    }

    @Override
    public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
        // Archer:0x0083
        EntityAITasks[] ltasks = new EntityAITasks[2];
        ltasks[0] = pDefaultMove;

        //		ltasks[1].addTask(1, new EntityAIOwnerHurtByTarget(owner));
        //		ltasks[1].addTask(2, new EntityAIOwnerHurtTarget(owner));
        ltasks[1].addTask(3, new LMM_EntityAIHurtByTarget(getOwner(), true));
        ltasks[1].addTask(4, new LMM_EntityAINearestAttackableTarget(getOwner(), EntityLivingBase.class, 0, true));

    }

    @Override
    public String getSubModeName() {
        return "Blazingstar";
    }

    @Override
    public boolean changeMode(EntityPlayer pentityplayer) {
        ItemStack litemstack = getOwner().getInventory().getStackInSlot(0);
        if (litemstack != null) {
            if (litemstack.getItem() instanceof ItemBow || LMM_TriggerSelect.checkWeapon(getOwner().getMaidMaster(), "Bow", litemstack)) {
                if (getOwner().getInventory().hasItem(Items.flint_and_steel)) {
                    getOwner().setMaidMode("Blazingstar");
                } else {
                    getOwner().setMaidMode("Archer");
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setMode(int pMode) {
        switch (pMode) {
        case mmode_Archer:
            controller.aiAttack.setEnable(false);
            controller.aiShooting.setEnable(true);
            controller.setBloodsuck(false);
            return true;
        case mmode_Blazingstar:
            controller.aiAttack.setEnable(false);
            controller.aiShooting.setEnable(true);
            controller.setBloodsuck(true);
            return true;
        }

        return false;
    }

    @Override
    public int getNextEquipItem(int pMode) {
        int li;
        ItemStack litemstack;

        switch (pMode) {
        case mmode_Archer:
        case mmode_Blazingstar:
            for (li = 0; li < getOwner().getInventory().getSizeInventory(); li++) {
                litemstack = getOwner().getInventory().getStackInSlot(li);
                if (litemstack == null)
                    continue;

                if (litemstack.getItem() instanceof ItemBow || LMM_TriggerSelect.checkWeapon(getOwner().getMaidMaster(), "Bow", litemstack)) {
                    return li;
                }
            }
            break;
        }

        return -1;
    }

    @Override
    public boolean checkItemStack(ItemStack pItemStack) {
        String ls = getOwner().getMaidMaster();
        return (pItemStack.getItem() instanceof ItemBow) || (pItemStack.getItem() == Items.arrow) || LMM_TriggerSelect.checkWeapon(ls, "Bow", pItemStack) || LMM_TriggerSelect.checkWeapon(ls, "Arrow", pItemStack);
    }

    @Override
    public void onUpdate(int pMode) {
        switch (pMode) {
        case mmode_Archer:
        case mmode_Blazingstar:
            getOwner().getWeaponStatus();
            //			updateGuns();
            break;
        }

    }

    @Override
    public void updateAITick(int pMode) {
        switch (pMode) {
        case mmode_Archer:
            //			getOwner().getWeaponStatus();
            updateGuns();
            break;
        case mmode_Blazingstar:
            //			getOwner().getWeaponStatus();
            updateGuns();
            World lworld = getOwner().worldObj;
            List<Entity> llist = lworld.getEntitiesWithinAABB(Entity.class, getOwner().boundingBox.expand(16D, 16D, 16D));
            for (int li = 0; li < llist.size(); li++) {
                Entity lentity = llist.get(li);
                if (lentity.isEntityAlive() && lentity.isBurning() && getOwner().rand.nextFloat() > 0.9F) {
                    int lx = (int) getOwner().posX;
                    int ly = (int) getOwner().posY;
                    int lz = (int) getOwner().posZ;
                    if (lworld.isAirBlock(lx, ly, lz) || lworld.getBlock(lx, ly, lz).getMaterial().getCanBurn()) {
                        lworld.playSoundEffect(lx + 0.5D, ly + 0.5D, lz + 0.5D, "fire.ignite", 1.0F, getOwner().rand.nextFloat() * 0.4F + 0.8F);
                        lworld.setBlock(lx, ly, lz, Blocks.fire);
                    }
                }
            }
            break;
        }
    }

    protected void updateGuns() {
        if (getOwner().getAttackTarget() == null || !getOwner().getAttackTarget().isEntityAlive()) {
            if (!getOwner().avatar.weaponReload) {
                if (getOwner().avatar.isUsingItem()) {
                    if (getOwner().avatar.isItemReload) {
                        getOwner().avatar.stopUsingItem();
                        mod_LMM_littleMaidMob.Debug(String.format("id:%d cancel reload.", getOwner()));
                    } else {
                        getOwner().avatar.clearItemInUse();
                        mod_LMM_littleMaidMob.Debug(String.format("id:%d clear.", getOwner()));
                    }
                }
            } else {
                getOwner().mstatAimeBow = true;
            }
        }
        if (getOwner().weaponReload && !getOwner().avatar.isUsingItem()) {
            getOwner().getInventory().getCurrentItem().useItemRightClick(getOwner().worldObj, getOwner().avatar);
            mod_LMM_littleMaidMob.Debug("id:%d force reload.", getOwner());
            getOwner().mstatAimeBow = true;
        }

    }

    @Override
    public String getName() {
        return "Archer";
    }

}
