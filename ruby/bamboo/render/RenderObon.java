package ruby.bamboo.render;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import ruby.bamboo.entity.EntityObon;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;

public class RenderObon extends Render {
    private static final ResourceLocation res = new ResourceLocation("textures/entitys/obon.png");
    protected static final RenderItem itemRenderer = new RenderItem();
    private ModelObon model;
    private Random random;

    public RenderObon() {
        model = new ModelObon();
        random = new Random();
        itemRenderer.setRenderManager(RenderManager.instance);
    }

    private EntityItem entityItem;

    @Override
    public void doRender(Entity entity, double d0, double d1, double d2, float f, float f1) {
        GL11.glPushMatrix();
        this.bindEntityTexture(entity);
        GL11.glTranslatef((float) d0, (float) d1 + 0.2F, (float) d2);
        GL11.glRotatef(180, 1.0F, 0, 0);
        model.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
        entityItem = ((EntityObon) entity).getEntityItem();

        if (entityItem != null) {
            doRenderItem(entityItem, d0, d1 + 0.18, d2 - 0.07, 0, 0);
        }
    }

    private ResourceLocation func_110796_a(EntityItem par1EntityItem) {
        return this.renderManager.renderEngine.getResourceLocation(par1EntityItem.getEntityItem().getItemSpriteNumber());
    }

    public void doRenderItem(EntityItem par1EntityItem, double par2, double par4, double par6, float par8, float par9) {
        this.bindTexture(func_110796_a(par1EntityItem));
        this.random.setSeed(187L);
        ItemStack itemstack = par1EntityItem.getEntityItem();

        if (itemstack.getItem() != null) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float) par2, (float) par4 + 0.1F, (float) par6);
            byte b0 = getMiniBlockCount(itemstack);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            float f4;
            float f5;
            float f6;
            int i;
            float f8;

            if (itemstack.getItemSpriteNumber() == 1 && itemstack.getItem().requiresMultipleRenderPasses()) {
                GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                GL11.glTranslatef(0.0F, -0.05F, 0.0F);

                for (int k = 0; k < itemstack.getItem().getRenderPasses(itemstack.getItemDamage()); ++k) {
                    this.random.setSeed(187L);
                    Icon icon = itemstack.getItem().getIcon(itemstack, k);
                    f8 = 1.0F;
                    i = Item.itemsList[itemstack.itemID].getColorFromItemStack(itemstack, k);
                    f5 = (i >> 16 & 255) / 255.0F;
                    f4 = (i >> 8 & 255) / 255.0F;
                    f6 = (i & 255) / 255.0F;
                    GL11.glColor4f(f5 * f8, f4 * f8, f6 * f8, 1.0F);
                    this.renderDroppedItem(par1EntityItem, icon, b0, par9, f5 * f8, f4 * f8, f6 * f8, k);
                }
            } else {
                GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                GL11.glTranslatef(0.0F, -0.05F, 0.0F);
                Icon icon1 = itemstack.getIconIndex();
                int l = Item.itemsList[itemstack.itemID].getColorFromItemStack(itemstack, 0);
                f8 = (l >> 16 & 255) / 255.0F;
                float f9 = (l >> 8 & 255) / 255.0F;
                f5 = (l & 255) / 255.0F;
                f4 = 1.0F;
                this.renderDroppedItem(par1EntityItem, icon1, b0, par9, f8 * f4, f9 * f4, f5 * f4);
            }

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
        }
    }

    private byte getMiniBlockCount(ItemStack stack) {
        byte ret = 1;

        if (stack.stackSize > 1) {
            ret = 2;
        }

        if (stack.stackSize > 5) {
            ret = 3;
        }

        if (stack.stackSize > 20) {
            ret = 4;
        }

        if (stack.stackSize > 40) {
            ret = 5;
        }

        return ret;
    }

    private void renderDroppedItem(EntityItem par1EntityItem, Icon par2Icon, int par3, float par4, float par5, float par6, float par7) {
        renderDroppedItem(par1EntityItem, par2Icon, par3, par4, par5, par6, par7, 0);
    }

    private void renderDroppedItem(EntityItem par1EntityItem, Icon par2Icon, int par3, float par4, float par5, float par6, float par7, int pass) {
        Tessellator tessellator = Tessellator.instance;

        if (par2Icon == null) {
            TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
            ResourceLocation resourcelocation = texturemanager.getResourceLocation(par1EntityItem.getEntityItem().getItemSpriteNumber());
            par2Icon = ((TextureMap) texturemanager.getTexture(resourcelocation)).registerIcon("missingno");
        }

        float f4 = par2Icon.getMinU();
        float f5 = par2Icon.getMaxU();
        float f6 = par2Icon.getMinV();
        float f7 = par2Icon.getMaxV();
        float f8 = 1.0F;
        float f9 = 0.5F;
        float f10 = 0.25F;
        float f11;

        for (int l = 0; l < par3; ++l) {
            GL11.glPushMatrix();

            if (l > 0) {
                f11 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                float f16 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                float f17 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                GL11.glTranslatef(f11, f16, f17);
            }

            GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glColor4f(par5, par6, par7, 1.0F);
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            tessellator.addVertexWithUV(0.0F - f9, 0.0F - f10, 0.0D, f4, f7);
            tessellator.addVertexWithUV(f8 - f9, 0.0F - f10, 0.0D, f5, f7);
            tessellator.addVertexWithUV(f8 - f9, 1.0F - f10, 0.0D, f5, f6);
            tessellator.addVertexWithUV(0.0F - f9, 1.0F - f10, 0.0D, f4, f6);
            tessellator.draw();
            GL11.glPopMatrix();
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return res;
    }
}
