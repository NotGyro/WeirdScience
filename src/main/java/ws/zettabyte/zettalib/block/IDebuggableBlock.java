package ws.zettabyte.zettalib.block;

import net.minecraft.world.IBlockAccess;

//TODO: Probably kill this, and let the DebugStick do smartypants things with interfaces for gathering information.
public interface IDebuggableBlock {
	String getDebugInfo(IBlockAccess world, int x, int y, int z, int metadata);
}