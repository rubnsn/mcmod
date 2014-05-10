package ruby.bamboo.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import ruby.bamboo.BambooInit;

public class BlockLiangBamboo extends BlockLiangBase {
    public enum EnumBlock {
        sakuraLog(0, BambooInit.sakuralog, 1);
        EnumBlock(int id, Block block, int meta) {
            this.block = block;
            this.meta = meta;
            this.id = id;
        }

        private Block block;
        private int meta;
        private int id;

        public Block getBlock() {
            return block;
        }

        public int getMeta() {
            return meta;
        }

        public int getId() {
            return id;
        }
    }

    public BlockLiangBamboo(float minWidth, float maxWidth, float minHeight, float maxHeight) {
        super(minWidth, maxWidth, minHeight, maxHeight);
    }

    @Override
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        List<IIcon> list = new ArrayList<IIcon>();
        for (EnumBlock e : EnumBlock.values()) {
            list.add(e.getBlock().getIcon(0, e.getMeta()));
        }
        icons = list.toArray(new IIcon[0]);
    }

}
