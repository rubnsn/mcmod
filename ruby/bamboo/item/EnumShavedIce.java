package ruby.bamboo.item;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public enum EnumShavedIce
{
    Normal(0, 0xFFFFFF,	0, null),
    Melon(1, 0xff3a00,	1, Item.melon),
    Choco(2, 0xb28850,	4, new ItemStack(Item.dyePowder, 1, 3)),
    Pumpkin(3, 0xff8000,	2, Block.pumpkin),
    Cactus(4, 0xa40035,	1, Block.cactus),
    Squid(5, 0x535353,	1, new ItemStack(Item.dyePowder, 1, 0)),
    Apple(6, 0xfff89d,	2, Item.appleRed),
    Blue(7, 0x5ddaff,	3, new ItemStack(Item.potion, 1, -1)),
    Tea(8, 0x789e25,	1, new ItemStack(Block.leaves, 1, -1));
    private EnumShavedIce(int i1, int i2, int i3, Object obj)
    {
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
