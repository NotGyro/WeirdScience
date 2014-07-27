package ws.zettabyte.weirdscience.tileentity;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import ws.zettabyte.weirdscience.block.BlockMetaTank;
import ws.zettabyte.zettalib.ContentRegistry;
import ws.zettabyte.zettalib.baseclasses.TileEntityBase;
import ws.zettabyte.zettalib.interfaces.IConfiggable;
import ws.zettabyte.zettalib.interfaces.IRegistrable;

public class TileEntityBloodDonation extends TileEntityBase implements IFluidHandler, IFluidTank, IConfiggable, IRegistrable {
	
    protected FluidStack fluidTank;
    protected static int capacity = 0;
    protected static int outputSpeed = 0;
	
    public TileEntityBloodDonation() {
		super();
		outputSpeed = 1000;
	}

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        if (!tag.hasKey("Empty")) {
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(tag);

            if (fluid != null) {
            	fluidTank = fluid;
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        if (fluidTank != null) {
        	fluidTank.writeToNBT(tag);
        }
        else {
        	tag.setString("Empty", "");
        }
    }
    @Override
	public Packet getDescriptionPacket() {
	    NBTTagCompound nbt = new NBTTagCompound();
	    writeToNBT(nbt);
	    return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, nbt);
	}

	private static Fluid bloodFluid;
	
	public static void setBloodFluid(Fluid newfluid) {
		bloodFluid = newfluid;
	}
	
	public static void setStorageCap(int setMax) {
		capacity = setMax;
	}
	
	@Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return fill(resource, doFill);
    }
	
	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		return drain(from, resource.amount, doDrain);
	}
	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return drain(maxDrain, doDrain);
	}
	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		// TODO Auto-generated method stub
		return new FluidTankInfo[] { getInfo() };
	}

	@Override
	public FluidStack getFluid() {
		// TODO Auto-generated method stub
		return fluidTank;
	}

	@Override
	public int getFluidAmount() {
        if (fluidTank == null) {
            return 0;
        }
        return fluidTank.amount;
	}

	@Override
	public int getCapacity() {
		return capacity;
	}

	@Override
	public FluidTankInfo getInfo() {
		return new FluidTankInfo(this);
	}

	public int fillFromBlock(FluidStack resource, boolean doFill) {
		//Is our blood fluid set?
		if (bloodFluid != null) {
			//Is our resource set?
			if (resource == null) {
		        return 0;
		    }
			//Make sure our resource is blood.
			if(bloodFluid.getName().contentEquals(resource.getFluid().getName())) {
				//Get simulation values.
				if (!doFill) {
		            if (fluidTank == null) {
		                return Math.min(capacity, resource.amount);
		            }
		            return Math.min(capacity - fluidTank.amount, resource.amount);
		        }
				//Create the fluid tank if it's empty.
				if (fluidTank == null) {
					fluidTank = new FluidStack(resource, Math.min(capacity, resource.amount));
					//Some network thing.
		            FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(fluidTank, this.worldObj, this.xCoord, this.yCoord, this.zCoord, this));
		            return fluidTank.amount;
		        }
				int filled;
		        if ((fluidTank.amount + resource.amount) < capacity) {
		        	//Will we still be under capacity with this new influx of resources?
		        	fluidTank.amount += resource.amount;
		        	filled = resource.amount;
		        }
		        else {
		        	//Over capacity?
		        	//Get the difference between current and capacity, that's what we're filling.
		        	filled = capacity - fluidTank.amount;
		        	fluidTank.amount = capacity;
		        }
		
		        if (fluidTank != null) {
		        	//Some network thing.
		            FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(fluidTank, this.worldObj, this.xCoord, this.yCoord, this.zCoord, this));
		        }

	            updateTank();
	            
		        return filled;
		
			}
			else {
				return 0;
			}
		}
		else {
			return 0;
		}
	}
	
	@Override
	public int fill(FluidStack resource, boolean doFill) {
		return 0;
	}

	//If doDrain is false, the drain is only simulated
	//to get the FluidStack that would be returned.
	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		if ((fluidTank == null) || (maxDrain == 0)) {
            return null;
        }

        int drained = maxDrain;
        if (fluidTank.amount <= drained) {
            drained = fluidTank.amount;
        }

        FluidStack stack = new FluidStack(fluidTank, drained);
        if (doDrain) {
        	fluidTank.amount -= drained;
            if (fluidTank.amount <= 0) {
            	fluidTank = null;
            }
            updateTank();
            FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(fluidTank, this.worldObj, this.xCoord, this.yCoord, this.zCoord, this));
        }
        return stack;
	}

	@Override
	public void updateEntity()
	{		
		super.updateEntity();
		//Clientside is for suckers.
		if(!worldObj.isRemote) {
			//Do we have blood to dispense?
			if(fluidTank != null) {
				//Attempt to dump tank into surrounding Forge fluid handlers.
				if(fluidTank != null) {
					ForgeDirection dir;
					IFluidHandler adjFluidHandler;
					for(int i = 0; i < 6; ++i) {
						dir = ForgeDirection.VALID_DIRECTIONS[i];
						adjFluidHandler = this.adjFluidHandlers[i];
						if(adjFluidHandler != null) {
							FluidStack toDrain = new FluidStack(fluidTank.getFluid(), fluidTank.amount);
							drain(adjFluidHandler.fill(dir.getOpposite(), toDrain, true), true);
				            updateTank();
				            
							if(fluidTank == null) {
								break;
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void doConfig(Configuration config, ContentRegistry cr) {
		capacity = config.get("Blood", "Blood Donation Station internal capacity", 1000).getInt();
		outputSpeed = config.get("Blood", "Blood Donation Station output rate per tick", 500).getInt();
	}

	@Override
	public String getEnglishName() {
		// TODO Auto-generated method stub
		return "Blood Donation Station";
	}

	@Override
	public String getGameRegistryName() {
		// TODO Auto-generated method stub
		return "bloodDonation";
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	

	
	public void updateTank() { 
		if(!worldObj.isRemote) {
			if(Block.blocksList[worldObj.getBlockId(xCoord, yCoord, zCoord)] instanceof BlockMetaTank) {
				BlockMetaTank bmt = (BlockMetaTank)(Block.blocksList[worldObj.getBlockId(xCoord, yCoord, zCoord)]);
				if(fluidTank == null) {
					bmt.setMetaByFillPercent(worldObj, xCoord, yCoord, zCoord, 0);
				} 
				else {
					bmt.setMetaByFillPercent(worldObj, xCoord, yCoord, zCoord,
							(fluidTank.amount*100)/capacity);
				}
			}
		}
	}
}
