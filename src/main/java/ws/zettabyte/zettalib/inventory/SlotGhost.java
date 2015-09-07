package ws.zettabyte.zettalib.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

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
