package ws.zettabyte.zettalib.block;

import net.minecraft.tileentity.TileEntity;

/**
 * An interface to be implemented on any Block which has a single, static type of Tile Entity which it can
 * produce. This interface is used for automatically registering TileEntities and processing their @Conf annotations
 * with InitUtils.
 * Blocks with different tile entities for different metadatas will be a different interface.
 * @author Sam "Gyro" C.
 */
public interface IInfoTileEntity {
	Class getTileEntityType();
}
