package ws.zettabyte.ferretlib;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import ws.zettabyte.ferretlib.block.IDebuggableBlock;
import ws.zettabyte.ferretlib.initutils.ICreativeTabInfo;

public class ItemDebugStick extends Item implements ICreativeTabInfo {

	public ItemDebugStick() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public ItemStack onItemRightClick(ItemStack heldStack, World world, EntityPlayer clickingPlayer) {
		// Get the object the user clicked on (raytrace) - the last 'flag' parameter controls whether or not to trace through transparent objects, with 'true' meaning it won't 
		MovingObjectPosition clickedObject = this.getMovingObjectPositionFromPlayer(world, clickingPlayer, true);
		
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
				Block b = world.getBlock(x, y, z);
				if(b == null) return heldStack;
				int meta = world.getBlockMetadata(x, y, z);
				System.out.println(b.getUnlocalizedName());
				System.out.println("Metadata: " + meta);
				if (b instanceof IDebuggableBlock) {
					System.out.println(((IDebuggableBlock)b).getDebugInfo(world, x, y, z, meta));
				}
				return heldStack;
			}
			else {
				return heldStack;
			}
		}
	}
}
