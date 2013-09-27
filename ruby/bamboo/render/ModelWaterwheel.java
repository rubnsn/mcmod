package ruby.bamboo.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelWaterwheel extends ModelBase
{
    public ModelRenderer box;
    public ModelRenderer box0;
    public ModelRenderer box1;
    public ModelRenderer box2;
    public ModelRenderer box3;
    public ModelRenderer box4;
    private final float[] rot = new float[12];
    private final float[] baqet = new float[12];
    public ModelWaterwheel()
    {
        box = new ModelRenderer(this, 0, 0);
        box.addBox(-8F, -3F, -3F, 14, 6, 6);
        box0 = new ModelRenderer(this, 58, 0);
        box0.addBox(4F, 2F, -1F, 1, 30, 2);
        box1 = new ModelRenderer(this, 58, 0);
        box1.addBox(-4F, 2F, -1F, 1, 30, 2);
        box2 = new ModelRenderer(this, 45, 10);
        box2.addBox(-2F, 21F, -1F, 5, 10, 1);
        box3 = new ModelRenderer(this, 26, 7);
        box3.addBox(3F, 20F, -5F, 1, 10, 15);
        box3.rotateAngleX = (float)(Math.PI * 20) / 180;
        box4 = new ModelRenderer(this, 26, 7);
        box4.addBox(-3F, 20F, -5F, 1, 10, 15);
        box4.rotateAngleX = (float)(Math.PI * 20) / 180;

        for (int i = 0; i < 12; i++)
        {
            rot[i] = (float)(Math.PI * i * 30) / 180;
            baqet[i] = (float)(Math.PI * (i * 30 - 20)) / 180;
        }
    }
    @Override
    public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        box.render(par7);

        for (int i = 0; i < 12; i++)
        {
            box0.rotateAngleX = rot[i];
            box1.rotateAngleX = rot[i];
            box2.rotateAngleX = rot[i];
            box3.rotateAngleX = baqet[i];
            box4.rotateAngleX = baqet[i];
            box0.render(par7);
            box1.render(par7);
            box2.render(par7);
            box3.render(par7);
            box4.render(par7);
        }
    }
}
