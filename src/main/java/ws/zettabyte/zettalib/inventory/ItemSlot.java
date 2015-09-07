package ws.zettabyte.zettalib.inventory;

import ws.zettabyte.zettalib.client.gui.IGUIWidget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;


//TODO: Where to begin...
// * Figure out whether or not we're aliasing another type from the Container system -- We are, but
// I'm fine with that since my model is easier to work with. I'll just wrap their model in mine.
// * Figure out how to make this class useful for easing TileEnt <-> Container <-> GUI code.
// * Produce an IItemSlot so we can do clever things.
// * Whitelisted, blacklisted slots.

//I know this is a reimplementation of package net.minecraft.inventory.Slot, but it's worth it in this case.
public class ItemSlot extends Slot {
	protected ItemStack stack = null;
	public int maxSize = 64;
	
	public final int slotNumber;
	public String name;
	public final IInventory inventory;
	
	public IGUIWidget guiInfo = null;
	
	public ItemSlot(IInventory inv, int slotnum) {
		super(inv, slotnum, 0, 0);
		inventory = inv;
		slotNumber = slotnum;
		name = null;
	}

	public ItemSlot(IInventory inv, int slotnum, String name) {
		this(inv, slotnum);
		this.name = name;
	}

	/**
	 * 
	 * @param s Input stack.
	 * @return If we called acceptInput, would it do anything?
	 */
	@Override
	public boolean isItemValid(ItemStack stack) { return true; }
	
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
	        this.onSlotChanged();
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
	        this.onSlotChanged();
		}
		return s;
	}
	
	public ItemStack getStack() {
		return stack;
	}
	
	public ItemStack takeStack() {
		ItemStack ret = stack;
		stack = null;
        this.onSlotChanged();
		return ret;
	}


	@Override
	public ItemStack decrStackSize(int amt) {
		ItemStack ret = null;
		if(amt >= stack.stackSize) {
	        ret = takeStack();
		}
		else {
			ret = stack.copy();
			ret.stackSize = amt;
			stack.stackSize -= amt;
	        this.onSlotChanged();
		}
		return ret;
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

	@Override
	public boolean getHasStack() {
		return (stack != null);
	}

	//TODO: Make this not stupid
	@Override
	public void putStack(ItemStack stack) {
		super.putStack(stack);
	}

	@Override
	public boolean isSlotInInventory(IInventory inv, int num) {
		return ((inv == inventory) && (slotNumber == num));
	}

	@Override
	public boolean canTakeStack(EntityPlayer p) {
		return super.canTakeStack(p) && canOutput();
	}

	@Override
	public int getSlotIndex() {
		return slotNumber;
	}
	
	//TODO: Run item-stack NBT saving through this, so that ghost slots can save properly.
	
	
}
