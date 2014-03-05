package zettabyte.weirdscience.core.tileentity;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import zettabyte.weirdscience.core.interfaces.ISolidFuelInfo;

public abstract class TileEntitySolidFueled extends TileEntityGenerator {
	
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
	
	public abstract void receiveByproduct(ItemStack byproductStack);
	public abstract void receiveExhaust(FluidStack exhaustStack);
	
	//Argument is capacity.
	public TileEntitySolidFueled() {
		super();
		energy = 0;
	}
}
