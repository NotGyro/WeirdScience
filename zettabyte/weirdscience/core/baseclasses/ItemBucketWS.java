package zettabyte.weirdscience.core.baseclasses;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import zettabyte.weirdscience.core.interfaces.ISubBucket;

public class ItemBucketWS extends ItemSubItems implements IFluidContainerItem {
	
	protected ArrayList<ISubBucket> buckets = new ArrayList<ISubBucket>(8);
	
	//Exclude viable block IDs from default ID values.
	protected static final int emptyBucketID = 69;

	public void addSubBucket(ISubBucket b) {
		addSubItem(b);
		buckets.add(b);
	}
	
	public Block getContained(int subDamage) {
		ISubBucket bucket = buckets.get(subDamage);
		if(bucket != null) {
			return bucket.getContained();
		}
		else { 
			return null;
		}
	}
	
	@Override
	public FluidStack getFluid(ItemStack container) {
		ISubBucket bucket = buckets.get(container.getItemDamage());
		if(bucket != null) {
			if(bucket.getFluid() == null) {
				return null;
			}
			return new FluidStack(bucket.getFluid(), 1000);
		}
		else {
			return null;
		}
	}

	@Override
	public int getCapacity(ItemStack container) {
		// TODO Auto-generated method stub
		return 1000;
	}

	@Override
	public int fill(ItemStack container, FluidStack resource, boolean doFill) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
		ISubBucket bucket = buckets.get(container.getItemDamage());
		if(bucket == null) {
			return null;
		}
		if(bucket.getFluid() == null) {
			return null;
		}
		//Cannot drain a whole bucket
		if(maxDrain < 1000) {
			return null;
		}
		FluidStack drained = new FluidStack(bucket.getFluid(), 1000);
		if(doDrain) {
			//Empty the bucket.
			//container.itemID = emptyBucketID;
			//container.stackSize = 1;
			//container.setItemDamage(0);
		}
		return drained;
	}
	
    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, false);

        if (movingobjectposition == null) {
            return itemStack;
        }
        else {
            if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE) {
                int x = movingobjectposition.blockX;
                int y = movingobjectposition.blockY;
                int z = movingobjectposition.blockZ;

                //If we don't have permissions for this block, don't do anything.
                if (!world.canMineBlock(player, x, y, z)) {
                    return itemStack;
                }
                //Go to the block that is opposite the side we have hit.
                if (movingobjectposition.sideHit == 0) {
                	--y;
                }
                if (movingobjectposition.sideHit == 1) {
                	++y;
                }

                if (movingobjectposition.sideHit == 2) {
                	--z;
                }
                if (movingobjectposition.sideHit == 3) {
                	++z;
                }

                if (movingobjectposition.sideHit == 4) {
                	--x;
                }
                if (movingobjectposition.sideHit == 5) {
                	++x;
                }

                //If the player has permission to edit this...
                if (!player.canPlayerEdit(x, y, z, movingobjectposition.sideHit, itemStack)) {
                        return itemStack;
                }

                //Place the contained liquid, and drain the bucket if a. the place succeeded and b. the player is not in creative mode.
                if (this.tryPlaceContainedLiquid(world, x, y, z, itemStack.getItemDamage()) && !player.capabilities.isCreativeMode) {
                	return new ItemStack(Item.bucketEmpty);
                }
            }
        }
        return itemStack;
    }

    /**
     * Attempts to place the liquid contained inside the bucket.
     */
    public boolean tryPlaceContainedLiquid(World world, int x, int y, int z, int damage)
    {
    	ISubBucket bucket = buckets.get(damage);
        if(bucket == null) {
            return false;
        }
        else {
            Material material = world.getBlockMaterial(x, y, z);
            boolean flag = !material.isSolid();

            if (!world.isAirBlock(x, y, z) && !flag) {
                return false;
            }
            else {
            	if (!world.isRemote && flag && !material.isLiquid()) {
                        world.destroyBlock(x, y, z, true);
                }
            	world.setBlock(x, y, z, bucket.getContained().blockID, bucket.getContainedMeta(), 3);
            	return true;
            }
        }
    }

	public ItemBucketWS(Configuration config, String name, int defaultID) {
		super(config, name, defaultID);
		setContainerItem(Item.bucketEmpty);
		// TODO Auto-generated constructor stub
	}

	public ItemBucketWS(Configuration config, String name) {
		super(config, name);
		setContainerItem(Item.bucketEmpty);
		// TODO Auto-generated constructor stub
	}

	public ItemBucketWS(int id) {
		super(id);
		setContainerItem(Item.bucketEmpty);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean hasContainerItem() {
		// TODO Auto-generated method stub
		return true;
	}
}
