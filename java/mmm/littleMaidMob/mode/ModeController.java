package mmm.littleMaidMob.mode;

import java.util.HashMap;
import java.util.Map;

import mmm.littleMaidMob.littleMaidMob;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.nbt.NBTTagCompound;

/**
 * モード管理用クラス
 * 
 */
public class ModeController {

    // default AIs
    public EntityAIBase aiSit;
    // AI
    /*
    public EntityAITempt aiTempt;
    public LMM_EntityAIBeg aiBeg;
    public LMM_EntityAIBegMove aiBegMove;
    public EntityAIOpenDoor aiOpenDoor;
    public EntityAIRestrictOpenDoor aiCloseDoor;
    public LMM_EntityAIAvoidPlayer aiAvoidPlayer;
    public LMM_EntityAIFollowOwner aiFollow;
    public LMM_EntityAIAttackOnCollide aiAttack;
    public LMM_EntityAIAttackArrow aiShooting;
    public LMM_EntityAICollectItem aiCollectItem;
    public LMM_EntityAIRestrictRain aiRestrictRain;
    public LMM_EntityAIFleeRain aiFreeRain;
    public LMM_EntityAIWander aiWander;
    public LMM_EntityAIJumpToMaster aiJumpTo;
    public LMM_EntityAIFindBlock aiFindBlock;
    public LMM_EntityAITracerMove aiTracer;
    public EntityAISwimming aiSwiming;
    public EntityAIPanic aiPanic;
    */

    public Map<String, EntityModeBase> modeList = new HashMap<String, EntityModeBase>();
    /** 現在実行中のモード */
    private EntityModeBase activeMode;

    public void addMode(EntityModeBase pMode) {
        if (pMode.getName() != null) {
            modeList.put(pMode.getName(), pMode);
        } else {
            littleMaidMob.Debug("ModeClass is NoName: %s", pMode.toString());
        }
    }

    public EntityModeBase getActiveMode() {
        if (this.activeMode == null) {
            this.setMode("default");
        }
        return this.activeMode;
    }

    public EntityModeBase getMode(String pName) {
        return this.modeList.get(pName);
    }

    public void setMode(String pName) {
        this.activeMode = this.modeList.get(pName);
    }

    public void readModeNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("maidmode")) {
            this.setMode(nbt.getString("maidmode"));
            if (nbt.hasKey("modeNBT")) {
                activeMode.readEntityFromNBT((NBTTagCompound) nbt.getTag("modeNBT"));
            }
        }
    }

    public void writeModeNBT(NBTTagCompound nbt) {
        if (activeMode != null) {
            nbt.setString("maidmode", activeMode.getName());
            NBTTagCompound modeNbt = new NBTTagCompound();
            activeMode.writeEntityToNBT(modeNbt);
            nbt.setTag("modeNBT", modeNbt);
        }
    }

}
