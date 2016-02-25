package ws.zettabyte.weirdscience.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSkull;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;

//Giant hack.
public class BlockSkullOverride extends BlockSkull {

	public BlockSkullOverride() {
		super();
	}

    public void onBlockAdded(World world, int x, int y, int z) {
    	super.onBlockAdded(world, x, y, z);
    	for(EnumFacing dir : EnumFacing.VALID_DIRECTIONS) {
	    	Block b = world.getBlock(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ);
	    	if((b != null) && (b != Blocks.air)) {
	    		b.onNeighborChange(world, x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ, x, y, z);
	    	}
    	}
    }
}
