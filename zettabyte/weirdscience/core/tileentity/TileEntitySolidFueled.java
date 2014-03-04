package zettabyte.weirdscience.core.tileentity;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import zettabyte.weirdscience.core.interfaces.ISolidFuelInfo;
import cofh.api.energy.IEnergyHandler;
import cofh.api.tileentity.IEnergyInfo;

public abstract class TileEntitySolidFueled extends TileEntity implements
		IEnergyHandler, IEnergyInfo {

	protected int energy;
	protected int energyCap;
	protected int transferRate;
	
	protected ArrayList<ISolidFuelInfo> fuelInfo = new ArrayList<ISolidFuelInfo>(3);

	//Adds a fuel.
	public void addSolidFuelInfo(ISolidFuelInfo isf) {
		if(isf != null) {
			fuelInfo.add(isf);
		}
	}
	
	//Is this one of our fuels?
	public ISolidFuelInfo canBurn(ItemStack toBurn) {
		for(ISolidFuelInfo fuel : fuelInfo) {
			if(fuel.getFuel().getUnlocalizedName().contentEquals(toBurn.getUnlocalizedName())) {
				return fuel;
			}
		}
		return null;
	}
	
	public int doBurn(ItemStack toBurn, int quantity) {
		//Make sure there's room in the energy storage.
		if(energy < energyCap) {
			//Get our Solid Fuel Info for this stack.
			ISolidFuelInfo fuelInf = canBurn(toBurn);
			if(fuelInf != null) {
				//Get our initial quantity to try to burn.
				int burnQuantity = Math.min(toBurn.stackSize, quantity);
				
				//Figure out if the energy capacity is going to be a limiting factor here.
				int remainingEnergyCapacity = energyCap - energy;
				burnQuantity = Math.min(burnQuantity, remainingEnergyCapacity/fuelInf.getEnergyPer());
				//Produce energy!
				int energyProduced = (fuelInf.getEnergyPer() * burnQuantity);
	            energy += energyProduced;
	            //Deal with any byproducts.
				if(fuelInf.getByproduct() != null) {
					ItemStack byproductStack = fuelInf.getByproduct().copy();
					byproductStack.stackSize = (int) ((float)burnQuantity * fuelInf.getByproductMult());
					this.receiveByproduct(byproductStack);
				}
				if(fuelInf.getExhaust() != null) {
					FluidStack exh = fuelInf.getExhaust().copy();
					exh.amount *= burnQuantity;
					this.receiveExhaust(exh);
				}
				return burnQuantity;
			}
			else {
				return 0;
			}
		}
		else {
			return 0;
		}
	}
	//Check to see if we can push power into adjacent tile entities.
	public void powerAdjacent() {
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			TileEntity tileEntity = worldObj.getBlockTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
			if(tileEntity != null) {
				if(tileEntity instanceof IEnergyHandler) {
					((IEnergyHandler)tileEntity).receiveEnergy(dir.getOpposite(), Math.min(energy, transferRate), false);
					energy -= Math.min(energy, transferRate);
					if(energy == 0) {
						//Don't bother trying to output energy if we're out of it.
						break;
					}
				}
			}
		}
	}
	public abstract void receiveByproduct(ItemStack byproductStack);
	public abstract void receiveExhaust(FluidStack exhaustStack);
	
	//Argument is capacity.
	public TileEntitySolidFueled() {
		super();
		energy = 0;
	}
	public void setEnergyCapacity(int cap) {
		energyCap = cap;
	}
	@Override
	public int getEnergyPerTick() {
		// Requirement..?
		return 0;
	}

	@Override
	public int getMaxEnergyPerTick() {
		// Requirement..?
		return 0;
	}

	@Override
	public int getEnergy() {
		return energy;
	}

	@Override
	public int getMaxEnergy() {
		return energyCap;
	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive,
			boolean simulate) {
		// This isn't a battery.
		return 0;
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract,
			boolean simulate) {
		if(!simulate) { 
			if(energy < maxExtract) {
				maxExtract = energy;
				energy = 0;
				return maxExtract;
			}
			else {
				energy -= maxExtract;
				return maxExtract;
			}
		}
		else {
			if(energy < maxExtract) {
				return energy;
			}
			else {
				return maxExtract;
			}
		}
	}

	@Override
	public boolean canInterface(ForgeDirection from) {
		return true;
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		return energy;
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return energyCap;
	}


}
