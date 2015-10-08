package ws.zettabyte.zettalib.thermal;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * This is for entities / Tile Entities, etc that can do heat behavior.
 * @author Sam "Gyro" C.
 *
 */
public interface IHeatLogic {
	/**
	 * @return Heat of this object in degrees celsius.
	 */
	int getHeat();
	default int getHeat(ForgeDirection side) { return this.getHeat(); }

	/**
	 * Adds some amount to our temperature - if it is negative, our temperature is
	 * reduced, and if it is positive, our temperature is increased. 
	 * 
	 * Note: it is ALWAYS the responsibility of the hotter object to do balancing logic.
	 * 
	 * @param value What to alter the 
	 * @return How much we were actually able to modify the temperature by. 
	 */
	int modifyHeat(int value);
	default int modifyHeat(int value, ForgeDirection side) { return this.modifyHeat(value); }
	
	/**
	 * @return How many degrees celsius can we transfer into or out of this object
	 * per second?
	 */
	float getHeatRate();
	default float getHeatRate(ForgeDirection side) { return this.getHeatRate(); }
	
	/**
	 * @return How hot it is in our biome.
	 */
	int getAmbientHeat();
}