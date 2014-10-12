package ruby.bamboo.render;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelVillager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import ruby.bamboo.entity.villager.EntityTrueVillager;

public class ModelTrueVillager extends ModelVillager {

    public ModelTrueVillager(float p_i1163_1_) {
        this(p_i1163_1_, 0.0F, 64, 64);
    }

    public ModelTrueVillager(float p_i1164_1_, float p_i1164_2_, int p_i1164_3_, int p_i1164_4_) {
        super(p_i1164_1_, p_i1164_2_, p_i1164_3_, p_i1164_4_);
        this.textureWidth = 256;
        this.textureHeight = 256;
        this.setTextureOffset("wing.bone", 112, 88);
        this.setTextureOffset("wing.skin", -56, 88);
        this.setTextureOffset("wingtip.bone", 112, 136);
        this.setTextureOffset("wingtip.skin", -56, 144);
        this.wing = new ModelRenderer(this, "wing");
        this.wing.setRotationPoint(-12.0F, 5.0F, 2.0F);
        this.wing.addBox("bone", -56.0F, -4.0F, -4.0F, 56, 8, 8);
        this.wing.addBox("skin", -56.0F, 0.0F, 2.0F, 56, 0, 56);
        this.wingTip = new ModelRenderer(this, "wingtip");
        this.wingTip.setRotationPoint(-56.0F, 0.0F, 0.0F);
        this.wingTip.addBox("bone", -56.0F, -2.0F, -2.0F, 56, 4, 4);
        this.wingTip.addBox("skin", -56.0F, 0.0F, 2.0F, 56, 0, 56);
        this.wing.addChild(this.wingTip);
    }

    @Override
    public void render(Entity entity, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
        super.render(entity, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_);
        renderWing((EntityTrueVillager) entity, p_78088_7_);
    }

    private ModelRenderer wing;
    private ModelRenderer wingTip;
    private ResourceLocation res = new ResourceLocation("textures/entity/enderdragon/dragon.png");

    public void renderWing(EntityTrueVillager entity, float p_78088_7_) {
        RenderManager.instance.renderEngine.bindTexture(res);

        //render
        GL11.glPushMatrix();
        GL11.glTranslatef(0.2F, 0, 0.2F);
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        float f15;
        entity.wing = entity.wing < (float) Math.PI * 2.0F ? entity.wing + 0.01F : 0;
        for (int j = 0; j < 2; ++j) {
            GL11.glEnable(GL11.GL_CULL_FACE);
            f15 = entity.wing * (float) Math.PI * 2.0F;
            this.wing.rotateAngleX = 0.125F - (float) Math.cos((double) f15) * 0.2F;
            this.wing.rotateAngleY = 0.25F;
            this.wing.rotateAngleZ = (float) (Math.sin((double) f15) + 0.125D) * 0.8F;
            this.wingTip.rotateAngleZ = -((float) (Math.sin((double) (f15 + 2.0F)) + 0.5D)) * 0.75F;
            this.wing.render(p_78088_7_);
            GL11.glTranslatef(-0.8F, 0, 0.1F);
            GL11.glScalef(-1.0F, 1.0F, 1.0F);
            if (j == 0) {
                GL11.glCullFace(GL11.GL_FRONT);
            }
        }

        GL11.glCullFace(GL11.GL_BACK);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
}
