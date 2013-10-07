package ruby.bamboo;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BambooUtil {
    public static Block getBlcokInstance(int blockID){
    	return Block.blocksList[blockID];
    }
    
    public static Item getItemInstance(int itemID){
    	return Item.itemsList[itemID];
    }

    public static byte getPlayerDir(Entity entity)
    {
        return (byte)(MathHelper.floor_double((entity.rotationYaw * 4F) / 360F + 0.5D) & 3);
    }
    
    public static boolean isInnerCircleCollision(int posX,int posY,int posZ,int tposX,int tposY,int tposZ,int r){
		return Math.sqrt(Math.pow((posX-tposX),2) + Math.pow((posY-tposY),2) + Math.pow((posZ-tposZ),2))<=r;
	}
    
    public static float getInnerCircleCollision(int posX,int posY,int posZ,int tposX,int tposY,int tposZ){
		return (float) Math.sqrt(Math.pow((posX-tposX),2) + Math.pow((posY-tposY),2) + Math.pow((posZ-tposZ),2));
	}
    
	public static boolean isUnbreakBlock(World world,int posX,int posY,int posZ){
		return Block.blocksList[world.getBlockId(posX, posY, posZ)]!=null?Block.blocksList[world.getBlockId(posX, posY, posZ)].getBlockHardness(world, posX,posY,posZ)<0:true;
	}
}
