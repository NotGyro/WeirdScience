package ws.zettabyte.zettalib.block;


/**
 * A convenience class, mostly for use with foreach type stuff, which can be used to iterate
 * over each block position in a sphere around an origin block.
 * 
 * The origin block is, by default, excluded from the query.
 * 
 * Importantly, it is inclusive on its upper bounds.
 * @author Samuel "Gyro" C.
 *
 */
public class BlockRadius extends BlockRange {

	protected int radius;
	protected int radiusSq;
	public BlockRadius(int x, int y, int z, int r) {
		radius = r;
		radiusSq = r^2;
		
		setBounds(x - r, y - r, z - r, x + r, y + r, z + r);		
		
		excludeOurs = true;
		ourX = x; ourY = y; ourZ = z;
		
		buildList();
	}

	@Override
	protected boolean isValidPos(int x, int y, int z) {
		if (super.isValidPos(x, y, z) == false) return false;
		//Distance function. TODO: Abstract into a Math package somewhere.
		int distanceSq = ((x - ourX)^2)
				+ ((y - ourY)^2)
				+ ((z - ourZ)^2);
		if(distanceSq > radiusSq) return false;
		return true;
	}
}
