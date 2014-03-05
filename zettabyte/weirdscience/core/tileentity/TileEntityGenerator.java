package zettabyte.weirdscience.core.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import zettabyte.weirdscience.core.baseclasses.TileEntityBase;
import cofh.api.energy.IEnergyHandler;
import cofh.api.tileentity.IEnergyInfo;

public class TileEntityGenerator extends TileEntityBase implements
		IEnergyHandler, IEnergyInfo {

	protected int energy;
	protected int energyCap;
	protected int transferRate = 80;
	
	//Check to see if we can push power into adjacent tile entities.
	public void powerAdjacent() {
		IEnergyHandler ehandler;
		for(int i = 0; i < 6; i++) {
			ehandler = adjEnergyHandlers[i];
			if(ehandler != null) {
				ehandler.receiveEnergy(ForgeDirection.VALID_DIRECTIONS[ForgeDirection.OPPOSITES[i]],
						Math.min(energy, transferRate), false);
				energy -= Math.min(energy, transferRate);
				if(energy == 0) {
					//Don't bother trying to output energy if we're out of it.
					break;
				}
			}
		}
	}
	
	//Argument is capacity.
	public TileEntityGenerator() {
		super();
		energy = 0;
	}
	public void setEnergyTransferRate(int tr) {
		transferRate = tr;
	}
	public int getEnergyTransferRate() {
		return transferRate;
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
	

	@Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        //Write energy
        nbt.setInteger("Energy", this.energy);
    }
	@Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        //Write energy
        this.energy = nbt.getInteger("Energy");
    }


}
