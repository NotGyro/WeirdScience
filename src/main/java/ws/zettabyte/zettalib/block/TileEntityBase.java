package ws.zettabyte.zettalib.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;
import ws.zettabyte.zettalib.inventory.IComponentContainer;

public abstract class TileEntityBase extends TileEntity implements ICachedTileEntity, IComponentContainer {
	public TileEntityBase() {
		super();
	}
	
	//TODO: Cached version
	protected TileEntity getAdjacent(EnumFacing d) {
		int x = xCoord; int y = yCoord; int z = zCoord;
		x += d.offsetX; y += d.offsetY; z += d.offsetZ;
		return worldObj.getTileEntity(x, y, z);
	}

	@Override
	public void onKill() {
		// TODO remove this Tile Entity from tables of adjacent Tile Entities.
		
	}

	@Override
	public void updateAdjacency(TileEntity te, EnumFacing side) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean shouldRefresh(Block oldBlock, Block newBlock, int oldMeta, int newMeta, World world, int x, int y, int z) {
		//Ignore metadata, only refresh when block changes.
		return (oldBlock != newBlock);
	}
}
