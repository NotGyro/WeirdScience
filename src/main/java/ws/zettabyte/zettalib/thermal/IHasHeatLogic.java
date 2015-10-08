package ws.zettabyte.zettalib.thermal;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * For convenience while doing composition things instead of inheritance things.
 * @author Sam "Gyro" C.
 */
public interface IHasHeatLogic extends IHeatLogic {
	
	IHeatLogic getHeatLogic();
	
	default int getHeat() { return getHeatLogic().getHeat(); };
	default int modifyHeat(int value) { return getHeatLogic().modifyHeat(value); };
	default float getHeatRate() { return getHeatLogic().getHeatRate(); };
	default int getAmbientHeat() { return getHeatLogic().getAmbientHeat(); };
}
