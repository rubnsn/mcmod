package ruby.bamboo.entity.magatama;

import java.util.EnumSet;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class MagatamaTickHandler implements ITickHandler{
	public static MagatamaTickHandler instance;
	static{
		instance=new MagatamaTickHandler();
		TickRegistry.registerTickHandler(instance, Side.SERVER);
		TickRegistry.registerTickHandler(instance, Side.CLIENT);
	}
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		
	}

	@Override
	public EnumSet<TickType> ticks() {
		// TODO 自動生成されたメソッド・スタブ
		return TickType.WORLD.partnerTicks();
	}

	@Override
	public String getLabel() {
		// TODO 自動生成されたメソッド・スタブ
		return "magatama";
	}

}
