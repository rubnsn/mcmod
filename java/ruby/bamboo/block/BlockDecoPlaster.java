package ruby.bamboo.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import ruby.bamboo.BambooCore;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockDecoPlaster extends BlockQuadRotateBlockBase {

    public BlockDecoPlaster() {
        super(Material.ground);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
        list.add(new ItemStack(item, 1, 2));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        String[] iconName = new String[] { "decoplaster_i", "decoplaster_l", "decoplaster_t" };
        this.icons = new IIcon[iconName.length];
        for (int i = 0; i < iconName.length; ++i) {
            this.icons[i] = p_149651_1_.registerIcon(BambooCore.resourceDomain + iconName[i]);
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
    }

    @Override
    public int getPlayerDir(Entity e) {
        return 0;
    }

    @Override
    public int onBlockPlaced(World par1World, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
        int iconMeta = meta & this.getMetaMask();
        int dir = 0;
        ForgeDirection fd = ForgeDirection.VALID_DIRECTIONS[side];
        if (fd != ForgeDirection.DOWN && fd != ForgeDirection.UP) {
            if (0.5F <= hitY) {
                dir = 1;
            }
        } else {
            //縦方向は本体の糞バグ影響をうけるため、更新先送り。
            if (fd == ForgeDirection.UP) {
                dir = 2;
            } else {
                dir = 3;
            }
        }
        return iconMeta | (dir << getDirShiftBit());
    }

    @Override
    public void onPostBlockPlaced(World world, int x, int y, int z, int metadata) {
        int dirPattern = metadata >> this.getDirShiftBit();
        if (dirPattern == 2) {

        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRotateMeta(int meta) {
        return meta >> getDirShiftBit();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getuvRotateSouth(int meta) {
        switch (this.getRotateMeta(meta)) {
        case 0:
            return 2;
        case 1:
            return 1;
        case 2:
            return 0;
        case 3:
            return 3;
        }
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getuvRotateEast(int meta) {
        switch (this.getRotateMeta(meta)) {
        case 0:
            return 2;
        case 1:
            return 1;
        case 2:
            return 0;
        case 3:
            return 3;
        }
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getuvRotateWest(int meta) {
        switch (this.getRotateMeta(meta)) {
        case 0:
            return 1;
        case 1:
            return 2;
        case 2:
            return 3;
        case 3:
            return 0;
        }
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getuvRotateNorth(int meta) {
        switch (this.getRotateMeta(meta)) {
        case 0:
            return 1;
        case 1:
            return 2;
        case 2:
            return 3;
        case 3:
            return 0;
        }
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getuvRotateTop(int meta) {
        return this.getRotateMeta(meta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getuvRotateBottom(int meta) {
        return this.getRotateMeta(meta);
    }
}
