package ruby.bamboo.tileentity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import ruby.bamboo.Config;
import ruby.bamboo.entity.EntityDummyChair;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityHuton extends TileEntity {
    public boolean isOccupy = false;

    @Override
    public boolean canUpdate() {
        return false;
    }

    public void mountDummyChair(World world, int posX, int posY, int posZ, Entity entity) {
        if (!isOccupy) {
            EntityDummyChair dummyEntity = new EntityDummyChair(world) {
                private int timer = 0;
                private static final int timeacc = 1000;

                @Override
                public void updateDummy() {
                    if (Config.timeAccel) {
                        if (riddenByEntity instanceof EntityPlayer) {
                            if (timer++ > 100) {
                                worldObj.getWorldInfo().setWorldTime(worldObj.getWorldInfo().getWorldTime() + timeacc);
                                if (worldObj.getWorldInfo().isRaining()) {
                                    if (worldObj.getWorldInfo().getRainTime() > timeacc) {
                                        worldObj.getWorldInfo().setRainTime(worldObj.getWorldInfo().getRainTime() - timeacc);
                                    }
                                }
                                if (worldObj.getWorldInfo().isThundering()) {
                                    if (worldObj.getWorldInfo().getThunderTime() > timeacc) {
                                        worldObj.getWorldInfo().setThunderTime(worldObj.getWorldInfo().getThunderTime() - timeacc);
                                    }
                                }
                                int hour = (int) (((worldObj.getWorldInfo().getWorldTime() + 6000) % 24000) / 1000);
                                int minute = (int) (((worldObj.getWorldInfo().getWorldTime() + 6000) % 600) / 10);
                                ((EntityPlayer) riddenByEntity).addChatMessage(new ChatComponentText(hour + ":" + minute + (worldObj.getWorldInfo().isRaining() ? " RainTime at" + worldObj.getWorldInfo().getRainTime() : "") + (worldObj.getWorldInfo().isThundering() ? " ThunderTime at" + worldObj.getWorldInfo().getThunderTime() : "")));

                                timer = 0;
                            }
                        }
                    }
                }
            };
            dummyEntity.setPosition(posX + 0.5F, posY + 0.25F, posZ + 0.5F);
            dummyEntity.setChairBlock(posX, posY, posZ);
            world.spawnEntityInWorld(dummyEntity);
            entity.mountEntity(dummyEntity);
            isOccupy = true;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getAABBPool().getAABB(xCoord - 1, yCoord, zCoord - 1, xCoord + 2, yCoord + 0.25F, zCoord + 2);
    }
}
