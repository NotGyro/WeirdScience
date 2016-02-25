package ws.zettabyte.zettalib.thermal;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * Anything that you can start a fire in.
 * Read: If it has behavior you can initiate by right-clicking it with
 * a flint and steel, this is the interface to allow people to do that with
 * automation.
 * 
 * @author Sam "Gyro" C.
 *
 */
public interface IIgnitable {
	//Returns true if succeeded. The return value is mostly useful if you want to, say, consume durability or power.
	public boolean ignite(World world, BlockPos pos);
	//Returns true if our engine is lit / block is burning / plasma is above 2000 C / whatever.
	public boolean isBurning(World world, BlockPos pos);
}