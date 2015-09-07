package ws.zettabyte.zettalib.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public interface IDescriptiveInventory extends IInventory {
	Iterable<ItemSlot> getSlots();
	
	boolean canInteractWith(EntityPlayer player);
}
