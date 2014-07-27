package ws.zettabyte.weirdscience.core.fluid;

import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;

/**
 * Implement this interface on a fluid block that holds a specific type and varying amount of fluid.
 * 
 * @author Gyro
 * 
 */
public interface IFiniteFluidBlock extends IFluidBlock {
	/**
     * Gets the highest value in Forge millibuckets (mB) that can be stored in this block.
     * 
     * @return
     */
	int getMaxMB();

	/**
     * Gets the amount of Forge millibuckets (mB) that are stored in this specific instance of the block.
     * 
     * @return
     */
	int getBlockInMB(World world, int x, int y, int z);

	/**
     * Gets the minimum amount of mB that can have meaning in this block. 
     * 
     * @return
     */
	int getQuantaSize();

	/**
     * Self-descriptive: does this fluid use a tile entity?
     * 
     * @return
     */
	boolean isTileEntity();
	/**
     * Does this specific instance of the block have a tile entity?
     * 
     * @return
     */
	boolean isTileEntity(World world, int x, int y, int z);
    /**
     * Attempt to drain some amount from the block.
     * 
     * NOTE: Much like with an IFluidBlock, the block is intended to handle its own state changes.
     * 
     * @param doDrain
     *            If false, the drain will only be simulated.
     * @param amount
     *            The amount of millibuckets of Forge fluid to be drained from the block.
     * @return
     */
    FluidStack partialDrain(World world, int x, int y, int z, int amount, boolean doDrain);
}
