package ruby.bamboo.render.magatama;

import org.lwjgl.opengl.*;
import ruby.bamboo.entity.magatama.EntityClock;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderClock extends Render {
    private ResourceLocation GEAR = new ResourceLocation("textures/entitys/gear.png");
    private ResourceLocation SECOND_HAND = new ResourceLocation("textures/entitys/secondhand.png");
    private ResourceLocation CLOCK_NEEDLE = new ResourceLocation("textures/entitys/clockneedle.png");

    public RenderClock() {
    }

    @Override
    public void doRender(Entity entity, double posX, double posY, double posZ, float f, float f1) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) posX, (float) posY, (float) posZ);
        GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glScalef(0.75F, 0.75F, 0.75F);
        GL11.glDisable(GL11.GL_LIGHTING);
        this.renderClock((EntityClock) entity, posX, posY, posZ, ((EntityClock) entity).height / 2, ((EntityClock) entity).getTime());
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    private void renderClock(EntityClock entity, double posX, double posY, double posZ, float size, int time) {
        float hour = 360 * time / 12000F;
        float minute = 360 * (time % 1000) / 1000F;
        float second = 360 * (time % 17) / 17F;
        GL11.glColor3f(1, 1, 1);
        GL11.glPushMatrix();
        GL11.glRotatef(-minute - 180, 0, 0, 1F);
        this.bindTexture(CLOCK_NEEDLE);
        float a = 0.0625F;
        float b = -1.56F;
        float depth = 0.3F;
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex3f(-size / 4 + a, -size / 2 + b, depth);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex3f(size / 4 + a, -size / 2 + b, depth);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex3f(size / 4 + a, size / 2 + b, depth);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex3f(-size / 4 + a, size / 2 + b, depth);
        GL11.glEnd();
        GL11.glPopMatrix();
        depth = 0.25F;
        GL11.glPushMatrix();
        GL11.glRotatef(-hour - 180, 0, 0, 1F);
        GL11.glScalef(0.9F, 0.7F, 0.9F);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex3f(-size / 4 + a, -size / 2 + b, depth);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex3f(size / 4 + a, -size / 2 + b, depth);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex3f(size / 4 + a, size / 2 + b, depth);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex3f(-size / 4 + a, size / 2 + b, depth);
        GL11.glEnd();
        GL11.glPopMatrix();
        depth = 0.35F;
        this.bindTexture(SECOND_HAND);
        GL11.glPushMatrix();
        GL11.glRotatef(-second - 180, 0, 0, 1F);
        GL11.glScalef(1, 1, 1);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex3f(-size / 4 + a, -size / 2 + b, depth);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex3f(size / 4 + a, -size / 2 + b, depth);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex3f(size / 4 + a, size / 2 + b, depth);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex3f(-size / 4 + a, size / 2 + b, depth);
        GL11.glEnd();
        GL11.glPopMatrix();

        /*
         * this.func_110776_a(GEAR); a=0.00F; b=-0.00F; GL11.glPushMatrix();
         * GL11.glRotatef(-second-180, 0,0, 1F); GL11.glScalef(1, 1, 1);
         * GL11.glBegin(GL11.GL_QUADS); GL11.glTexCoord2f(0, 0);
         * GL11.glVertex3f(-size/4+a,-size/4+b,depth); GL11.glTexCoord2f(1, 0);
         * GL11.glVertex3f(size/4+a,-size/4+b,depth); GL11.glTexCoord2f(1, 1);
         * GL11.glVertex3f(size/4+a,size/4+b,depth); GL11.glTexCoord2f(0, 1);
         * GL11.glVertex3f(-size/4+a,size/4+b,depth); GL11.glEnd();
         * GL11.glPopMatrix();
         */
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

}
