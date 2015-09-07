package ws.zettabyte.zettalib.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotOutput extends ItemSlot {
	public SlotOutput(IInventory inv, int slotnum, String name) {
		super(inv, slotnum, name);
	}
	public SlotOutput(IInventory inv, int slotnum) {
		super(inv, slotnum);
	}
	
	@Override
	public ItemStack acceptInput(ItemStack s) {
		return s;
	}
	@Override
	public boolean isItemValid(ItemStack s) {
		return false;
	}
	
}
