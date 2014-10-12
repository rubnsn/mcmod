package ruby.bamboo.entity.villager;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import ruby.bamboo.render.tileentity.RenderVillagerBlock;

public class RenderVillagerHead extends Render {

    @Override
    public void doRender(Entity entity, double d1, double d2, double d3, float f, float f1) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        float f2 = this.func_82400_a(entity.prevRotationYaw, entity.rotationYaw, f1);
        float f3 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * f1;
        GL11.glTranslatef((float) d1, (float) d2, (float) d3);
        float f4 = 0.0625F;
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        this.bindTexture(RenderVillagerBlock.instance.res);
        RenderVillagerBlock.instance.model.render(entity, 0.0F, 0.0F, 0.0F, f2, f3, f4);
        GL11.glPopMatrix();
    }

    private float func_82400_a(float p_82400_1_, float p_82400_2_, float p_82400_3_) {
        float f3;

        for (f3 = p_82400_2_ - p_82400_1_; f3 < -180.0F; f3 += 360.0F) {
            ;
        }

        while (f3 >= 180.0F) {
            f3 -= 360.0F;
        }

        return p_82400_1_ + p_82400_3_ * f3;
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return RenderVillagerBlock.instance.res;
    }

}
