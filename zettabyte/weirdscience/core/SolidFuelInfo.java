package zettabyte.weirdscience.core;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import zettabyte.weirdscience.core.interfaces.ISolidFuelInfo;

public class SolidFuelInfo implements ISolidFuelInfo {
	//The item (with damage) that is our fuel.
	public ItemStack ourFuel = null;
	//Energy produced per unit of of our fuel. Generally interpreted as RF.
	public int energyPer = 1;
	//Item byproduct produced by this fuel, per unit of fuel. Optional and only used by some engines.
	public ItemStack byproduct = null;
	//Fluid byproduct produced by this fuel, per unit of fuel. Optional and only used by some engines.
	public FluidStack exhaust = null;
	
	//Getters
	@Override
	public ItemStack getFuel() {
		return ourFuel;
	}
	@Override
	public int getEnergyPer() {
		return energyPer;
	}
	@Override
	public ItemStack getByproduct() {
		return byproduct;
	}
	@Override
	public FluidStack getExhaust() {
		return exhaust;
	}

	//Setters
	public void setFuel(ItemStack ourFuel) {
		this.ourFuel = ourFuel;
	}
	public void setEnergyPer(int energyPer) {
		this.energyPer = energyPer;
	}
	public void setByproduct(ItemStack byproduct) {
		this.byproduct = byproduct;
	}
	public void setExhaust(FluidStack exhaust) {
		this.exhaust = exhaust;
	}
}
