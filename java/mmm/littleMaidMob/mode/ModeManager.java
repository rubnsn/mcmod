package mmm.littleMaidMob.mode;

import java.util.ArrayList;

import mmm.littleMaidMob.entity.EntityLittleMaidBase;

/**
 * 動作モードの登録、管理
 * 
 */
public class ModeManager {
    private ArrayList<Class<? extends EntityModeBase>> modeList = new ArrayList<Class<? extends EntityModeBase>>();
    public static final ModeManager instance = new ModeManager();

    private ModeManager() {
    };

    public void init() {
        this.addModes(ModeDefault.class);
    }

    /**
     * Modeは利用者が少ないので、ローダー形式はやめてAPIによる任意追加とする
     */
    public void addModes(Class<? extends EntityModeBase> modebase) {
        this.modeList.add(modebase);
    }

    public ModeController getModeControler(EntityLittleMaidBase maid) {
        ModeController modeController = new ModeController();
        for (Class<? extends EntityModeBase> clazz : modeList) {
            try {
                modeController.addMode(clazz.getConstructor(EntityLittleMaidBase.class).newInstance(maid));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return modeController;
    }

}
