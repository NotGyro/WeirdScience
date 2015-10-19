package ws.zettabyte.zettalib.inventory;

import net.minecraft.inventory.IInventory;

public class SlotInput extends ItemSlot {

	public SlotInput(IInventory inv, int slotnum) {
		super(inv, slotnum);
	}

	public SlotInput(IInventory inv, int slotnum, String name) {
		super(inv, slotnum, name);
	}
	@Override
	public boolean canOutputMachine() {
		return false;
	}
}
