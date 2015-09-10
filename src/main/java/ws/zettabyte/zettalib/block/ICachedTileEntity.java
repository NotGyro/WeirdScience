package ws.zettabyte.zettalib.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * An interface for TileEntities which can cache the locations in memory
 * of TileEntities adjacent to it, in order to optimize past Minecraft's
 * incredibly slow Tile Entity lookups.
 * 
 * @author Sam "Gyro" C.
 *
 */
public interface ICachedTileEntity {

	/**
	 * Called when the block associated with this TileEntity is removed from the world.
	 */
	void onKill();

	/**
	 * @param te Adjacent TileEntity. Null is a valid value for te, and it drops the Tile Entity on side from this Tile Entity's cache.
	 * @param side Side of our block on which te lies.
	 */
	void updateAdjacency(TileEntity te, ForgeDirection side);

}
