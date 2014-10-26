package mmm.littleMaidMob.mode.ai;

import mmm.littleMaidMob.entity.EntityLittleMaidBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class LMM_EntityAIBegMove extends EntityAIBase {

    private EntityLittleMaidBase theMaid;
    private EntityPlayer thePlayer;
    private float moveSpeed;

    public LMM_EntityAIBegMove(EntityLittleMaidBase pEntityLittleMaid, float pmoveSpeed) {
        theMaid = pEntityLittleMaid;
        moveSpeed = pmoveSpeed;

        setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        return theMaid.isLookSuger();
    }

    @Override
    public void startExecuting() {
        thePlayer = theMaid.modeController.aiBeg.getPlayer();
    }

    @Override
    public void resetTask() {
        thePlayer = null;
    }

    @Override
    public boolean continueExecuting() {
        return shouldExecute();
    }

    @Override
    public void updateTask() {
        if (theMaid.modeController.aiBeg.getDistanceSq() < 3.5D) {
            theMaid.getNavigator().clearPathEntity();
        } else {
            theMaid.getNavigator().tryMoveToEntityLiving(thePlayer, moveSpeed);
        }
    }

}
