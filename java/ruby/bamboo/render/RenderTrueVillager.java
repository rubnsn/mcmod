package ruby.bamboo.render;

import net.minecraft.client.renderer.entity.RenderVillager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.IBossDisplayData;

public class RenderTrueVillager extends RenderVillager {

    public RenderTrueVillager() {
        this.mainModel = this.villagerModel = new ModelTrueVillager(0.0F);
    }

    @Override
    public void doRender(Entity entity, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        BossStatus.setBossStatus((IBossDisplayData) entity, false);
        super.doRender(entity, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);

    }
}
