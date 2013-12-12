package ruby.bamboo.render.magatama;

import org.lwjgl.opengl.GL11;

import ruby.bamboo.entity.magatama.EntityDummy;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderDummy extends RenderPlayer {
    private Entity entity = null;

    public RenderDummy() {
    }

    public RenderDummy(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void doRender(Entity entity, double d0, double d1, double d2, float f, float f1) {
        if (entity instanceof EntityDummy) {
            this.renderDummy((EntityDummy) entity, d0, d1, d2, f, f1);
        } else {
            this.renderDummy(this.entity, d0, d1, d2, f, f1);
        }
    }

    private void renderDummy(EntityDummy dummy, double d0, double d1, double d2, float f, float f1) {
        if (dummy.getEntity() != null) {
            this.renderManager.getEntityRenderObject(dummy.getEntity()).doRender(dummy.getEntity(), d0, d1, d2, f, f1);
        }
    }

    private void renderDummy(Entity entity, double d0, double d1, double d2, float f, float f1) {
        if (this.renderManager == null) {
            this.renderManager = RenderManager.instance;
        }
        if (entity != null) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)d0, (float)d1-entity.yOffset, (float)d2);
            this.renderManager.getEntityRenderObject(entity).doRender(entity, d0, d1, d2, f, f1);
            GL11.glPopMatrix();
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

}
