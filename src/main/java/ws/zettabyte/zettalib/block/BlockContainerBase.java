package ws.zettabyte.zettalib.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
/**
 * A BlockContainer (that is, block which has an associated Tile Entity) with all of the 
 * extra functionality required to make use of ZettaLib's bells and whistles.
 * Will only work with an ICachedTileEntity.
 * @author Sam "Gyro" C.
 */
public abstract class BlockContainerBase extends BlockContainer implements IInfoTileEntity {

	public BlockContainerBase(Material material) {
		super(material);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#onNeighborChange(net.minecraft.world.IBlockAccess, int, int, int, int, int, int)
	 */
    @Override
    public void onNeighborChange (IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ) {
        super.onNeighborChange(world, x, y, z, tileX, tileY, tileZ);
    	//Our implementation of this function handles caching for ICachedTileEntities.
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null) {
        	ICachedTileEntity b = (ICachedTileEntity) te;
            TileEntity te2 = world.getTileEntity(tileX, tileY, tileZ);
            //Check directionality
            for (int i = 0; i < 6; i++) {
                if ((((tileX - x) == ForgeDirection.VALID_DIRECTIONS[i].offsetX) && ((tileY - y) == ForgeDirection.VALID_DIRECTIONS[i].offsetY))
                        && ((tileZ - z) == ForgeDirection.VALID_DIRECTIONS[i].offsetZ))
                    b.updateAdjacency(te2, ForgeDirection.VALID_DIRECTIONS[i]);
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see net.minecraft.block.Block#onBlockPreDestroy(net.minecraft.world.World, int, int, int, int)
     */
    @Override
    public void onBlockPreDestroy (World world, int x, int y, int z, int oldmeta) {
        super.onBlockPreDestroy(world, x, y, z, oldmeta);
    	//Our implementation of this function allows us to provide tile entities with a sort of destructor.

        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null) {
            if(te instanceof ICachedTileEntity) {
            	ICachedTileEntity b = (ICachedTileEntity)te;
                b.onKill();
            }
        }
    }
}
