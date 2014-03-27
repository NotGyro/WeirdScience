package ws.zettabyte.weirdscience.fluid;

import net.minecraft.block.Block;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.fluids.Fluid;
import ws.zettabyte.zettalib.fluid.BlockGasBase;

public class BlockGasExplosive extends BlockGasBase {

	public float explosionStrength = 4.0f;
	public boolean explosionsEnabled = true;
	public int explosionThreshholdConcentration = 0;
	
	public BlockGasExplosive(Configuration config, String name, Fluid fluid) {
		super(config, name, fluid);
		isReactive = true;
	}
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
	//In millibuckets
	public int getExplosionThreshhold() {
		return this.getMbPerConcentration() * explosionThreshholdConcentration;
	}
	//In millibuckets
	public void setExplosionThreshhold(int explosionThreshhold) {
		this.explosionThreshholdConcentration = this.getConcentrationFromMB(explosionThreshhold);
	}
	@Override
    public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion explosion) {
    	if(canExplode(world, x, y, z)) {
            world.createExplosion(null, x, y, z, explosionStrength, true);
            world.setBlockToAir(x, y, z);
    	}
    }
    public boolean canExplode(World world, int x, int y, int z) {
    	if(explosionsEnabled & (getConcentration(world, x, y, z) > explosionThreshholdConcentration)) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    //Check to see if the new neighbor is a block that should cause us to explode.
    @Override
	public void tryReaction(World world, int x, int y, int z, int xO, int yO, int zO) {
    	int id = world.getBlockId(xO, yO, zO);
    	if(((id == Block.fire.blockID || id == Block.torchWood.blockID) || id == Block.lavaStill.blockID) || id == Block.lavaMoving.blockID){
        	if(canExplode(world, x, y, z)) {
                world.createExplosion(null, x, y, z, explosionStrength, true);
                world.setBlockToAir(x, y, z);
        	}
    	}
    }
}
