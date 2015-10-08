package ws.zettabyte.zettalib.thermal;

import ws.zettabyte.zettalib.inventory.IInvComponent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class SimpleHeatLogic implements IHeatLogic, IInvComponent {

	protected int temperature;
	protected int ambTemperature;
	
	/*
	 * These values result in Plains with an ambient temperature of 18.8 C
	 * and Desert with 62 C. Good enough for now.
	 */
	protected static final float tempToCelsius = 24.0F;
	protected static final float tempCelsOffset = -2.0F;
	
	protected static int celsiusFromBiome(float in) {
		return (int) ((tempToCelsius * in) + tempCelsOffset);
	};
	public SimpleHeatLogic() {}

	public SimpleHeatLogic(World world, int x, int y, int z) {
		ambTemperature = celsiusFromBiome(world.getBiomeGenForCoords(x, z).temperature);
		temperature = ambTemperature;
	}
	
	public void setupAmbientHeat(World world, int x, int y, int z) {
		ambTemperature = celsiusFromBiome(world.getBiomeGenForCoords(x, z).temperature);
		temperature = ambTemperature;
	}

	@Override
	public int getHeat() {
		return temperature;
	}

	@Override
	public int modifyHeat(int value) {
		temperature += value;
		return value;
	}

	@Override
	public float getHeatRate() {
		return 100.0F; //TODO
	}

	@Override
	public int getAmbientHeat() {
		return ambTemperature;
	}
	
	public String getComponentName() {
		return "temperature";
	}

    public SimpleHeatLogic readFromNBT(NBTTagCompound nbt)
    {
        if (nbt.hasKey("Temperature")) {
        	this.temperature = nbt.getInteger("Temperature");
        }
        else {
        	this.temperature = ambTemperature;
        }
        return this;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
    	nbt.setInteger("Temperature", temperature);
        return nbt;
    }
}
