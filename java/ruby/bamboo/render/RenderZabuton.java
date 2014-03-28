package ruby.bamboo.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import ruby.bamboo.BambooCore;
import ruby.bamboo.entity.EntityZabuton.EnumZabutonColor;
import ruby.bamboo.entity.IZabuton;

public class RenderZabuton extends Render {

    private class ModelZabuton extends ModelBase {
        private final ModelRenderer box;
        private final ModelRenderer[] pattern;

        private ModelZabuton() {
            box = new ModelRenderer(this, 0, 0).addBox(0, 0, 0, 14, 2, 14);

            box.rotateAngleZ = (float) Math.PI;
            box.setRotationPoint(7F, 2F, -7F);
            pattern = new ModelRenderer[4];
            for (int i = 0; i < pattern.length; i++) {
                pattern[i] = new ModelRenderer(this, i * 14, 16).addBox(0, 0, 0, 14, 0, 14);
                pattern[i].rotateAngleZ = (float) Math.PI;
                pattern[i].setRotationPoint(7F, 2F, -7F);
                pattern[i].offsetY = 0.0001F;
            }
        }

        @Override
        public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
            box.render(par7);
        }

        public void renderPattern(int num) {
            this.pattern[num].render(0.0625F);
        }
    }

    private final ModelZabuton model = new ModelZabuton();
    private static final ResourceLocation ZABUTON = new ResourceLocation(BambooCore.resourceDomain + "textures/entitys/zabuton.png");

    @Override
    public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9) {
        GL11.glPushMatrix();
        this.bindTexture(getEntityTexture(var1));
        GL11.glTranslatef((float) var2, (float) var4, (float) var6);
        GL11.glRotatef(180.0F - var1.rotationYaw, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(180.0F - var1.rotationPitch, 0.0F, 0.0F, 1.0F);
        EnumZabutonColor color = EnumZabutonColor.getColor(((IZabuton) var1).getColor());
        GL11.glColor3f(color.getRed() / (float) (0xFF), color.getGreen() / (float) (0xFF), color.getBlue() / (float) (0xFF));
        model.render(var1, (float) var2, (float) var4, (float) var6, var8, var9, 0.0625F);

        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity var1) {
        return ZABUTON;
    }

}
