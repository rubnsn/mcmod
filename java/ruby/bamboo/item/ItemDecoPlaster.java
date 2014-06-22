package ruby.bamboo.item;

import net.minecraft.block.Block;

public class ItemDecoPlaster extends ItemSimpleSubtype {

    public ItemDecoPlaster(Block block) {
        super(block);
    }
    /*
        @Override
        public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {

            if (!world.setBlock(x, y, z, field_150939_a, metadata, 3)) {
                return false;
            }

            if (world.getBlock(x, y, z) == field_150939_a) {
                field_150939_a.onBlockPlacedBy(world, x, y, z, player, stack);
                System.out.println(metadata);
                field_150939_a.onPostBlockPlaced(world, x, y, z, metadata | (BambooUtil.getPlayerDir(player) << ((BlockDecoPlaster) this.field_150939_a).getDirShiftBit()));
            }

            return true;
        }*/
}
