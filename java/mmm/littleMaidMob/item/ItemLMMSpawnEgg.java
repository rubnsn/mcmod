package mmm.littleMaidMob.item;

import mmm.littleMaidMob.entity.EntityLittleMaidBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemLMMSpawnEgg extends Item {
    private IIcon theIcon;

    public ItemLMMSpawnEgg() {
        this.setHasSubtypes(true);
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    @Override
    public String getItemStackDisplayName(ItemStack p_77653_1_) {
        String s = ("" + StatCollector.translateToLocal(this.getUnlocalizedName() + ".name")).trim();
        String s1 = "littleMaidMob";

        if (s1 != null) {
            s = s + " " + StatCollector.translateToLocal("entity." + s1 + ".name");
        }

        return s;
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack p_82790_1_, int p_82790_2_) {
        return p_82790_2_ == 0 ? 0xefffef : 0x9f5f5f;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int posX, int posY, int posZ, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
        if (world.isRemote) {
            return true;
        } else {
            Block block = world.getBlock(posX, posY, posZ);
            posX += Facing.offsetsXForSide[p_77648_7_];
            posY += Facing.offsetsYForSide[p_77648_7_];
            posZ += Facing.offsetsZForSide[p_77648_7_];
            double d0 = 0.0D;

            if (p_77648_7_ == 1 && block.getRenderType() == 11) {
                d0 = 0.5D;
            }

            Entity entity = spawnCreature(world, (double) posX + 0.5D, (double) posY + d0, (double) posZ + 0.5D);

            if (entity != null) {
                if (entity instanceof EntityLivingBase && itemStack.hasDisplayName()) {
                    ((EntityLiving) entity).setCustomNameTag(itemStack.getDisplayName());
                }

                if (!player.capabilities.isCreativeMode) {
                    --itemStack.stackSize;
                }

            }

            return true;
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (world.isRemote) {
            return itemStack;
        } else {
            MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, true);

            if (movingobjectposition == null) {
                return itemStack;
            } else {
                if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                    int i = movingobjectposition.blockX;
                    int j = movingobjectposition.blockY;
                    int k = movingobjectposition.blockZ;

                    if (!world.canMineBlock(player, i, j, k)) {
                        return itemStack;
                    }

                    if (!player.canPlayerEdit(i, j, k, movingobjectposition.sideHit, itemStack)) {
                        return itemStack;
                    }

                    if (world.getBlock(i, j, k) instanceof BlockLiquid) {
                        Entity entity = spawnCreature(world, (double) i, (double) j, (double) k);

                        if (entity != null) {
                            if (entity instanceof EntityLivingBase && itemStack.hasDisplayName()) {
                                ((EntityLiving) entity).setCustomNameTag(itemStack.getDisplayName());
                            }

                            if (!player.capabilities.isCreativeMode) {
                                --itemStack.stackSize;
                            }
                        }
                    }
                }

                return itemStack;
            }
        }
    }

    public Entity spawnCreature(World world, double posX, double posY, double posZ) {

        Entity entity = new EntityLittleMaidBase(world);
        if (entity != null && entity instanceof EntityLivingBase) {
            EntityLiving entityliving = (EntityLiving) entity;
            entity.setLocationAndAngles(posX, posY, posZ, MathHelper.wrapAngleTo180_float(world.rand.nextFloat() * 360.0F), 0.0F);
            entityliving.rotationYawHead = entityliving.rotationYaw;
            entityliving.renderYawOffset = entityliving.rotationYaw;
            entityliving.onSpawnWithEgg((IEntityLivingData) null);
            world.spawnEntityInWorld(entity);
            entityliving.playLivingSound();
        }

        return entity;

    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int p_77618_1_, int p_77618_2_) {
        return p_77618_2_ > 0 ? this.theIcon : super.getIconFromDamageForRenderPass(p_77618_1_, p_77618_2_);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister p_94581_1_) {
        super.registerIcons(p_94581_1_);
        this.theIcon = p_94581_1_.registerIcon(this.getIconString() + "_overlay");
    }
}
