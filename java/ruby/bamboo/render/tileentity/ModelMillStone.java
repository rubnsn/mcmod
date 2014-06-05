package ruby.bamboo.render.tileentity;

import ruby.bamboo.tileentity.TileEntityMillStone;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelMillStone extends ModelBase {
    // fields
    ModelRenderer bottm;
    ModelRenderer top;

    public ModelMillStone() {
        textureWidth = 64;
        textureHeight = 64;
        bottm = new ModelRenderer(this, 0, 25);
        bottm.addBox(-8F, 0F, -8F, 16, 8, 16);
        bottm.setRotationPoint(0F, 0F, 0F);
        bottm.setTextureSize(64, 64);
        bottm.mirror = true;
        setRotation(bottm, 0F, 0F, 0F);
        top = new ModelRenderer(this, 0, 0);
        top.addBox(-8F, -8F, -8F, 16, 8, 16);
        top.setRotationPoint(0F, 0F, 0F);
        top.setTextureSize(64, 64);
        top.mirror = true;
        setRotation(top, 0F, 0F, 0F);
    }

    public void render(TileEntityMillStone entity, float f, float f1, float f2, float f3, float f4, float f5) {
        bottm.rotateAngleY = (float) (Math.PI * entity.getRoll()) / 180;
        bottm.render(f5);
        top.render(f5);
    }

    public void renderInv() {
        bottm.rotateAngleY = 0;
        bottm.render(0.0625F);
        top.render(0.0625F);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
