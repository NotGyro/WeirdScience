package ws.zettabyte.weirdscience.core.tileentity;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;


public class TileEntityFluidGenerator extends TileEntityGenerator implements IFluidHandler {

	protected FluidStack tank = null;
	protected int tankCap;

	protected String fluidName;
	protected int fluidID;
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if(resource.getFluid().getName().contentEquals(fluidName)) {
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
		if(resource.getFluid().getName().contentEquals(fluidName)) {
			return drain(from, resource.amount, doDrain);
		}
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		FluidStack returnVal = null;
		if(tank != null) {
			returnVal = new FluidStack(fluidID, Math.min(tank.amount, maxDrain));
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
		return (fluid.getName().contentEquals(fluidName));
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return (fluid.getName().contentEquals(fluidName));
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[]{new FluidTankInfo(tank, tankCap)};
	}
	
	
	protected void updateTank() {}
	
	public void setFluid(String fName) {
		fluidName = fName;
	}
	
	@Override
	public void init() {
		super.init();
		fluidID = FluidRegistry.getFluidID(fluidName);
	}


}
