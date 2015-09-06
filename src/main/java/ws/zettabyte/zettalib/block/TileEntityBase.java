package ws.zettabyte.zettalib.block;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityBase extends TileEntity implements ICachedTileEntity {
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateAdjacency(TileEntity te, ForgeDirection side) {
		// TODO Auto-generated method stub
		
	}

}
