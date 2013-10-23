package ruby.bamboo.block;

import java.util.Random;

import ruby.bamboo.BambooCore;
import ruby.bamboo.CustomRenderHandler;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockKitunebi extends Block {
    // Client only
    private boolean isVisible = false;

    public BlockKitunebi(int id) {
        super(id, Material.air);
        setTickRandomly(true);
        setHardness(0.0F);
        setLightValue(1F);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        return null;
    }

    @Override
    public int getRenderType() {
        return CustomRenderHandler.kitunebiUID;
    }

    @Override
    public void updateTick(World world, int i, int j, int k, Random random) {
        isVisible = false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World world, int i, int j, int k, Random random) {
        setVisibleFlg(world, i, j, k);
    }

    private void setVisibleFlg(World world, int i, int j, int k) {
        if (world.isRemote) {
            ItemStack is = FMLClientHandler.instance().getClient().thePlayer.getCurrentEquippedItem();
            isVisible = false;

            if (is != null) {
                Item item = is.getItem();

                if (item instanceof ItemBlock) {
                    if (((ItemBlock) item).getBlockID() == this.blockID) {
                        isVisible = true;
                    }
                }
            }

            // isVisible = is != null ? is.itemID == blockID ? true : false :
            // false;

            if (isVisible) {
                world.setBlock(i, j, k, blockID, world.getBlockMetadata(i, j, k) | 8, 3);
                setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            } else {
                world.setBlock(i, j, k, blockID, world.getBlockMetadata(i, j, k) & 7, 3);
                setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister) {
        this.blockIcon = par1IconRegister.registerIcon(BambooCore.resorceDmain + this.getTextureName());
    }

    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        return par1World.getBlockMetadata(par2, par3, par4) == 0 ? super.getSelectedBoundingBoxFromPool(par1World, par2, par3, par4) : AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0);
    }
}
