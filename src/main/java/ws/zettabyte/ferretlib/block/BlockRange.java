package ws.zettabyte.ferretlib.block;

import java.util.Iterator;

/**
 * Convenience class entirely for use with foreach type stuff.
 * @author gyro
 *
 */
public class BlockRange implements Iterable<BlockCoord> {
	int x0, y0, z0, x1, y1, z1; 
	public BlockRange(int X0, int Y0, int Z0, int X1, int Y1, int Z1) {
		x0 = X0; y0 = Y0; z0 = Z0;
		x1 = X1; y1 = Y1; z1 = Z1;
	};
	/**
	 * A cube of side length radius*2 centered around x0, y0, z0.
	 */
	public BlockRange(int X0, int Y0, int Z0, int r) {
		x0 = X0 - r; y0 = Y0 - r; z0 = Z0 - r;
		x1 = X0 + r; y1 = Y0 + r; z1 = Z0 + r;
	};


	@Override
	public Iterator<BlockCoord> iterator() {
		// TODO Auto-generated method stub
		return null;
	}
}
