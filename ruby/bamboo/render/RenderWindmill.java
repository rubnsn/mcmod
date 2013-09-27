package ruby.bamboo.render;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import ruby.bamboo.entity.EntityWindmill;

public class RenderWindmill extends Render
{
    private ModelWindmill model;
    private static ResourceLocation textuer[];
    public RenderWindmill()
    {
        model = new ModelWindmill();
        textuer = new ResourceLocation[2];
        textuer[0] = new ResourceLocation("textures/entitys/windmill.png");
        textuer[1] = new ResourceLocation("textures/entitys/windmill_cloth.png");
    }
    @Override
    public void doRender(Entity var1, double var2, double var4, double var6,
                         float var8, float var9)
    {
        renderWindmill((EntityWindmill) var1, var2, var4, var6, var8, var9);
    }
    public void renderWindmill(EntityWindmill var1, double var2, double var4, double var6,
                               float var8, float var9)
    {
        GL11.glPushMatrix();
        func_110777_b(var1);
        byte dir = var1.getDir();

        if (dir == 0 || dir == 2)
        {
            if (dir == 0)
            {
                GL11.glTranslatef((float)var2, (float)var4, (float)var6 - (var1.getSize() - 1) / 2F);
            }
            else
            {
                GL11.glTranslatef((float)var2, (float)var4, (float)var6 + (var1.getSize() - 1) / 2F);
            }
        }
        else
        {
            if (dir == 3)
            {
                GL11.glTranslatef((float)var2 - (var1.getSize() - 1) / 2F, (float)var4, (float)var6);
            }
            else
            {
                GL11.glTranslatef((float)var2 + (var1.getSize() - 1) / 2F, (float)var4, (float)var6);
            }
        }

        GL11.glScalef(var1.getSize(), var1.getSize(), var1.getSize());
        dir += dir == 1 || dir == 3 ? 2 : 0;
        GL11.glRotatef(dir * 90, 0, 1, 0);
        GL11.glRotatef(var1.getRoll(), 0, 0, 1);
        model.setBladeNum(var1.getBladeNum());
        model.render(var1, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }
    @Override
    protected ResourceLocation func_110775_a(Entity entity)
    {
        // TODO 自動生成されたメソッド・スタブ
        return textuer[((EntityWindmill)entity).getTexNum()];
    }
}
