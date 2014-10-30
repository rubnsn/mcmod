package mmm.littleMaidMob.mode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 動作モードの登録、管理
 * 
 */
public class ModeManager {
    private static List<Class<? extends EntityModeBase>> modeClassList = new ArrayList<Class<? extends EntityModeBase>>();
    public static final ModeManager instance = new ModeManager();

    private ModeManager() {
    };

    public void init() {
        this.addModes(ModeBasic.class);
    }

    /**
     * Modeは利用者が少ないので、ローダー形式はやめてAPIによる任意追加とする
     */
    public void addModes(Class<? extends EntityModeBase> modebase) {
        modeClassList.add(modebase);
    }

    public List<EntityModeBase> getModeList(ModeController controller) {
        Comparator<EntityModeBase> comp = new Comparator<EntityModeBase>() {
            @Override
            public int compare(EntityModeBase o1, EntityModeBase o2) {
                if (o1.priority() == o2.priority()) {
                    return 0;
                }
                return o1.priority() < o2.priority() ? 1 : -1;
            }
        };
        List<EntityModeBase> list = new ArrayList<EntityModeBase>();
        for (Class<? extends EntityModeBase> clazz : modeClassList) {
            try {
                list.add(clazz.getConstructor(ModeController.class).newInstance(controller));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Collections.sort(list, comp);
        return list;
    }
}
