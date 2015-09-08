package ws.zettabyte.zettalib.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import ws.zettabyte.zettalib.client.gui.IGUIWidget;

/*
 * I seriously implemented all of this before realizing Forge had a class of the same name for the same purpose. Oh well.
 */
public class FluidTankNamed extends FluidTank {

	protected String name;

	public FluidTankNamed(int capacity) {
		super(capacity);
	}
	public FluidTankNamed(Fluid fluid, int amount, int capacity) {
		super(fluid, amount, capacity);
	}
	public FluidTankNamed(FluidStack stack, int capacity) {
		super(stack, capacity);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setTile(TileEntity t) {
		tile = t;
	}
	/*
	protected FluidStack content = null;
	protected int capacity = 1000;
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public IGUIWidget guiInfo = null;

	public FluidTankNamed(){};
	public FluidTankNamed(String n){ name = n; };
	
	protected void onChanged() {};
	
	/**
	 * 
	 * @param input Input stack.
	 * @return If we called acceptInput, would it do anything?
	 */
	//public boolean isFluidValid(FluidStack input) { return true; }
	
	/**
	 * 
	 * @return If we called any of our fluid output functions, would they do anything?
	 */
	//public boolean canOutput() { return true; }
	
	/**
	 * 
	 * @param input Input stack.
	 * @return Leftovers: Whatever cannot fit into the slot.
	 */
	/* public FluidStack acceptInput(FluidStack input) { 
		if(input == null) return null;
		if(content == null) {
			//Nothing in the slot
			content = input;
			input = null;
	        this.onChanged();
		}
		else if(input.isFluidEqual(content)) {
			int total = input.amount + content.amount;
			if(total > capacity) {
				content.amount = capacity;
				input.amount = total - capacity;
			}
			else {
				content.amount = total;
				input = null;
			}
	        this.onChanged();
		}
		return input;
	}
	
	public FluidStack getContent() {
		return content;
	}
	
	public FluidStack takeContent() {
		FluidStack ret = content;
		content = null;
        this.onChanged();
		return ret;
	}

	public FluidStack drain(int amt) {
		FluidStack ret = null;
		if(amt >= content.amount) {
	        ret = takeContent();
		}
		else {
			ret = content.copy();
			ret.amount = amt;
			content.amount -= amt;
	        this.onChanged();
		}
		return ret;
	}
	
	public Fluid getFluidForRender() {
		return content.getFluid();
	}*/
	/**
	 * Ignore all input / output rules and just set our stack.
	 */
	/*public void setForce(FluidStack s) {
		this.content = s;
	}

	public boolean isEmpty() {
		return (content == null);
	}
	
	//TODO: Does this break with multiple tanks in one compound?
    public FluidTankNamed readFromNBT(NBTTagCompound nbt) {
        if (!nbt.hasKey("Empty")) {
        	content = FluidStack.loadFluidStackFromNBT(nbt);
        }
        else {
        	content = null;
        }
        return this;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        if (content != null) {
        	content.writeToNBT(nbt);
        }
        else {
            nbt.setString("Empty", "");
        }
        return nbt;
    }*/

}
