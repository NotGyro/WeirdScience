package zettabyte.weirdscience.fluid;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public class BlockGasExplosive extends BlockGasBase {

	public float explosionStrength = 4.0f;
	public boolean explosionsEnabled = true;
	public int explosionThreshhold = 0;
	
	public BlockGasExplosive(int id, Fluid fluid, Material material) {
		super(id, fluid, material);
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
	public int getExplosionThreshhold() {
		return explosionThreshhold;
	}
	public void setExplosionThreshhold(int explosionThreshhold) {
		this.explosionThreshhold = explosionThreshhold;
	}
	@Override
    public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion explosion) {
    	if(canExplode(world, x, y, z)) {
            world.createExplosion(null, x, y, z, explosionStrength, true);
            world.setBlockToAir(x, y, z);
    	}
    }
    public boolean canExplode(World world, int x, int y, int z) {
    	if(explosionsEnabled & (getConcentration(world, x, y, z) > explosionThreshhold)) {
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
