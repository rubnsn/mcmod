package ruby.bamboo.block;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class BlockGrowableBase extends BlockBush implements IGrowable {
    private IIcon[] icons;

    public BlockGrowableBase() {
        super();

        this.setCreativeTab((CreativeTabs) null);
        this.setHardness(0.0F);
        this.setStepSound(soundTypeGrass);
        this.disableStats();
    }

    public void tryBonemealGrow(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z) + MathHelper.getRandomIntegerInRange(world.rand, 2, 5);

        if (meta > this.getMaxGrowthStage() - 1) {
            meta = this.getMaxGrowthStage() - 1;
        }

        world.setBlockMetadataWithNotify(x, y, z, meta, 2);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (meta < 0 || meta > this.getMaxGrowthStage() - 1) {
            meta = this.getMaxGrowthStage() - 1;
        }

        return this.icons[meta];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        this.icons = new IIcon[this.getMaxGrowthStage()];
        for (int i = 0; i < this.icons.length; ++i) {
            this.icons[i] = p_149651_1_.registerIcon(this.getTextureName() + "_stage_" + i);
        }
    }

    public abstract Item getSeed();

    //実
    public abstract Item getProduct();

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        super.updateTick(world, x, y, z, rand);

        if (world.getBlockLightValue(x, y + 1, z) >= 9) {
            int l = world.getBlockMetadata(x, y, z);

            if (l < this.getMaxGrowthStage() - 1) {
                float f = this.getGrowRate(world, x, y, z);

                if (rand.nextInt((int) (25.0F / f) + 1) == 0) {
                    ++l;
                    world.setBlockMetadataWithNotify(x, y, z, l, 2);
                }
            }
        }
    }

    public float getGrowRate(World world, int x, int y, int z) {
        float f = 1.0F;
        Block block = world.getBlock(x, y, z - 1);
        Block block1 = world.getBlock(x, y, z + 1);
        Block block2 = world.getBlock(x - 1, y, z);
        Block block3 = world.getBlock(x + 1, y, z);
        Block block4 = world.getBlock(x - 1, y, z - 1);
        Block block5 = world.getBlock(x + 1, y, z - 1);
        Block block6 = world.getBlock(x + 1, y, z + 1);
        Block block7 = world.getBlock(x - 1, y, z + 1);
        boolean flag = block2 == this || block3 == this;
        boolean flag1 = block == this || block1 == this;
        boolean flag2 = block4 == this || block5 == this || block6 == this || block7 == this;

        for (int l = x - 1; l <= x + 1; ++l) {
            for (int i1 = z - 1; i1 <= z + 1; ++i1) {
                float f1 = 0.0F;

                if (world.getBlock(l, y - 1, i1).canSustainPlant(world, l, y - 1, i1, ForgeDirection.UP, this)) {
                    f1 = 1.0F;

                    if (world.getBlock(l, y - 1, i1).isFertile(world, l, y - 1, i1)) {
                        f1 = 3.0F;
                    }
                }

                if (l != x || i1 != z) {
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        if (flag2 || flag && flag1) {
            f /= 2.0F;
        }

        return f;
    }

    @Override
    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int meta, float dropRate, int fortune) {
        super.dropBlockAsItemWithChance(world, x, y, z, meta, dropRate, 0);
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return p_149650_1_ == this.getMaxGrowthStage() - 1 ? this.getProduct() : this.getSeed();
    }

    @Override
    public int quantityDropped(Random rand) {
        return 1;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> ret = super.getDrops(world, x, y, z, metadata, fortune);

        if (metadata >= this.getMaxGrowthStage() - 1) {
            for (int i = 0; i < 3 + fortune; ++i) {
                if (world.rand.nextInt(this.getMaxGrowthStage() * 2 + 1) <= metadata) {
                    ret.add(new ItemStack(this.getSeed(), 1, 0));
                }
            }
        }

        return ret;
    }

    public abstract int getMaxGrowthStage();

    //blockに対して設置できるか？
    @Override
    public abstract boolean canPlaceBlockOn(Block block);

    //最大成長状態？
    @Override
    public boolean func_149851_a(World world, int x, int y, int z, boolean var5) {
        return world.getBlockMetadata(x, y, z) != this.getMaxGrowthStage() - 1;
    }

    //骨粉は有効か
    @Override
    public boolean func_149852_a(World world, Random rand, int x, int y, int z) {
        return true;
    }

    //骨粉を使用された時に呼ばれる
    @Override
    public void func_149853_b(World world, Random rand, int x, int y, int z) {
        this.tryBonemealGrow(world, x, y, z);
    }

}
