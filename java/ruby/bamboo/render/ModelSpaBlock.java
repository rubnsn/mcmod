package ruby.bamboo.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelSpaBlock extends ModelBase {
    // variables init:
    public ModelRenderer box;

    public ModelSpaBlock() {
        // constructor:
        box = new ModelRenderer(this, 0, 0);
        box.addBox(-8F, -10F, -8F, 16, 10, 16);
        box.rotateAngleZ = (float) Math.PI;
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        // render:
        box.render(f5);
    }

    public void render() {
        box.render(0.0625F);
    }
}
