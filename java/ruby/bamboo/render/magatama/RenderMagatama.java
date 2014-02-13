package ruby.bamboo.render.magatama;

import net.minecraft.entity.Entity;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;

import ruby.bamboo.entity.magatama.EntityMagatama;
import ruby.bamboo.render.RenderThrowable;

public class RenderMagatama extends RenderThrowable {
    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
        int color = ((EntityMagatama) par1Entity).getColor();
        GL11.glColor3f((color >> 16) / 255F, ((color >> 8) & 0xFF) / 255F, (color & 0xFF) / 255F);
        super.doRender(par1Entity, par2, par4, par6, par8, par9);
    }

    @Override
    public IIcon getIcon(Entity par1Entity) {
        // TODO 自動生成されたメソッド・スタブ
        return ((EntityMagatama) par1Entity).getThrowableIcon();
    }

}
