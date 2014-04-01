package ruby.bamboo.render;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import ruby.bamboo.BambooCore;

public class RenderKaguya extends RenderLiving {

    private static final ResourceLocation kaguyaTex = new ResourceLocation(BambooCore.resourceDomain + "textures/entitys/kaguya.png");
    protected ModelKaguya villagerModel;
    private static final String __OBFID = "CL_00001032";

    public RenderKaguya() {
        super(new ModelKaguya(), 0.5F);
        this.villagerModel = (ModelKaguya) this.mainModel;
    }

    protected int shouldRenderPass(EntityVillager par1EntityVillager, int par2, float par3) {
        return -1;
    }

    public void doRender(EntityVillager par1EntityVillager, double par2, double par4, double par6, float par8, float par9) {
        super.doRender((EntityLiving) par1EntityVillager, par2, par4, par6, par8, par9);
    }

    protected ResourceLocation getEntityTexture(EntityVillager par1EntityVillager) {
        switch (par1EntityVillager.getProfession()) {
        default:
            return kaguyaTex;
        }
    }

    protected void renderEquippedItems(EntityVillager par1EntityVillager, float par2) {
        super.renderEquippedItems(par1EntityVillager, par2);
    }

    protected void preRenderCallback(EntityVillager par1EntityVillager, float par2) {
        float f1 = 0.9375F;

        if (par1EntityVillager.getGrowingAge() < 0) {
            f1 = (float) ((double) f1 * 0.5D);
            this.shadowSize = 0.25F;
        } else {
            this.shadowSize = 0.5F;
        }

        GL11.glScalef(f1, f1, f1);
    }

    @Override
    public void doRender(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {

        this.doRender((EntityVillager) par1EntityLiving, par2, par4, par6, par8, par9);

    }

    @Override
    protected void preRenderCallback(EntityLivingBase par1EntityLivingBase, float par2) {
        this.preRenderCallback((EntityVillager) par1EntityLivingBase, par2);
    }

    @Override
    protected int shouldRenderPass(EntityLivingBase par1EntityLivingBase, int par2, float par3) {
        return this.shouldRenderPass((EntityVillager) par1EntityLivingBase, par2, par3);
    }

    @Override
    protected void renderEquippedItems(EntityLivingBase par1EntityLivingBase, float par2) {
        this.renderEquippedItems((EntityVillager) par1EntityLivingBase, par2);
    }

    @Override
    public void doRender(EntityLivingBase par1Entity, double par2, double par4, double par6, float par8, float par9) {

        this.doRender((EntityVillager) par1Entity, par2, par4, par6, par8, par9);

    }

    @Override
    protected ResourceLocation getEntityTexture(Entity par1Entity) {
        return this.getEntityTexture((EntityVillager) par1Entity);
    }

    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
        this.doRender((EntityVillager) par1Entity, par2, par4, par6, par8, par9);
    }
}
