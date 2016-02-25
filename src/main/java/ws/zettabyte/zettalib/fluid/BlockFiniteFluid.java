package ws.zettabyte.zettalib.fluid;

import java.util.List;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import ws.zettabyte.weirdscience.chemistry.IBioactive;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;
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
	public float getFilledPercentage(World world, BlockPos pos) {
		return ((float)getBlockMB(world, pos) / (float)getMaxMB()) * 100.0F;
	}

	public int getMaxMB() {
		return getMaxConcentration() * mbPerConcentration;
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
	public int quantityDropped(Random par1Random) {
	    return 0;
	}

	@Override
	public FluidStack drain(World world, BlockPos pos,
			boolean doDrain) {
				// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canDrain(World world, BlockPos pos) {
		//We're all source blocks here. You must be a source block too, or else you wouldn't have come here.
		return true;
	}

	public int getQuantaValue(IBlockAccess world, BlockPos pos) {
		return mbPerConcentration;
	}

	public boolean equivalent(IBlockState b) {
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

	protected int getConcentrationUnsafe(World world, BlockPos pos) {
		//return world.getBlockStateMetadata(pos) + 1;
		return 0;
	}

	public int getConcentration(World world, BlockPos pos) {
		IBlockState b = world.getBlockState(pos);
		if(b == null) return 0;
		if(b == this) {
			return getConcentrationUnsafe(world, pos);
		}
		else if(this.equivalent(b)) {
			return ((BlockGas)b).getConcentration(world,pos);
		}
		else if(b.getBlock().isAir(world, pos)) {
			return 0;
		}
		else {
			return this.getMaxConcentration() + 1;
		}
	}

	public int getBlockMB(World world, BlockPos pos) {
		return this.getConcentration(world, pos) * this.mbPerConcentration;
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

	public void setConcentration(World world, BlockPos pos, int concen) {
				if (concen < getMinConcentration()) {
					world.setBlockToAir(pos);
				}
				else {
					/*
					TODO: Port this
					if(world.getBlockState(pos).isAir(world, pos)) {
						world.setBlock(pos, this);
					}
					//if(concen >= getMaxConcentration()) {
					//	world.setBlockMetadataWithNotify(pos, 15, 1|2);
						//Todo: spillover code
					//}
					//else {
					if(doesConcentrationMatter) {
						world.setBlockMetadataWithNotify(pos, concen-1, 1|4);
					}
					else {
						world.setBlockMetadataWithNotify(pos, concen-1, 4);
					}
					//}
					 */
				}
				
			}

	public void addConcentration(World world, BlockPos pos, int concen) {
				this.setConcentration(world, pos, (this.getConcentration(world, pos) + concen));
				
			}

	public void setConcentrationByMB(World world, BlockPos pos, int mbuckets) {
				this.setConcentration(world, pos, this.getConcentrationFromMB(mbuckets));
			}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, Entity entity) {
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
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block other) {
				super.onNeighborBlockChange(world, pos, state, other);
				updateReaction(world, pos);
			}

	public void updateReaction(World world, BlockPos pos) {
				if(isReactive ) {
					IBlockState adjBlock;
					for(EnumFacing dir : EnumFacing.values()) {
						BlockPos moved = pos.offset(dir);
						adjBlock = world.getBlockState(moved);
						if(adjBlock != null) {
							tryReaction(world, pos, moved, adjBlock);
						}
					}
				}
			}

	public void tryReaction(World world, BlockPos pos, BlockPos neighorPos, IBlockState b) { }


	public FluidStack wouldDrain(World world, BlockPos pos, int amount) {
				int mb = this.getBlockMB(world, pos);
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
	public int pushIntoBlock(World world, BlockPos pos, int amount) {
				if(getConcentrationFromMB(amount) >= 1) {
					IBlockState block = world.getBlockState(pos);
					if(block == null || block.getBlock().isAir(world, pos)) {
						//Is this air?
						setConcentration(world, pos, getConcentrationFromMB(amount));
						
						if(amount > getMaxMB()) {
							return amount - getMaxMB();
						}
						return 0;
					}
					else if(equivalent(block)) {
						int blockGas = getBlockMB(world, pos);
						if((blockGas + amount) > getMaxMB()) {
							setConcentrationByMB(world, pos, getMaxConcentration()); //Set it to max.
							return (blockGas + amount) - getMaxMB(); //Return leftovers.
						}
						else {
							setConcentrationByMB(world, pos, blockGas + amount); //Add amount to block.
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
	public FluidStack partialDrain(World world, BlockPos pos, int amount) {
				FluidStack f = this.wouldDrain(world, pos, amount);
				if(f.amount >= this.getBlockMB(world, pos)) {
					//Remove our block.
				}
				else {
					this.addConcentration(world,pos, -f.amount);
				}
				return f;
			}
	//Nature tick
}