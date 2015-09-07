package ws.zettabyte.zettalib.inventory;

import net.minecraft.item.ItemStack;

public class SlotInput extends ItemSlot {

	public SlotInput(int num) {
		super(num);
	}
	public SlotInput(int num, String n) {
		super(num, n);
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
	public ItemStack splitStack(int amt) {
		return null;
	}
	
	
}
