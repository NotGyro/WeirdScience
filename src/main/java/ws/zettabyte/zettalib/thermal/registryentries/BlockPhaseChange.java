package ws.zettabyte.zettalib.thermal.registryentries;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import ws.zettabyte.zettalib.thermal.HeatRegistry;
import ws.zettabyte.zettalib.thermal.IHeatLogic;

/**
 * Created by Sam "Gyro" Cutlip on 1/2/2016.
 */
public class BlockPhaseChange extends BlockHeatBehavior {
    public int targetHeat = 100000; //100C
    public boolean rising = true; //Our temperature has to rise for this behavior to take place. If false: Falling

    public IBlockState from = null;
    public IBlockState to = null;

    public int getBlockHeat(World world, BlockPos pos) {
        int ambient = HeatRegistry.celsiusFromBiome(world.getBiomeGenForCoords(pos).temperature) * 1000;
        return ambient + HeatRegistry.getInstance().getBlockHeat(world.getBlockState(pos));
    }

    public boolean canPerform(World world, BlockPos pos) {
        return true;
    }
    protected void change(World world, BlockPos pos) {
        world.setBlockState(pos, to, 1 | 2 | 4);
    }
    public boolean doBehavior(World world, BlockPos pos, IHeatLogic heatSource) {
        if(!this.canPerform(world, pos)) return false;
        int ourHeat = getBlockHeat(world,pos);
        int requiredChange = this.targetHeat - ourHeat;
        int hsHeat = heatSource.getHeat(); // + heatSource.getAmbientHeat()
        if(rising) {
            if(requiredChange <= 0) return false;
            if(hsHeat < ourHeat) return false;
            if((hsHeat - ourHeat) < requiredChange) return false;
        }
        else {
            if(requiredChange >= 0) return false;
            if(hsHeat > ourHeat) return false;
            if((ourHeat + hsHeat) > requiredChange) return false;
        }
        //At this point, we know that the heat source is appropriately hotter or colder than our block.
        heatSource.modifyHeat(-requiredChange);
        this.change(world,pos);
        return true;
    }
    public BlockPhaseChange() {}
}
