package ruby.bamboo.block;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import ruby.bamboo.BambooUtil;
import ruby.bamboo.CustomRenderHandler;
import ruby.bamboo.tileentity.TileEntityVillagerBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockVillager extends BlockContainer {

    public BlockVillager() {
        super(Material.ground);
        this.setHardness(0.5F);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityVillagerBlock();
    }

    @Override
    public boolean onBlockActivated(World world, int posX, int posY, int posZ, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(posX, posY, posZ);
        if (player != null && tile != null && tile instanceof TileEntityVillagerBlock) {
            ItemStack is = player.getEquipmentInSlot(0);
            if (!world.isRemote) {
                if (is != null && is.getItem() == Item.getItemFromBlock(Blocks.emerald_block)) {
                    is.stackSize--;
                    ((TileEntityVillagerBlock) tile).addExp(1);
                } else if (is != null && is.getItem() == Items.stick) {
                    for (ItemStack item : ((TileEntityVillagerBlock) tile).getInnerItems()) {
                        if (item != null && 0 < item.stackSize) {
                            this.dropBlockAsItem(world, posX, posY, posZ, item);
                        }
                    }
                } else {
                    ((IMerchant) tile).setCustomer(player);
                    player.displayGUIMerchant((IMerchant) tile, "VillagerBlock");
                }
            } else {
                if (is != null && is.getItem() == Items.stick) {
                    ((TileEntityVillagerBlock) tile).decrRollSpeed();
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void onBlockHarvested(World world, int posX, int posY, int posZ, int meta, EntityPlayer player) {
        this.dropBlockAsItem(world, posX, posY, posZ, meta, 0);
        super.onBlockHarvested(world, posX, posY, posZ, meta, player);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int posX, int posY, int posZ, int meta, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        {
            ItemStack itemstack = new ItemStack(this, 1, 0);
            TileEntityVillagerBlock tile = (TileEntityVillagerBlock) world.getTileEntity(posX, posY, posZ);
            if (tile != null) {
                NBTTagCompound nbt = new NBTTagCompound();
                itemstack.setTagCompound(nbt);
                tile.writeToVillagerNBT(nbt);
                ret.add(itemstack);
            }
        }
        return ret;
    }

    @Override
    public void onBlockPlacedBy(World world, int posX, int posY, int posZ, EntityLivingBase living, ItemStack itemstack) {
        if (!world.isRemote) {
            world.setBlockMetadataWithNotify(posX, posY, posZ, BambooUtil.getPlayerDir(living), 3);
            if (itemstack.hasTagCompound() && itemstack.getTagCompound().hasKey("list")) {
                if (world.getBlock(posX, posY - 1, posZ) == Blocks.soul_sand) {
                    ForgeDirection[] dir = new ForgeDirection[] { ForgeDirection.EAST, ForgeDirection.NORTH };
                    for (ForgeDirection fd : dir) {
                        if (world.getBlock(posX + fd.offsetX, posY - 1, posZ + fd.offsetZ) == Blocks.soul_sand) {
                            if (world.getBlock(posX + fd.getOpposite().offsetX, posY - 1, posZ + fd.getOpposite().offsetZ) == Blocks.soul_sand) {
                                if (world.getBlock(posX, posY - 2, posZ) == Blocks.soul_sand) {
                                    world.setBlockToAir(posX, posY, posZ);
                                    world.setBlockToAir(posX, posY - 1, posZ);
                                    world.setBlockToAir(posX, posY - 2, posZ);
                                    world.setBlockToAir(posX + fd.offsetX, posY - 1, posZ + fd.offsetZ);
                                    world.setBlockToAir(posX + fd.getOpposite().offsetX, posY - 1, posZ + fd.getOpposite().offsetZ);
                                    EntityVillager entity = new EntityVillager(world);
                                    NBTTagCompound nbt = new NBTTagCompound();
                                    entity.writeEntityToNBT(nbt);
                                    nbt.setTag("Offers", itemstack.getTagCompound().getTag("list"));
                                    entity.readFromNBT(nbt);
                                    entity.setPosition(posX, posY, posZ);
                                    world.spawnEntityInWorld(entity);
                                    System.out.println(entity.posX);
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    TileEntity tile = world.getTileEntity(posX, posY, posZ);
                    if (tile instanceof TileEntityVillagerBlock) {
                        ((TileEntityVillagerBlock) tile).readFromVillagerNBT(itemstack.getTagCompound());
                    }
                }
            }
        }
    }

    @Override
    public int getRenderType() {
        return CustomRenderHandler.villagerBlockUID;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
        return Blocks.soul_sand.getBlockTextureFromSide(p_149691_1_);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int posX, int posY, int posZ, Random rand) {
        if ((world.getBlockMetadata(posX, posY, posZ) & 4) != 0) {
            this.generateRandomParticles(world, "heart", posX, posY, posZ);
            //this.getWorldObj().playSoundEffect(this.xCoord, this.yCoord, this.zCoord, "mob.villager.yes", 1F, (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F + 1.0F);
        }
    }

    private void generateRandomParticles(World worldObj, String particlesName, int xCoord, int yCoord, int zCoord) {
        for (int i = 0; i < 5; ++i) {
            double d0 = worldObj.rand.nextGaussian() * 0.02D;
            double d1 = worldObj.rand.nextGaussian() * 0.02D;
            double d2 = worldObj.rand.nextGaussian() * 0.02D;
            worldObj.spawnParticle(particlesName, xCoord + (double) (worldObj.rand.nextFloat() * 2.0F) - 0.5D, yCoord + 1.0D + (double) (worldObj.rand.nextFloat()), zCoord + (double) (worldObj.rand.nextFloat() * 2.0F) - 0.5D, d0, d1, d2);
        }
    }
}
