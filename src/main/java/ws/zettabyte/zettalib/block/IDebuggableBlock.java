package ws.zettabyte.zettalib.block;

import net.minecraft.world.IBlockAccess;

public interface IDebuggableBlock {
	String getDebugInfo(IBlockAccess world, int x, int y, int z, int metadata);
}