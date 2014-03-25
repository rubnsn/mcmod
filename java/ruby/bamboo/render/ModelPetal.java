package ruby.bamboo.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import ruby.bamboo.entity.EntitySakuraPetal;

public class ModelPetal extends ModelBase {
    // variables init:
    public final ModelRenderer[] box = new ModelRenderer[16];

    public ModelPetal() {
        int x = 0;
        int y = 0;
        for (int i = 0; i < box.length; i++) {
            box[i] = new ModelRenderer(this, x, y);
            box[i].addBox(0F, 0F, 0F, 5, 5, 0);
            if (x < 60) {
                x += 10;
            } else {
                x = 0;
                y += 5;
            }
        }
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        box[((EntitySakuraPetal) entity).getTexNum()].render(f5);
    }
}
