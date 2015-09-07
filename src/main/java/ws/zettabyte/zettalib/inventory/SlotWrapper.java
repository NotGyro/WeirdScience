package ws.zettabyte.zettalib.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

/**
 * Used to allow Minecraft slots to work sanely with Zettalib slots.
 * 
 */
public class SlotWrapper extends Slot {
	public ItemSlot slotSpec;
	public SlotWrapper(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_,
			int p_i1824_4_, ItemSlot is) {
		super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
		slotSpec = is;
	}

}
