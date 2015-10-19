package ws.zettabyte.zettalib.block;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import ws.zettabyte.zettalib.inventory.FluidTankNamed;
import ws.zettabyte.zettalib.inventory.IDescriptiveInventory;
import ws.zettabyte.zettalib.inventory.IInvComponent;
import ws.zettabyte.zettalib.inventory.ItemSlot;
import ws.zettabyte.zettalib.inventory.SlotOutput;

public abstract class TileEntityInventoryBase extends TileEntityBase implements
		IDescriptiveInventory, ISidedInventory {

	protected ArrayList<ItemSlot> slots = new ArrayList<ItemSlot>(3);
	protected ArrayList<IInvComponent> fullComponentList = null; //new ArrayList<IInvComponent>(8);
	
	public TileEntityInventoryBase() {}

	@Override
	public ItemStack getStackInSlot(int s) {
		if(s < slots.size()) {
			return slots.get(s).getStack();
		}
		return null;
	}
	
	@Override
	public ItemStack decrStackSize(int s, int amount) {
		//Attempts to remove amount from slot # slotID. Returns a usable
		//itemstack taken out of the slot: Split or just yanked.
		if(s < slots.size()) {
			return slots.get(s).decrStackSize(amount);
		}
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int s) {
		if(s < slots.size()) {
			return slots.get(s).getStack();
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int s, ItemStack stack) {
		if(s < slots.size()) {
			slots.get(s).setStackForce(stack);

	        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
	        	stack.stackSize = this.getInventoryStackLimit();
	        }
	        markDirty();
		}
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return (player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) 
				<= 16.0D);
	}

	//More relevant to chests, I think..?
	@Override
	public void openInventory() { }
	@Override
	public void closeInventory() { }

	@Override
	public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
		int[] result = new int[this.slots.size()];
		for(int i = 0; i < this.slots.size(); ++i) {
			result[i] = i;
		}
		return result;
	}

	@Override
	public boolean canInsertItem(int s, ItemStack stack,
			int fromDirection) {
		return isItemValidForSlot(s, stack);
	}

	//TODO: Make this a property of the slots somehow.
	@Override
	public boolean canExtractItem(int s, ItemStack stack,
			int fromDirection) {
		if(s < slots.size()) {
			return slots.get(s).canOutputMachine();
		}
        return false;
	}

	@Override
	public int getSizeInventory() {
		return slots.size();
	}

	@Override
	public boolean isItemValidForSlot(int s, ItemStack stack) {
		if(s < slots.size()) {
			return slots.get(s).isItemValid(stack);
		}
        return false;
	}

	@Override
	public Iterable<IInvComponent> getComponents() {
		if(fullComponentList == null) buildComponentList();
		return fullComponentList;
	}

	protected void buildComponentList() {
		fullComponentList = new ArrayList<IInvComponent>(slots.size());
		for(ItemSlot s : slots) {
			fullComponentList.add(s);
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return isUseableByPlayer(player);
	}

	@Override
	public Iterable<ItemSlot> getSlots() {
		return slots;
	}
	@Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        //Read the item stacks.
        NBTTagList itemList = (NBTTagList)nbt.getTag("Items");
        NBTTagCompound nbttagcompound1 = null;
        for (int i = 0; i < itemList.tagCount(); ++i) {
        	nbttagcompound1 = (NBTTagCompound)itemList.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.slots.size()) {
                slots.get(b0).setStackForce(ItemStack.loadItemStackFromNBT(nbttagcompound1));
            }
        }
    }

	@Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        //Write item stacks.
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < slots.size(); ++i) {
            if (slots.get(i).getStack() != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.slots.get(i).getStack().writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
        nbt.setTag("Items", nbttaglist);
	}
}
