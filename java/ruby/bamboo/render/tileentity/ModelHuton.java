package ruby.bamboo.render.tileentity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelHuton extends ModelBase {
    // variables init:
    public ModelRenderer box0;
    public ModelRenderer box1;
    public ModelRenderer box2;
    public ModelRenderer box3;

    // constructor:

    // render:

    // constructor:
    public ModelHuton() {
        box0 = new ModelRenderer(this, 0, 0);
        box0.addBox(-16F, 0F, -8F, 8, 2, 16);
        box1 = new ModelRenderer(this, 0, 0);
        box1.addBox(-12F, -2F, -8F, 12, 4, 16);
        box1.setRotationPoint(4F, 2F, 0F);
        box1.rotateAngleY = (float) Math.PI;
        box2 = new ModelRenderer(this, 0, 0);
        box2.addBox(-12F, -2F, -8F, 12, 4, 16);
        box2.setRotationPoint(4F, 2F, 0F);
        box3 = new ModelRenderer(this, 0, 19);
        box3.addBox(-14F, 2F, -5F, 6, 3, 10);
    }

    public void renderHead(float f5) {
        box0.render(f5);
        box3.render(f5);
    }

    public void renderFoot(float f5) {
        box1.render(f5);
        box2.render(f5);
    }
}
