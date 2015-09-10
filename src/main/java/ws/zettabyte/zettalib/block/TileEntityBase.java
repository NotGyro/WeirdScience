package ws.zettabyte.zettalib.block;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import ws.zettabyte.zettalib.inventory.IComponentContainer;

public abstract class TileEntityBase extends TileEntity implements ICachedTileEntity, IComponentContainer {
	public TileEntityBase() {
		super();
	}
	
	//TODO: Cached version
	protected TileEntity getAdjacent(ForgeDirection d) {
		int x = xCoord; int y = yCoord; int z = zCoord;
		x += d.offsetX; y += d.offsetY; z += d.offsetZ;
		return worldObj.getTileEntity(x, y, z);
	}

	@Override
	public void onKill() {
		// TODO remove this Tile Entity from tables of adjacent Tile Entities.
		
	}

	@Override
	public void updateAdjacency(TileEntity te, ForgeDirection side) {
		// TODO Auto-generated method stub
		
	}

}
