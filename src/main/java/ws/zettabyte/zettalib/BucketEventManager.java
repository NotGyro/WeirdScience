package ws.zettabyte.zettalib;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.event.entity.player.FillBucketEvent;
/**
 * A handler for Forge bucket events which also allows you to map fluid blocks
 * to the resulting filled bucket item, rather than explicitly checking for each one.
 * @author Sam "Gyro" C.
 *
 */
public class BucketEventManager {
	protected Map<IBlockState, ItemStack> fluidToBucket;
	
	public BucketEventManager() {
		fluidToBucket = new HashMap<IBlockState, ItemStack>();
	};
	
	public boolean addRecipe(IBlockState b, ItemStack bucket) {
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
				IBlockState b = event.world.getBlockState(target.getBlockPos());
				if(fluidToBucket.get(b) != null) {
					//Set our event's item to our fluid.
					event.result = fluidToBucket.get(b).copy();
					//Allow this to happen.
					event.setResult(Result.ALLOW);
					//Set the block to 0 so we don't just have infinite liquid.
					event.world.setBlockToAir(target.getBlockPos());
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