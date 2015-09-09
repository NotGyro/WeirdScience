package ws.zettabyte.zettalib.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

/**
 * TODO: Refactor this, make it work with components.
 * @author Sam "Gyro" Cutlip
 *
 */
public interface IDescriptiveInventory extends IInventory {
	Iterable<ItemSlot> getSlots();
	/**
	 * Get by slot index.
	 */
	default ItemSlot getSlot(int idx) { 
		for(ItemSlot e : this.getSlots()) {
			if(e.getSlotIndex() == idx) {
				return e;
			}
		}
		return null;
	};
	
	/**
	 * Get by name.
	 */
	default ItemSlot getSlot(String name) { 
		for(ItemSlot e : this.getSlots()) {
			if(e.name != null) {
				if(e.name.equals(name)) {
					return e;
				}
			}
		}
		return null;
	};
	
	boolean canInteractWith(EntityPlayer player);
}
