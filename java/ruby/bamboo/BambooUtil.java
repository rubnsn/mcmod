package ruby.bamboo;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BambooUtil {

    public static byte getPlayerDir(Entity entity) {
        return entity != null ? (byte) (MathHelper.floor_double((entity.rotationYaw * 4F) / 360F + 0.5D) & 3) : 0;
    }

    public static boolean isInnerCircleCollision(int posX, int posY, int posZ, int tposX, int tposY, int tposZ, int r) {
        return Math.sqrt(Math.pow((posX - tposX), 2) + Math.pow((posY - tposY), 2) + Math.pow((posZ - tposZ), 2)) <= r;
    }

    public static float getInnerCircleCollision(int posX, int posY, int posZ, int tposX, int tposY, int tposZ) {
        return (float) Math.sqrt(Math.pow((posX - tposX), 2) + Math.pow((posY - tposY), 2) + Math.pow((posZ - tposZ), 2));
    }

    public static boolean isUnbreakBlock(World world, int posX, int posY, int posZ) {
        return world.getBlock(posX, posY, posZ) != null ? world.getBlock(posX, posY, posZ).getBlockHardness(world, posX, posY, posZ) < 0 : true;
    }

    public static void print(Object... obj) {
        StringBuilder stb = new StringBuilder();
        for (Object o : obj) {
            stb.append(o);
            stb.append(" ");
        }
        System.out.println(stb.toString());
    }
}
