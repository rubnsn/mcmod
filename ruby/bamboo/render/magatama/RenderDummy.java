package ruby.bamboo.render.magatama;

import ruby.bamboo.entity.magatama.EntityDummy;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderDummy extends Render {

    @Override
    public void doRender(Entity entity, double d0, double d1, double d2, float f, float f1) {
        this.renderDummy((EntityDummy) entity, d0, d1, d2, f, f1);
    }

    private void renderDummy(EntityDummy dummy, double d0, double d1, double d2, float f, float f1) {
        if (dummy.getEntity() != null) {
            this.renderManager.getEntityRenderObject(dummy.getEntity()).doRender(dummy.getEntity(), d0, d1, d2, f, f1);
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

}
