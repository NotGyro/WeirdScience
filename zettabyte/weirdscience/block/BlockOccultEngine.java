package zettabyte.weirdscience.block;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import zettabyte.weirdscience.tileentity.TileEntityOccultEngine;


//TODO: Particle effect when an appropriate block is on top of this engine.
public class BlockOccultEngine extends BlockBloodEngine implements
		IBlockMetaPower {
	
	public static ArrayList<String> idols = new ArrayList<String>(8);

	public BlockOccultEngine(Configuration config, String name, int defaultID,
			Material material) {
		super(config, name, defaultID, material);
		// TODO Auto-generated constructor stub
	}

	public BlockOccultEngine(Configuration config, String name, int defaultID) {
		super(config, name, defaultID);
		// TODO Auto-generated constructor stub
	}

	public BlockOccultEngine(Configuration config, String name,
			Material material) {
		super(config, name, material);
		// TODO Auto-generated constructor stub
	}

	public BlockOccultEngine(Configuration config, String name) {
		super(config, name);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void recievePowerOn(World world, int x, int y, int z) {
		//TODO
	}

	@Override
	public void recievePowerOff(World world, int x, int y, int z) {
		//TODO
	}
	

	@Override
	public TileEntity createNewTileEntity(World world) {
		TileEntityOccultEngine te = new TileEntityOccultEngine();
		return te;
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int neighborBlockID) {
		Block b = blocksList[world.getBlockId(x, y+1, z)];
		if(b != null) {
			for(String s : idols) {
				if(s.contentEquals(b.getUnlocalizedName())) {
					TileEntity te = world.getBlockTileEntity(x, y, z);
					if(te != null) {
						if(te instanceof TileEntityOccultEngine) {
							((TileEntityOccultEngine)te).updateCurrentIdol(b.getUnlocalizedName());
						}
					}
				}
			}
		}
	}

}
