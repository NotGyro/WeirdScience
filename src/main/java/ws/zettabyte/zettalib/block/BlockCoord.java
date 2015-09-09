package ws.zettabyte.zettalib.block;

/**
 * An immutable representation of a cell in a Voxel world.
 * Used mostly for iteration and query results.
 * @author Sam "Gyro" Cutlip
 *
 */
public final class BlockCoord {
	public final int x, y, z;
	public BlockCoord(int X, int Y, int Z) {
		x = X; y = Y; z = Z;
	}
}
