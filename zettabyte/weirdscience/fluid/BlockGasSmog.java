package zettabyte.weirdscience.fluid;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public class BlockGasSmog extends BlockGasExplosive {

	protected int acidThreshhold;
	protected Block blockAcid;
	public BlockGasSmog(int id, Fluid fluid, Material material) {
		super(id, fluid, material);
		acidThreshhold = 3;//(int)Math.ceil((float)this.getMaxConcentration()/4.0f);
		entitiesInteract = true;
	}
	public Block getBlockAcid() {
		return blockAcid;
	}
	public void setBlockAcid(Block acid) {
		this.blockAcid = acid;
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
	}

}
