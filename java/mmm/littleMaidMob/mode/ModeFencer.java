package mmm.littleMaidMob.mode;

import mmm.littleMaidMob.mode.ai.LMM_EntityAIHurtByTarget;
import mmm.littleMaidMob.mode.ai.LMM_EntityAINearestAttackableTarget;
import mmm.util.MMM_Helper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

/**
 * 独自基準としてモード定数は0x0080は平常、0x00c0は血まみれモードと区別。
 */
public class ModeFencer extends EntityModeBase {

    public static final int mmode_Fencer = 0x0080;
    public static final int mmode_Bloodsucker = 0x00c0;

    public ModeFencer(ModeController pEntity) {
        super(pEntity);
    }

    @Override
    public int priority() {
        return 3000;
    }

    @Override
    public void init() {
        // 登録モードの名称追加
        //LMM_TriggerSelect.appendTriggerItem(null, "Sword", "");
        //LMM_TriggerSelect.appendTriggerItem(null, "Axe", "");
    }

    @Override
    public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
        // Fencer:0x0080
        EntityAITasks[] ltasks = new EntityAITasks[2];
        ltasks[0] = pDefaultMove;
        ltasks[1] = new EntityAITasks(controller.aiProfiler);

        //      ltasks[1].addTask(1, new EntityAIOwnerHurtByTarget(owner));
        //      ltasks[1].addTask(2, new EntityAIOwnerHurtTarget(owner));
        ltasks[1].addTask(3, new LMM_EntityAIHurtByTarget(getOwner(), true));
        ltasks[1].addTask(4, new LMM_EntityAINearestAttackableTarget(getOwner(), EntityLivingBase.class, 0, true));

        controller.addMaidMode(ltasks, "Fencer", mmode_Fencer);

        // Bloodsucker:0x00c0
        EntityAITasks[] ltasks2 = new EntityAITasks[2];
        ltasks2[0] = pDefaultMove;
        ltasks2[1] = new EntityAITasks(controller.aiProfiler);

        ltasks2[1].addTask(1, new LMM_EntityAIHurtByTarget(getOwner(), true));
        ltasks2[1].addTask(2, new LMM_EntityAINearestAttackableTarget(getOwner(), EntityLivingBase.class, 0, true));

        controller.addMaidMode(ltasks2, "Bloodsucker", mmode_Bloodsucker);
    }

    @Override
    public boolean changeMode(EntityPlayer pentityplayer) {
        ItemStack litemstack = getOwner().inventory.getStackInSlot(0);
        if (litemstack != null) {
            if (litemstack.getItem() instanceof ItemSword || TriggerSelect.checkWeapon(getOwner().getOwnerName(), "Sword", litemstack)) {
                controller.setMaidMode("Fencer");
                return true;
            } else if (litemstack.getItem() instanceof ItemAxe || TriggerSelect.checkWeapon(getOwner().getOwnerName(), "Axe", litemstack)) {
                controller.setMaidMode("Bloodsucker");
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setMode(int pMode) {
        switch (pMode) {
        case mmode_Fencer:
            //          pentitylittlemaid.maidInventory.currentItem = getNextEquipItem(pentitylittlemaid, pMode);
            getOwner().setBloodsuck(false);
            controller.aiAttack.isGuard = true;
            return true;
        case mmode_Bloodsucker:
            //          pentitylittlemaid.maidInventory.currentItem = getNextEquipItem(pentitylittlemaid, pMode);
            getOwner().setBloodsuck(true);
            return true;
        }

        return false;
    }

    @Override
    public int getNextEquipItem(int pMode) {
        int li;
        int ll = -1;
        double ld = 0;
        double lld;
        ItemStack litemstack;

        // モードに応じた識別判定、速度優先
        switch (pMode) {
        case mmode_Fencer:
            for (li = 0; li < getOwner().inventory.getInitInvSize(); li++) {
                litemstack = getOwner().inventory.getStackInSlot(li);
                if (litemstack == null)
                    continue;

                // 剣
                if (litemstack.getItem() instanceof ItemSword || TriggerSelect.checkWeapon(getOwner().getOwnerName(), "Sword", litemstack)) {
                    return li;
                }

                // 攻撃力な高いものを記憶する
                lld = 1;
                try {
                    lld = MMM_Helper.getAttackVSEntity(litemstack);
                } catch (Exception e) {
                }
                if (lld > ld) {
                    ll = li;
                    ld = lld;
                }
            }
            break;
        case mmode_Bloodsucker:
            for (li = 0; li < getOwner().inventory.getInitInvSize(); li++) {
                litemstack = getOwner().inventory.getStackInSlot(li);
                if (litemstack == null)
                    continue;

                // 斧
                if (litemstack.getItem() instanceof ItemAxe || TriggerSelect.checkWeapon(getOwner().getOwnerName(), "Axe", litemstack)) {
                    return li;
                }

                // 攻撃力な高いものを記憶する
                lld = 1;
                try {
                    lld = MMM_Helper.getAttackVSEntity(litemstack);
                } catch (Exception e) {
                }
                if (lld > ld) {
                    ll = li;
                    ld = lld;
                }
            }
            break;
        }

        return ll;
    }

    @Override
    public boolean checkItemStack(ItemStack pItemStack) {
        // 装備アイテムを回収
        return pItemStack.getItem() instanceof ItemSword || pItemStack.getItem() instanceof ItemAxe;
    }

    @Override
    public String getName() {
        return null;
    }

}