package ruby.bamboo.render.magatama;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;

import ruby.bamboo.entity.EntityFirecracker;
import ruby.bamboo.entity.magatama.EntityFlareEffect;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderFlareEffect extends Render {

	private static final Sphere sphere;
	static{
		sphere = new Sphere();
	}

	public RenderFlareEffect() {
		//s.setTextureFlag(true);
	}

	@Override
	public void doRender(Entity entity, double d0, double d1, double d2,
			float f, float f1) {
		render((EntityFlareEffect) entity, d0, d1, d2);
	}

	private void render(EntityFlareEffect entity, double posX, double posY,
			double posZ) {
		glPushMatrix();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		glTranslatef((float) posX, (float) posY, (float) posZ);
		float scale = entity.getExplodeSize();
		glScalef(scale, scale, scale);
		GL11.glColor4f(1F, 0.8F, 0.2F, entity.getAlpha());
		glRotatef(90.0f, 1f, 0, 0);
		sphere.draw(0.5f, 8, 8);
		glPopMatrix();
	}

	@Override
	protected ResourceLocation func_110775_a(Entity entity) {
		return null;
	}

}
