package ruby.bamboo.tileentity;

import net.minecraft.entity.EntityLivingBase;

public interface ITileEntitySpa {
    public void addColor(int deycolor);

    public int getColor();

    public boolean isStay();

    public int[] getParentPosition();

    public void onEntityCollision(EntityLivingBase entity);

    public void colorUpdate();

    public boolean isTickScheduled();

    public void setTickSchedule(boolean flg);

    public int getLastTickMeta();

    public void setLastTickMeta(int meta);
}
