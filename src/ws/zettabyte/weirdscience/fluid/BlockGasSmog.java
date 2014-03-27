package ws.zettabyte.weirdscience.fluid;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.fluids.Fluid;
import ws.zettabyte.zettalib.ContentRegistry;
import ws.zettabyte.zettalib.interfaces.IConfiggable;

public class BlockGasSmog extends BlockGasExplosive implements IConfiggable {

	public BlockGasSmog(Configuration config, String name, Fluid fluid) {
		super(config, name, fluid);
		// TODO Auto-generated constructor stub
	}
	protected int acidThreshhold;
	protected static Block blockAcid;
	public static Block blockRust = null;
	public static int metaRust = 0;
	
	public static Block getBlockAcid() {
		return blockAcid;
	}
	public static void setBlockAcid(Block acid) {
		blockAcid = acid;
	}
	public int getAcidThreshhold() {
		return acidThreshhold;
	}
	public void setAcidThreshhold(int threshhold) {
		this.acidThreshhold = threshhold;
	}
	@Override
	public void tryReaction(World world, int x, int y, int z, int xO, int yO, int zO) {
		super.tryReaction(world, x, y, z, xO, yO, zO);
		int id = world.getBlockId(xO, yO, zO);
		if((id == Block.waterStill.blockID) && 
				(this.getConcentration(world, x, y, z) >= acidThreshhold)) {
			world.setBlock(xO, yO, zO, blockAcid.blockID, 0, 1|2);
			setConcentration(world, x, y, z, getConcentration(world, x, y, z) - acidThreshhold);
		}
		if((id == Block.blockIron.blockID) && 
				(this.getConcentration(world, x, y, z) >= acidThreshhold) &&
				this.blockRust != null) {
			world.setBlock(xO, yO, zO, blockRust.blockID, metaRust, 1|2);
			setConcentration(world, x, y, z, getConcentration(world, x, y, z) - acidThreshhold);
		}
	}

	@Override
	public void doConfig(Configuration config, ContentRegistry cr) {
		int explThresh = config.get("Gas", "Smog explosion threshold", 200).getInt();
		int acidThresh = config.get("Gas", "Smog water-to-acid threshold", 4).getInt();
		setAcidThreshhold(acidThresh);
		setExplosionThreshhold(explThresh);
	}

}
