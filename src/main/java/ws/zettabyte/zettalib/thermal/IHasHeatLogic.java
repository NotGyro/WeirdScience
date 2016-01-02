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
	default int getHeatTransferRate() { return getHeatLogic().getHeatTransferRate(); };
	default int getAmbientHeat() { return getHeatLogic().getAmbientHeat(); };
	
	default void doBalance(Iterable<IHeatLogic> list) { getHeatLogic().doBalance(list); }
	default void recieveBalance(int h) { getHeatLogic().recieveBalance(h); }
	default void doPassiveLoss() { getHeatLogic().doPassiveLoss(); }
}
