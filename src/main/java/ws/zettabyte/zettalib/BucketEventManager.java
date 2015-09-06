package ws.zettabyte.zettalib;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.event.entity.player.FillBucketEvent;

public class BucketEventManager {
	protected Map<Block, ItemStack> fluidToBucket;
	
	public BucketEventManager() {
		fluidToBucket = new HashMap<Block, ItemStack>();
	};
	
	public boolean addRecipe(Block b, ItemStack bucket) {
		if((b == null) || (fluidToBucket.containsKey(b))) {
			return false;
		}
		else {
			fluidToBucket.put(b, bucket);
			return true;
		}
	};
	@SubscribeEvent
	public void bucketFill(FillBucketEvent event) {
		//Prevent stupid shit in single player.
		if(!event.world.isRemote){
			MovingObjectPosition target = event.target;
			//Have we clicked a tile with an empty bucket?
			if (event.current.getItem() == Items.bucket && event.target.typeOfHit == MovingObjectType.BLOCK) {
				//Is there an entry for this block's ID?
				if(fluidToBucket.get(event.world.getBlock(target.blockX, target.blockY, target.blockZ)) != null) {
					//Set our event's item to our fluid.
					event.result = fluidToBucket.get(event.world.getBlock(target.blockX, target.blockY, target.blockZ)).copy();
					//Allow this to happen.
					event.setResult(Result.ALLOW);
					//Set the block to 0 so we don't just have infinite liquid.
					event.world.setBlockToAir(target.blockX, target.blockY, target.blockZ);
				}
				else {
					//Nothing we recognize.
					return;
				}
			}
			else {
				//Not an acceptable event.
				return;
			}
		}
	}
}