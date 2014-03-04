package zettabyte.weirdscience.core;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import zettabyte.weirdscience.core.interfaces.ISolidFuelInfo;

public class SolidFuelInfo implements ISolidFuelInfo {
	//The item (with damage) that is our fuel.
	public ItemStack ourFuel = null;
	//Energy produced per unit of of our fuel. Generally interpreted as RF.
	public int energyPer = 1;
	//Item byproducted by burning this fuel. Optional and only used by some engines.
	public ItemStack byproduct = null;
	//Fluid byproducted by burning this fuel, per unit of fuel. Optional and only used by some engines.
	public FluidStack exhaust = null;

	//Quantity of item byproduct produced by this fuel, per unit of fuel. Optional and only used by some engines.
	public float byproductMult = 1.0f;
	
	public SolidFuelInfo() {}
	public SolidFuelInfo(ItemStack fuel, int en) {
		ourFuel = fuel;
		energyPer = en;
	}
	public SolidFuelInfo(ItemStack fuel, int en, ItemStack byprod) {
		this(fuel, en);
		byproduct = byprod;
	}

	public SolidFuelInfo(ItemStack fuel, int en, ItemStack byprod, float mult) {
		this(fuel, en, byprod);
		byproductMult = mult;
	}
	public SolidFuelInfo(ItemStack fuel, int en, ItemStack byprod, float mult, FluidStack exh) {
		this(fuel, en, byprod, mult);
		exhaust = exh;
	}
	public SolidFuelInfo(ItemStack fuel, int en, ItemStack byprod, FluidStack exh) {
		this(fuel, en, byprod);
		exhaust = exh;
	}

	public SolidFuelInfo(ItemStack fuel, int en, FluidStack exh) {
		this(fuel, en);
		exhaust = exh;
	}
	
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
	@Override
	public float getByproductMult() {
		return byproductMult;
	}
}
