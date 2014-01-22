package zettabyte.weirdscience.item;

import zettabyte.weirdscience.CreativeTabWeirdScience;
import zettabyte.weirdscience.WeirdScience;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class Coagulant extends Item {
	public static final int BONEMEAL_ID = 15;
	
	public static final int craftCount = 2;
	public static Object[] recipe = {new ItemStack(Item.dyePowder, 1, BONEMEAL_ID), Item.wheat};
	
	public Coagulant(int par1) {
		super(par1);
		
		setCreativeTab(WeirdScience.tabWeirdScience);
		setUnlocalizedName("coagulant");
	}
	
	@Override
	public void registerIcons(IconRegister reg) {
		if (this.itemID == WeirdScience.coagulant.itemID) {
			this.itemIcon = reg.registerIcon("weirdscience:coagulant");
		}
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
			if (clickedObject.typeOfHit == EnumMovingObjectType.TILE) {
				int x = clickedObject.blockX, y = clickedObject.blockY, z = clickedObject.blockZ;
				
				// Make sure the player has permission to edit the clicked-on block, otherwise just cancel this
				if (!clickingPlayer.canPlayerEdit(x, y, z, clickedObject.sideHit, heldStack)) {
					return heldStack;
				}
				// If it's fluid blood, make it congealed and remove a coagulant
				if (currentWorld.getBlockId(x, y, z) == WeirdScience.fluidBloodBlock.blockID) {
					currentWorld.setBlock(x, y, z, WeirdScience.congealedBloodBlock.blockID);
					--heldStack.stackSize;
				}
				return heldStack;
			}
			else {
				return heldStack;
			}
		}
	}

}
