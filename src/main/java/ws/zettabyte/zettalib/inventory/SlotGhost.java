package ws.zettabyte.zettalib.inventory;

import net.minecraft.item.ItemStack;

public class SlotGhost extends ItemSlot {

	public SlotGhost(int num, String nom) {
		super(num, nom);
	}

	public SlotGhost(int num) {
		super(num);
	}

	@Override
	public boolean canInput(ItemStack s) {
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
	public ItemStack splitStack(int amt) {
		return null;
	}

	@Override
	public ItemStack getStackForRender() {
		return stack;
	}
	
	
}
