package zettabyte.weirdscience.core.tileentity;

import java.util.ArrayList;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import zettabyte.weirdscience.core.interfaces.ISolidFuelInfo;
import cofh.api.energy.IEnergyHandler;
import cofh.api.tileentity.IEnergyInfo;

public class TileEntitySolidFueled extends TileEntity implements
		IEnergyHandler, IEnergyInfo {

	protected int energy;
	protected int energyCap;
	protected int transferRate;
	
	protected ArrayList<ISolidFuelInfo> fuelInfo = new ArrayList<ISolidFuelInfo>(3);
	
	public TileEntitySolidFueled() {
		super();
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
