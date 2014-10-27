package mmm.littleMaidMob.mode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mmm.littleMaidMob.littleMaidMob;

/**
 * 動作モードの登録、管理
 * 
 */
public class ModeManager {
    private static Map<String, Class<? extends EntityModeBase>> nameToModeMap = new HashMap<String, Class<? extends EntityModeBase>>();
    public static final ModeManager instance = new ModeManager();

    private ModeManager() {
    };

    public void init() {
        this.addModes(ModeBasic.class, "basic");
    }

    /**
     * Modeは利用者が少ないので、ローダー形式はやめてAPIによる任意追加とする
     */
    public void addModes(Class<? extends EntityModeBase> modebase, String modeName) {
        nameToModeMap.put(modeName, modebase);
    }

    public EntityModeBase createModeInstance(ModeController controller, String modeName) {
        try {
            if (nameToModeMap.containsKey(modeName)) {
                return nameToModeMap.get(modeName).getConstructor(ModeController.class).newInstance(controller);
            } else {
                littleMaidMob.Debug("ModeClass is Not found: %s", modeName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModeBasic(controller);
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
        for (Class<? extends EntityModeBase> clazz : nameToModeMap.values()) {
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
