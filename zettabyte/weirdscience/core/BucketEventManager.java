package zettabyte.weirdscience.core;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.FillBucketEvent;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class BucketEventManager {
	protected Map<ImmutablePair<Integer, Integer>, ItemStack> fluidToBucket;
	
	public BucketEventManager() {
		fluidToBucket = new HashMap<ImmutablePair<Integer, Integer>, ItemStack>();
	};
	
	public boolean addRecipe(Block sourceBlock, int meta, ItemStack bucket) {
		if((sourceBlock.blockID == 0) || (fluidToBucket.containsKey(new ImmutablePair<Integer, Integer>(sourceBlock.blockID, meta)))) {
			return false;
		}
		else {
			fluidToBucket.put(new ImmutablePair<Integer, Integer>(sourceBlock.blockID, meta), bucket);
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
				ImmutablePair<Integer, Integer> iPair = new ImmutablePair<Integer, Integer>(event.world.getBlockId(target.blockX, target.blockY, target.blockZ), 
						event.world.getBlockMetadata(target.blockX, target.blockY, target.blockZ));
				if(fluidToBucket.get(iPair) != null) {
					//Set our event's item to our fluid.
					event.result = fluidToBucket.get(iPair).copy();
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
