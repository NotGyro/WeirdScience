package ws.zettabyte.zettalib.fluid;

import java.util.List;
import java.util.Random;

import ws.zettabyte.weirdscience.chemistry.IBioactive;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;

public class BlockFiniteFluid extends Block implements IFiniteFluidBlock {

	public int mbPerConcentration = 64;
	public int dissipationConcentration = 2;
	public boolean entitiesInteract = false;
	protected Fluid fluid;
	protected boolean doesConcentrationMatter;
	protected boolean isReactive;

	public BlockFiniteFluid(Material mat) {
		super(mat);
	}

	@Override
	public Fluid getFluid() {
		return fluid;
	}

	@Override
	public float getFilledPercentage(World world, int x, int y,
			int z) {
		return ((float)getBlockMB(world, x, y, z) / (float)getMaxMB()) * 100.0F;
	}

	public int getMaxMB() {
		return getMaxConcentration() * mbPerConcentration;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y,
			int z, int side) {
				//Do not render gas<->gas faces
			    if (this.equivalent(world.getBlock(x,y,z))) return false;
			    return !world.getBlock(x, y, z).isOpaqueCube();
				//return true;
			}

	@Override
	public int getRenderBlockPass() {
	    return 1;
	}

	/**
	 * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
	 * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
	 */
	@Override
	public boolean isOpaqueCube() {
	    return false;
	}

	@Override
	public void addCollisionBoxesToList(World p_149743_1_, int p_149743_2_,
			int p_149743_3_, int p_149743_4_, AxisAlignedBB p_149743_5_, List p_149743_6_, Entity p_149743_7_) { }

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_,
			int p_149668_3_, int p_149668_4_) {
		return null;
	}

	@Override
	public int quantityDropped(Random par1Random) {
	    return 0;
	}

	@Override
	public FluidStack drain(World world, int x, int y, int z,
			boolean doDrain) {
				// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canDrain(World world, int x, int y, int z) {
		//We're all source blocks here. You must be a source block too, or else you wouldn't have come here.
		return true;
	}

	public int getQuantaValue(IBlockAccess world, int x, int y,
			int z) {
		return mbPerConcentration;
	}

	@Override
	public boolean canCollideCheck(int meta, boolean fullHit) {
		return false;
	}

	public boolean equivalent(Block b) {
		return (b == this);
	}

	public int getMaxConcentration() {
		return 16;
	}

	public int getMinConcentration() {
		return 1;
	}

	public int getDissipationConcentration() {
		return 1;
	}

	protected int getConcentrationUnsafe(World world, int x,
			int y, int z) {
		return world.getBlockMetadata(x, y, z) + 1;
	}

	public int getConcentration(World world, int x, int y,
			int z) {
		Block b = world.getBlock(x,y,z);
		if(b == null) return 0;
		if(b == this) {
			return getConcentrationUnsafe(world, x, y, z);
		}
		else if(this.equivalent(b)) {
			return ((BlockGas)b).getConcentration(world,x,y,z);
		}
		else if(b.isAir(world, x, y, z)) {
			return 0;
		}
		else {
			return this.getMaxConcentration() + 1;
		}
	}

	public int getBlockMB(World world, int x, int y, int z) {
		return this.getConcentration(world, x, y, z) * this.mbPerConcentration;
	}

	public int getConcentrationFromMB(int amount) {
		if(amount < getMaxConcentration() * this.mbPerConcentration) {
			return (int)Math.floor((float)amount / (float)mbPerConcentration);
			//Return the closest metadata value for a given fluid amount.
		}
		else {
			//Are we over the max? Set it to max.
			return getMaxConcentration();
		}
	}

	protected BlockFiniteFluid getBlockForConc(int concen) {
		return this;
	}

	public void setConcentration(World world, int x, int y,
			int z, int concen) {
				if (concen < getMinConcentration()) {
					world.setBlock(x, y, z, Blocks.air, 0, 1|2);
				}
				else {
					if(world.getBlock(x, y, z).isAir(world, x, y, z)) {
						world.setBlock(x, y, z, this);
					}
					//if(concen >= getMaxConcentration()) {
					//	world.setBlockMetadataWithNotify(x, y, z, 15, 1|2);
						//Todo: spillover code
					//}
					//else {
					if(doesConcentrationMatter) {
						world.setBlockMetadataWithNotify(x, y, z, concen-1, 1|4);
					}
					else {
						world.setBlockMetadataWithNotify(x, y, z, concen-1, 4);
					}
					//}
				}
				
			}

	public void addConcentration(World world, int x, int y,
			int z, int concen) {
				this.setConcentration(world, x, y, z, (this.getConcentration(world, x, y, z) + concen));
				
			}

	public void setConcentrationByMB(World world, int x, int y,
			int z, int mbuckets) {
				this.setConcentration(world, x, y, z, this.getConcentrationFromMB(mbuckets));		
			}

	@Override
	public void onEntityCollidedWithBlock(World world, int x,
			int y, int z, Entity entity) {
				if(fluid == null) return;
			    if(entitiesInteract & (fluid instanceof IBioactive)) {
			    	IBioactive bioFluid = (IBioactive)fluid;
			    	if(entity == null) return;
			    	if(entity instanceof EntityLivingBase) {
			    		bioFluid.breatheAffectCreature((EntityLivingBase)entity);
			    	}
			    }
			}

	@Override
	public void onNeighborBlockChange(World world, int x, int y,
			int z, Block other) {
				// TODO Auto-generated method stub
				super.onNeighborBlockChange(world, x, y, z, other);
				updateReaction(world, x, y, z);
			}

	public void updateReaction(World world, int x, int y,
			int z) {
				if(isReactive ) {
					Block adjBlock;
					for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
						adjBlock = world.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
						if(adjBlock != null) {
							tryReaction(world, x, y, z, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, adjBlock);
						}
					}
				}
			}

	public void tryReaction(World world, int x, int y,
			int z, int xO, int yO, int zO, Block b) { }


	public FluidStack wouldDrain(World world, int x, int y,
			int z, int amount) {
				// TODO Auto-generated method stub
				int mb = this.getBlockMB(world, x, y, z);
				if(amount > mb) {
					amount = mb;
				}
				return new FluidStack(this.getFluid(), amount);
			}


	@Override
	public int getFloorMB() {
		return this.mbPerConcentration;
	}

	@Override
	public int pushIntoBlock(World world, int x, int y,
			int z, int amount) {
				if(getConcentrationFromMB(amount) >= 1) {
					Block block = world.getBlock(x, y, z);
					if(block == null || block.isAir(world, x , y, z)) {
						//Is this air?
						setConcentration(world, x, y, z, getConcentrationFromMB(amount));
						
						if(amount > getMaxMB()) {
							return amount - getMaxMB();
						}
						return 0;
					}
					else if(equivalent(block)) {
						int blockGas = getBlockMB(world, x, y, z);
						if((blockGas + amount) > getMaxMB()) {
							setConcentrationByMB(world, x, y, z, getMaxConcentration()); //Set it to max.
							return (blockGas + amount) - getMaxMB(); //Return leftovers.
						}
						else {
							setConcentrationByMB(world, x, y, z, blockGas + amount); //Add amount to block.
							return 0;
						}
					}
					else {
						//Cannot push gas into block, all of it is left over. 
						return amount;
					}
				}
				else {
					//Less than minimum mB for a gas block.
					//Just fizzle it.
					return 0;
				}
			}

	@Override
	public FluidStack partialDrain(World world, int x, int y,
			int z, int amount) {
				FluidStack f = this.wouldDrain(world, x, y, z, amount);
				if(f.amount >= this.getBlockMB(world, x, y, z)) {
					//Remove our block.
				}
				else {
					this.addConcentration(world,x,y,z, -f.amount);
				}
				return f;
			}
	//Nature tick
}