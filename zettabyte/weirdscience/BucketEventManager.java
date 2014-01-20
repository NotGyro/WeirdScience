package zettabyte.weirdscience;

import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.FillBucketEvent;

public class BucketEventManager {
	@ForgeSubscribe
	public void bucketFill(FillBucketEvent event) {
		ItemStack result;
		MovingObjectPosition target = event.target;
		if (event.world.getBlockId(target.blockX, target.blockY, target.blockZ) == WeirdScience.instance.fluidBloodBlock.blockID) {
			event.world.setBlock(target.blockX, target.blockY, target.blockZ, 0);
			result = new ItemStack(WeirdScience.instance.itemBloodBucket);
		}
		else if (event.world.getBlockId(target.blockX, target.blockY, target.blockZ) == WeirdScience.instance.fluidAcidBlock.blockID) {
			event.world.setBlock(target.blockX, target.blockY, target.blockZ, 0);
			result = new ItemStack(WeirdScience.instance.itemAcidBucket);
		}
		else {
			//Nothing we recognize.
			return;
		}
		event.result = result;
		event.setResult(Result.ALLOW);
	}
}
