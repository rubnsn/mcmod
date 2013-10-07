package ruby.bamboo.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelKaguya extends ModelBase
{
    //variables init:
    public ModelRenderer box7;
    public ModelRenderer box0;
    public ModelRenderer head;
    public ModelRenderer soder;
    public ModelRenderer sodel;
    public ModelRenderer hair;
    public ModelRenderer momil;
    public ModelRenderer momir;
    public ModelRenderer sidehairr;
    public ModelRenderer sidehairl;
    public ModelRenderer handr;
    public ModelRenderer handl;
    public ModelKaguya()
    {
        //constructor:
        box7 = new ModelRenderer(this, 32, 13);
        box7.addBox(-3F, -3F, -3F, 6, 6, 6);
        box7.setRotationPoint(0F, 2F, 0F);
        box7.rotateAngleZ = (float)Math.PI;
        box0 = new ModelRenderer(this, 32, 0);
        box0.addBox(-3F, -11F, -3F, 6, 7, 6);
        box0.rotateAngleZ = (float)Math.PI;
        head = new ModelRenderer(this, 0, 0);
        head.addBox(-4F, -6F, -4F, 8, 7, 8);
        head.setRotationPoint(0F, 12F, 1F);
        head.rotateAngleZ = (float)Math.PI;
        soder = new ModelRenderer(this, 0, 17);
        soder.addBox(0F, -4F, -8F, 2, 7, 8);
        soder.setRotationPoint(6F, 6F, 1F);
        soder.rotateAngleX = (float)(Math.PI * -15) / 180;
        soder.rotateAngleY = (float)(Math.PI * -25) / 180;
        soder.rotateAngleZ = (float)(Math.PI * -170) / 180;
        sodel = new ModelRenderer(this, 0, 17);
        //sodel.mirror=true;
        sodel.addBox(-2F, -4F, -8F, 2, 7, 8);
        sodel.setRotationPoint(-6F, 6F, 1F);
        sodel.rotateAngleX = (float)(Math.PI * -15) / 180;
        sodel.rotateAngleY = (float)(Math.PI * -335) / 180;
        sodel.rotateAngleZ = (float)(Math.PI * -190) / 180;
        hair = new ModelRenderer(this, 12, 15);
        hair.addBox(-4F, 1F, -4F, 8, 8, 1);
        hair.setRotationPoint(0F, 12F, 1F);
        hair.rotateAngleZ = (float)Math.PI;
        momil = new ModelRenderer(this, 0, 0);
        momil.addBox(-4F, -2F, -4F, 1, 2, 2);
        momil.setRotationPoint(0F, 12F, 1F);
        momir = new ModelRenderer(this, 0, 0);
        momir.addBox(3F, -2F, -4F, 1, 2, 2);
        momir.setRotationPoint(0F, 12F, 1F);
        sidehairl = new ModelRenderer(this, 56, 1);
        sidehairl.addBox(3F, 0F, 0F, 1, 9, 3);
        sidehairl.setRotationPoint(0F, 12F, 1F);
        sidehairl.rotateAngleZ = (float)Math.PI;
        sidehairr = new ModelRenderer(this, 56, 1);
        sidehairr.addBox(-4F, 0F, 0F, 1, 9, 3);
        sidehairr.setRotationPoint(0F, 12F, 1F);
        sidehairr.rotateAngleZ = (float)Math.PI;
        handr = new ModelRenderer(this, 55, 13);
        handr.addBox(0F, -4F, -9F, 2, 2, 1);
        handr.setRotationPoint(6F, 6F, 1F);
        handr.rotateAngleX = soder.rotateAngleX;
        handr.rotateAngleY = soder.rotateAngleY;
        handr.rotateAngleZ = soder.rotateAngleZ;
        handl = new ModelRenderer(this, 55, 13);
        handl.mirror = true;
        handl.addBox(-2F, -4F, -9F, 2, 2, 1);
        handl.setRotationPoint(-6F, 6F, 1F);
        handl.rotateAngleX = sodel.rotateAngleX;
        handl.rotateAngleY = sodel.rotateAngleY;
        handl.rotateAngleZ = sodel.rotateAngleZ;
    }
    @Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        //render:
        setRotationAngles(f, f1, f2, f3, f4, f5);
        box7.render(f5);
        box0.render(f5);
        head.render(f5);
        soder.render(f5);
        sodel.render(f5);
        hair.render(f5);
        sidehairl.render(f5);
        sidehairr.render(f5);
        momil.render(f5);
        momir.render(f5);
        handr.render(f5);
        handl.render(f5);
    }
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
    {
        head.rotateAngleY = f3 / (180F / (float)Math.PI);
        head.rotateAngleX = f4 / (180F / (float)Math.PI) - 0.4F;
        momir.rotateAngleX = -head.rotateAngleX;
        momir.rotateAngleY = -head.rotateAngleY;
        momil.rotateAngleX = -head.rotateAngleX;
        momil.rotateAngleY = -head.rotateAngleY;
        hair.rotateAngleX = head.rotateAngleX * 0.5F;
        hair.rotateAngleY = head.rotateAngleY + (float)Math.PI;
        sidehairr.rotateAngleX = -head.rotateAngleX * 0.5F;
        sidehairr.rotateAngleY = head.rotateAngleY;
        sidehairl.rotateAngleX = -head.rotateAngleX * 0.5F;
        sidehairl.rotateAngleY = head.rotateAngleY;
    }
}/*
//variables init:
public ModelRenderer box;
public ModelRenderer box0;
public ModelRenderer box1;
public ModelRenderer box10;
public ModelRenderer box2;
public ModelRenderer box3;
public ModelRenderer box4;
public ModelRenderer box5;
public ModelRenderer box6;
public ModelRenderer box7;
public ModelRenderer box8;
public ModelRenderer box9;

//constructor:
box = new ModelRenderer(32, 13);
box.addBox(-3F, -3F, -3F, 6, 6, 6);
box.setPosition(0F, 2F, 0F);
box.rotateAngleZ = (float)Math.PI;

box0 = new ModelRenderer(32, 0);
box0.addBox(-3F, -11F, -3F, 6, 7, 6);
box0.rotateAngleZ = (float)Math.PI;

box1 = new ModelRenderer(0, 0);
box1.addBox(-4F, -6F, -4F, 8, 7, 8);
box1.setPosition(0F, 12F, 0F);
box1.rotateAngleZ = (float)Math.PI;

box10 = new ModelRenderer(55, 13);
box10.addBox(1F, -4F, -8F, 2, 2, 2);
box10.setPosition(6F, 6F, -1F);
box10.rotateAngleX = 0.24434609527920614F;
box10.rotateAngleY = 5.846852994181004F;
box10.rotateAngleZ = 2.9670597283903604F;

box2 = new ModelRenderer(0, 17);
box2.addBox(1F, -4F, -7F, 2, 7, 8);
box2.setPosition(6F, 6F, -1F);
box2.rotateAngleX = 0.24434609527920614F;
box2.rotateAngleY = 5.846852994181004F;
box2.rotateAngleZ = 2.9670597283903604F;

box3 = new ModelRenderer(0, 17);
box3.addBox(-3F, -4F, -7F, 2, 7, 8);
box3.setPosition(-6F, 6F, -1F);
box3.rotateAngleX = 0.24434609527920614F;
box3.rotateAngleY = 0.4363323129985824F;
box3.rotateAngleZ = 3.3161255787892263F;

box4 = new ModelRenderer(12, 15);
box4.addBox(-4F, 1F, 4F, 8, 8, 1);
box4.setPosition(0F, 12F, -1F);
box4.rotateAngleZ = (float)Math.PI;

box5 = new ModelRenderer(0, 0);
box5.addBox(-4F, -2F, -5F, 1, 2, 2);
box5.setPosition(0F, 12F, 1F);

box6 = new ModelRenderer(0, 0);
box6.addBox(3F, -2F, -4F, 1, 2, 2);
box6.setPosition(0F, 12F, 0F);

box7 = new ModelRenderer(56, 1);
box7.addBox(3F, 0F, 2F, 1, 9, 3);
box7.setPosition(0F, 12F, -1F);
box7.rotateAngleZ = (float)Math.PI;

box8 = new ModelRenderer(56, 1);
box8.addBox(-4F, 0F, 2F, 1, 9, 3);
box8.setPosition(0F, 12F, -1F);
box8.rotateAngleZ = (float)Math.PI;

box9 = new ModelRenderer(55, 13);
box9.addBox(-3F, -4F, -8F, 2, 2, 2);
box9.setPosition(-6F, 6F, -1F);
box9.rotateAngleX = 0.24434609527920614F;
box9.rotateAngleY = 0.4363323129985824F;
box9.rotateAngleZ = 3.3161255787892263F;

//render:
box.render(f5);
box0.render(f5);
box1.render(f5);
box10.render(f5);
box2.render(f5);
box3.render(f5);
box4.render(f5);
box5.render(f5);
box6.render(f5);
box7.render(f5);
box8.render(f5);
box9.render(f5);

*/
