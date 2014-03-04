package zettabyte.weirdscience.core.chemistry;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.fluids.Fluid;
import zettabyte.weirdscience.core.baseclasses.BlockFluidClassicWS;

//TODO : Code for corroding blocks.
public class BlockFluidReactive extends BlockFluidClassicWS implements
		IReactionReceiver {

	protected ArrayList<IReactionSpec> Reactions = new ArrayList<IReactionSpec>(12);
	protected ArrayList<ItemStack> solutesItems = new ArrayList<ItemStack>(4);
	protected ArrayList<Fluid> solutesFluids = new ArrayList<Fluid>(4);
	protected ArrayList<Block> solutesBlocks = new ArrayList<Block>(4);
	@Override
	public boolean registerReaction(IReactionSpec react) {
		if(react.getSolvent().getName() == this.getFluid().getName()) {
			Reactions.add(react);
			//Build sub-lists for sanity here:
			//Items
			if(react.getSolute() instanceof ItemStack) {
				solutesItems.add((ItemStack) react.getSolute());
			}
			else if(react.getSolute() instanceof Item) {
				solutesItems.add(new ItemStack((Item)react.getSolute()));
			}
			else {
				solutesItems.add(null);
			}
			//Blocks
			if(react.getSolute() instanceof Block) {
				solutesBlocks.add((Block) react.getSolute());
			}
			else {
				solutesBlocks.add(null);
			}
			//Fluids
			if(react.getSolute() instanceof Fluid) {
				solutesFluids.add((Fluid) react.getSolute());
			}
			else {
				solutesFluids.add(null);
			}
			//End list building.
			
			return true;
		}
		else {
			return false;
		}
	}

	//Herein is our reaction code.
	@Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		super.onEntityCollidedWithBlock(world, x, y, z, entity);
		if(!world.isRemote) {
			//Do item reactions.
			if(entity instanceof EntityItem) {
				EntityItem entityItem = (EntityItem)entity;
				ItemStack itemStack = entityItem.getEntityItem();
				if(solutesItems.size() > 0) {
					for(int i = 0; i < solutesItems.size(); ++i) {
						//System.out.println(itemStack.getItem().getUnlocalizedName());
						//System.out.println(solutesItems.get(i).getItem().getUnlocalizedName());
						if(itemStack.getItem().getUnlocalizedName().contentEquals(solutesItems.get(i).getItem().getUnlocalizedName())) {
							//And now, the meat of the reaction code.
							//Is this reaction possible for us?
							if(1000 >= Reactions.get(i).getSolventMin()) {
								//Do we have the minimum amount of solute needed?
								if(itemStack.stackSize >= Reactions.get(i).getSoluteMin()) {
									//Mess with the item stack.
									if(Reactions.get(i).isSoluteAffected()) { 
										if(Reactions.get(i).getSoluteTarget() != null) {
											//Make sure we can do something sane here.
											if(Reactions.get(i).getSoluteTarget() instanceof ItemStack) {
												ItemStack newItemStack = ((ItemStack)Reactions.get(i).getSoluteTarget()).copy();
												newItemStack.stackSize *= itemStack.stackSize;
												
												//Spawn our new item.
												EntityItem newEntityItem = new EntityItem(world, entityItem.posX, entityItem.posY, entityItem.posZ, newItemStack);
												newEntityItem.motionX = 0;
												newEntityItem.motionY = 0;
												newEntityItem.motionZ = 0;
									            world.spawnEntityInWorld(newEntityItem);
									            
												entityItem.setDead();
											}
										}
										else { 
											//Destroys solute.
											entityItem.setDead();
										}
									}
									//Mess with this block.
									if(Reactions.get(i).isSolventAffected()) { 
										if(Reactions.get(i).getSolventTarget() != null) {
											//Try to do the item thing.
											if(Reactions.get(i).getSolventTarget() instanceof ItemStack) {
												ItemStack newItemStack = ((ItemStack)Reactions.get(i).getSolventTarget()).copy();
												
												//Spawn our new item.
												EntityItem newEntityItem = new EntityItem(world, entityItem.posX, entityItem.posY, entityItem.posZ, newItemStack);
									            entityItem.motionX = 0;
									            entityItem.motionY = 0;
									            entityItem.motionZ = 0;
									            world.spawnEntityInWorld(newEntityItem);
											}
											//Try to do the block thing.
											else {
												if(Reactions.get(i).getSolventTarget() instanceof Block) {
													Block newBlock = (Block)Reactions.get(i).getSolventTarget();
													world.setBlock(x, y, z, newBlock.blockID);
												}
												else if(Reactions.get(i).getSolventTarget() instanceof Fluid) {
													//If it 's a fluid we still try to do the block thing.
													int newBlock = ((Fluid)Reactions.get(i).getSolventTarget()).getBlockID();
													world.setBlock(x, y, z, newBlock);
												}
											}
										}
										else { 
											//Set this block to air if solvent is affected and there is a null target.
											world.setBlock(x, y, z, 0);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public BlockFluidReactive(Configuration config, String name, Fluid fluid) {
		super(config, name, fluid);
		// TODO Auto-generated constructor stub
	}

	public BlockFluidReactive(Configuration config, String name, int defaultID,
			Fluid fluid) {
		super(config, name, defaultID, fluid);
		// TODO Auto-generated constructor stub
	}

	public BlockFluidReactive(Configuration config, String name, int defaultID,
			Material material, Fluid fluid) {
		super(config, name, defaultID, material, fluid);
		// TODO Auto-generated constructor stub
	}

	public BlockFluidReactive(Configuration config, String name,
			Material material, Fluid fluid) {
		super(config, name, material, fluid);
		// TODO Auto-generated constructor stub
	}

	public BlockFluidReactive(int id, Fluid fluid, Material material) {
		super(id, fluid, material);
		// TODO Auto-generated constructor stub
	}

}
