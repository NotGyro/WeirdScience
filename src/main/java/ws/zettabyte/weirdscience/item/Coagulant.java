package ws.zettabyte.weirdscience.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.IFluidBlock;

public class Coagulant extends Item { //Base {

	public static Block congealedBlock = null;
	
	public static final int BONEMEAL_ID = 15;
	
	public static final int craftCount = 2;
	/*
	public Coagulant(int par1) {
		super(par1);
		
		//setCreativeTab(WeirdScience.tabWeirdScience);
		//setUnlocalizedName("coagulant");
	}*/
	/*
	@Override
	public void registerIcons(IconRegister reg) {
		this.itemIcon = reg.registerIcon("weirdscience:coagulant");
	}*/
	
	public Coagulant(Configuration config, String name) {
		super();
		// TODO Auto-generated constructor stub
	}

	public Coagulant() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public ItemStack onItemRightClick(ItemStack heldStack, World currentWorld, EntityPlayer clickingPlayer) {
		// Get the object the user clicked on (raytrace) - the last 'flag' parameter controls whether or not to trace through transparent objects, with 'true' meaning it won't 
		MovingObjectPosition clickedObject = this.getMovingObjectPositionFromPlayer(currentWorld, clickingPlayer, true);
		
		// If no object was within the click-range, just return normally
		if (clickedObject == null) {
			return heldStack;
		}
		else {
			// If the user clicked on a tile of some sort
			if (clickedObject.typeOfHit == MovingObjectType.BLOCK) {
				int x = clickedObject.blockX, y = clickedObject.blockY, z = clickedObject.blockZ;
				
				// Make sure the player has permission to edit the clicked-on block, otherwise just cancel this
				if (!clickingPlayer.canPlayerEdit(x, y, z, clickedObject.sideHit, heldStack)) {
					return heldStack;
				}
				// If it's fluid blood, make it congealed and remove a coagulant
				if (currentWorld.getBlock(x, y, z) != Blocks.air) {
					if (currentWorld.getBlock(x, y, z) instanceof IFluidBlock) {
						IFluidBlock fluidBlock = (IFluidBlock)currentWorld.getBlock(x, y, z);
						if(fluidBlock.getFluid().getName().contentEquals("blood")) {
							currentWorld.setBlock(x, y, z, congealedBlock);
							if(!clickingPlayer.capabilities.isCreativeMode) {
								--heldStack.stackSize;
							}
						}
					}
				}
				return heldStack;
			}
			else {
				return heldStack;
			}
		}
	}

}
