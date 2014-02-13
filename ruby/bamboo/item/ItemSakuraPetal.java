package ruby.bamboo.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemSakuraPetal extends ItemBlock {
    public ItemSakuraPetal(Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int i) {
        return i;
    }
}
