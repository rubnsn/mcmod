package ruby.bamboo.item;

import java.lang.reflect.Field;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import ruby.bamboo.BambooCore;
import ruby.bamboo.gui.GuiHandler;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSack extends Item {
    // private static ItemStack backkup;
    private Field remainingHighlightTicks;

    public ItemSack() {
        super();
        setHasSubtypes(true);
        setMaxDamage(1025);
        setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par3World, EntityPlayer par2EntityPlayer) {
        if (par1ItemStack.getTagCompound() == null) {
            par2EntityPlayer.openGui(BambooCore.getInstance(), GuiHandler.GUI_SACK, par3World, (int) par2EntityPlayer.posX, (int) par2EntityPlayer.posY, (int) par2EntityPlayer.posZ); // par2EntityPlayer.openGui(mod_Bamboo,0,par3World,par2EntityPlayer.posX
            return par1ItemStack;
        }

        short count = par1ItemStack.getTagCompound().getShort("count");
        String type = par1ItemStack.getTagCompound().getString("type");
        short meta = par1ItemStack.getTagCompound().getShort("meta");

        if (!isStorage(getItem(type))) {
            return par1ItemStack;
        }

        MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(par3World, par2EntityPlayer, par2EntityPlayer.isSneaking());

        if (movingobjectposition == null) {
            ItemStack[] is = par2EntityPlayer.inventory.mainInventory;

            for (int i = 0; i < is.length; i++) {
                if (is[i] == null) {
                    continue;
                }

                if (is[i].getItem() == getItem(type) && is[i].getItemDamage() == meta) {
                    if ((count + is[i].stackSize) < getMaxDamage()) {
                        count += is[i].stackSize;
                        is[i] = null;
                    } else {
                        is[i].stackSize -= (getMaxDamage() - count - 1);
                        count = (short) (getMaxDamage() - 1);
                    }
                }
            }

            par1ItemStack.getTagCompound().setShort("count", count);
            par1ItemStack.setItemDamage(getMaxDamage() - count);
        } else {
            int stacksize = getItem(type).onItemRightClick(new ItemStack(getItem(type), count, meta), par3World, par2EntityPlayer).stackSize;

            if (stacksize < count) {
                par1ItemStack.setItemDamage(par1ItemStack.getItemDamage() + count + stacksize);
                count -= stacksize;
                par1ItemStack.getTagCompound().setShort("count", count);
            }
        }

        return par1ItemStack;
    }

    private Item getItem(String str) {
        return (Item) Item.itemRegistry.getObject(str);
    }

    private boolean isStorage(Item item) {
        return item instanceof ItemBlock ? true : item instanceof ItemSeeds ? true : item instanceof ItemSeedFood ? true : false;
    }

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
        if (par1ItemStack.getTagCompound() == null) {
            return false;
        }

        short count = par1ItemStack.getTagCompound().getShort("count");
        String type = par1ItemStack.getTagCompound().getString("type");
        short meta = par1ItemStack.getTagCompound().getShort("meta");

        if (!isStorage(getItem(type))) {
            return false;
        }

        if (!par3World.canMineBlock(par2EntityPlayer, par4, par5, par6)) {
            return false;
        }

        if (count != 0) {
            ItemStack is = new ItemStack(getItem(type), 1, meta);
            if (Block.getBlockFromItem(getItem(type)) != Blocks.air) {
                if (getItem(type) instanceof ItemSlab) {
                    if (!canPlaceItemBlockSlabOnSide(par3World, par4, par5, par6, par7, par2EntityPlayer, is)) {
                        return false;
                    }
                } else {
                    if (!canPlaceItemBlockOnSide(par3World, par4, par5, par6, par7, par2EntityPlayer, is)) {
                        return false;
                    }
                }
                if (getItem(type) instanceof ItemBlock) {
                    if (getItem(type).onItemUse(is, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10)) {
                        par1ItemStack.setItemDamage(par1ItemStack.getItemDamage() + 1);
                        count--;
                        par1ItemStack.getTagCompound().setShort("count", count);
                        par2EntityPlayer.swingItem();
                    }
                }
            }
            if (getItem(type) instanceof ItemSeeds || getItem(type) instanceof ItemSeedFood) {
                for (int i = -2; i <= 2; i++) {
                    for (int j = -2; j <= 2; j++) {
                        if (getItem(type).onItemUse(is, par2EntityPlayer, par3World, par4 + i, par5, par6 + j, par7, par8, par9, par10)) {
                            par1ItemStack.setItemDamage(par1ItemStack.getItemDamage() + 1);
                            count--;
                            par1ItemStack.getTagCompound().setShort("count", count);
                            par2EntityPlayer.swingItem();

                            if (count < 1) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    // ItemBlockに存在するが、ItemBlockを継承しないと参照されないため
    private boolean canPlaceItemBlockOnSide(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer, ItemStack par7ItemStack) {
        Block var8 = par1World.getBlock(par2, par3, par4);

        if (var8 == Blocks.snow_layer) {
            par5 = 1;
        } else if (var8 != Blocks.vine && var8 != Blocks.tallgrass && var8 != Blocks.deadbush && (var8 == null || !var8.isReplaceable(par1World, par2, par3, par4))) {
            if (par5 == 0) {
                --par3;
            }

            if (par5 == 1) {
                ++par3;
            }

            if (par5 == 2) {
                --par4;
            }

            if (par5 == 3) {
                ++par4;
            }

            if (par5 == 4) {
                --par2;
            }

            if (par5 == 5) {
                ++par2;
            }
        }
        return par1World.canPlaceEntityOnSide(Block.getBlockFromItem(par7ItemStack.getItem()), par2, par3, par4, false, par5, (Entity) null, par7ItemStack);
    }

    private boolean canPlaceItemBlockSlabOnSide(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer, ItemStack par7ItemStack) {
        int var8 = par2;
        int var9 = par3;
        int var10 = par4;
        Block var11 = par1World.getBlock(par2, par3, par4);
        int var12 = par1World.getBlockMetadata(par2, par3, par4);
        int var13 = var12 & 7;
        boolean var14 = (var12 & 8) != 0;
        if ((par5 == 1 && !var14 || par5 == 0 && var14) && var11 == Block.getBlockFromItem(par7ItemStack.getItem()) && var13 == par7ItemStack.getItemDamage()) {
            return true;
        } else {
            if (par5 == 0) {
                --par3;
            }

            if (par5 == 1) {
                ++par3;
            }

            if (par5 == 2) {
                --par4;
            }

            if (par5 == 3) {
                ++par4;
            }

            if (par5 == 4) {
                --par2;
            }

            if (par5 == 5) {
                ++par2;
            }

            var11 = par1World.getBlock(par2, par3, par4);
            var12 = par1World.getBlockMetadata(par2, par3, par4);
            var13 = var12 & 7;
            var14 = (var12 & 8) != 0;
            return var11 == Block.getBlockFromItem(par7ItemStack.getItem()) && var13 == par7ItemStack.getItemDamage() ? true : canPlaceItemBlockOnSide(par1World, var8, var9, var10, par5, par6EntityPlayer, par7ItemStack);
        }
    }

    public void release(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        if (par1ItemStack.getTagCompound() != null && !par2World.isRemote) {
            short count = par1ItemStack.getTagCompound().getShort("count");
            String type = par1ItemStack.getTagCompound().getString("type");
            short meta = par1ItemStack.getTagCompound().getShort("meta");
            double x = par3EntityPlayer.posX;
            double y = par3EntityPlayer.posY;
            double z = par3EntityPlayer.posZ;

            if (count > 0) {
                par2World.spawnEntityInWorld(new EntityItem(par2World, x, y, z, new ItemStack(getItem(type), count, meta)));
                // returnItem(par3EntityPlayer, new
                // ItemStack(getItem(type),count,meta));
                count = 0;
            }

            par1ItemStack.setTagCompound(null);
        }
    }

    // 負荷はこっちのほうが低いだろうけども、挙動が気に入らない
    private void returnItem(EntityPlayer entity, ItemStack is) {
        if (!entity.inventory.addItemStackToInventory(is)) {
            entity.entityDropItem(is, 0.5F);
        }
    }

    @SideOnly(Side.CLIENT)
    private void renderToolHighlight() {
        // リフレクションじゃないほうがいいかな～？
        try {
            if (remainingHighlightTicks == null) {
                for (int i = 0; i < GuiIngame.class.getDeclaredFields().length; i++) {
                    if (GuiIngame.class.getDeclaredFields()[i].getType() == ItemStack.class) {
                        remainingHighlightTicks = GuiIngame.class.getDeclaredFields()[i - 1];
                    }
                }

                remainingHighlightTicks.setAccessible(true);
            }

            remainingHighlightTicks.setInt(FMLClientHandler.instance().getClient().ingameGUI, 40);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean getShareTag() {
        return true;
    }

    @Override
    public String getItemStackDisplayName(ItemStack par1ItemStack) {
        String name = super.getItemStackDisplayName(par1ItemStack);
        if (par1ItemStack.getTagCompound() != null && getItem(par1ItemStack.getTagCompound().getString("type")) != null) {
            name += (":" + getItem(par1ItemStack.getTagCompound().getString("type")).getItemStackDisplayName(new ItemStack(getItem(par1ItemStack.getTagCompound().getString("type")), 1, par1ItemStack.getTagCompound().getShort("meta"))) + ":" + par1ItemStack.getTagCompound().getShort("count")).trim();
        }

        return name;
    }

    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
        if (par5 && par2World.isRemote) {
            renderToolHighlight();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        this.itemIcon = par1IconRegister.registerIcon(BambooCore.resourceDomain + "itemsack");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack par1ItemStack, int pass) {
        return par1ItemStack.getTagCompound() != null;
    }
}
