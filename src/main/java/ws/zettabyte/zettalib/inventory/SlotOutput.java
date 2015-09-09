package ws.zettabyte.zettalib.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
/**
 * An item slot that you can take items out of but not place them into.
 * 
 * Generally, the way items get in there in the first place is that the
 * machine which owns this slot puts them there.
 * 
 * @author Sam "Gyro" Cutlip
 *
 */
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
