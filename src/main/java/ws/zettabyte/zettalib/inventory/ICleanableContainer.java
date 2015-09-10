package ws.zettabyte.zettalib.inventory;

/**
 * An interface to implement on containers which support removing invalidly-positioned slots.
 * @author Sam "Gyro" C.
 *
 */
public interface ICleanableContainer {
	/**
	 * Deletes any slot in position (-1, -1).
	 */
	void cleanupUnlinkedSlots();
}
