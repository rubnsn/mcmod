package ruby.bamboo.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import ruby.bamboo.BambooCore;
import ruby.bamboo.BambooInit;
import ruby.bamboo.BambooUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockDecoPlaster extends Block implements IExOnBLockPlacedBy {
    private IIcon[] icons;
    private EnumPattern pattern;

    public enum EnumPattern {
        straight(new byte[][] { { 0, 0, 0, 0, 0, 0 }, { 1, 1, 1, 1, 1, 1 }, { 2, 2, 2, 2, 2, 2 }, { 3, 3, 3, 3, 3, 3 } }),
        curve(new byte[][] { { 0, 0, 0, 3, 0, 3 }, { 1, 1, 1, 2, 1, 2 }, { 2, 2, 2, 1, 2, 1 }, { 3, 3, 3, 0, 3, 0 } });
        private EnumPattern(byte[][] pattern) {
            this.pattern = pattern;
        }

        private byte[][] pattern;

        public byte[][] getPattern() {
            return new byte[][] { { 0, 0, 0, 2, 2, 0 }, { 1, 1, 1, 1, 1, 1 }, { 2, 2, 2, 0, 0, 2 }, { 3, 3, 3, 3, 3, 3 } };
        }
    }

    public BlockDecoPlaster(EnumPattern pattern) {
        super(Material.ground);
        this.pattern = pattern;
        this.setHardness(0.2F);
        this.setResistance(1.0F);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        ItemStack is = player.getCurrentEquippedItem();
        if (is != null && is.getItem() == BambooInit.itembamboo) {
            int meta = world.getBlockMetadata(x, y, z) & 3;
            int sideMeta = (side / 2) << 2;
            world.setBlockMetadataWithNotify(x, y, z, sideMeta | ((meta & 3) < 3 ? meta + 1 : meta - 3), 2);
            return true;
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        this.icons = new IIcon[4];
        for (int i = 0; i < 4; i++) {
            this.icons[i] = p_149651_1_.registerIcon(BambooCore.resourceDomain + this.getTextureName() + "_" + i);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (side / 2 != meta >> 2) {
            return BambooInit.decoration.getIcon(0, 0);
        }
        byte[][] iconsDir;
        return icons[(pattern.getPattern()[meta & 3][side]) % icons.length];
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, EntityLivingBase entity, ItemStack itemStack, int meta) {
        int playerDir = BambooUtil.getPlayerDir(entity);
        int sideMeta = 1;
        if (playerDir == 1 || playerDir == 3) {
            sideMeta = 2;
        }
        world.setBlockMetadataWithNotify(x, y, z, sideMeta << 2, 2);
    }

    @Override
    public int damageDropped(int meta) {
        return 0;
    }
}
