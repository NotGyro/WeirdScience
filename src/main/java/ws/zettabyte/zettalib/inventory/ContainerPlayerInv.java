package ws.zettabyte.zettalib.inventory;

import java.util.HashMap;
import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * A container object which provides access to the player's inventory and hotbar, and works with
 * more dynamic ItemSlots and the Widget system.
 * @author Sam "Gyro" C.
 *
 */
public class ContainerPlayerInv extends Container implements ICleanableContainer {

	protected IDescriptiveInventory inv;
	public HashMap<String, ItemSlot> namedSlots = new HashMap<String, ItemSlot>(3);

	protected int beginPlayerInvRange = 0;
	protected int endPlayerInvRange = 27;
	protected int beginHotbarRange = 27;
	protected int endHotbarRange = 36;
	
	public ContainerPlayerInv(IDescriptiveInventory ourInv, InventoryPlayer inventoryPlayer) {
		inv = ourInv;
		Iterable<ItemSlot> slots = ourInv.getSlots();
		for(ItemSlot slot : slots) {
			//TODO: Reposition.
			addSlotToContainer(slot);
			
			if(slot.name != null) {
				namedSlots.put(slot.name, slot);
			}
			
			++beginPlayerInvRange;
			++endPlayerInvRange;
			
			++beginHotbarRange;
			++endHotbarRange;
		}
		

		// Display the player's inventory.
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(inventoryPlayer,
						(j + i * 9 + 9), 8 + j * 18, 84 + i * 18));
			}
		}

		for (int i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18,
					142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return inv.canInteractWith(player);
	}
	
	/* I'm fairly certain this is meant to return whatever is going to end up in your hand.
	 * e.g. you cannot automatically place this item anywhere.
	 * 
	 * SlotID is the slot we are shift-clicking on.
	 * 
	 * Watch out: Shift-clicking is the most spaghetticoded system in Minecraft.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotID) {
		Slot slot = (Slot) this.inventorySlots.get(slotID);

		if (slot != null && slot.getHasStack()) {
			ItemStack stackCurrent = slot.getStack();
			ItemStack stackInitial = stackCurrent.copy();

			//We are shift-clicking in the machine.
			if(slot.slotNumber < this.beginPlayerInvRange) {
				//Do clever checks n' stuff
				if(slot instanceof ItemSlot) {
					if(((ItemSlot)slot).canOutput()) {
						stackCurrent = putToPlayerInventory(stackCurrent);
						slot.onSlotChange(stackCurrent, stackInitial);
					}
					else {
						//Do not even try to take the stack, shouldn't do anything here.
					}
				} 
				else { //Native-type slot
					stackCurrent = putToPlayerInventory(stackCurrent);
					slot.onSlotChange(stackCurrent, stackInitial);
				}
			}
			else {
				for(ItemSlot e : inv.getSlots()) {
					if(e.isItemValid(stackCurrent)) {
						stackCurrent = e.acceptInput(stackCurrent);
					}
				}
			}

			if(stackCurrent == null) {
				slot.putStack((ItemStack) null);
				slot.onSlotChanged();
				return null;
			}
			else if (stackCurrent.stackSize == 0) {
				slot.putStack((ItemStack) null);
				slot.onSlotChanged();
			}

			if (stackCurrent.stackSize == stackInitial.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(player, stackCurrent);
			return stackInitial;
		} else {
			//Nothing in the slot
			return null;
		}
	}
	
	protected ItemStack putToPlayerInventory(ItemStack stackCurrent) {
		if(stackCurrent == null) return null;
		// mergeItemStack takes the item stack to attempt to merge, and
		// the slots on this object to attempt to merge it into. Bool at
		// the end is "reverse order or no?"
		//
		// Returns "could we do anything with it? 
		mergeItemStack(stackCurrent, beginPlayerInvRange, endHotbarRange, true);
		if(stackCurrent.stackSize <= 0) return null;
		return stackCurrent;
	}

	/**
	 * Remove any slot with a negative position variable.
	 * 
	 * Note: Missing slots will break sync between client and server.
	 */
	@Override
	public void cleanupUnlinkedSlots() {
		Iterator iter = inventorySlots.iterator();
		int i = -1;
		while(iter.hasNext()) {
			Slot slot = (Slot)iter.next();
			//The world's silliest hack, right here.
			if(slot.xDisplayPosition < 0) {
				slot.xDisplayPosition = -99999;
				slot.yDisplayPosition = -99999;
			}
			else if(slot.yDisplayPosition < 0) {
				slot.xDisplayPosition = -99999;
				slot.yDisplayPosition = -99999;
			}
		}
	}

	
}
