package zettabyte.weirdscience.core.fluid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import zettabyte.weirdscience.core.baseclasses.BlockBase;
import zettabyte.weirdscience.core.chemistry.IBioactive;

public class BlockGasBase extends BlockBase implements IFiniteFluidBlock {

	protected int mbPerConcentration = 64;
	protected int dissipationConcentration = 2; //If a block's concentration is lower than or equal to this, dissipate.
	protected int floorMB; 
	
	protected boolean isReactive = false;

	//protected int flowRate = 4;
	
	protected ArrayList<Integer> blockIDs; //Includes this and others.
	
	protected boolean entitiesInteract = false;
	
	protected int dissipationChance = 4;
	
	protected int ourConcentration = 0; //The base concentration level of this particular BlockID.
	// ^ As of yet unused. Higher concentration brackets were originally a hack.
	// A later rewrite for optimization's sake will use this.
	
	protected Fluid ourFluid;

	public BlockGasBase(Configuration config, String name, Fluid fluid) {
		super(config, name);
		ourFluid = fluid;
		blockIDs = new ArrayList<Integer>();
		setMaterial(Material.air); //Cross your fingers...
	}
	
	public void addExtenderID(int id) {
		blockIDs.add(id);
	}
	
	public boolean isAssociatedBlockID(int id) {
		return blockIDs.contains((Integer)id);
	}
	
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
		return getMBPerConcentration();
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
        if (!isAssociatedBlockID(world.getBlockId(x, y, z))) {
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
	
	public void setMBMax(int newMax) {
		mbPerConcentration = newMax / getMaxConcentration();
	}
	
	public int getMaxConcentration() {
		return getConcentrationPerBracket() * this.blockIDs.size();
	}
	
	/*
	 * Abstracting the gas concentration away from metadata.
	 */

	protected int getConcentrationBase(World world, int x, int y, int z) {
		return world.getBlockMetadata(x, y, z) + 1;
	} /*
	protected void setBaseConcentration(World world, int x, int y, int z, int concentration) {
		world.setBlockMetadataWithNotify(x, y, z, concentration - 1, 1|2);
	}*/
	
	protected int getConcentrationMultiplier(World world, int x, int y, int z) {
		return blockIDs.indexOf((Integer)world.getBlockId(x, y, z));
	}
	public int getConcentration(World world, int x, int y, int z) {
		return (getConcentrationMultiplier(world, x, y, z) * 16) + getConcentrationBase(world, x, y, z);
	}
	protected int getConcentrationPerBracket() {
		return 16;
	}
	protected int getIDByConcentration(int concen) {
		return blockIDs.get((concen-1)/getConcentrationPerBracket());
	}
	public void setConcentration(World world, int x, int y, int z, int concen) {
		if (concen <= 0) {
			world.setBlock(x, y, z, 0, 0, 1|2);
		}
		else {
			//I honestly don't expect to be able to comment this in a way that makes sense.
			//It's a pretty horrible hack.
			//Basically the extended IDs are ever higher brackets of concentration.
			if(concen <= getMaxConcentration()) {
				world.setBlock(x, y, z, getIDByConcentration(concen),
						(concen%getConcentrationPerBracket())-1, 1|2);
			}
			else {
				world.setBlock(x, y, z, getIDByConcentration(getMaxConcentration()),
						15, 1|2);
			}
		}
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
			return getMaxConcentration();
		}
	}

	@Override
	public int getMaxMB() {
		//15 metadata values with a range of 1, 16 concentration values. 0 concentration is air.
		return getMaxConcentration() * mbPerConcentration;
	}
	public int getMBPerConcentration() {
		return mbPerConcentration;
	}

	public Fluid getFluidType() { 
		return ourFluid;
	}
	public void setFluidType(Fluid fluid) { 
		ourFluid = fluid;
	}
	
	//Sets the metadata to be equal to the appropriate value for an amount of
	//milibuckets chosen.
	/*public boolean setMetadataByMB(World world, int x, int y, int z, int amount) {
		if(getConcentrationFromMB(amount) >= 1) {
			setConcentration(world, x, y, z, getConcentrationFromMB(amount));
			return true;
		}
		else {
			world.setBlockToAir(x, y, z);
			return false;
		}
	}*/
	/*
	 * Attempts to push an amount of this gas into the block specified.
	 * Block specified doesn't need to be this gas.
	 * Returns the amount left over.
	 */
	public int pushIntoBlock(World world, int x, int y, int z, int amount) {
		if(getConcentrationFromMB(amount) >= 1) {
			int block = world.getBlockId(x, y, z);
			if(block == 0 || blocksList[block].isAirBlock(world, x , y, z)) {
				//Is this air?
				setConcentration(world, x, y, z, getConcentrationFromMB(amount));
				
				if(amount > getMaxMB()) {
					return amount - getMaxMB();
				}
				return 0;
			}
			else if(isAssociatedBlockID(block)) {
				int blockGas = getBlockInMB(world, x, y, z);
				if((blockGas + amount) > getMaxMB()) {
					setConcentrationByMB(world, x, y, z, getMaxConcentration()); //Set it to max.
					return (blockGas + amount) - getMaxMB(); //Return leftovers.
				}
				else {
					setConcentrationByMB(world, x, y, z, blockGas + amount); //Add amount to block.
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

	private void setConcentrationByMB(World world, int x, int y, int z, int mbuckets) {
		setConcentration(world, x, y, z, mbuckets/mbPerConcentration);		
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
    /*@Override
    public int onBlockPlaced(World world, int x, int y, int z, int par5, float par6, float par7, float par8, int meta) {
    	super.onBlockPlaced(world, x, y, z, par5, par6, par7, par8, meta);
    	return 15;
    }*/
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
		if(ourConcentration >= dissipationConcentration) {
			//Do chemical reactions.
			updateReaction(world, x, y, z);
			int adjBlockID;
			//Iterate through each direction.
			ForgeDirection dirLeastConcentration = ForgeDirection.UP;

			ForgeDirection[] testDirs = new ForgeDirection[6];
			Vector<ForgeDirection> dirList = new Vector<ForgeDirection>(Arrays.asList(ForgeDirection.VALID_DIRECTIONS));
			
			//Set the flow rate to half of this block's concentration level, rounded up.
			int flowRate = (int) Math.ceil(((float)ourConcentration)/2.0f);
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
				else if(isAssociatedBlockID(adjBlockID)) {
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
					setConcentration(world, x + dirLeastConcentration.offsetX, y + dirLeastConcentration.offsetY, z + dirLeastConcentration.offsetZ,
							toDrain); //Set the adjacent block 
					ourConcentration -= toDrain;
				}
				else if(isAssociatedBlockID(adjBlockID)) {
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
		else if(ourConcentration <= dissipationConcentration) {
			//Too much has been extracted from this gas block, it will now eventually be dissipated.
			//Roll for save vs. wind
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
		world.scheduleBlockUpdateWithPriority(x, y, z, getIDByConcentration(ourConcentration), tickRate(world), 0);
    }
	//And so ends the giant function.
	
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
	@Override
	public boolean InCreativeTab() { 
		return false;
	}
}
