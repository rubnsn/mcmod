package ruby.bamboo.render;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelWindmill extends ModelBase
{
    public ModelRenderer box;
    public ModelRenderer box1;
    public ModelRenderer box4;
    public ModelRenderer box8;
    private int bladeNum;
    private final Object[] blades;
    //private Map<Integer,float[]> blades;

    public ModelWindmill()
    {
        box = new ModelRenderer(this, 0, 0);
        box.addBox(-3F, -3F, -2F, 6, 6, 4);
        box1 = new ModelRenderer(this, 55, 0);
        box1.addBox(-1F, 3F, -1F, 2, 30, 2);
        box4 = new ModelRenderer(this, 24, 0);
        box4.addBox(1F, 3F, -1F, 10, 30, 1);
        box4.rotateAngleY = (float)(Math.PI * -12) / 180;
        box8 = new ModelRenderer(this, 0, 12);
        box8.addBox(-2F, -2F, 0F, 4, 4, 8);
        blades = new Object[5];

        for (int i = 4; i <= 8; i++)
        {
            float[] temp = new float[i];

            for (int j = 0; j < i; j++)
            {
                temp[j] = (float)(Math.PI * 360 / i * j) / 180;
            }

            blades[i - 4] = temp;
        }
    }
    public void setBladeNum(int i)
    {
        bladeNum = i;
    }
    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        box.render(f5);

        for (int i = 0; i < bladeNum; i++)
        {
            //box1.rotateAngleZ=(float)(Math.PI * 360/bladeNum * i) / 180;
            //box4.rotateAngleZ=(float)(Math.PI * 360/bladeNum * i) / 180;
            box1.rotateAngleZ = box4.rotateAngleZ = ((float[])blades[bladeNum - 4])[i];
            box1.render(f5);
            box4.render(f5);
        }

        box8.render(f5);
    }
}
