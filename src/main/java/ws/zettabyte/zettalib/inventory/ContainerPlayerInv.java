package ws.zettabyte.zettalib.inventory;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Class for any container which provides access to the player's inventory and hotbar to inherit from.
 * @author gyro
 *
 */
public class ContainerPlayerInv extends Container {

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
	 * Beware: Here I am dealing with the most spaghetticoded system in Minecraft, and that's
	 * one hell of a title.
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
						if(putToPlayerInventory(stackCurrent) == null) {
							return null;
						}
						slot.onSlotChange(stackCurrent, stackInitial);
					}
					else {
						//Do not even try to merge, shouldn't do anything here.
						return null;
					}
				} 
				else { //Native-type slot
					if(putToPlayerInventory(stackCurrent) == null) {
						return null;
					}
					slot.onSlotChange(stackCurrent, stackInitial);
				}
			}
			else {
				for(ItemSlot e : inv.getSlots()) {
					if(e.isItemValid(stackCurrent)) {
						if (!mergeItemStack(stackCurrent, e.slotNumber, e.slotNumber, false)) {
							return null;
						}
					}
				}
			}

			if (stackCurrent.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}

			if (stackCurrent.stackSize == stackInitial.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(player, stackCurrent);
			return stackInitial;
		} else {
			return null;
		}
	}
	
	protected ItemStack putToPlayerInventory(ItemStack stackCurrent) {
		// mergeItemStack takes the item stack to attempt to merge, and
		// the slots on this object to attempt to merge it into. Bool at
		// the end is "reverse order or no?"
		if (!this.mergeItemStack(stackCurrent, beginPlayerInvRange, endHotbarRange, true)) {
			return null;
		}
		return stackCurrent;
	}

}
