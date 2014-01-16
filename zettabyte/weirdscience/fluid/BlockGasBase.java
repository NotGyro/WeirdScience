package zettabyte.weirdscience.fluid;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

import cofh.api.energy.IEnergyHandler;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;

public class BlockGasBase extends Block implements IFluidBlock {

	protected int mbPerConcentration = 64; //1024 MB in a fully concentrated block (64 * 16).
	protected int dissipationConcentration = 1; //If a block's concentration is lower than or equal to this, dissipate.
	protected int floorMB; 

	//protected int flowRate = 4;
	
	protected boolean entitiesInteract;

	protected int dissipationChance = 12;
	
	protected Fluid ourFluid;
	
	public BlockGasBase(int id, Fluid fluid, Material material) {
		super(id, material);
		ourFluid = fluid;
	}
	@Override
	public FluidStack drain(World world, int x, int y, int z, boolean doDrain) {
		FluidStack outputVal = new FluidStack(this.getFluid(), getBlockInMB(world, x, y, z));
		if(doDrain) {
			world.setBlockToAir(x, y, z);
		}
		return outputVal;
	}

	public int getBlockInMB(World world, int x, int y, int z) {
		return (world.getBlockMetadata(x, y, z) + 1) * mbPerConcentration;
	}
	public int getMetadataFromMB(int amount) {
		if(amount > getMaxMB()) {
			return (int)Math.floor((float)amount / (float)mbPerConcentration) - 1;
			//Return the closest metadata value for a given fluid amount.
		}
		else {
			//Are we over the max? Set it to max.
			return 15;
		}
	}
	public int getMaxMB() {
		//15 metadata values with a range of 1, 16 concentration values. 0 concentration is air.
		return 16 * mbPerConcentration;
	}
	public int getMBPerConcentration() {
		return mbPerConcentration;
	}
	
	//Sets the metadata to be equal to the appropriate value for an amount of
	//milibuckets chosen.
	public boolean setMetadataByMB(World world, int x, int y, int z, int amount) {
		if(getMetadataFromMB(amount) >= 0) {
			world.setBlockMetadataWithNotify(x, y, z, getMetadataFromMB(amount), 3);
			return true;
		}
		else {
			world.setBlockToAir(x, y, z);
			return false;
		}
	}
	/*
	 * Attempts to push an amount of this gas into the block specified.
	 * Block specified doesn't need to be this gas.
	 * Returns the amount left over.
	 */
	public int pushIntoBlock(World world, int x, int y, int z, int amount) {
		if(getMetadataFromMB(amount) >= 0) {
			int block = world.getBlockId(x, y, z);
			if(block == 0 || blocksList[blockID].isAirBlock(world, x , y, z)) {
				//Is this air?
				world.setBlock(x, y, z, this.blockID, getMetadataFromMB(amount), 3); //Set the block to this one.
				return 0;
			}
			else if(block == this.blockID) {
				int blockGas = getBlockInMB(world, x, y, z);
				if((blockGas + amount) > getMaxMB()) {
					world.setBlockMetadataWithNotify(x, y, z, 15, 3); //set to max
					return (blockGas + amount) - getMaxMB(); //Return leftovers.
				}
				else {
					setMetadataByMB(world, x, y, z, blockGas + amount); //Add amount to block.
					return 0;
				}
			}
			else {
				//Cannot push gas into block, all of it is left over. 
				return amount;
			}
		}
		else {
			//Less than minimum mB for a gas block.
			//Just fizzle it.
			return 0;
		}
	}

	@Override
	public boolean canDrain(World world, int x, int y, int z) {
		//We're all source blocks here. You must be a source block too, or else you wouldn't have come here.
		return true;
	}

	//C&P'd with modification from a Forge class.
    public int getQuantaValue(IBlockAccess world, int x, int y, int z) {
		//We apparently need these strange sanity checks.
        if (world.getBlockId(x, y, z) == 0) {
            return 0;
        }
        if (world.getBlockId(x, y, z) != blockID) {
            return -1;
        }

        int quantaRemaining = (world.getBlockMetadata(x, y, z) + 1); //Range is 1, 16.
        return quantaRemaining;
    }

    //Basic fluid hacks on minecraft functions follow
    @Override
    public boolean canCollideCheck(int meta, boolean fullHit) {
        return entitiesInteract;
    }
	@Override
	public Fluid getFluid() {
		return ourFluid;
	}
	@Override
    public boolean isBlockNormalCube(World world, int x, int y, int z) {
        return false;
    }
	@Override
    public boolean isBlockReplaceable(World world, int x, int y, int z) {
        return true;
    }
    public int quantityDropped(Random par1Random) {
        return 0;
    }
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
    	return null;
	}
    //Block placement stuff.
    
    //Added to the world by any means
    public void onBlockAdded(World world, int x, int y, int z) {
		world.scheduleBlockUpdateWithPriority(x, y, z, this.blockID, tickRate(world), 0);
    }
    //Placed via itemblock (for creative mode & testing).
    public int onBlockPlaced(World world, int x, int y, int z, int par5, float par6, float par7, float par8, int meta) {
		world.setBlockMetadataWithNotify(x, y, z, 15, 1&2);
		return 15;
    }
    //Tick stuff.
    /**
     * How many world ticks before ticking
     */
    public int tickRate(World par1World)
    {
        return 20;
    }
    //The meat of gas behavior.
	@Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
		int ourConcentration = world.getBlockMetadata(x, y, z);
		if(ourConcentration > dissipationConcentration) {
			//System.out.println("How often does this happen?");
			int adjBlockID;
			//Iterate through each direction.
			ForgeDirection dirLeastConcentration = ForgeDirection.UP;

			ForgeDirection[] testDirs = new ForgeDirection[6];
			Vector<ForgeDirection> dirList = new Vector<ForgeDirection>(Arrays.asList(ForgeDirection.VALID_DIRECTIONS));
			
			//Set the flow rate to half of this block's concentration level, rounded up.
			int flowRate = (int) Math.ceil(((float)ourConcentration)/4);
			//Shuffle the list of directions so as to avoid stupidity.
			int toRand = 0;
			for(int count = 0; count < 6; count ++){ 
				toRand = rand.nextInt(6-count);
				testDirs[count] = dirList.get(toRand);
				dirList.removeElementAt(toRand);
			}
			int valLeastConcentration = 17; //Begin one higher than the highest possible value.
			boolean canGoAnywhereFlag = false;
			//Locate the direction to expand:
			for(ForgeDirection dir : testDirs) {
				adjBlockID = world.getBlockId(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
				if((adjBlockID == 0) || blocksList[adjBlockID].isAirBlock(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ)) {
					dirLeastConcentration = dir;
					valLeastConcentration = 0;
					canGoAnywhereFlag = true;
				}
				else if(adjBlockID == this.blockID) {
					//The adjacent block is more of this gas. Attempt to equalize them.
					int adjBlockMeta = world.getBlockMetadata(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
					if((adjBlockMeta < valLeastConcentration) && (adjBlockMeta < ourConcentration) ) {
						dirLeastConcentration = dir;
						valLeastConcentration = adjBlockMeta + 1;
						canGoAnywhereFlag = true;
					}
				}
			}
			if(canGoAnywhereFlag) {
				adjBlockID = world.getBlockId(x + dirLeastConcentration.offsetX, y + dirLeastConcentration.offsetY, z + dirLeastConcentration.offsetZ);
				// || blocksList[adjBlockID].isAirBlock(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ)
				if(adjBlockID == 0) {
					int toDrain = Math.min(ourConcentration, flowRate);
					world.setBlock(x + dirLeastConcentration.offsetX, y + dirLeastConcentration.offsetY, z + dirLeastConcentration.offsetZ, 
							this.blockID, toDrain, 3); //Set the adjacent block 
					ourConcentration -= toDrain;
				}
				else if(adjBlockID == this.blockID) {
					//The adjacent block is more of this gas. Attempt to equalize them.
					int adjBlockMeta = world.getBlockMetadata(x + dirLeastConcentration.offsetX, y + dirLeastConcentration.offsetY, z + dirLeastConcentration.offsetZ);
					if(adjBlockMeta < ourConcentration) {
						int toDrain = Math.min(ourConcentration - adjBlockMeta, flowRate);
						world.setBlockMetadataWithNotify(x + dirLeastConcentration.offsetX, y + dirLeastConcentration.offsetY, z + dirLeastConcentration.offsetZ, toDrain + adjBlockMeta, 3);
						ourConcentration -= toDrain;
					}
				}
			}
		}
		if(ourConcentration < 0) {
			//Negative concentration is not allowed at all.
			world.setBlockToAir(x, y, z);
		}
		if(ourConcentration <= dissipationConcentration) {
			//We have dissipated this block by extracting too much from it.
			//world.setBlock(x, y, z, 0, 0, 1&2); //Set to air
			if(rand.nextInt(dissipationChance) == 0) {
				world.setBlockToAir(x, y, z);
				//world.setBlockMetadataWithNotify(x, y, z, ourConcentration, 1&2);
			}
			else {
				world.setBlockMetadataWithNotify(x, y, z, ourConcentration, 1&2);
			}
		}
		else {
			world.setBlockMetadataWithNotify(x, y, z, ourConcentration, 1&2);
		}

		//Ensure that the next tick happens.
		world.scheduleBlockUpdateWithPriority(x, y, z, this.blockID, tickRate(world), 0);
    }
}
