package zettabyte.weirdscience.fluid;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

import zettabyte.weirdscience.chemistry.IBioactive;

import cofh.api.energy.IEnergyHandler;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;

public class BlockGasBase extends Block implements IFiniteFluidBlock {

	protected int mbPerConcentration = 64; //1024 MB in a fully concentrated block (64 * 16).
	protected int dissipationConcentration = 2; //If a block's concentration is lower than or equal to this, dissipate.
	protected int floorMB; 
	
	protected boolean isReactive = false;

	//protected int flowRate = 4;
	
	protected boolean entitiesInteract = false;

	public int getMbPerConcentration() {
		return mbPerConcentration;
	}

	public void setMbPerConcentration(int mbPerConcentration) {
		this.mbPerConcentration = mbPerConcentration;
	}

	public int getDissipationConcentration() {
		return dissipationConcentration;
	}

	public void setDissipationConcentration(int dissipationConcentration) {
		this.dissipationConcentration = dissipationConcentration;
	}

	public int getFloorMB() {
		return floorMB;
	}

	public void setFloorMB(int floorMB) {
		this.floorMB = floorMB;
	}

	public int getDissipationChance() {
		return dissipationChance;
	}

	public void setDissipationChance(int dissipationChance) {
		this.dissipationChance = dissipationChance;
	}

	public void setEntitiesInteract(boolean entitiesInteract) {
		this.entitiesInteract = entitiesInteract;
	}
	protected int dissipationChance = 4;
	
	protected Fluid ourFluid;
	
	public BlockGasBase(int id, Fluid fluid, Material material) {
		super(id, material);
		ourFluid = fluid;
	}
	public void tryReaction(World world, int x, int y, int z, int xO, int yO, int zO) { }
	public void updateReaction(World world, int x, int y, int z) {
		if(isReactive) {
			int adjBlockID;
			for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				adjBlockID = world.getBlockId(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
				if(adjBlockID != 0) {
					tryReaction(world, x, y, z, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
				}
			}
		}
	}
	@Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int id) {
    	super.onNeighborBlockChange(world, x, y, z, id);
    	updateReaction(world, x, y, z);
    }
	

    @Override
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
    {
        if (world.getBlockId(x, y, z) != blockID) {
            return !world.isBlockOpaqueCube(x, y, z);
        }
        return false;
    }

    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
	@Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        if(entitiesInteract & (ourFluid instanceof IBioactive)) {
        	IBioactive bioFluid = (IBioactive)ourFluid;
        	if(entity instanceof EntityLivingBase) {
        		bioFluid.breatheAffectCreature((EntityLivingBase)entity);
        	}
        }
    }
	
	@Override
	public int getLightOpacity(World world, int x, int y, int z) {
		return 0;
	}
	
	@Override
	public int getRenderBlockPass()
    {
        return 1;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
	@Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
	@Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
	
	@Override
	public FluidStack drain(World world, int x, int y, int z, boolean doDrain) {
		FluidStack outputVal = new FluidStack(this.getFluid(), getBlockInMB(world, x, y, z));
		if(doDrain) {
			world.setBlockToAir(x, y, z);
		}
		return outputVal;
	}
	
	/*
	 * Abstracting the gas concentration away from metadata.
	 */

	public int getConcentration(World world, int x, int y, int z) {
		return world.getBlockMetadata(x, y, z) + 1;
	}
	public void setConcentration(World world, int x, int y, int z, int concentration) {
		world.setBlockMetadataWithNotify(x, y, z, concentration - 1, 1|2);
	}
	@Override
	public int getBlockInMB(World world, int x, int y, int z) {
		return getConcentration(world, x, y, z) * mbPerConcentration;
	}
	
	public int getConcentrationFromMB(int amount) {
		if(amount < getMaxMB()) {
			return (int)Math.floor((float)amount / (float)mbPerConcentration);
			//Return the closest metadata value for a given fluid amount.
		}
		else {
			//Are we over the max? Set it to max.
			return 16;
		}
	}

	@Override
	public int getMaxMB() {
		//15 metadata values with a range of 1, 16 concentration values. 0 concentration is air.
		return 16 * mbPerConcentration;
	}
	public int getMBPerConcentration() {
		return mbPerConcentration;
	}
	public int getMaxConcentration() {
		return 16;
	}
	
	public Fluid getFluidType() { 
		return ourFluid;
	}
	
	//Sets the metadata to be equal to the appropriate value for an amount of
	//milibuckets chosen.
	public boolean setMetadataByMB(World world, int x, int y, int z, int amount) {
		if(getConcentrationFromMB(amount) >= 1) {
			setConcentration(world, x, y, z, getConcentrationFromMB(amount));
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
		if(getConcentrationFromMB(amount) >= 1) {
			int block = world.getBlockId(x, y, z);
			if(block == 0 || blocksList[blockID].isAirBlock(world, x , y, z)) {
				//Is this air?
				world.setBlock(x, y, z, this.blockID, getConcentrationFromMB(amount) - 1, 3); //Set the block to this one.
				//this.onBlockAdded(world, x, y, z);
				if(amount > getMaxMB()) {
					return amount - getMaxMB();
				}
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

	@Override
    public float getFilledPercentage(World world, int x, int y, int z) {
    	return ((float)getBlockInMB(world, x, y, z) / (float)getMaxMB()) * 100.0F;
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
    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
    	super.onBlockAdded(world, x, y, z);
		world.scheduleBlockUpdateWithPriority(x, y, z, this.blockID, tickRate(world), 0);
		updateReaction(world, x, y, z);
    }
    //Placed via itemblock (for creative mode & testing).
    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int par5, float par6, float par7, float par8, int meta) {
    	super.onBlockPlaced(world, x, y, z, par5, par6, par7, par8, meta);
    	setConcentration(world, x, y, z, getMaxConcentration());
		return 15;
    }
    //Tick stuff.
    /**
     * How many world ticks before ticking
     */
    @Override
    public int tickRate(World par1World)
    {
        return 20;
    }
    //The meat of gas behavior.
	@Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
		int ourConcentration = getConcentration(world, x, y, z);
		if(ourConcentration > dissipationConcentration) {
			//Do chemical reactions.
			updateReaction(world, x, y, z);
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
			int valLeastConcentration = getMaxConcentration() + 1; //Begin one higher than the highest possible value.
			boolean canGoAnywhereFlag = false;
			//Locate the direction to expand:
			for(ForgeDirection dir : testDirs) {
				adjBlockID = world.getBlockId(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
				if((adjBlockID == 0) || blocksList[adjBlockID].isAirBlock(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ)) {
					dirLeastConcentration = dir;
					valLeastConcentration = -1;
					canGoAnywhereFlag = true;
				}
				else if(adjBlockID == this.blockID) {
					//The adjacent block is more of this gas. Attempt to equalize them.
					int adjBlockConcentration = getConcentration(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
					if((adjBlockConcentration < valLeastConcentration) && (adjBlockConcentration < ourConcentration) ) {
						dirLeastConcentration = dir;
						valLeastConcentration = adjBlockConcentration;
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
					int adjBlockConcentration = getConcentration(world, x + dirLeastConcentration.offsetX, y + dirLeastConcentration.offsetY, z + dirLeastConcentration.offsetZ);
					if(adjBlockConcentration < ourConcentration) {
						int toDrain = Math.min(ourConcentration - adjBlockConcentration, flowRate);
						setConcentration(world, x + dirLeastConcentration.offsetX, y + dirLeastConcentration.offsetY, z + dirLeastConcentration.offsetZ, toDrain + adjBlockConcentration);
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
			}
			else {
				setConcentration(world, x, y, z, ourConcentration);
			}
		}
		else {
			setConcentration(world, x, y, z, ourConcentration);
		}

		//Ensure that the next tick happens.
		world.scheduleBlockUpdateWithPriority(x, y, z, this.blockID, tickRate(world), 0);
    }
	@Override
	public int getQuantaSize() {
		return mbPerConcentration;
	}
	@Override
	public boolean isTileEntity() {
		return false;
	}
	@Override
	public boolean isTileEntity(World world, int x, int y, int z) {
		return isTileEntity();
	}
	@Override
	public FluidStack partialDrain(World world, int x, int y, int z,
			int amount, boolean doDrain) {
		// TODO Auto-generated method stub
		return null;
	}
}
