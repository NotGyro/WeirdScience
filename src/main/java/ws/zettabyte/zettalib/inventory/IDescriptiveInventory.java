package ws.zettabyte.zettalib.inventory;

import net.minecraft.inventory.IInventory;

/**
 * Any inventory or inventory provider which can provide you with information about ItemSlot objects directly.
 * @author Sam "Gyro" C.
 *
 */
public interface IDescriptiveInventory extends IInventory, IComponentContainer {
	
	Iterable<ItemSlot> getSlots();

	/**
	 * Gets a slot by the index in the IInventory's slot array.
	 * @return Can be null.
	 */
	default ItemSlot getSlot(int idx) {
		for(ItemSlot e : getSlots()) {
			if(e.getSlotIndex() == idx) {
				return e;
			}
		}
		return null;
	};
	
	/**
	 * @param name Component name of the slot.
	 * @return Can be null.
	 */
	default ItemSlot getNamedSlot(String name) {
		for(ItemSlot e : getSlots()) {
			if(e.getComponentName().equalsIgnoreCase(name)) {
				return e;
			}
		}
		return null;
	};
	
}
