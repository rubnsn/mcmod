package ruby.bamboo.item.enchant;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class EnchantChain extends EnchantBase {

    public EnchantChain(int id, String name, int maxLevel, float weight, float tp) {
        super(id, name, maxLevel, weight, tp);
    }

    @Override
    void effect(ItemStack itemStack, World world, int posX, int posY, int posZ, EntityLivingBase entity, int enchantLvl) {
        chainBreak(world, posX, posY, posZ, getFortune(itemStack), 0);
    }

    private int getFortune(ItemStack is) {
        if (!is.stackTagCompound.hasKey("ench", 9)) {
            is.stackTagCompound.setTag("ench", new NBTTagList());
        }
        NBTTagList nbttaglist = is.getEnchantmentTagList();
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            if (nbttaglist.getCompoundTagAt(i).getShort("id") == Enchantment.fortune.effectId) {
                return nbttaglist.getCompoundTagAt(i).getShort("lvl");
            }
        }
        return 0;
    }

    private void chainBreak(World world, int posX, int posY, int posZ, int fortune, int loopCount) {
        if (loopCount > 100) {
            return;
        }
        Block block;
        for (ForgeDirection fd : ForgeDirection.VALID_DIRECTIONS) {
            block = world.getBlock(posX + fd.offsetX, posY + fd.offsetY, posZ + fd.offsetZ);
            if (block instanceof BlockOre) {
                block.dropBlockAsItem(world, posX, posY, posZ, world.getBlockMetadata(posX + fd.offsetX, posY + fd.offsetY, posZ + fd.offsetZ), fortune);
                world.setBlockToAir(posX + fd.offsetX, posY + fd.offsetY, posZ + fd.offsetZ);
                this.chainBreak(world, posX + fd.offsetX, posY + fd.offsetY, posZ + fd.offsetZ, fortune, ++loopCount);
            }
        }
    }
}
