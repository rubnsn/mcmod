package ruby.bamboo.render;

import java.util.HashMap;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import ruby.bamboo.entity.EntitySakuraPetal;
public class RenderPetal extends Render
{
    private ModelPetal modelPetal;
    private static HashMap<String, ResourceLocation> resMap = new HashMap<String, ResourceLocation>();
    public RenderPetal()
    {
        // TODO 自動生成されたコンストラクター・スタブ
        modelPetal = new ModelPetal();
    }
    public void renderPetal(EntitySakuraPetal entity, double d, double d1, double d2,
                            float f, float f1)
    {
        // TODO 自動生成されたメソッド・スタブ
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
        //loadTexture(entity.getTexPath());
        func_110777_b(entity);
        /*GL11.glRotatef(180F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-renderManager.playerViewX, 1.0F, 0.0F, 0.0F);*/
        GL11.glColor3f(entity.getRedColorF(), entity.getGreenColorF(), entity.getBlueColorF());
        GL11.glScalef(0.5f,	0.5f, 0.5f);
        GL11.glRotatef(180.0F, entity.getRx(), entity.getRy(), entity.getRz());
        modelPetal.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }
    @Override
    public void doRender(Entity entity, double d, double d1, double d2,
                         float f, float f1)
    {
        renderPetal((EntitySakuraPetal)entity, d, d1, d2, f, f1);
    }
    @Override
    protected ResourceLocation func_110775_a(Entity entity)
    {
        if (!resMap.containsKey(((EntitySakuraPetal)entity).getTexPath()))
        {
            resMap.put(((EntitySakuraPetal)entity).getTexPath(), new ResourceLocation(((EntitySakuraPetal)entity).getTexPath()));
        }

        return resMap.get(((EntitySakuraPetal)entity).getTexPath());
    }
}
