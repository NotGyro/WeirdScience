package ws.zettabyte.weirdscience.fluid;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import ws.zettabyte.weirdscience.WeirdScience;
import ws.zettabyte.weirdscience.chemistry.BlockFluidClassicChem;

public class BlockAcid extends BlockFluidClassicChem {

	public BlockAcid(Fluid fluid, Material m) {
		super(fluid, m);
	}

	public BlockAcid(Fluid fluid) {
		super(fluid);
	}

	@Override
	public void tryReaction(World world, int x, int y, int z, int xO, int yO,
			int zO, Block b) {
		if(b == Blocks.iron_block) {
			world.setBlock(xO, yO, zO, WeirdScience.blockRust); //TODO: rusty
		}
	}
}
