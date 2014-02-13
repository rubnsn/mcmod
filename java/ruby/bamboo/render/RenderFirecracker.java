package ruby.bamboo.render;

import net.minecraft.entity.Entity;
import net.minecraft.util.IIcon;
import ruby.bamboo.BambooInit;
import ruby.bamboo.entity.EntityFirecracker;

public class RenderFirecracker extends RenderThrowable {

    @Override
    public IIcon getIcon(Entity par1Entity) {
        return BambooInit.firecracker.getIconFromDamage(((EntityFirecracker) par1Entity).getExplodeLv() - 1);
    }

}
