package ruby.bamboo.render.magatama;

import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import ruby.bamboo.entity.magatama.EntityDummy;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class RenderDummy extends RenderPlayer {
    private Entity entity = null;
    private final static HashMap<String, Render> nameToRenderMap = new HashMap<String, Render>();

    public RenderDummy() {
    }

    public RenderDummy(Entity entity,String name) {
        this.renderManager = RenderManager.instance;
        this.setEntityPlayerToDummyRender(name, entity) ;
    }

    public void setEntityPlayerToDummyRender(String name, Entity dummy) {
        nameToRenderMap.put(name, RenderManager.instance.getEntityRenderObject(dummy));
        this.entity = dummy;
    }

    public static void removeEntityPlayerToDummyRender(EntityPlayer entityPlayer) {
        nameToRenderMap.remove(entityPlayer.getEntityName());
    }

    @Override
    public void doRender(Entity entity, double d0, double d1, double d2, float f, float f1) {
        if (entity instanceof EntityDummy) {
            this.renderDummy((EntityDummy) entity, d0, d1, d2, f, f1);
        } else {
            this.renderDummy((EntityPlayer) entity, this.entity, d0, d1, d2, f, f1);
        }
    }

    private void renderDummy(EntityDummy dummy, double d0, double d1, double d2, float f, float f1) {
        if (dummy.getEntity() != null) {
            this.renderManager.getEntityRenderObject(dummy.getEntity()).doRender(dummy.getEntity(), d0, d1, d2, f, f1);
        }
    }

    private void renderDummy(EntityPlayer entity, Entity dummy, double d0, double d1, double d2, float f, float f1) {

        if (dummy != null) {
            GL11.glPushMatrix();
            if (nameToRenderMap.containsKey(entity.getEntityName())) {
                GL11.glTranslatef((float) d0, (float) d1 - dummy.yOffset, (float) d2);
                this.renderManager.getEntityRenderObject(dummy).doRender(dummy, d0, d1, d2, f, f1);
            } else {
                this.renderManager.getEntityClassRenderObject(EntityPlayer.class).doRender(entity, d0, d1, d2, f, f1);
            }
            GL11.glPopMatrix();
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

}
