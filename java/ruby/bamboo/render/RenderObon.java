package ruby.bamboo.render;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import ruby.bamboo.BambooCore;
import ruby.bamboo.entity.EntityObon;

public class RenderObon extends Render {
    private static final ResourceLocation res = new ResourceLocation(BambooCore.resourceDomain + "textures/entitys/obon.png");
    protected static final RenderItem itemRenderer = new RenderItem();
    private final ModelObon model;
    private final Random random;

    public RenderObon() {
        model = new ModelObon();
        random = new Random();
        itemRenderer.setRenderManager(RenderManager.instance);
    }

    @Override
    public void doRender(Entity entity, double d0, double d1, double d2, float f, float f1) {
        this.doRender((EntityObon) entity, d0, d1, d2, f, f1);
    }

    private List<ItemStack> renderItems = new ArrayList<ItemStack>();
    private float f;

    public void doRender(EntityObon entity, double d0, double d1, double d2, float f, float f1) {
        GL11.glPushMatrix();
        this.bindEntityTexture(entity);
        GL11.glTranslatef((float) d0, (float) d1 + 0.2F, (float) d2);
        GL11.glRotatef(180, 1.0F, 0, 0);
        GL11.glRotatef(entity.rotationYaw + 180.0F, 0.0F, 1.0F, 0.0F);
        model.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glScalef(0.7F, 0.7F, 0.7F);
        renderItems.clear();
        for (int i = 0; i < entity.MAX_SIZE; i++) {
            ItemStack is = entity.getItemData(i);
            if (is != null && is.getItem() != entity.EMPTY.getItem()) {
                renderItems.add(is);
            }
        }
        byte b = 0;
        float distance = 0.25F;
        float angle = 360 / (float) renderItems.size();
        if (renderItems.size() != 1) {
            for (ItemStack is : renderItems) {
                double rad = Math.toRadians(angle * b++ + (entity.rotationYaw + 180.0F));
                if (renderItems.size() == 3) {
                    rad += 0.5;
                } else if (renderItems.size() == 4) {
                    rad -= 0.78;
                    distance = 0.3F;
                }

                else if (renderItems.size() == 5) {
                    rad -= 0.32;
                    distance = 0.325F;
                }
                double offsetX = distance * Math.cos(rad);
                double offsetZ = distance * Math.sin(rad);
                doRenderItem(is, d0 + offsetX, d1 + 0.18, d2 + offsetZ, f, f1);
            }
        } else {
            doRenderItem(renderItems.get(0), d0, d1 + 0.18, d2, 0, 0.0625F);
        }
        GL11.glPopMatrix();
    }

    private ResourceLocation func_110796_a(ItemStack itemStack) {
        return this.renderManager.renderEngine.getResourceLocation(itemStack.getItemSpriteNumber());
    }

    public void doRenderItem(ItemStack itemStack, double par2, double par4, double par6, float par8, float par9) {
        this.bindTexture(func_110796_a(itemStack));
        this.random.setSeed(187L);

        if (itemStack.getItem() != null) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float) par2, (float) par4 + 0.1F, (float) par6);
            byte b0 = 1;
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            float f4;
            float f5;
            float f6;
            int i;
            float f8;

            if (itemStack.getItemSpriteNumber() == 1 && itemStack.getItem().requiresMultipleRenderPasses()) {
                GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                GL11.glTranslatef(0.0F, -0.05F, 0.0F);

                for (int k = 0; k < itemStack.getItem().getRenderPasses(itemStack.getItemDamage()); ++k) {
                    this.random.setSeed(187L);
                    IIcon icon = itemStack.getItem().getIcon(itemStack, k);
                    f8 = 1.0F;
                    i = itemStack.getItem().getColorFromItemStack(itemStack, k);
                    f5 = (i >> 16 & 255) / 255.0F;
                    f4 = (i >> 8 & 255) / 255.0F;
                    f6 = (i & 255) / 255.0F;
                    GL11.glColor4f(f5 * f8, f4 * f8, f6 * f8, 1.0F);
                    this.renderDroppedItem(itemStack, icon, b0, par9, f5 * f8, f4 * f8, f6 * f8, k);
                }
            } else {
                GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                GL11.glTranslatef(0.0F, -0.05F, 0.0F);
                IIcon icon1 = itemStack.getIconIndex();
                int l = itemStack.getItem().getColorFromItemStack(itemStack, 0);
                f8 = (l >> 16 & 255) / 255.0F;
                float f9 = (l >> 8 & 255) / 255.0F;
                f5 = (l & 255) / 255.0F;
                f4 = 1.0F;
                this.renderDroppedItem(itemStack, icon1, b0, par9, f8 * f4, f9 * f4, f5 * f4);
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

    private void renderDroppedItem(ItemStack itemStack, IIcon par2Icon, int par3, float par4, float par5, float par6, float par7) {
        renderDroppedItem(itemStack, par2Icon, par3, par4, par5, par6, par7, 0);
    }

    private void renderDroppedItem(ItemStack itemStack, IIcon par2Icon, int par3, float par4, float par5, float par6, float par7, int pass) {
        Tessellator tessellator = Tessellator.instance;

        if (par2Icon == null) {
            TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
            ResourceLocation resourcelocation = texturemanager.getResourceLocation(itemStack.getItemSpriteNumber());
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
