package ruby.bamboo.render;

import ruby.bamboo.BambooInit;
import ruby.bamboo.entity.EntityFirecracker;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.Icon;

public class RenderFirecracker extends RenderThrowable{

    @Override
    public Icon getIcon(Entity par1Entity) {
	return Item.itemsList[BambooInit.firecrackerIID].getIconFromDamage(((EntityFirecracker)par1Entity).getExplodeLv() - 1);
    }

}
