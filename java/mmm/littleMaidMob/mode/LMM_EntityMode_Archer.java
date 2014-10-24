package mmm.littleMaidMob.mode;

import java.util.List;

import mmm.littleMaidMob.entity.EntityLittleMaidBase;
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

    public LMM_EntityMode_Archer(EntityLittleMaidBase pEntity) {
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
        ltasks[1].addTask(3, new LMM_EntityAIHurtByTarget(owner, true));
        ltasks[1].addTask(4, new LMM_EntityAINearestAttackableTarget(owner, EntityLivingBase.class, 0, true));

        owner.addMaidMode(ltasks, "Archer", mmode_Archer);

        // Blazingstar:0x00c3
        EntityAITasks[] ltasks2 = new EntityAITasks[2];
        ltasks2[0] = pDefaultMove;
        ltasks2[1] = new EntityAITasks(owner.aiProfiler);

        ltasks2[1].addTask(1, new LMM_EntityAIHurtByTarget(owner, true));
        ltasks2[1].addTask(2, new LMM_EntityAINearestAttackableTarget(owner, EntityLivingBase.class, 0, true));

        owner.addMaidMode(ltasks2, "Blazingstar", mmode_Blazingstar);
    }

    @Override
    public boolean changeMode(EntityPlayer pentityplayer) {
        ItemStack litemstack = owner.getInventory().getStackInSlot(0);
        if (litemstack != null) {
            if (litemstack.getItem() instanceof ItemBow || LMM_TriggerSelect.checkWeapon(owner.getMaidMaster(), "Bow", litemstack)) {
                if (owner.getInventory().hasItem(Items.flint_and_steel)) {
                    owner.setMaidMode("Blazingstar");
                } else {
                    owner.setMaidMode("Archer");
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
            owner.aiAttack.setEnable(false);
            owner.aiShooting.setEnable(true);
            owner.setBloodsuck(false);
            return true;
        case mmode_Blazingstar:
            owner.aiAttack.setEnable(false);
            owner.aiShooting.setEnable(true);
            owner.setBloodsuck(true);
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
            for (li = 0; li < owner.getInventory().maxInventorySize; li++) {
                litemstack = owner.getInventory().getStackInSlot(li);
                if (litemstack == null)
                    continue;

                if (litemstack.getItem() instanceof ItemBow || LMM_TriggerSelect.checkWeapon(owner.getMaidMaster(), "Bow", litemstack)) {
                    return li;
                }
            }
            break;
        }

        return -1;
    }

    @Override
    public boolean checkItemStack(ItemStack pItemStack) {
        String ls = owner.getMaidMaster();
        return (pItemStack.getItem() instanceof ItemBow) || (pItemStack.itemID == Item.arrow.itemID) || LMM_TriggerSelect.checkWeapon(ls, "Bow", pItemStack) || LMM_TriggerSelect.checkWeapon(ls, "Arrow", pItemStack);
    }

    @Override
    public void onUpdate(int pMode) {
        switch (pMode) {
        case mmode_Archer:
        case mmode_Blazingstar:
            owner.getWeaponStatus();
            //			updateGuns();
            break;
        }

    }

    @Override
    public void updateAITick(int pMode) {
        switch (pMode) {
        case mmode_Archer:
            //			owner.getWeaponStatus();
            updateGuns();
            break;
        case mmode_Blazingstar:
            //			owner.getWeaponStatus();
            updateGuns();
            World lworld = owner.worldObj;
            List<Entity> llist = lworld.getEntitiesWithinAABB(Entity.class, owner.boundingBox.expand(16D, 16D, 16D));
            for (int li = 0; li < llist.size(); li++) {
                Entity lentity = llist.get(li);
                if (lentity.isEntityAlive() && lentity.isBurning() && owner.rand.nextFloat() > 0.9F) {
                    int lx = (int) owner.posX;
                    int ly = (int) owner.posY;
                    int lz = (int) owner.posZ;
                    if (lworld.isAirBlock(lx, ly, lz) || lworld.getBlock(lx, ly, lz).getMaterial().getCanBurn()) {
                        lworld.playSoundEffect(lx + 0.5D, ly + 0.5D, lz + 0.5D, "fire.ignite", 1.0F, owner.rand.nextFloat() * 0.4F + 0.8F);
                        lworld.setBlock(lx, ly, lz, Blocks.fire);
                    }
                }
            }
            break;
        }
    }

    protected void updateGuns() {
        if (owner.getAttackTarget() == null || !owner.getAttackTarget().isEntityAlive()) {
            if (!owner.avatar.weaponReload) {
                if (owner.avatar.isUsingItem()) {
                    if (owner.avatar.isItemReload) {
                        owner.avatar.stopUsingItem();
                        mod_LMM_littleMaidMob.Debug(String.format("id:%d cancel reload.", owner));
                    } else {
                        owner.avatar.clearItemInUse();
                        mod_LMM_littleMaidMob.Debug(String.format("id:%d clear.", owner));
                    }
                }
            } else {
                owner.mstatAimeBow = true;
            }
        }
        if (owner.weaponReload && !owner.avatar.isUsingItem()) {
            owner.getInventory().getCurrentItem().useItemRightClick(owner.worldObj, owner.avatar);
            mod_LMM_littleMaidMob.Debug("id:%d force reload.", owner);
            owner.mstatAimeBow = true;
        }

    }

    @Override
    public String getName() {
        return "Archer";
    }

}
