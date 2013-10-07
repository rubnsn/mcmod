package ruby.bamboo.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPetal extends ModelBase
{
    //variables init:
    private float x, y, z;
    public ModelRenderer box;
    public ModelPetal()
    {
        box = new ModelRenderer(this, 0, 0);
        box.addBox(0F, 0F, 0F, 5, 5, 0);
    }

    @Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        box.render(f5);
    }
}
