package ws.zettabyte.zettalib.inventory;

import ws.zettabyte.zettalib.client.gui.IGUIWidget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * An implementation of the Slot class which also encapsulates the ItemStack - not only
 * is it a description of properties and a way of interacting with Inventories, now, but it's
 * actually a part of the inventory.
 * @author Sam "Gyro" C.
 *
 */
public class ItemSlot extends Slot implements IInvComponent {
	protected ItemStack stack = null;
	public int maxSize = 64;
	
	//public final int slotNumber;
	protected String name;
	public final IInventory inventory;
	
	public IGUIWidget guiInfo = null;
	
	public ItemSlot(IInventory inv, int slotnum) {
		super(inv, slotnum, -1, -1);
		inventory = inv;
		//slotNumber = slotnum;
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
	 * Checked by Tile Entities and such but not by GUIs.
	 */
	public boolean canOutputMachine() { return true; }
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

	@Override
	public String getComponentName() {
		return name;
	}	
}
