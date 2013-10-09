package ruby.bamboo;

import java.util.HashMap;

import net.minecraft.entity.Entity;

public class KaginawaHandler {
    private static HashMap<Entity, Boolean> kagimap;
    private static boolean localUse;
    static {
        kagimap = new HashMap<Entity, Boolean>();
    }

    public static boolean getUsageState(Entity e) {
        if (kagimap.containsKey(e)) {
            return kagimap.get(e);
        }

        return false;
    }

    public static void setUsageState(Entity e, boolean b) {
        kagimap.put(e, b);
    }

    public static void remuveUser(Entity e) {
        kagimap.remove(e);
    }

    public static boolean isUsed() {
        return localUse;
    }

    public static void setUsageState(boolean b) {
        localUse = b;
    }
}
