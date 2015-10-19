package ws.zettabyte.zettalib.fluid;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraft.block.Block;
import net.minecraft.world.World;

//This has to be separate from the specific implementations, I know from experience.
public interface IGasBlock extends IFiniteFluidBlock {
	//How heavy is it? I.e., will it more likely float up or settle down?
	GasWeight getWeight(World world, int x, int y, int z);
}
