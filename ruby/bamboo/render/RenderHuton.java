package ruby.bamboo.render;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import ruby.bamboo.entity.EntityHuton;

public class RenderHuton extends Render
{
    public String tex;
    private ModelHuton modelHuton;
    private static ResourceLocation MAKURA = new ResourceLocation("textures/entitys/makura.png");
    private static ResourceLocation HUTON = new ResourceLocation("textures/entitys/huton.png");
    public RenderHuton()
    {
        // TODO 自動生成されたコンストラクター・スタブ
        modelHuton = new ModelHuton();
    }

    public void renderHuton(EntityHuton entity, double d, double d1, double d2,
                            float f, float f1, boolean b)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
        byte dir = entity.getDir();

        switch (dir)
        {
            case 0:
                //GL11.glTranslatef((float)d+0.5F, (float)d1, (float)d2+1F);
                dir = 1;
                break;

            case 1:
                //GL11.glTranslatef((float)d, (float)d1, (float)d2+0.5F);
                dir = 0;
                break;

            case 2:
                //GL11.glTranslatef((float)d+0.5F, (float)d1, (float)d2);
                dir = 3;
                break;

            case 3:
                //GL11.glTranslatef((float)d+1F, (float)d1, (float)d2+0.5F);
                dir = 2;
                break;
        }

        GL11.glRotatef(dir * 90.0F, 0.0F, 1.0F, 0.0F);

        if (b)
        {
            //loadTexture("/textures/entitys/huton.png");
            func_110777_b(entity, b);
            modelHuton.renderFoot(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        }
        else
        {
            //loadTexture("/textures/entitys/makura.png");
            func_110777_b(entity, b);
            modelHuton.renderHead(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        }

        GL11.glPopMatrix();
    }

    @Override
    public void doRender(Entity entity, double d, double d1, double d2,
                         float f, float f1)
    {
        renderHuton((EntityHuton)entity, d, d1, d2, f, f1, true);
        renderHuton((EntityHuton)entity, d, d1, d2, f, f1, false);
    }

    protected void func_110777_b(Entity par1Entity, boolean b)
    {
        if (b)
        {
            this.bindTexture(HUTON);
        }
        else
        {
            this.bindTexture(MAKURA);
        }
    }
    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return MAKURA;
    }
}
