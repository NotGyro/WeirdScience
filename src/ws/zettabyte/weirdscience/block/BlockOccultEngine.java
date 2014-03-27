package ws.zettabyte.weirdscience.block;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import ws.zettabyte.weirdscience.tileentity.TileEntityOccultEngine;


//TODO: Particle effect when an appropriate block is on top of this engine.
public class BlockOccultEngine extends BlockBloodEngine implements
		IBlockMetaPower {
	
	public static ArrayList<String> idols = new ArrayList<String>(2);

	private void initIdols(){
		idols.add(Block.skull.getUnlocalizedName());
		idols.add(Block.dragonEgg.getUnlocalizedName());
	}
	public BlockOccultEngine(Configuration config, String name, int defaultID,
			Material material) {
		super(config, name, defaultID, material);
		initIdols();
	}

	public BlockOccultEngine(Configuration config, String name, int defaultID) {
		super(config, name, defaultID);
		initIdols();
	}

	public BlockOccultEngine(Configuration config, String name,
			Material material) {
		super(config, name, material);
		initIdols();
	}

	public BlockOccultEngine(Configuration config, String name) {
		super(config, name);
		initIdols();
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
		super.onNeighborBlockChange(world, x, y, z, neighborBlockID);
		updateIdol(world, x, y, z);
	}
	
	private void updateIdol(World world, int x, int y, int z) {
		Block b = blocksList[world.getBlockId(x, y+1, z)];
		if(b != null) {
			for(String s : idols) {
				if(s.contentEquals(b.getUnlocalizedName())) {
					if(b.getUnlocalizedName().contentEquals(Block.skull.getUnlocalizedName())) {
						//Special case for the wither skull
						TileEntity teUp = world.getBlockTileEntity(x, y+1, z);
						if(teUp != null) {
							if(teUp instanceof TileEntitySkull) {
								TileEntitySkull teS = (TileEntitySkull) teUp;
								if(teS.getSkullType() == 1) {
									TileEntity te = world.getBlockTileEntity(x, y, z);
									if(te != null) {
										if(te instanceof TileEntityOccultEngine) {
											((TileEntityOccultEngine)te).updateCurrentIdol(b.getUnlocalizedName());
											return;
										}
									}
								}
							}
						}
					}
					else {
						//Every other block.
						TileEntity te = world.getBlockTileEntity(x, y, z);
						if(te != null) {
							if(te instanceof TileEntityOccultEngine) {
								((TileEntityOccultEngine)te).updateCurrentIdol(b.getUnlocalizedName());
							}
						}
					}
				}
			}
			if(b.blockID == 0) {
				//air

				TileEntity te = world.getBlockTileEntity(x, y, z);
				if(te != null) {
					if(te instanceof TileEntityOccultEngine) {
						((TileEntityOccultEngine)te).updateCurrentIdol(null);
					}
				}
				
			}
		}
		else {
			TileEntity te = world.getBlockTileEntity(x, y, z);
			if(te != null) {
				if(te instanceof TileEntityOccultEngine) {
					((TileEntityOccultEngine)te).updateCurrentIdol(null);
				}
			}
		}
	}
}
