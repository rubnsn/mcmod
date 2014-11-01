package mmm.littleMaidMob.gui;

import java.util.Random;

import mmm.lib.Client;
import mmm.littleMaidMob.entity.EntityLittleMaidBase;
import mmm.littleMaidMob.inventory.ContainerInventory;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.EXTRescaleNormal;
import org.lwjgl.opengl.GL11;

public class GuiLittleMaidInventory extends GuiEffectRenderer {

    protected static ResourceLocation defGuiTex = new ResourceLocation("mmm", "textures/gui/container/littlemaidinventory.png");
    private Random rand;
    private int updateCounter;
    public EntityLittleMaidBase maid;
    public GuiButtonNextPage txbutton[] = new GuiButtonNextPage[4];
    public GuiButton selectbutton;

    private IInventory upperChestInventory;
    private IInventory lowerChestInventory;

    public GuiLittleMaidInventory(EntityLittleMaidBase pMaid, EntityPlayer pPlayer) {
        super(new ContainerInventory(pMaid, pPlayer), pMaid);
        rand = new Random();
        upperChestInventory = pPlayer.inventory;
        lowerChestInventory = pMaid.inventory;
        maid = pMaid;
        ySize = 207;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        super.initGui();
        buttonList.add(txbutton[0] = new GuiButtonNextPage(100, guiLeft + 25, guiTop + 7, false));
        buttonList.add(txbutton[1] = new GuiButtonNextPage(101, guiLeft + 55, guiTop + 7, true));
        buttonList.add(txbutton[2] = new GuiButtonNextPage(110, guiLeft + 25, guiTop + 47, false));
        buttonList.add(txbutton[3] = new GuiButtonNextPage(111, guiLeft + 55, guiTop + 47, true));
        buttonList.add(selectbutton = new GuiButton(200, guiLeft + 25, guiTop + 25, 53, 17, "select"));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        // 背景
        //		ResourceLocation lrl = maid.textureData.getGUITexture();
        ResourceLocation lrl = null;
        if (lrl == null) {
            lrl = defGuiTex;
        }
        mc.getTextureManager().bindTexture(lrl);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int lx = guiLeft;
        int ly = guiTop;
        drawTexturedModalRect(lx, ly, 0, 0, xSize, ySize);

        // LP/AP
        drawHeathArmor(0, 0);

    }

    /** ボタン類の表示 */
    @Override
    public void drawScreen(int i, int j, float f) {
        super.drawScreen(i, j, f);
        //謎、とりあえず無くても問題ないっぽい
        //xSize = i;
        //ySize = j;
        int inGuiLeft = i - guiLeft;
        int inGuiTop = j - guiTop;
        if (inGuiLeft > 25 && inGuiLeft < 78 && inGuiTop > 7 && inGuiTop < 60) {
            // ボタンの表示
            txbutton[0].visible = true;
            txbutton[1].visible = true;
            txbutton[2].visible = true;
            txbutton[3].visible = true;
            selectbutton.visible = true;

            // テクスチャ名称の表示
            GL11.glPushMatrix();
            GL11.glTranslatef(i - inGuiLeft, j - inGuiTop, 0.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            if (maid.getMultiModel().model.getModelClass()[0] != null) {
                String ls1 = maid.getMultiModel().model.name;
                String ls2 = maid.getMultiModel().model.getTextureName(1);
                int ltw1 = fontRendererObj.getStringWidth(ls1);
                int ltw2 = fontRendererObj.getStringWidth(ls2);
                int ltwmax = (ltw1 > ltw2) ? ltw1 : ltw2;
                int lbx = 52 - ltwmax / 2;
                int lby = 68;
                int lcolor;
                lcolor = inGuiTop < 20 ? 0xc0882222 : 0xc0000000;
                drawGradientRect(lbx - 3, lby - 4, lbx + ltwmax + 3, lby + 8, lcolor, lcolor);
                fontRendererObj.drawStringWithShadow(ls1, 52 - ltw1 / 2, lby - 2, -1);
                lcolor = inGuiTop > 46 ? 0xc0882222 : 0xc0000000;
                drawGradientRect(lbx - 3, lby + 8, lbx + ltwmax + 3, lby + 16 + 4, lcolor, lcolor);
                fontRendererObj.drawStringWithShadow(ls2, 52 - ltw2 / 2, lby + 10, -1);
            }

            GL11.glPopMatrix();
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        } else {
            txbutton[0].visible = false;
            txbutton[1].visible = false;
            txbutton[2].visible = false;
            txbutton[3].visible = false;
            selectbutton.visible = false;
        }

    }

    /** HPとアーマーゲージと酸素 */
    protected void drawHeathArmor(int par1, int par2) {
        boolean var3 = maid.hurtResistantTime / 3 % 2 == 1;

        if (maid.hurtResistantTime < 10) {
            var3 = false;
        }

        Client.setTexture(this.icons);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int lhealth = MathHelper.ceiling_float_int(maid.getHealth());
        int llasthealth = MathHelper.ceiling_float_int(maid.prevHealth);
        this.rand.setSeed((long) (updateCounter * 312871));
        boolean var6 = false;
        //      FoodStats var7 = entitylittlemaid.getFoodStats();
        //      int var8 = var7.getFoodLevel();
        //      int var9 = var7.getPrevFoodLevel();
        IAttributeInstance var10 = maid.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth);
        int var13 = par2 - 39;
        float var14 = (float) var10.getAttributeValue();
        float var15 = maid.getAbsorptionAmount();
        int var16 = MathHelper.ceiling_float_int((var14 + var15) / 2.0F / 10.0F);
        int var17 = Math.max(10 - (var16 - 2), 3);
        float var19 = var15;
        int var21 = -1;

        if (maid.isPotionActive(Potion.regeneration)) {
            var21 = updateCounter % MathHelper.ceiling_float_int(var14 + 5.0F);
        }

        int ldrawx;
        int ldrawy;

        // AP
        int larmor = maid.getTotalArmorValue();
        ldrawy = guiTop + 36;
        for (int li = 0; li < 10; ++li) {
            if (larmor > 0) {
                ldrawx = guiLeft + li * 8 + 86;

                if (li * 2 + 1 < larmor) {
                    this.drawTexturedModalRect(ldrawx, ldrawy, 34, 9, 9, 9);
                }
                if (li * 2 + 1 == larmor) {
                    this.drawTexturedModalRect(ldrawx, ldrawy, 25, 9, 9, 9);
                }
                if (li * 2 + 1 > larmor) {
                    this.drawTexturedModalRect(ldrawx, ldrawy, 16, 9, 9, 9);
                }
            }
        }

        // LP
        for (int li = MathHelper.ceiling_float_int((var14 + var15) / 2.0F) - 1; li >= 0; --li) {
            int var23 = 16;
            if (maid.isPotionActive(Potion.poison)) {
                var23 += 36;
            } else if (maid.isPotionActive(Potion.wither)) {
                var23 += 72;
            }

            int var25 = MathHelper.ceiling_float_int((float) (li + 1) / 10.0F);
            ldrawx = guiLeft + li % 10 * 8 + 86;
            ldrawy = guiTop + 7 + var25 * var17;

            if (lhealth <= 4) {
                ldrawy += this.rand.nextInt(2);
            }
            if (li == var21) {
                ldrawy -= 2;
            }

            this.drawTexturedModalRect(ldrawx, ldrawy, var3 ? 25 : 16, 0, 9, 9);

            if (var3) {
                if (li * 2 + 1 < llasthealth) {
                    this.drawTexturedModalRect(ldrawx, ldrawy, var23 + 54, 0, 9, 9);
                }
                if (li * 2 + 1 == llasthealth) {
                    this.drawTexturedModalRect(ldrawx, ldrawy, var23 + 63, 0, 9, 9);
                }
            }

            if (var19 > 0.0F) {
                if (var19 == var15 && var15 % 2.0F == 1.0F) {
                    this.drawTexturedModalRect(ldrawx, ldrawy, var23 + 153, 0, 9, 9);
                } else {
                    this.drawTexturedModalRect(ldrawx, ldrawy, var23 + 144, 0, 9, 9);
                }

                var19 -= 2.0F;
            } else {
                if (li * 2 + 1 < lhealth) {
                    this.drawTexturedModalRect(ldrawx, ldrawy, var23 + 36, 0, 9, 9);
                }
                if (li * 2 + 1 == lhealth) {
                    this.drawTexturedModalRect(ldrawx, ldrawy, var23 + 45, 0, 9, 9);
                }
            }
        }

        // Air
        ldrawy = guiTop + 46;
        if (maid.isInsideOfMaterial(Material.water)) {
            int var23 = maid.getAir();
            int var35 = MathHelper.ceiling_double_int((double) (var23 - 2) * 10.0D / 300.0D);
            int var25 = MathHelper.ceiling_double_int((double) var23 * 10.0D / 300.0D) - var35;

            for (int var26 = 0; var26 < var35 + var25; ++var26) {
                ldrawx = guiLeft + var26 * 8 + 86;
                if (var26 < var35) {
                    this.drawTexturedModalRect(ldrawx, ldrawy, 16, 18, 9, 9);
                } else {
                    this.drawTexturedModalRect(ldrawx, ldrawy, 25, 18, 9, 9);
                }
            }
        }

    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        updateCounter++;
    }

    /** キャラクターとインベントリ文字 */
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        String ls;
        fontRendererObj.drawString(StatCollector.translateToLocal(lowerChestInventory.getInventoryName()), 8, 64, 0x404040);
        fontRendererObj.drawString(StatCollector.translateToLocal(upperChestInventory.getInventoryName()), 8, 114, 0x404040);
        //	      fontRenderer.drawString(StatCollector.translateToLocal(
        //	              "littleMaidMob.text.Health"), 86, 8, 0x404040);
        //	      fontRenderer.drawString(StatCollector.translateToLocal(
        //	              "littleMaidMob.text.AP"), 86, 32, 0x404040);
        fontRendererObj.drawString(StatCollector.translateToLocal("littleMaidMob.text.STATUS"), 86, 8, 0x404040);

        fontRendererObj.drawString(StatCollector.translateToLocal("littleMaidMob.mode.".concat(maid.modeController.getDisplayModeName())), 86, 61, 0x404040);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        // キャラ
        int lj = 0;
        int lk = 0;
        GL11.glEnable(EXTRescaleNormal.GL_RESCALE_NORMAL_EXT);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef(lj + 51, lk + 57, 50F);
        float f1 = 30F;
        GL11.glScalef(-f1, f1, f1);
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        float f2 = maid.renderYawOffset;
        float f3 = maid.rotationYaw;
        float f4 = maid.rotationYawHead;
        float f5 = maid.rotationPitch;
        //	      float f8 = (float) (lj + 51) - xSize_lo;
        //	      float f9 = (float) (lk + 75) - 50 - ySize_lo;
        float f8 = (float) (guiLeft + 51 - par1);
        float f9 = (float) (guiTop + 22 - par2);
        GL11.glRotatef(135F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-(float) Math.atan(f9 / 40F) * 20F, 1.0F, 0.0F, 0.0F);
        maid.renderYawOffset = (float) Math.atan(f8 / 40F) * 20F;
        maid.rotationYawHead = maid.rotationYaw = (float) Math.atan(f8 / 40F) * 40F;
        maid.rotationPitch = -(float) Math.atan(f9 / 40F) * 20F;
        GL11.glTranslatef(0.0F, maid.yOffset, 0.0F);
        RenderManager.instance.playerViewY = 180F;
        RenderManager.instance.renderEntityWithPosYaw(maid, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        maid.renderYawOffset = f2;
        maid.rotationYaw = f3;
        maid.rotationYawHead = f4;
        maid.rotationPitch = f5;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(EXTRescaleNormal.GL_RESCALE_NORMAL_EXT);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F / 1.0F, 240F / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

}
