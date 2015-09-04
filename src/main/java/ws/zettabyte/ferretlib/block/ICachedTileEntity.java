package ws.zettabyte.ferretlib.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public interface ICachedTileEntity {

	void onKill();

	void updateAdjacency(TileEntity te, ForgeDirection side);

}
