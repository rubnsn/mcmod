package ruby.bamboo.item;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public enum EnumShavedIce {
    Normal(0, 0xFFFFFF, 0, null),
    Melon(1, 0xff3a00, 1, Items.melon),
    Choco(2, 0xb28850, 4, new ItemStack(Items.dye, 1, 3)),
    Pumpkin(3, 0xff8000, 2, Blocks.pumpkin),
    Cactus(4, 0xa40035, 1, Blocks.cactus),
    Squid(5, 0x535353, 1, new ItemStack(Items.dye, 1, 0)),
    Apple(6, 0xfff89d, 2, Items.apple),
    Blue(7, 0x5ddaff, 3, new ItemStack(Items.potionitem, 1, -1)),
    Tea(8, 0x789e25, 1, new ItemStack(Blocks.leaves, 1, -1));
    private EnumShavedIce(int i1, int i2, int i3, Object obj) {
        id = i1;
        color = i2;
        rarity = i3;
        material = obj;
    }

    public final int id;
    public final int color;
    public final int rarity;
    public final Object material;
}
