package ws.zettabyte.ferretlib.block;

public class BlockRadius extends BlockRange {

	protected int radius;
	public BlockRadius(int x, int y, int z, int r) {
		radius = r;
		
		setBounds(x - r, y - r, z - r, x + r, y + r, z + r);		
		
		excludeOurs = true;
		ourX = x; ourY = y; ourZ = z;
		
		buildList();
	}

	@Override
	protected boolean isValidPos(int x, int y, int z) {
		if (super.isValidPos(x, y, z) == false) return false;
		//Distance function. TODO: Abstract into a Math package somewhere.
		int distance = (int)Math.sqrt((double)(((x - ourX)^2)
				+ ((y - ourY)^2)
				+ ((z - ourZ)^2)));
		if(distance > radius) return false;
		return true;
	}
}
