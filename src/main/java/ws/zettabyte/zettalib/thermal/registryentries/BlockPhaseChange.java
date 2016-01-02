package ws.zettabyte.zettalib.thermal.registryentries;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import ws.zettabyte.zettalib.thermal.HeatRegistry;
import ws.zettabyte.zettalib.thermal.IHeatLogic;

/**
 * Created by Sam "Gyro" Cutlip on 1/2/2016.
 */
public class BlockPhaseChange extends BlockHeatBehavior {
    public int targetHeat = 100000; //100C
    public boolean rising = true; //Our temperature has to rise for this behavior to take place. If false: Falling

    public boolean metaSensitive = false;
    public boolean toMetaSensitive = false;
    public short fromMeta = 0;
    public short toMeta = 0;
    public Block from = null;
    public Block to = null;

    public int getBlockHeat(World world, int x, int y, int z) {
        int ambient = HeatRegistry.celsiusFromBiome(world.getBiomeGenForCoords(x, z).temperature) * 1000;
        return ambient + HeatRegistry.getInstance().getBlockHeat(world.getBlock(x,y,z));
    }

    public boolean canPerform(World world, int x, int y, int z) {
        //Right metadata?
        if(this.metaSensitive) {
            if(world.getBlockMetadata(x,y,z) != fromMeta) return false;
        }
        return true;
    }
    protected void change(World world, int x, int y, int z) {
        if(toMetaSensitive) {
            world.setBlock(x,y,z, to, toMeta, 1|2|4);
        }
        else {
            world.setBlock(x, y, z, to);
        }
    }
    public boolean doBehavior(World world, int x, int y, int z, IHeatLogic heatSource) {
        if(!this.canPerform(world, x, y, z)) return false;
        int ourHeat = getBlockHeat(world,x,y,z);
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
        this.change(world,x,y,z);
        return true;
    }
    public BlockPhaseChange() {}
}
