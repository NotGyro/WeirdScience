package ws.zettabyte.zettalib.thermal;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * This is for entities / Tile Entities, etc that can do heat behavior.
 * @author Sam "Gyro" C.
 *
 */
public interface IHeatLogic {
	/**
	 * @return Heat of this object in mC (thousandths of a degree celsius).
	 */
	int getHeat();
	default int getHeat(ForgeDirection side) { return this.getHeat(); }

	/**
	 * Adds some amount to our temperature - if it is negative, our temperature is
	 * reduced, and if it is positive, our temperature is increased. 
	 * 
	 * Note: it is ALWAYS the responsibility of the hotter object to do balancing logic.
	 * 
	 * @param value How much to alter our temperature by.
	 * @return How much we were actually able to modify the temperature by. 
	 */
	int modifyHeat(int value);
	default int modifyHeat(int value, ForgeDirection side) { return this.modifyHeat(value); }
	
	/**
	 * @return How many thousandths of a degree celsius can we transfer into or out of this object
	 * per second?
	 */
	int getHeatTransferRate();
	default int getHeatRate(ForgeDirection side) { return this.getHeatTransferRate(); }
	
	/**
	 * @return How hot it is in our biome.
	 */
	int getAmbientHeat();
	

	/**
	 * Perform passive transfer of heat from one Heat Logic to several others.
	 * NOTE: Order should not matter, and it is always the responsibility of
	 * the object of higher temperature to do the balancing behavior.
	 */
	void doBalance(Iterable<IHeatLogic> list);
	/**
	 * Doesn't go through ModifyHeat - instead,
	 * all ticking changes to heat are deferred by one tick,
	 * to avoid direction / order bugs.
	 */
	void recieveBalance(int h);
	/**
	 * Lose heat to or gain heat from the atmosphere
	 */
	void doPassiveLoss();
}