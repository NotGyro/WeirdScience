package ws.zettabyte.zettalib.fluid;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;
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
	public GasWeight getWeight(World world, BlockPos pos) {
		return weight;
	}

	//world.scheduleBlockUpdate(pos, this, 20, 0); was deprecated, finally gotta write your own tracker for this.
	//@Override
    public void updateTick (World world, BlockPos pos, Random rand) {
        if (world.isRemote) return;
        
        doFlow(world,pos,rand);
    }

	//simulation of movement of gas
	public void doFlow(World world, BlockPos pos, Random rand) {
		int ourConcentration = getConcentrationUnsafe(world, pos);
		if(this.getWeight(world, pos) == GasWeight.HEAVIER) {
			if(this.doFlowForce(world, pos, rand, ourConcentration, EnumFacing.DOWN)) return;
		}
		else if(this.getWeight(world, pos) == GasWeight.LIGHTER) {
			if(this.doFlowForce(world, pos, rand, ourConcentration, EnumFacing.UP)) return;
		}
		if(ourConcentration >= dissipationConcentration) {
			//The preferred routes have failed, try other options.
			int dirIndex = rand.nextInt(6);
			EnumFacing dirLeastConc = EnumFacing.values()[dirIndex];
			int amtLeastConc = this.getMaxConcentration()+1;
			for(int count = 0; count < 6; count++)
			{
				//Do circular behavior: At the end of valid directions, start over from the beginning.
				if(dirIndex >= 6) dirIndex = 0;
				//The adjacent block is more of this gas. Attempt to equalize them.
				int adjBlockConcentration = this.getConcentration(world, pos.offset(EnumFacing.values()[dirIndex]));
				if((adjBlockConcentration < amtLeastConc) && (adjBlockConcentration < ourConcentration) ) {
					dirLeastConc = EnumFacing.values()[dirIndex];
					amtLeastConc = adjBlockConcentration;
				}
				dirIndex++;
			}
			//If nothing was found:
			if(!(amtLeastConc == (this.getMaxConcentration()+1))) {
				//Otherwise, try to equalize into the direction of least concentration.
				doFlowToKnown(world, pos, rand, ourConcentration, amtLeastConc, dirLeastConc);
			}
			updateReaction(world, pos);
		}
		tryDissipate(world, pos, ourConcentration, rand);
	}
	//Try to flow in a particular direction. Returns true if any flow happened.
	public boolean doFlowTo(World world, BlockPos pos, Random rand, int ourConcentration, EnumFacing dir) {
		int adjBlockConcentration = this.getConcentration(world, pos.offset(dir));
		return doFlowToKnown(world, pos, rand, ourConcentration, adjBlockConcentration, dir);
	}
	/* Try to flow in a particular direction, into a block with a known concentration.
	 * The magic happens here, in terms of determining how much is actually transferred.
	 */
	protected boolean doFlowToKnown(World world, BlockPos pos, Random rand,
		int ourConcentration, int theirConcentration, EnumFacing dir) {
		if(ourConcentration <= theirConcentration) return false;
		
		if(!this.equivalent(world.getBlockState(pos.offset(dir)))){
			//world.setBlockState(pos.offset(dir), this, 0, 1|2);
			//TODO
		}
		int rate = (ourConcentration - theirConcentration)/2;
		if(rate == 0) rate = 1;
		if((ourConcentration - rate) >= this.dissipationConcentration)
		{
			//TODO: Something less hard-coded, something less magic-number-ey
			setConcentration(world, pos.offset(dir), theirConcentration + rate);
			setConcentration(world, pos, ourConcentration - rate);
			return true;
		}
		else
		{
			return false;
		}
		
	}
	
	protected boolean doFlowForce(World world, BlockPos pos, Random rand,
			int ourConcentration, EnumFacing dir) {
		int theirConcentration = this.getConcentration(world, pos.offset(dir));
		if(ourConcentration <= theirConcentration) return false;
		
		if(!this.equivalent(world.getBlockState(pos.offset(dir)))){
			//world.setBlock(pos.offset(dir), this, 0, 1|2);
		}
		int rate = Math.min(4, ourConcentration);//(ourConcentration - theirConcentration)/2;
		setConcentration(world, pos.offset(dir), theirConcentration + rate);
		setConcentration(world, pos, ourConcentration - rate);
		return true;
	}
	
	//Added to the world by any means
    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
    	super.onBlockAdded(world, pos, state);
		//world.scheduleBlockUpdate(pos, this, 20, 0);
		updateReaction(world, pos);
    }
    //Placed via itemblock (for creative mode & testing).
	@Override
    public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float par6, float par7, float par8, int meta, EntityLivingBase placer) {
    	super.onBlockPlaced(world, pos, facing, par6, par7, par8, meta, placer);
    	setConcentration(world, pos, getMaxConcentration());
		return null; //TODO
    }

	protected void tryDissipate(World world, BlockPos pos, int ourConcentration, Random rand) {
				if(ourConcentration <= dissipationConcentration) {
					if(rand.nextInt(8) == 0) {
						//TODO: Something not stupid here.
						world.setBlockToAir(pos);
					}
				}
			}

	/*
	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		super.registerBlockIcons(p_149651_1_);
		this.fluid.setIcons(blockIcon);
		this.fluid.setIcons(blockIcon,blockIcon);
	}*/
}
