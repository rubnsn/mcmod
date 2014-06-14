package ruby.bamboo.inventory;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;

public class InventoryCooking extends InventoryCrafting {

    public InventoryCooking(Container par1Container, int par2, int par3) {
        super(par1Container, par2, par3);
    }

    public String getInventoryName() {
        return "bamboo.container.cooking";
    }
}
