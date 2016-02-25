package ws.zettabyte.zettalib.fluid;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public class BlockGasFlammable extends BlockGas {

	public float explosionStrength = 4.0f;
	public boolean explosionsEnabled = true;
	public int explosionThreshhold = 0;
	
	public BlockGasFlammable(Fluid fluid) {
		super(fluid); //Just like liquid hydrogen!
		isReactive = true;
	}
	/*
    public float getExplosionStrength() {
		return explosionStrength;
	}
	public void setExplosionStrength(float explosionStrength) {
		this.explosionStrength = explosionStrength;
	}
	public boolean isExplosionsEnabled() {
		return explosionsEnabled;
	}
	public void setExplosionsEnabled(boolean explosionsEnabled) {
		this.explosionsEnabled = explosionsEnabled;
	}
	public int getExplosionThreshhold() {
		return explosionThreshhold;
	}
	public void setExplosionThreshhold(int explosionThreshhold) {
		this.explosionThreshhold = explosionThreshhold;
	}
	@Override
    public void onBlockDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
    	if(canExplode(world, pos)) {
            world.createExplosion(null, pos, explosionStrength, true);
            world.setBlockToAir(pos);
    	}
    }*/
    public boolean canExplode(World world, BlockPos pos) {
    	if(explosionsEnabled & (getConcentration(world, pos) > explosionThreshhold)) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    //Check to see if the new neighbor is a block that should cause us to explode.
    @Override
	public void tryReaction(World world, BlockPos pos, BlockPos posOther, IBlockState b) {
    	if(((b == Blocks.fire || b == Blocks.torch) || b == Blocks.lava)){
        	if(canExplode(world, pos)) {
                world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), explosionStrength, true);
                world.setBlockToAir(pos);
        	}
    	}
    }
}