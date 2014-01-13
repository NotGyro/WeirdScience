package zettabyte.weirdscience.tileentity;

import java.util.ArrayList;

import zettabyte.weirdscience.WeirdScience;
import mekanism.api.Object3D;
import mekanism.common.PacketHandler;
import mekanism.common.PacketHandler.Transmission;
import mekanism.common.network.PacketTileEntity;
import mekanism.common.util.ChargeUtils;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.TileFluidHandler;

public class TileEntityBloodDonation extends TileEntity implements IFluidHandler, IFluidTank {
	
    protected FluidStack fluidTank;
    protected int capacity;
    protected int outputSpeed;
	
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

	private Fluid bloodFluid;
	
	public void setBloodFluid(Fluid newfluid) {
		bloodFluid = newfluid;
	}
	
	public void setStorageCap(int setMax) {
		capacity = setMax;
		if(fluidTank != null) {
			if(fluidTank.amount > capacity) {
				fluidTank.amount = capacity;
			}
		}
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

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if (bloodFluid != null) {
			if (resource == null) {
		        return 0;
		    }
			if(bloodFluid.getID() == resource.getFluid().getID()) {
				if (!doFill) {
		            if (fluidTank == null) {
		                return Math.min(capacity, resource.amount);
		            }
		            return Math.min(capacity - fluidTank.amount, resource.amount);
		        }
				if (fluidTank == null) {
					fluidTank = new FluidStack(resource, Math.min(capacity, resource.amount));
		            FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(fluidTank, this.worldObj, this.xCoord, this.yCoord, this.zCoord, this));
		            return fluidTank.amount;
		        }
				
		        int filled = capacity - fluidTank.amount;
		
		        if (resource.amount < filled) {
		        	fluidTank.amount += resource.amount;
		            filled = resource.amount;
		        }
		        else {
		        	fluidTank.amount = capacity;
		        }
		
		        if (fluidTank != null) {
		            FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(fluidTank, this.worldObj, this.xCoord, this.yCoord, this.zCoord, this));
		        }
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
	public FluidStack drain(int maxDrain, boolean doDrain) {
		if (fluidTank == null) {
            return null;
        }

        int drained = maxDrain;
        if (fluidTank.amount < drained) {
            drained = fluidTank.amount;
        }

        FluidStack stack = new FluidStack(fluidTank, drained);
        if (doDrain)
        {
        	fluidTank.amount -= drained;
            if (fluidTank.amount <= 0) {
            	fluidTank = null;
            }
            FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(fluidTank, this.worldObj, this.xCoord, this.yCoord, this.zCoord, this));
        }
        return stack;
	}

	@Override
	public void updateEntity()
	{		
		super.updateEntity();
		
		if(fluidTank != null) {
			for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				TileEntity tileEntity = worldObj.getBlockTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
				if(tileEntity != null) {
					if(tileEntity instanceof IFluidHandler) {
						FluidStack toDrain = new FluidStack(fluidTank.getFluid(), Math.min(outputSpeed, fluidTank.amount));
						drain(((IFluidHandler)tileEntity).fill(dir.getOpposite(), toDrain, true), true);
						
						if(fluidTank == null) {
							break;
						}
					}
				}
			}
		}
	}
}
