package ws.zettabyte.zettalib.block;

import net.minecraft.tileentity.TileEntity;

/**
 * Blocks with different tile entities for different metadatas will be a different interface.
 * @author gyro
 *
 */
public interface IInfoTileEntity {
	Class getTileEntityType();
}
