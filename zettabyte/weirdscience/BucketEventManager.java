package zettabyte.weirdscience;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.FillBucketEvent;

public class BucketEventManager {
	protected Map<Integer, ItemStack> fluidToBucket;
	
	public BucketEventManager() {
		fluidToBucket = new HashMap<Integer, ItemStack>();
	};
	
	public boolean addRecipe(Integer id, ItemStack bucket) {
		if((id == 0) || (fluidToBucket.containsKey(id))) {
			return false;
		}
		else {
			fluidToBucket.put(id, bucket);
			return true;
		}
	};
	@ForgeSubscribe
	public void bucketFill(FillBucketEvent event) {
		//Prevent stupid shit in single player.
		if(!event.world.isRemote){
			MovingObjectPosition target = event.target;
			//Have we clicked a tile with an empty bucket?
			if (event.current.getItem() == Item.bucketEmpty && event.target.typeOfHit == EnumMovingObjectType.TILE) {
				//Is there an entry for this block's ID?
				if(fluidToBucket.get(event.world.getBlockId(target.blockX, target.blockY, target.blockZ)) != null) {
					//Set our event's item to our fluid.
					event.result = fluidToBucket.get(event.world.getBlockId(target.blockX, target.blockY, target.blockZ)).copy();
					//Allow this to happen.
					event.setResult(Result.ALLOW);
					//Set the block to 0 so we don't just have infinite liquid.
					event.world.setBlock(target.blockX, target.blockY, target.blockZ, 0);
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
