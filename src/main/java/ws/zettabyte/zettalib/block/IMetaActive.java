package ws.zettabyte.zettalib.block;

import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public interface IMetaActive {
	public void setActiveStatus(boolean status, World world, int x, int y, int z);
	public boolean getActiveStatus(World world, int x, int y, int z);
}
