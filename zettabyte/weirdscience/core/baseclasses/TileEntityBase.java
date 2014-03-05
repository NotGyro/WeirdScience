package zettabyte.weirdscience.core.baseclasses;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;
import cofh.api.energy.IEnergyHandler;

public abstract class TileEntityBase extends TileEntity {

	protected TileEntity[] adjTileEnts = new TileEntity[6];
	//To make the coder's life easier and save performance via caching casts. Gotta go fast.
	protected TileEntityBase[] adjTEBs = new TileEntityBase[6];
	protected IEnergyHandler[] adjEnergyHandlers = new IEnergyHandler[6];
	protected IFluidHandler[] adjFluidHandlers = new IFluidHandler[6];
	
	public TileEntityBase() {}
	
	protected boolean initialized = false;
	
	protected void updateAdjacency(TileEntity toUpdate, int side) {
		adjTileEnts[side] = toUpdate;
		if(toUpdate instanceof TileEntityBase) {
			adjTEBs[side] = (TileEntityBase)toUpdate;
		}
		if(toUpdate instanceof IEnergyHandler) {
			adjEnergyHandlers[side] = (IEnergyHandler)toUpdate;
		}
		if(toUpdate instanceof IFluidHandler) {
			adjFluidHandlers[side] = (IFluidHandler)toUpdate;
		}
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if(!initialized) {
			init();
			initialized = true;
		}
	}
	
	protected void init() {
		//Update adjacency info.
		ForgeDirection dir;
		for(int i = 0; i < 6; i++) {
			dir = ForgeDirection.VALID_DIRECTIONS[i];
			TileEntity tileEntity = worldObj.getBlockTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
			if(tileEntity != null) {
				this.updateAdjacency(tileEntity, i);
			}
		}
	}
	
	
}
