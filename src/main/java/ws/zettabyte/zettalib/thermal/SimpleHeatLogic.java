package ws.zettabyte.zettalib.thermal;

import ws.zettabyte.zettalib.ZettaLib;
import ws.zettabyte.zettalib.inventory.IInvComponent;
import ws.zettabyte.zettalib.inventory.IInvComponentInt;
import ws.zettabyte.zettalib.network.MessageTileIntComponent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class SimpleHeatLogic implements IHeatLogic, IInvComponentInt {

	/**
	 * If the Tile Entity remains null, we cannot send packets from client to server when people do GUI things to this component.
	 * Only really relevant to a HeatLogic for the purposes of my testing, but this will become important later for other GUI Components.
	 */
	public TileEntity te = null;
	protected int temperature;
	protected int ambTemperature;
	
	protected int balanceCounter = 0;
	
	/**
	 * Do we need to resynchronize this variable from client to server or vice-versa?
	 */
	protected boolean dirty = false;
	
	public boolean initialized = false;
	
	/*
	 * These values result in Plains with an ambient temperature of 18.8 C
	 * and Desert with 62 C. Good enough for now.
	 */
	protected static final float tempToCelsius = 24.0F;
	protected static final float tempCelsOffset = -2.0F;
	
	//Prevent heat logics from tossing one degree back and forth forever.
	protected static final int minDiffToTransfer = 2;
	
	protected static int celsiusFromBiome(float in) {
		return (int) ((tempToCelsius * in) + tempCelsOffset);
	};
	public SimpleHeatLogic() {}

	public SimpleHeatLogic(World world, int x, int y, int z) {
		setupAmbientHeat(world, x, y, z);
	}
	
	public void setupAmbientHeat(World world, int x, int y, int z) {
		ambTemperature = celsiusFromBiome(world.getBiomeGenForCoords(x, z).temperature) * 1000;
		temperature = ambTemperature;
		initialized = true;
	}

	@Override
	public int getHeat() {
		return temperature;
	}

	@Override
	public int modifyHeat(int value) {
		if(value != 0) dirty = true;
		temperature += value;
		return value;
	}

	@Override
	public int getHeatTransferRate() {
		return 2000; //TODO
	}
	
	public int getTicksPerPassiveLoss() {
		return 8;
	}

	@Override
	public int getAmbientHeat() {
		return ambTemperature;
	}
	
	public String getComponentName() {
		return "temperature";
	}

    public SimpleHeatLogic readFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("Temperature")) {
        	this.temperature = nbt.getInteger("Temperature");
        }
        else {
        	this.temperature = ambTemperature;
        }
        if (nbt.hasKey("TBalanceCounter")) {
        	this.balanceCounter = nbt.getInteger("TBalanceCounter");
        }
        else {
        	this.balanceCounter = 0;
        }
        if (nbt.hasKey("AmbientTemperature")) {
        	this.ambTemperature = nbt.getInteger("AmbientTemperature");
        }
        else {
        	if(te != null) {
        		this.setupAmbientHeat(te.getWorldObj(), te.xCoord, te.yCoord, te.zCoord);
        	} else {
        		this.ambTemperature = 0;
        	}
        }
        initialized = true;
        return this;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
    	nbt.setInteger("Temperature", temperature);
    	nbt.setInteger("TBalanceCounter", balanceCounter);
    	nbt.setInteger("AmbientTemperature", ambTemperature);
        return nbt;
    }
    
    protected void sendValClientToServer() {
    	if(te != null) {
    		ZettaLib.network.sendToServer(new MessageTileIntComponent(this, te));
    	}
    }
    
    protected void GUISet() {
		if(this.te != null) {
			if(this.te.getWorldObj().isRemote) {
				sendValClientToServer();
			}
			else {
				dirty = true;
			}
		}
		else {
			dirty = true;
		}
    }
    
	@Override
	public void setComponentVal(int v) {
		this.temperature = v;
		GUISet();
	}
	@Override
	public int getComponentVal() {
		return this.temperature;
	}
	
	@Override
	public void doBalance(Iterable<IHeatLogic> list) {
		for(IHeatLogic l : list) {
			if(this.getHeat() > l.getHeat()) {
				int transfer = (this.getHeat() - l.getHeat())/2;
				transfer = Math.min(transfer, (getHeatTransferRate()+l.getHeatTransferRate())/2);
				this.modifyHeat(-transfer);
				l.modifyHeat(transfer);
			}
		}
        this.dirty = true;
	}

	//TODO: Saner passive loss all around. One quarter of a C.
	protected final int lossPerTick = 250;
	
	@Override
	public void doPassiveLoss() {
		//TODO: Dependent on insulation, etc. 
		if(!initialized) return;
		int transfer = Math.min(lossPerTick, Math.abs(temperature - ambTemperature));
		if(transfer == 0) return;
		if(temperature > ambTemperature) {
			this.recieveBalance(-transfer);
		}
		else if(temperature < ambTemperature) {
			this.recieveBalance(transfer);
		}
	}

	@Override
	public void recieveBalance(int h) {
		balanceCounter += h;
	}
	
	public void process() {
		if(balanceCounter != 0) {
			this.dirty = true;
			this.modifyHeat(balanceCounter);
		}
		balanceCounter = 0;
	}
	
	public boolean isDirty() {
		if(dirty) {
			dirty = false;
			return true;
		}
		return false;
	}
}
