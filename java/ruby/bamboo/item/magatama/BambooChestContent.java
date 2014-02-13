package ruby.bamboo.item.magatama;

import java.util.Random;

import ruby.bamboo.BambooInit;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;

public class BambooChestContent extends WeightedRandomChestContent {

    public BambooChestContent(int par1, int par2, int par3, int par4, int par5) {
        super(par1, par2, par3, par4, par5);
    }

    public BambooChestContent(ItemStack par1ItemStack, int par2, int par3, int par4) {
        super(par1ItemStack, par2, par3, par4);
    }

    @Override
    protected ItemStack[] generateChestContent(Random random, IInventory newInventory) {
        if (random.nextFloat() < 0.001) {
            return super.generateChestContent(random, newInventory);
        } else {
            return new ItemStack[] { new ItemStack(random.nextBoolean() ? BambooInit.takenokoIID : BambooInit.sakuraBID, 1, 0) };
        }
    }
}
