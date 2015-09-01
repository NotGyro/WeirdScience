package ws.zettabyte.weirdscience.gas;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class BlockGas extends Block implements IGasBlock {
	
	public int mbPerConcentration = 64;
	
	//If a block's concentration is lower than or equal to this, there is a chance to dissipate.
	public int dissipationConcentration = 2;
	
	public GasWeight weight = GasWeight.LIGHTER;
	
	public boolean doesConcentrationMatter = false; //Do changes in Concentration incur block updates?
	
	protected boolean entitiesInteract = false;
	
	protected Fluid fluid;
	
	//int flowRate
	
	public BlockGas(Fluid f) {
		//TODO: "Gas" material.
		super(Material.clay);
		this.fluid = f;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fluid getFluid() {
		// TODO Auto-generated method stub
		return fluid;
	}

	@Override
	public float getFilledPercentage(World world, int x, int y, int z) {
    	return ((float)getBlockMB(world, x, y, z) / (float)getMaxMB()) * 100.0F;
    }

	private float getMaxMB() {
		return getMaxConcentration() * mbPerConcentration;
	}
	
	//Oddly, I'm fairly sure the X, Y and Z here are of the block we're checking against.
    @Override
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
    {
    	//Do not render gas<->gas faces
        if (this.equivalent(world.getBlock(x,y,z))) return false;
        return !world.getBlock(x, y, z).isOpaqueCube();
    	//return true;
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
	
	@Override
	public void addCollisionBoxesToList(World p_149743_1_, int p_149743_2_,
			int p_149743_3_, int p_149743_4_, AxisAlignedBB p_149743_5_,
			List p_149743_6_, Entity p_149743_7_) { }
	
	

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_,
			int p_149668_2_, int p_149668_3_, int p_149668_4_) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
    public int quantityDropped(Random par1Random) {
        return 0;
    }
	@Override
	public FluidStack drain(World world, int x, int y, int z, boolean doDrain) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canDrain(World world, int x, int y, int z) {
		//We're all source blocks here. You must be a source block too, or else you wouldn't have come here.
		return true;
	}

	public int getQuantaValue(IBlockAccess world, int x, int y, int z) {
		return mbPerConcentration;
	}

	@Override
	public boolean canCollideCheck(int meta, boolean fullHit) {
		return false;
	}

	//Is this block (on the technical level) intended to represent the same substance (abstractly)?
	public boolean equivalent(Block b) {
		return (b == this);
	}

	public int getMaxConcentration() {
		return 16;
	}
	
	public int getMinConcentration() {
		return 1;
	}
/*
	@Override
	public int getMbPerConcentration() {
			
			//Vector<ForgeDirection> testDirs = new Vector<ForgeDirection>(6);
			//Set the flow rate to half of this block's concentration level, rounded up.
			//int flowRate = (int) Math.ceil(((float)ourConcentration)/2.0f);
			
		return mbPerConcentration;
	}
*/
	public int getDissipationConcentration() {
		return 1;
	}

	@Override
	public int getFloorMB() {
		return this.mbPerConcentration;
	}

	protected int getConcentrationUnsafe(World world, int x, int y, int z) {
		return world.getBlockMetadata(x, y, z) + 1;
	}
	
	public int getConcentration(World world, int x, int y, int z) {
		Block b = world.getBlock(x,y,z);
		if(b == null) return 0;
		if(b == this) {
			return getConcentrationUnsafe(world, x, y, z);
		}
		else if(this.equivalent(b)) {
			return ((BlockGas)b).getConcentration(world,x,y,z);
		}
		else if(b.isAir(world, x, y, z)) {
			return 0;
		}
		else {
			return this.getMaxConcentration() + 1;
		}
	}

	public int getBlockMB(World world, int x, int y, int z) {
		// TODO Auto-generated method stub
		return this.getConcentration(world, x, y, z) * this.mbPerConcentration;
	}
	
	public int getConcentrationFromMB(int amount) {
		if(amount < getMaxConcentration() * this.mbPerConcentration) {
			return (int)Math.floor((float)amount / (float)mbPerConcentration);
			//Return the closest metadata value for a given fluid amount.
		}
		else {
			//Are we over the max? Set it to max.
			return getMaxConcentration();
		}
	}
	
	protected BlockGas getBlockForConc(int concen) {
		return this;
	}

	public void setConcentration(World world, int x, int y, int z, int concen) {
		if (concen < getMinConcentration()) {
			world.setBlock(x, y, z, Blocks.air, 0, 1|2);
		}
		else {
			//if(concen >= getMaxConcentration()) {
			//	world.setBlockMetadataWithNotify(x, y, z, 15, 1|2);
				//Todo: spillover code
			//}
			//else {
			if(doesConcentrationMatter) {
				world.setBlockMetadataWithNotify(x, y, z, concen-1, 1|4);
			}
			else {
				world.setBlockMetadataWithNotify(x, y, z, concen-1, 4);
			}
			//}
		}
		
	}
	//Should work gracefully with negative values.
	public void addConcentration(World world, int x, int y, int z, int concen) {
		this.setConcentration(world, x, y, z, (this.getConcentration(world, x, y, z) + concen));
		
	}

	public void setConcentrationByMB(World world, int x, int y, int z,
			int mbuckets) {
		this.setConcentration(world, x, y, z, this.getConcentrationFromMB(mbuckets));		
	}

	@Override
	public int pushIntoBlock(World world, int x, int y, int z, int amount) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public FluidStack partialDrain(World world, int x, int y, int z, int amount) {
		FluidStack f = this.wouldDrain(world, x, y, z, amount);
		if(f.amount >= this.getBlockMB(world, x, y, z)) {
			//Remove our block.
		}
		else {
			this.addConcentration(world,x,y,z, -f.amount);
		}
		return f;
	}
	
	public FluidStack wouldDrain(World world, int x, int y, int z, int amount) {
		// TODO Auto-generated method stub
		int mb = this.getBlockMB(world, x, y, z);
		if(amount > mb) {
			amount = mb;
		}
		return new FluidStack(this.getFluid(), amount);
	}

	@Override
	public GasWeight getWeight(World world, int x, int y, int z) {
		return weight;
	}
	
	//Nature tick
	@Override
    public void updateTick (World world, int x, int y, int z, Random rand) {
        if (world.isRemote) return;
        
        doFlow(world,x,y,z,rand);
        
		world.scheduleBlockUpdateWithPriority(x, y, z, this, 20, 0);
    }

	//simulation of movement of gas
	public void doFlow(World world, int x, int y, int z, Random rand) {
		int ourConcentration = getConcentrationUnsafe(world, x, y, z);
		if(this.getWeight(world, x, y, z) == GasWeight.HEAVIER) {
			if(this.doFlowForce(world, x, y, z, rand, ourConcentration, ForgeDirection.DOWN)) return;
		}
		else if(this.getWeight(world, x, y, z) == GasWeight.LIGHTER) {
			if(this.doFlowForce(world, x, y, z, rand, ourConcentration, ForgeDirection.UP)) return;
		}
		if(ourConcentration >= dissipationConcentration) {
			//The preferred routes have failed, try other options.
			int dirIndex = rand.nextInt(6);
			ForgeDirection dirLeastConc = ForgeDirection.VALID_DIRECTIONS[dirIndex];
			int amtLeastConc = this.getMaxConcentration()+1;
			for(int count = 0; count < 6; count++)
			{
				//Do circular behavior: At the end of valid directions, start over from the beginning.
				if(dirIndex >= 6) dirIndex = 0;
				//The adjacent block is more of this gas. Attempt to equalize them.
				int adjBlockConcentration = this.getConcentration(world, 
						x + ForgeDirection.VALID_DIRECTIONS[dirIndex].offsetX, 
						y + ForgeDirection.VALID_DIRECTIONS[dirIndex].offsetY, 
						z + ForgeDirection.VALID_DIRECTIONS[dirIndex].offsetZ);
				if((adjBlockConcentration < amtLeastConc) && (adjBlockConcentration < ourConcentration) ) {
					dirLeastConc = ForgeDirection.VALID_DIRECTIONS[dirIndex];
					amtLeastConc = adjBlockConcentration;
				}
				dirIndex++;
			}
			//If nothing was found:
			if(!(amtLeastConc == (this.getMaxConcentration()+1))) {
				//Otherwise, try to equalize into the direction of least concentration.
				doFlowToKnown(world, x, y, z, rand, ourConcentration, amtLeastConc, dirLeastConc);
			}
		}
		tryDissipate(world, x, y, z, ourConcentration, rand);
	}
	protected void tryDissipate(World world, int x, int y, int z, int ourConcentration, Random rand) {
		if(ourConcentration <= dissipationConcentration) {
			if(rand.nextInt(8) == 0) {
				//TODO: Something not stupid here.
				world.setBlockToAir(x, y, z);
			}
		}
	}
	//Try to flow in a particular direction. Returns true if any flow happened.
	public boolean doFlowTo(World world, int x, int y, int z, Random rand, int ourConcentration, ForgeDirection dir) {
		int adjBlockConcentration = this.getConcentration(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
		return doFlowToKnown(world, x, y, z, rand, ourConcentration, adjBlockConcentration, dir);
	}
	/* Try to flow in a particular direction, into a block with a known concentration.
	 * The magic happens here, in terms of determining how much is actually transferred.
	 */
	protected boolean doFlowToKnown(World world, int x, int y, int z, Random rand, 
		int ourConcentration, int theirConcentration, ForgeDirection dir) {
		if(ourConcentration <= theirConcentration) return false;
		
		if(!this.equivalent(world.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ))){
			world.setBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, this, 0, 1|2);
		}
		int rate = (ourConcentration - theirConcentration)/2;
		if(rate == 0) rate = 1;
		if((ourConcentration - rate) >= this.dissipationConcentration)
		{
			//TODO: Something less hard-coded, something less magic-number-ey
			setConcentration(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, theirConcentration + rate);
			setConcentration(world, x, y, z, ourConcentration - rate);
			return true;
		}
		else
		{
			return false;
		}
		
	}
	
	protected boolean doFlowForce(World world, int x, int y, int z, Random rand, 
			int ourConcentration, ForgeDirection dir) {
		int theirConcentration = this.getConcentration(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
		if(ourConcentration <= theirConcentration) return false;
		
		if(!this.equivalent(world.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ))){
			world.setBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, this, 0, 1|2);
		}
		int rate = Math.min(4, ourConcentration);//(ourConcentration - theirConcentration)/2;
		setConcentration(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, theirConcentration + rate);
		setConcentration(world, x, y, z, ourConcentration - rate);
		return true;
	}
	
	//Added to the world by any means
    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
    	super.onBlockAdded(world, x, y, z);
		world.scheduleBlockUpdateWithPriority(x, y, z, this, 20, 0);
    }
    //Placed via itemblock (for creative mode & testing).
	@Override
    public int onBlockPlaced(World world, int x, int y, int z, int par5, float par6, float par7, float par8, int meta) {
    	super.onBlockPlaced(world, x, y, z, par5, par6, par7, par8, meta);
    	setConcentration(world, x, y, z, getMaxConcentration());
		return 15;
    }
}
