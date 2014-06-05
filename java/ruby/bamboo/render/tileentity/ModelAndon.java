package ruby.bamboo.render.tileentity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelAndon extends ModelBase {
    // variables init:
    public ModelRenderer box;
    public ModelRenderer box0;
    public ModelRenderer box1;
    public ModelRenderer box2;
    public ModelRenderer box3;
    public ModelRenderer box4;
    public ModelRenderer box5;

    // constructor:
    public ModelAndon() {
        // TODO 自動生成されたコンストラクター・スタブ
        box = new ModelRenderer(this, 0, 0);
        box.addBox(-3F, 0F, -3F, 6, 10, 6);
        box.setRotationPoint(0F, 4F, 0F);
        box0 = new ModelRenderer(this, 24, 0);
        box0.addBox(-1F, 0F, -1F, 1, 16, 1);
        box0.setRotationPoint(-3F, 0F, -3F);
        box1 = new ModelRenderer(this, 24, 0);
        box1.addBox(-1F, 0F, -1F, 1, 16, 1);
        box1.setRotationPoint(4F, 0F, -3F);
        box2 = new ModelRenderer(this, 24, 0);
        box2.addBox(-1F, 0F, -1F, 1, 16, 1);
        box2.setRotationPoint(-3F, 0F, 4F);
        box3 = new ModelRenderer(this, 24, 0);
        box3.addBox(-1F, 0F, -1F, 1, 16, 1);
        box3.setRotationPoint(4F, 0F, 4F);
        box4 = new ModelRenderer(this, 0, 16);
        box4.addBox(-4F, -1F, -4F, 8, 1, 8);
        box4.setRotationPoint(0F, 4F, 0F);
        box5 = new ModelRenderer(this, 0, 16);
        box5.addBox(-4F, 1F, -4F, 8, 1, 8);
        box5.setRotationPoint(0F, 13F, 0F);
    }

    public void renderAndon() {
        render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        box.render(f5);
        box0.render(f5);
        box1.render(f5);
        box2.render(f5);
        box3.render(f5);
        box4.render(f5);
        box5.render(f5);
    }

    // render:

}
