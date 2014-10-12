package ruby.bamboo.render.tileentity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelVillagerBlock extends ModelBase {
    public ModelRenderer villagerHead;

    public ModelVillagerBlock() {
        this.villagerHead = (new ModelRenderer(this)).setTextureSize(64, 64);
        this.villagerHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.villagerHead.setTextureOffset(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, 0);
        ModelRenderer villagerNose = (new ModelRenderer(this)).setTextureSize(64, 64);
        villagerNose.setRotationPoint(0.0F, 0 - 2.0F, 0.0F);
        villagerNose.setTextureOffset(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2, 4, 2, 0);
        this.villagerHead.addChild(villagerNose);
    }

    @Override
    public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        this.villagerHead.render(p_78088_7_);
    }

    @Override
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
        super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);
        this.villagerHead.rotateAngleY = p_78087_4_ / (180F / (float) Math.PI);
        this.villagerHead.rotateAngleX = p_78087_5_ / (180F / (float) Math.PI);
    }
}
