package ws.zettabyte.zettalib.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * An item slot you can put stacks into but not take them out of.
 * @author Sam "Gyro" C.
 */
public class SlotInputOnly extends SlotInput {

	public SlotInputOnly(IInventory inv, int slotnum, String name) {
		super(inv, slotnum, name);
		// TODO Auto-generated constructor stub
	}
	public SlotInputOnly(IInventory inv, int slotnum) {
		super(inv, slotnum);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean canOutput() {
		return false;
	}
	@Override
	public ItemStack getStack() {
		return super.getStack();
	}
	@Override
	public ItemStack takeStack() {
		return null;
	}
	@Override
	public ItemStack decrStackSize(int amt) {
		return null;
	}
	
	
}
