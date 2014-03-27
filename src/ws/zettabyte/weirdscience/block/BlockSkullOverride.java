package ws.zettabyte.weirdscience.block;

import net.minecraft.block.BlockSkull;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

//Giant hack.
public class BlockSkullOverride extends BlockSkull {

	public BlockSkullOverride(int par1) {
		super(par1);
	}

    public void onBlockAdded(World world, int x, int y, int z) {
    	super.onBlockAdded(world, x, y, z);
    	for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
	    	int bID = world.getBlockId(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ);
	    	if(blocksList[bID] != null) {
	    		blocksList[bID].onNeighborBlockChange(world, x, y, z, this.blockID);
	    	}
    	}
    }
}
