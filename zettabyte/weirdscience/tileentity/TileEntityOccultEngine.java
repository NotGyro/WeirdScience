package zettabyte.weirdscience.tileentity;

import java.util.HashMap;

import net.minecraft.entity.Entity;
import net.minecraftforge.common.Configuration;
import zettabyte.weirdscience.core.ContentRegistry;
import zettabyte.weirdscience.core.interfaces.IConfiggable;
import zettabyte.weirdscience.core.interfaces.IRegistrable;

public class TileEntityOccultEngine extends TileEntityBloodEngine implements IConfiggable, IRegistrable {
	
	//Static values
	protected static int staticTankCap;
	protected static float staticRfPerMB;
	protected static int staticMbPerBurn;
	protected static int staticTicksPerBurn = 20;
	protected static int staticRfPerTick;
	protected static int staticEnergyCap;
	
	protected static String staticFluidName = "blood";
	protected static String engineName = "Occult Dynamo";
	
	public class idolData {
		public int fuelMultiplier = 1;
		//Power of Minecraft explosion
		public float explPower = 4.0f;
		//Range of blocks to just straight-up set to air on engine failure.
		public int anihilationRange = 0;
		public Entity spawnOnFail = null;
	}
	
	//Block unlocalized name to fuel multiplier
	protected static HashMap<String, idolData> Idols = new HashMap<String, idolData>();

	public TileEntityOccultEngine() {
		super();
	}
	@Override
	public void doConfig(Configuration config, ContentRegistry cr) {
		staticRfPerMB = (float)config.get(engineName, "RF generated per MB of fuel", 0.1f).getDouble(0.1d);
		staticRfPerTick = config.get(engineName, "RF transfer rate", 20).getInt();
		staticEnergyCap = config.get(engineName, "Capacity of internal energy buffer", 4000).getInt();
		tankCap = config.get(engineName, "Internal fuel tank capacity", 4000).getInt();
		staticMbPerBurn = 400; //Amount of fuel to attempt to consume at once.
	    staticTicksPerBurn = 20; //Time between ticks where we burn fuel. To reduce lag.
		ticksUntilBurn = staticTicksPerBurn;
		energyCap = staticEnergyCap;
	}
	//ENTITY UPDATE:
	@Override
	public void updateEntity() //The meat of our block.
    {
		super.updateEntity();
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
	
	public void updateCurrentIdol(String unlocalizedName) {
		
	}
}
