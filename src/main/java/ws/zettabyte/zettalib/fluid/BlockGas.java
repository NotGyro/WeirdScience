package ws.zettabyte.zettalib.fluid;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class BlockGas extends BlockFiniteFluid implements IGasBlock {
	
	public GasWeight weight = GasWeight.NEUTRAL;
	
	public boolean doesConcentrationMatter = false; //Do changes in Concentration incur block updates?
	
	public boolean isReactive = false;
	
	public BlockGas(Fluid f) {
		//TODO: "Gas" material.
		super(MaterialGas.instance);
		this.fluid = f;
	}

	@Override
	public GasWeight getWeight(World world, int x, int y, int z) {
		return weight;
	}
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
			updateReaction(world, x, y, z);
		}
		tryDissipate(world, x, y, z, ourConcentration, rand);
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
		updateReaction(world, x, y, z);
    }
    //Placed via itemblock (for creative mode & testing).
	@Override
    public int onBlockPlaced(World world, int x, int y, int z, int par5, float par6, float par7, float par8, int meta) {
    	super.onBlockPlaced(world, x, y, z, par5, par6, par7, par8, meta);
    	setConcentration(world, x, y, z, getMaxConcentration());
		return 15;
    }

	protected void tryDissipate(World world, int x, int y,
			int z, int ourConcentration, Random rand) {
				if(ourConcentration <= dissipationConcentration) {
					if(rand.nextInt(8) == 0) {
						//TODO: Something not stupid here.
						world.setBlockToAir(x, y, z);
					}
				}
			}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		super.registerBlockIcons(p_149651_1_);
		this.fluid.setIcons(blockIcon);
		this.fluid.setIcons(blockIcon,blockIcon);
	}
	
	
}
