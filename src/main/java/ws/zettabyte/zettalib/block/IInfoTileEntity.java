package ws.zettabyte.zettalib.block;

import net.minecraft.tileentity.TileEntity;

public interface IInfoTileEntity {
	Class<? extends TileEntity> getTileEntityType();
}
