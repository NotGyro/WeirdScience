package zettabyte.weirdscience.tileentity;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import zettabyte.weirdscience.block.BlockMetaTank;
import zettabyte.weirdscience.core.ContentRegistry;
import zettabyte.weirdscience.core.interfaces.IConfiggable;
import zettabyte.weirdscience.core.interfaces.IDeferredInit;
import zettabyte.weirdscience.core.interfaces.IRegistrable;
import zettabyte.weirdscience.core.tileentity.TileEntityGenerator;

public class TileEntityBloodEngine extends TileEntityGenerator implements
		IFluidHandler, IConfiggable, IDeferredInit, IRegistrable {
	
	//Static values
	protected static int tankCap;
	protected static float rfPerMB;
	protected static int mbPerBurn;
	protected static int ticksPerBurn;
	protected static int rfPerTickStatic;
	protected static int energyCapStatic;

	protected static String fuelName = "blood";
	protected static String engineName = "Hemoionic Dynamo";

	private int fuelFluidID;
	
	//Instance-specific values.
	protected int ticksUntilBurn;
	protected FluidStack tank = null;

	public TileEntityBloodEngine() {
		super();
		this.setEnergyCapacity(energyCapStatic);
		this.setEnergyTransferRate(rfPerTickStatic);
		ticksUntilBurn = ticksPerBurn;

		energy = 0;

        this.energyCap = energyCapStatic;
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
        this.energyCap = energyCapStatic;
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
    }
	@Override
	public void doConfig(Configuration config, ContentRegistry cr) {
		rfPerMB = (float)config.get(engineName, "RF generated per MB of fuel", 0.1f).getDouble(0.1d);
		rfPerTickStatic = config.get(engineName, "RF transfer rate", 20).getInt();
		energyCapStatic = config.get(engineName, "Capacity of internal energy buffer", 4000).getInt();
		tankCap = config.get(engineName, "Internal fuel tank capacity", 4000).getInt();
		mbPerBurn = 400; //Amount of fuel to attempt to consume at once.
	    ticksPerBurn = 20; //Time between ticks where we burn fuel. To reduce lag.
		ticksUntilBurn = ticksPerBurn;
		energyCap = energyCapStatic;
	}
	@Override
	public void DeferredInit(ContentRegistry cr) {
		fuelFluidID = FluidRegistry.getFluidID(fuelName);
	}
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if(resource.getFluid().getName() == fuelName) {
			int ourValue = 0;
			if(tank != null) {
				ourValue = tank.amount;
			}
			int resultValue = ourValue + resource.amount;
			if(resultValue > tankCap) {
				resultValue = tankCap;
			}
			if(doFill) {
				if (tank != null) {
					tank.amount = resultValue;
				}
				else {
					tank = resource.copy();
				}
				this.updateTank();
			}
			return resultValue - ourValue;
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
		super.updateEntity();
		//Clientside is for suckers.
		if(!worldObj.isRemote) {
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
		            if ((this.tank.amount >= 1) && (energy < energyCapStatic)) {
		            	int toBurn = Math.min(mbPerBurn, this.tank.amount); //Either eat mbPerBurn fuel or the entire stack.
		            	drain(ForgeDirection.UP, toBurn, true);
		            	
		            	energy += (int)(((float)toBurn)*rfPerMB);
		            	if(energy > energyCapStatic) {
		            		energy = energyCapStatic;
		            	}
		        		flagHasPower = true;
						//updateTank()
						
			            ticksUntilBurn = ticksPerBurn; //Reset the timer, but only if we did anything.
		            }
				}
	        }
	        if(flagHasPower) {
	    		//And now, attempt to charge surrounding blocks.
	            this.powerAdjacent();
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
}
