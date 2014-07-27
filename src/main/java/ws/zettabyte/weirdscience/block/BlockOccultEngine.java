package ws.zettabyte.weirdscience.block;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import ws.zettabyte.weirdscience.tileentity.TileEntityOccultEngine;


//TODO: Particle effect when an appropriate block is on top of this engine.
public class BlockOccultEngine extends BlockBloodEngine implements
		IBlockMetaPower {
	
	public static ArrayList<String> idols = new ArrayList<String>(2);

	private void initIdols(){
		idols.add(Blocks.skull.getUnlocalizedName());
		idols.add(Blocks.dragon_egg.getUnlocalizedName());
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
	public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ) {
		super.onNeighborChange(world, x, y, z, tileX, tileY, tileZ);
		updateIdol(world, x, y, z);
	}
	
	private void updateIdol(IBlockAccess world, int x, int y, int z) {
		Block b = world.getBlock(x, y+1, z);
		if(b != null) {
			for(String s : idols) {
				if(s.contentEquals(b.getUnlocalizedName())) {
					if(b.getUnlocalizedName().contentEquals(Blocks.skull.getUnlocalizedName())) {
						//Special case for the wither skull
						TileEntity teUp = world.getTileEntity(x, y+1, z);
						if(teUp != null) {
							if(teUp instanceof TileEntitySkull) {
								TileEntitySkull teS = (TileEntitySkull) teUp;
								if(teS.getBlockMetadata() == 1) {
									TileEntity te = world.getTileEntity(x, y, z);
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
						TileEntity te = world.getTileEntity(x, y, z);
						if(te != null) {
							if(te instanceof TileEntityOccultEngine) {
								((TileEntityOccultEngine)te).updateCurrentIdol(b.getUnlocalizedName());
							}
						}
					}
				}
			}
			if((b == null) || (b == Blocks.air)) {
				//air

				TileEntity te = world.getTileEntity(x, y, z);
				if(te != null) {
					if(te instanceof TileEntityOccultEngine) {
						((TileEntityOccultEngine)te).updateCurrentIdol(null);
					}
				}
				
			}
		}
		else {
			TileEntity te = world.getTileEntity(x, y, z);
			if(te != null) {
				if(te instanceof TileEntityOccultEngine) {
					((TileEntityOccultEngine)te).updateCurrentIdol(null);
				}
			}
		}
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

	public BlockOccultEngine(Material material) {
		super(material);
		// TODO Auto-generated constructor stub
	}
	
}
