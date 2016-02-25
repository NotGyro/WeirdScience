package ws.zettabyte.zettalib.thermal.registryentries;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import ws.zettabyte.zettalib.thermal.IHeatLogic;

/**
 * Created by Sam "Gyro" C. on 1/2/2016.
 */
public class BlockHeatBehavior {

    //Returns whether or not we could actually do anything here
    public boolean doBehavior(World world, BlockPos pos, IHeatLogic heatSource) {
        return false;
    }
    public boolean canPerform (World world, BlockPos pos) {return false;}
}