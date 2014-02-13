package ruby.togglesneak.asm;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovementInputFromOptions;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

public class ToggleSneak implements IFMLLoadingPlugin {
    //-Dfml.coreMods.load=ruby.togglesneak.asm.ToggleSneak
    static File loc;

    @Override
    public String[] getASMTransformerClass() {
        return new String[] { MovementInputFromOptionsTransformer.class.getName() };
    }

    @Override
    public String getModContainerClass() {
        return ToggleSneakContainer.class.getName();
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        if (data.containsKey("coremodLocation")) {
            loc = (File) data.get("coremodLocation");
        }
    }

    private static int pressTime;
    private static int time;
    private static Field field_pressTime = null;//余計なprivateしやがってクソが

    public static void hook(MovementInputFromOptions mifo, GameSettings gameSettings) {
        if (field_pressTime == null) {
            try {
                field_pressTime = KeyBinding.class.getDeclaredField("pressTime");
                field_pressTime.setAccessible(true);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
        mifo.moveStrafe = 0.0F;
        mifo.moveForward = 0.0F;

        if (gameSettings.keyBindForward.isPressed()) {
            ++mifo.moveForward;
        }

        if (gameSettings.keyBindBack.isPressed()) {
            --mifo.moveForward;
        }

        if (gameSettings.keyBindLeft.isPressed()) {
            ++mifo.moveStrafe;
        }

        if (gameSettings.keyBindRight.isPressed()) {
            --mifo.moveStrafe;
        }

        mifo.jump = gameSettings.keyBindJump.isPressed();

        if (mifo.sneak && gameSettings.keyBindSneak.isPressed()) {
            ++time;
        } else {
            if (mifo.sneak && time > 10) {
                mifo.sneak = false;
            }

            time = 0;
        }
        int nowPressTime = 0;
        try {
            nowPressTime = field_pressTime.getInt(gameSettings.keyBindSneak);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (!Minecraft.getMinecraft().thePlayer.capabilities.isFlying && gameSettings.keyBindSneak.isPressed() && pressTime != nowPressTime) {
            mifo.sneak = !mifo.sneak;
            pressTime = nowPressTime;
        } else if (Minecraft.getMinecraft().thePlayer.capabilities.isFlying) {
            mifo.sneak = gameSettings.keyBindSneak.isPressed();
        }

        if (mifo.sneak) {
            mifo.moveStrafe = (float) (mifo.moveStrafe * 0.3D);
            mifo.moveForward = (float) (mifo.moveForward * 0.3D);
        }
    }

    @Override
    public String getAccessTransformerClass() {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }
}
