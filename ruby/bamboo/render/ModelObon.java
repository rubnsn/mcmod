package ruby.bamboo.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelObon extends ModelBase
{
    // fields
    ModelRenderer longbar;
    ModelRenderer longbar2;
    ModelRenderer board;
    ModelRenderer shortbar;
    ModelRenderer shortbar2;
    ModelRenderer foot1;
    ModelRenderer foot2;
    ModelRenderer foot3;
    ModelRenderer foot4;

    public ModelObon()
    {
        textureWidth = 64;
        textureHeight = 32;
        board = new ModelRenderer(this, 0, 17);
        board.addBox(-7F, 1F, -6F, 14, 1, 14);
        board.setRotationPoint(0F, 0F, 0F);
        board.setTextureSize(64, 32);
        board.mirror = true;
        setRotation(board, 0F, 0F, 0F);
        longbar = new ModelRenderer(this, 0, 0);
        longbar.addBox(-8F, 0F, 8F, 16, 2, 1);
        longbar.setRotationPoint(0F, 0F, 0F);
        longbar.setTextureSize(64, 32);
        longbar.mirror = true;
        setRotation(longbar, 0F, 0F, 0F);
        longbar2 = new ModelRenderer(this, 0, 0);
        longbar2.addBox(-8F, 0F, -7F, 16, 2, 1);
        longbar2.setRotationPoint(0F, 0F, 0F);
        longbar2.setTextureSize(64, 32);
        longbar2.mirror = true;
        setRotation(longbar2, 0F, 0F, 0F);
        shortbar = new ModelRenderer(this, 34, 0);
        shortbar.addBox(7F, 0F, -6F, 1, 2, 14);
        shortbar.setRotationPoint(0F, 0F, 0F);
        shortbar.setTextureSize(64, 32);
        shortbar.mirror = true;
        setRotation(shortbar, 0F, 0F, 0F);
        shortbar2 = new ModelRenderer(this, 34, 0);
        shortbar2.addBox(-8F, 0F, -6F, 1, 2, 14);
        shortbar2.setRotationPoint(0F, 0F, 0F);
        shortbar2.setTextureSize(64, 32);
        shortbar2.mirror = true;
        setRotation(shortbar2, 0F, 0F, 0F);
        foot1 = new ModelRenderer(this, 0, 4);
        foot1.addBox(5F, 2F, 6F, 3, 1, 3);
        foot1.setRotationPoint(0F, 0F, 0F);
        foot1.setTextureSize(64, 32);
        foot1.mirror = true;
        setRotation(foot1, 0F, 0F, 0F);
        foot2 = new ModelRenderer(this, 0, 4);
        foot2.addBox(-8F, 2F, 6F, 3, 1, 3);
        foot2.setRotationPoint(0F, 0F, 0F);
        foot2.setTextureSize(64, 32);
        foot2.mirror = true;
        setRotation(foot2, 0F, 0F, 0F);
        foot3 = new ModelRenderer(this, 0, 4);
        foot3.addBox(5F, 2F, -7F, 3, 1, 3);
        foot3.setRotationPoint(0F, 0F, 0F);
        foot3.setTextureSize(64, 32);
        foot3.mirror = true;
        setRotation(foot3, 0F, 0F, 0F);
        foot4 = new ModelRenderer(this, 0, 4);
        foot4.addBox(-8F, 2F, -7F, 3, 1, 3);
        foot4.setRotationPoint(0F, 0F, 0F);
        foot4.setTextureSize(64, 32);
        foot4.mirror = true;
        setRotation(foot4, 0F, 0F, 0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3,
                       float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        longbar.render(f5);
        longbar2.render(f5);
        board.render(f5);
        shortbar.render(f5);
        shortbar2.render(f5);
        foot1.render(f5);
        foot2.render(f5);
        foot3.render(f5);
        foot4.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3,
                                  float f4, float f5, Entity entity)
    {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }
}
