package ruby.bamboo.block;

import java.util.Random;

import net.minecraft.block.BlockBed;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import ruby.bamboo.BambooInit;
import ruby.bamboo.tileentity.TileEntityHuton;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockHuton extends BlockBed implements ITileEntityProvider,
        IChairDeadListener {

    public BlockHuton() {
        super();
        this.setBounds();
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        if (!par1World.isRemote) {
            int posX = par2;
            int posY = par3;
            int posZ = par4;
            int i1 = par1World.getBlockMetadata(par2, par3, par4);

            if (!isBlockHeadOfBed(i1)) {
                int j1 = getDirection(i1);
                posX += field_149981_a[j1][0];
                posZ += field_149981_a[j1][1];

                if (par1World.getBlock(posX, posY, posZ) != this) {
                    return true;
                }
            }

            EntityPlayer.EnumStatus enumstatus = par5EntityPlayer.sleepInBedAt(posX, posY, posZ);
            if (enumstatus == EntityPlayer.EnumStatus.NOT_POSSIBLE_NOW) {
                //時間加速用
                int posX2 = par2;
                int posY2 = par3;
                int posZ2 = par4;
                int meta = par1World.getBlockMetadata(par2, par3, par4);
                if (isBlockHeadOfBed(meta)) {
                    meta = getDirection(meta);
                    posX2 -= field_149981_a[meta][0];
                    posZ2 -= field_149981_a[meta][1];
                }
                TileEntity tile = par1World.getTileEntity(posX2, posY2, posZ2);
                if (tile != null && tile instanceof TileEntityHuton) {
                    ((TileEntityHuton) tile).mountDummyChair(par1World, posX2, posY2, posZ2, par5EntityPlayer);
                }
                return true;
            }
        }
        return super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9);
    }

    @Override
    public boolean isBed(IBlockAccess world, int x, int y, int z, EntityLivingBase player) {
        return true;
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return isBlockHeadOfBed(p_149650_1_) ? Item.getItemById(0) : BambooInit.huton;
    }

    @SideOnly(Side.CLIENT)
    public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
        return BambooInit.huton;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
        return Blocks.wool.getIcon(0, 0);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        if ((var2 & 12) == 0) {
            return new TileEntityHuton();
        } else {
            return null;
        }
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess, int par2, int par3, int par4) {
        this.setBounds();
    }

    void setBounds() {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
    }

    @Override
    public void onChairDead(World world, int posX, int posY, int posZ) {
        TileEntity tile = world.getTileEntity(posX, posY, posZ);
        if (tile != null && tile instanceof TileEntityHuton) {
            ((TileEntityHuton) tile).isOccupy = false;
        }
    }
}
