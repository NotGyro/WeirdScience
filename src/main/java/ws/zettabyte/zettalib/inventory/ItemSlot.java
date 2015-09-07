package ws.zettabyte.zettalib.inventory;

import net.minecraft.item.ItemStack;


//TODO: Where to begin...
// * Figure out whether or not we're aliasing another type from the Container system -- We are, but
// I'm fine with that since my model is easier to work with. I'll just wrap their model in mine.
// * Figure out how to make this class useful for easing TileEnt <-> Container <-> GUI code.
// * Produce an IItemSlot so we can do clever things.
// * Whitelisted, blacklisted slots.

//I know this is a reimplementation of package net.minecraft.inventory.Slot, but it's worth it in this case.
public class ItemSlot {
	protected ItemStack stack = null;
	public int maxSize = 64;
	
	public final int slotNumber;
	public final String name;

	public ItemSlot(int num, String nom) {
		slotNumber = num;
		name = nom;
	}
	public ItemSlot(int num) {
		this(num, null);
	}
	/**
	 * 
	 * @param s Input stack.
	 * @return If we called acceptInput, would it do anything>
	 */
	public boolean canInput(ItemStack s) { return true; }
	
	/**
	 * 
	 * @return If we called any of our item output functions, would they do anything?
	 */
	public boolean canOutput() { return true; }
	/**
	 * 
	 * @param s Input stack.
	 * @return Leftovers: Whatever cannot fit into the slot.
	 */
	public ItemStack acceptInput(ItemStack s) { 
		if(stack == null) {
			//Nothing in the slot
			stack = s;
			s = null;
		}
		else if(s.isItemEqual(stack)) {
			int total = s.stackSize + stack.stackSize;
			if(total > maxSize) {
				stack.stackSize = maxSize;
				s.stackSize = total - maxSize;
			}
			else {
				stack.stackSize = total;
				s = null;
			}
		}
		return s;
	}
	
	public ItemStack getStack() {
		return stack;
	}
	
	public ItemStack takeStack() {
		stack = null;
		return stack;
	}
	
	public ItemStack splitStack(int amt) {
		if(amt >= stack.stackSize) {
			return takeStack();
		}
		else {
			ItemStack newStack = stack.copy();
			newStack.stackSize = amt;
			stack.stackSize -= amt;
			return newStack;
		}
	}
	
	public ItemStack getStackForRender() {
		return getStack();
	}
	/**
	 * Ignore all input / output rules and just set our stack.
	 */
	public void setStackForce(ItemStack s) {
		this.stack = s;
	}
	
	//TODO: Run item-stack NBT saving through this, so that ghost slots can save properly.
	
	
}
