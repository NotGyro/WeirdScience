package zettabyte.weirdscience.tileentity;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import zettabyte.weirdscience.block.BlockMetaTank;
import zettabyte.weirdscience.cofh.util.EnergyHelper;
import zettabyte.weirdscience.core.ContentRegistry;
import zettabyte.weirdscience.core.interfaces.IConfiggable;
import zettabyte.weirdscience.core.interfaces.IDeferredInit;
import zettabyte.weirdscience.core.interfaces.IRegistrable;
import cofh.api.energy.IEnergyHandler;
import cofh.api.tileentity.IEnergyInfo;

public class TileEntityBloodEngine extends TileEntity implements IEnergyHandler,
		IFluidHandler, IEnergyInfo, IConfiggable, IDeferredInit, IRegistrable {
	
	//Static values
	protected static int tankCap;
	protected static int energyCap;
	protected static float rfPerMB;
	protected static int mbPerBurn;
	protected static int ticksPerBurn;
	protected static int rfPerTick;

	protected final String fuelName;
	protected final String engineName;

	private int fuelFluidID;
	
	//Instance-specific values.
	protected int ticksUntilBurn;
	protected int energy = 0;
	protected FluidStack tank = null;
		
	public TileEntityBloodEngine(String engName, String fName) {
		super();
		fuelName = fName;
		engineName = engName;
		ticksPerBurn = 20;
		ticksUntilBurn = ticksPerBurn;

		energy = 0;
	}
	

	//NBT stuff
	@Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        //Read how far we are from doing another engine tick.
        this.ticksUntilBurn = nbt.getShort("BurnTime");

        //Read the internal fluid tank for smog storage
        if (!nbt.hasKey("Empty")) {
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(nbt);

            if (fluid != null) {
            	tank = fluid;
            }
        }
        //Fix the metadata for the block.
        updateTank();

        //Get energy
        energy = nbt.getInteger("Energy");
    }

	@Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        //Write time until next engine burn tick.
        nbt.setShort("BurnTime", (short)this.ticksUntilBurn);
        //Write our internal fluid tank (which stores smog)
        if (tank != null) {
        	tank.writeToNBT(nbt);
        }
        else {
        	nbt.setString("Empty", "");
        }

        //Write energy
        nbt.setInteger("Energy", this.energy);
    }
	@Override
	public void doConfig(Configuration config, ContentRegistry cr) {
		rfPerMB = (float)config.get(engineName, "RF generated per MB of fuel", 0.1f).getDouble(0.1d);
		rfPerTick = config.get(engineName, "RF transfer rate", 20).getInt();
		energyCap = config.get(engineName, "Capacity of internal energy buffer", 4000).getInt();
		tankCap = config.get(engineName, "Internal fuel tank capacity", 4000).getInt();
		mbPerBurn = 400; //Amount of fuel to attempt to consume at once.
	    ticksPerBurn = 20; //Time between ticks where we burn fuel. To reduce lag.
		ticksUntilBurn = ticksPerBurn;
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
					updateTank();
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
						updateTank();
					}
					return fin - tankCap;
				}
				else {
					if(doFill) {
						tank.amount = tank.amount + resource.amount;
						updateTank();
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
					updateTank();
				}
				else {
					tank.amount -= maxDrain;
					updateTank();
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
		boolean flagHasPower = energy > 0;
		
        if (this.ticksUntilBurn > 0) {
        	--this.ticksUntilBurn;
        }
        else {
        	//Do we have fuel?
			if (this.tank != null) {
				//Bugs are hard and Tile Entities are eccentric.
	            if ((this.tank.amount >= 1) && (energy < energyCap)) {
	            	int toBurn = Math.min(mbPerBurn, this.tank.amount); //Either eat dirtPerBurn fuel or the entire stack.
	            	drain(ForgeDirection.UP, toBurn, true);
	            	
	            	energy += (int)(((float)toBurn)*rfPerMB);
	        		flagHasPower = true;
					//updateTank()
	        		
	            	System.out.println(energy + " , " + energyCap);
					
		            ticksUntilBurn = ticksPerBurn; //Reset the timer, but only if we did anything.
	            }
			}
        }
		//And now, attempt to charge surrounding blocks.
		if (flagHasPower) {
			for(int i = 0; i < 6; ++i) {
				//Try every side.
				energy -= EnergyHelper.insertEnergyIntoAdjacentEnergyHandler(this, i, Math.min(rfPerTick, energy), false);
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
		return energy;
	}

	@Override
	public int getMaxEnergy() {
		return energyCap;
	}
	
	public void updateTank() { 
		if(!worldObj.isRemote) {
			if(Block.blocksList[worldObj.getBlockId(xCoord, yCoord, zCoord)] instanceof BlockMetaTank) {
				BlockMetaTank bmt = (BlockMetaTank)(Block.blocksList[worldObj.getBlockId(xCoord, yCoord, zCoord)]);
				if(tank == null) {
					bmt.setMetaByFillPercent(worldObj, xCoord, yCoord, zCoord, 0);
				} 
				else {
					bmt.setMetaByFillPercent(worldObj, xCoord, yCoord, zCoord,
							(this.tank.amount*100)/this.tankCap);
				}
			}
		}
	}


	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive,
			boolean simulate) {
		return 0;
	}


	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract,
			boolean simulate) {
		if(!simulate) {
			energy -= Math.min(maxExtract, rfPerTick);
		}
		return Math.min(maxExtract, rfPerTick);
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
