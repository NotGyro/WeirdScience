package ws.zettabyte.zettalib.block;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * A convenience class, mostly for use with foreach type stuff, which can be used to iterate
 * over each block position in a cuboid area.
 * 
 * Importantly, it is inclusive on its upper bounds.
 * @author Samuel "Gyro" C.
 *
 */
public class BlockRange implements Iterable<BlockCoord> {
	int x0, y0, z0, x1, y1, z1; 
	
	protected boolean excludeOurs = false;
	protected int ourX, ourY, ourZ; // Only used if excludeMiddle = true;
	
	//TODO: write an Iterator<BlockCoord> implementation for BlockRanges so we don't need this grotesque throwaway.
	protected ArrayList<BlockCoord> coords = new ArrayList<BlockCoord>(125);
	
	protected final void setBounds(int X0, int Y0, int Z0, int X1, int Y1, int Z1) {
		x0 = X0; y0 = Y0; z0 = Z0;
		x1 = X1; y1 = Y1; z1 = Z1;
		
		/* Ensure that the math isn't screwy by swapping a lot. <coord>1 must always be the upper bound,
		 * and <coord>0 must always be the lower bound.
		 */
		if(x1 < x0) {
			int temp = x1;
			x1 = x0; x0 = temp;
		}
		if(y1 < y0) {
			int temp = y1;
			y1 = y0; y0 = temp;
		}
		if(z1 < z0) {
			int temp = z1;
			z1 = z0; z0 = temp;
		}
	}
	protected boolean isValidPos(int x, int y, int z) {
		//Ignoring the center? Well, check if this is the center.
		if(excludeOurs && ((x == ourX) && (y == ourY) && (z == ourZ))) {
			return false;
		}
		return true;
	}
	//A later implementation will call this only if some schmuck calls remove on our iterator. A copy-on-write.
	//Once again, note: Inclusive on upper bound.
	protected void buildList() {
		for(int x = x0; x <= x1; ++x) {
			for(int y = y0; y <= y1; ++y) {
				for(int z = z0; z <= z1; ++z) { 
					if(isValidPos(x, y, z)) coords.add(new BlockCoord(x, y, z));
				}
			}
		}
	}
	protected BlockRange() {};
	
	/**
	 * A cuboid between the points (X0, Y0, Z0) and (X1, Y1, Z1).
	 */
	public BlockRange(int X0, int Y0, int Z0, int X1, int Y1, int Z1) {
		setBounds(X0, Y0, Z0, X1, Y1, Z1);
		buildList();
	};
	/**
	 * A cube of side length (radius*2)+1 centered around x0, y0, z0. Excludes our origin position.
	 */
	public BlockRange(int X0, int Y0, int Z0, int r) {
		setBounds(X0 - r, Y0 - r, Z0 - r, X0 + r, Y0 + r, Z0 + r);		
		excludeOurs = true;
		ourX = X0; ourY = Y0; ourZ = Z0;
		buildList();
	};


	@Override
	public Iterator<BlockCoord> iterator() {
		return coords.iterator();
	}
}
