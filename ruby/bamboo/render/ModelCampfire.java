package ruby.bamboo.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelCampfire extends ModelBase {
    // variables init:
    public ModelRenderer bone;
    public ModelRenderer potRod;
    public ModelRenderer fish;
    public ModelRenderer meat;
    public ModelRenderer pot;
    public ModelRenderer rod;
    public ModelRenderer rod2;
    public ModelRenderer wood;
    private float rotY[] = new float[8];
    private float rotX[] = new float[8];
    private float fishRotY[] = new float[4];

    public ModelCampfire() {
        bone = new ModelRenderer(this, 0, 30);
        bone.addBox(-8F, -0.5F, -0.5F, 16, 1, 1);
        bone.rotateAngleX = ((float) Math.PI * 2F);
        potRod = new ModelRenderer(this, 0, 30);
        potRod.addBox(-8F, 9F, -0.5F, 16, 1, 1);
        potRod.rotateAngleX = ((float) Math.PI * 2F);
        fish = new ModelRenderer(this, 0, 17);
        fish.addBox(-1F, -12F, 5F, 3, 13, 0);
        fish.setRotationPoint(0F, 10F, 0F);
        fish.rotateAngleX = (float) (Math.PI * -20) / 180;
        // fish.rotateAngleY = ((float)Math.PI / 4F);
        meat = new ModelRenderer(this, 8, 18);
        meat.addBox(-5F, -3F, -3F, 10, 6, 6);
        pot = new ModelRenderer(this, 6, 0);
        pot.addBox(-5F, 4F, -5F, 10, 8, 10);
        rod = new ModelRenderer(this, 0, 0);
        rod.addBox(-7F, 0F, -1F, 1, 15, 2);
        rod2 = new ModelRenderer(this, 0, 0);
        rod2.addBox(-7F, 0F, -1F, 1, 15, 2);
        rod2.rotateAngleY = (float) (Math.PI * 180) / 180;
        wood = new ModelRenderer(this, 44, 22);
        wood.addBox(-1F, 0F, -1F, 2, 2, 8);
        wood.setRotationPoint(0F, 1F, 0F);
        wood.rotateAngleX = (float) (Math.PI * 20) / 180;
        meat.addChild(bone);

        for (int i = 0; i < 7; i++) {
            rotY[i] = (float) (Math.PI * 360 / 7 * i) / 180;
            rotX[i] = (i % 2 == 0) ? (float) (Math.PI * 10) / 180 : (float) (Math.PI * 20) / 180;
        }

        for (int i = 0; i < 4; i++) {
            fishRotY[i] = (float) (Math.PI * (90 * i + 45)) / 180;
        }
    }

    public void renderWood() {
        /*
         * bone.render(par7); fish.render(par7); meat.render(par7);
         * pot.render(par7); rod.render(par7);
         */
        // wood.rotateAngleX=(float)(Math.PI * 360/20) / 180;
        wood.setRotationPoint(0F, 1F, 0F);

        for (int i = 0; i < 7; i++) {
            wood.rotateAngleY = rotY[i];
            wood.rotateAngleX = rotX[i];
            wood.render(0.0625F);
        }

        // renderPot();
    }

    public void renderRods() {
        rod.render(0.0625F);
        rod2.render(0.0625F);
    }

    public void renderMeat(int roll) {
        meat.setRotationPoint(0F, 11F, 0F);
        meat.rotateAngleX = (float) (Math.PI * roll) / 180;
        meat.render(0.0625F);
    }

    public void renderFish() {
        for (int i = 0; i < 4; i++) {
            fish.rotateAngleY = fishRotY[i];
            fish.render(0.0625F);
        }
    }

    public void renderPot() {
        potRod.render(0.0625F);
        pot.render(0.0625F);
    }
    // render:
}
