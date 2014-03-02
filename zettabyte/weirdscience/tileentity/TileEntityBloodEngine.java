package zettabyte.weirdscience.tileentity;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import zettabyte.weirdscience.cofh.api.energy.TileEnergyHandler;
import zettabyte.weirdscience.cofh.util.EnergyHelper;
import zettabyte.weirdscience.core.ContentRegistry;
import zettabyte.weirdscience.core.interfaces.IConfiggable;
import zettabyte.weirdscience.core.interfaces.IDeferredInit;
import zettabyte.weirdscience.core.interfaces.IRegistrable;
import cofh.api.energy.IEnergyHandler;
import cofh.api.tileentity.IEnergyInfo;

public class TileEntityBloodEngine extends TileEnergyHandler implements
		IFluidHandler, IEnergyInfo, IConfiggable, IDeferredInit, IRegistrable, IEnergyHandler {
	
	//Static values
	protected static int tankCap;
	protected static int energyCap;
	protected static int rfPerMB;
	protected static int mbPerBurn;
	protected static int ticksPerBurn;
	protected static int rfPerTick;

	protected final String fuelName;
	protected final String engineName;

	private int fuelFluidID;
	
	//Instance-specific values.
	protected int ticksUntilBurn;
	protected FluidStack tank = null;
		
	public TileEntityBloodEngine(String engName, String fName) {
		super();
		fuelName = fName;
		engineName = engName;
		ticksPerBurn = 20;
		ticksUntilBurn = ticksPerBurn;
	}

	@Override
	public void doConfig(Configuration config, ContentRegistry cr) {
		rfPerMB = config.get(engineName, "RF generated per MB of fuel", 1).getInt();
		rfPerTick = config.get(engineName, "RF transfer rate", 20).getInt();
		energyCap = config.get(engineName, "Capacity of internal energy buffer", 1000).getInt();
		tankCap = config.get(engineName, "Internal fuel tank capacity", 2000).getInt();
		mbPerBurn = 500; //Amount of fuel to attempt to consume at once.
	    ticksPerBurn = 20; //Time between ticks where we burn fuel. To reduce lag.
		ticksUntilBurn = ticksPerBurn;
		
		storage.setCapacity(energyCap);
		storage.setMaxTransfer(rfPerTick);
	}
	@Override
	public void DeferredInit(ContentRegistry cr) {
		fuelFluidID = FluidRegistry.getFluidID(fuelName);
	}
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if(resource.getFluid().getName() == fuelName) {
			if(tank == null) {
				if(doFill) {
					tank = resource.copy();
					if(tank.amount > tankCap) {
						tank.amount = tankCap;
					}
				}
				return Math.min(resource.amount, tankCap);
			}
			else {
				//We have fuel in the tank already.
				int fin = tank.amount + resource.amount;
				if(fin > tankCap) {
					if(doFill) {
						tank.amount = tankCap;
					}
					return tankCap;
				}
				else {
					if(doFill) {
						tank.amount = tank.amount + resource.amount;
					}
					return resource.amount;
				}
			}
		}
		else {
			return 0;
		}
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) {
		if(resource.fluidID == fuelFluidID) {
			return drain(from, resource.amount, doDrain);
		}
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		FluidStack returnVal = null;
		if(tank != null) {
			returnVal = new FluidStack(fuelFluidID, Math.min(tank.amount, maxDrain));
			if(doDrain) {
				if(maxDrain >= tank.amount) { 
					tank = null;
				}
				else {
					tank.amount -= maxDrain;
				}
			}
		}
		return returnVal;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return (fluid.getID() == fuelFluidID);
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		// TODO Auto-generated method stub
		return (fluid.getID() == fuelFluidID);
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		// TODO Auto-generated method stub
		return new FluidTankInfo[]{new FluidTankInfo(tank, tankCap)};
	}

	//ENTITY UPDATE:
	@Override
	public void updateEntity() //The meat of our block.
    {
		//Burn logic:
		//Are we still waiting to burn fuel?
		boolean flagHasPower = storage.getEnergyStored() > 0;
		
        if (this.ticksUntilBurn > 0) {
        	--this.ticksUntilBurn;
        }
        else {
        	//Do we have fuel?
			if (this.tank != null) {
	            if ((this.tank.amount >= 1) && (storage.getEnergyStored() < energyCap)) {
	            	int toBurn = Math.min(mbPerBurn, this.tank.amount); //Either eat dirtPerBurn fuel or the entire stack.
	            	this.tank.amount -= toBurn;
	            	storage.receiveEnergy(toBurn*rfPerMB, false);
	        		flagHasPower = true;
	            }
	            ticksUntilBurn = ticksPerBurn; //Reset the timer, but only if we did anything.
			}
        }
		//And now, attempt to charge surrounding blocks.
		if (flagHasPower) {
			for(int i = 0; i < 6; ++i) {
				//Try every side.
				EnergyHelper.insertEnergyIntoAdjacentEnergyHandler(this, i, rfPerTick, false);
			}
		}
    }

	@Override
	public String getEnglishName() {
		return engineName;
	}

	@Override
	public String getGameRegistryName() {
		return engineName.replace(" ", "");
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public int getEnergyPerTick() {
		return rfPerTick;
	}

	@Override
	public int getMaxEnergyPerTick() {
		return rfPerTick;
	}

	@Override
	public int getEnergy() {
		return storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergy() {
		return storage.getMaxEnergyStored();
	}
}
