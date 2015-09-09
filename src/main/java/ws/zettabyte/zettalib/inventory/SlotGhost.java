package ws.zettabyte.zettalib.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * An Item slot which, when inserted to, stores the type of item that was supposed to be placed in it (without consuming the item), and
 * upon any attempt to remove the item the slot clears itself without yielding anything. 
 * 
 * Intended for things like setting filters.
 * 
 * @author Sam "Gyro" Cutlip
 */
public class SlotGhost extends ItemSlot {

	public SlotGhost(IInventory inv, int slotnum, String name) {
		super(inv, slotnum, name);
	}

	public SlotGhost(IInventory inv, int slotnum) {
		super(inv, slotnum);
	}

	@Override
	public boolean isItemValid(ItemStack s) {
		return true;
	}

	@Override
	public boolean canOutput() {
		return true;
	}

	//Accept your stack, return the whole thing as leftovers.
	@Override
	public ItemStack acceptInput(ItemStack s) {
		this.stack = s.copy();
		this.stack.stackSize = 1;
		return s;
	}

	@Override
	public ItemStack getStack() {
		return null;
	}

	@Override
	public ItemStack takeStack() {
		this.stack = null;
		return null;
	}

	@Override
	public ItemStack decrStackSize(int amt) {
		return null;
	}

	@Override
	public ItemStack getStackForRender() {
		return stack;
	}
	
	
}
