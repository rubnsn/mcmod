package ruby.bamboo.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelManeki extends ModelBase
{
    ModelRenderer body;
    ModelRenderer earR;
    ModelRenderer earL;
    ModelRenderer hand;

    public ModelManeki()
    {
        textureWidth = 64;
        textureHeight = 32;
        body = new ModelRenderer(this, 0, 0);
        body.addBox(-3.5F, -8F, -3.5F, 7, 12, 7);
        body.setRotationPoint(0F, 0F, 0F);
        body.setTextureSize(64, 32);
        body.mirror = true;
        setRotation(body, 0F, 0F, 0F);
        earR = new ModelRenderer(this, 29, 0);
        earR.addBox(0.5F, 0F, 1F, 3, 2, 1);
        earR.setRotationPoint(0F, 4F, -1F);
        earR.setTextureSize(64, 32);
        earR.mirror = true;
        setRotation(earR, 0F, 0F, 0F);
        earL = new ModelRenderer(this, 29, 4);
        earL.addBox(-3.5F, 0F, 1F, 3, 2, 1);
        earL.setRotationPoint(0F, 4F, -1F);
        earL.setTextureSize(64, 32);
        earL.mirror = true;
        setRotation(earL, 0F, 0F, 0F);
        hand = new ModelRenderer(this, 29, 8);
        hand.addBox(-1F, -1F, -1F, 2, 7, 2);
        hand.setRotationPoint(-4.5F, -1F, 0F);
        hand.setTextureSize(64, 32);
        hand.mirror = true;
        setRotation(hand, 0F, 0F, 0F);
    }
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        body.render(f5);
        earR.render(f5);
        earL.render(f5);
    }
    public void renderHand(float x)
    {
        hand.rotateAngleX = (float)((Math.PI * x) / 180);
        hand.render(0.0625F);
    }
    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }
}
