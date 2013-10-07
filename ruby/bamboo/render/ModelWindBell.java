package ruby.bamboo.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelWindBell extends ModelBase
{
    //variables init:
    public ModelRenderer box;
    public ModelRenderer box0;

    public ModelWindBell()
    {
        box = new ModelRenderer(this, 0, 0);
        box.addBox(-2.5F, -6F, -2.5F, 5, 4, 5);
        box.setRotationPoint(0F, 16F, 0F);
        box0 = new ModelRenderer(this, 0, 15);
        box0.addBox(-2F, -8F, 0F, 4, 8, 0);
        box0.setRotationPoint(0F, 9F, 0F);
    }

    @Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        box.render(f5);
        box0.render(f5);
    }
    public void windAct(float x, float y, float z)
    {
        box0.rotateAngleX = x;
        box0.rotateAngleY = y;
        box0.rotateAngleZ = z;
    }
}
