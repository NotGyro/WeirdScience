package ws.zettabyte.zettalib.fluid;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;

public interface IFiniteFluidBlock extends IFluidBlock {
	//Properties of the type of block:
	
	//int getMaxConcentration();
	//int getMbPerConcentration();
	//int getDissipationConcentration();
	
	//The amount of MB this block can hold without being set to air instead.
	int getFloorMB();
	
	//Properties of specific blocks, determined by metadata or tile entity.
	//int getConcentration(World world, BlockPos pos);
	int getBlockMB(World world, BlockPos pos);
	
	//int getConcentrationFromMB(int amount);
	
	//void setConcentration(World world, BlockPos pos, int concen);
	//void setConcentrationByMB(World world, BlockPos pos, int mbuckets);
	
	/*
	 * Attempts to push an amount of this gas into the block specified.
	 * Block specified doesn't need to be this gas.
	 * Returns the amount left over.
	 */
	int pushIntoBlock(World world, BlockPos pos, int amount);

	FluidStack partialDrain(World world, BlockPos pos, int amount);
	//Gives you the same results a PartialDrain would, without affecting our block. 
	FluidStack wouldDrain(World world, BlockPos pos, int amount);
}
