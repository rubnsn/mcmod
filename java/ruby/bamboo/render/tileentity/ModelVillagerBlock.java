package ruby.bamboo.render.tileentity;

import net.minecraft.client.model.ModelVillager;
import net.minecraft.entity.Entity;

public class ModelVillagerBlock extends ModelVillager {

    public ModelVillagerBlock(float texOffset) {
        super(texOffset, 0.0F, 64, 64);
    }

    @Override
    public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
        this.villagerHead.rotateAngleY = 180 / (180F / (float) Math.PI);
        this.villagerHead.rotateAngleX = 180 / (180F / (float) Math.PI);
        this.villagerHead.render(p_78088_7_);
    }

}
