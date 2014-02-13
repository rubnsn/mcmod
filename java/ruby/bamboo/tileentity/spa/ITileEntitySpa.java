package ruby.bamboo.tileentity.spa;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;

public interface ITileEntitySpa {
    public void addColor(int deycolor);

    public int getColor();

    public boolean isStay();

    public int[] getParentPosition();

    public void onEntityLivingCollision(EntityLivingBase entity);

    public void onEntityItemCollision(EntityItem entity);

    public void colorUpdate();

    public boolean isTickScheduled();

    public void setTickSchedule(boolean flg);

    public int getLastTickMeta();

    public void setLastTickMeta(int meta);
}
