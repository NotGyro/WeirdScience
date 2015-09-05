package ws.zettabyte.ferretlib.block;

import ws.zettabyte.ferretlib.IDebugInfo;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
/**
 * Will only work with an ICachedTileEntity
 * @author gyro
 *
 */
public abstract class BlockContainerBase extends BlockContainer implements IDebuggableBlock {

	public BlockContainerBase(Material material) {
		super(material);
	}

    /**
     * Copy & pasted from Forge code. 
     * Called when a tile entity on a side of this block changes is created or is destroyed.
     * @param world The world
     * @param x The x position of this block instance
     * @param y The y position of this block instance
     * @param z The z position of this block instance
     * @param tileX The x position of the tile that changed
     * @param tileY The y position of the tile that changed
     * @param tileZ The z position of the tile that changed
     */

    @Override
    public void onNeighborChange (IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ)
    {
        super.onNeighborChange(world, x, y, z, tileX, tileY, tileZ);
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null)
        {
        	ICachedTileEntity b = (ICachedTileEntity) te;
            TileEntity te2 = world.getTileEntity(tileX, tileY, tileZ);
            //Check directionality
            for (int i = 0; i < 6; i++)
            {
                if ((((tileX - x) == ForgeDirection.VALID_DIRECTIONS[i].offsetX) && ((tileY - y) == ForgeDirection.VALID_DIRECTIONS[i].offsetY))
                        && ((tileZ - z) == ForgeDirection.VALID_DIRECTIONS[i].offsetZ))
                    b.updateAdjacency(te2, ForgeDirection.VALID_DIRECTIONS[i]);
            }
        }
    }

    @Override
    public void onBlockPreDestroy (World world, int x, int y, int z, int oldmeta)
    {
        super.onBlockPreDestroy(world, x, y, z, oldmeta);

        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null)
        {
            if(te instanceof ICachedTileEntity)
            {
            	ICachedTileEntity b = (ICachedTileEntity)te;
                b.onKill();
            }
        }
    }
    
	@Override
	public String getDebugInfo(IBlockAccess world, int x, int y, int z,
			int metadata) {
		TileEntity TE = world.getTileEntity(x, y, z);
		if(TE == null) {
			return "Tile Entity is null!";
		}
		if(TE instanceof IDebugInfo) return ((IDebugInfo)TE).getDebugInfo();
		return "";
	}

}
