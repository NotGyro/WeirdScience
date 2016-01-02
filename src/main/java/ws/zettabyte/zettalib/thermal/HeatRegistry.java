package ws.zettabyte.zettalib.thermal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import ws.zettabyte.zettalib.thermal.registryentries.BlockHeatBehavior;

/**
 * A singleton (formerly known as "HeatBehaviorRegistry," name cut down for the sake of convenience)
 * providing a registry of simple in-world heat behaviors and some static utility functions for heat stuff.
 * @author Sam "Gyro" C.
 *
 */
public class HeatRegistry
{
    //Protected constructor, for laffs.
    protected HeatRegistry() { }

    protected static HeatRegistry instance = new HeatRegistry();

    public static HeatRegistry getInstance () {
        return instance;
    }

    /*
     * These values result in Plains with an ambient temperature of 18.8 C
     * and Desert with 62 C. Good enough for now.
     */
    protected static final float biomeToCelsius = 24.0F;
    protected static final float biomeCelsOffset = -2.0F;
    public static int celsiusFromBiome(float in) {
        return (int) ((biomeToCelsius * in) + biomeCelsOffset);
    };


    //@Conf(name="Burners: Solid burner speed multiplier - generates heat & consumes fuel this many times as fast.", def="2")
    public static int burnSpeedMult = 2;

    /*
     * (p.s. Java now has the distinction of being more temperamental than C++,
     * in which vector<BlockHeatBehavior>* blockBehavior = new vector<BlockHeatBehavior>[4096]; would totally work.
     */
    private HashMap<Block, ArrayList<BlockHeatBehavior>> blockBehavior = new HashMap<Block, ArrayList<BlockHeatBehavior>>();

    //Maps blocks to amount of passive heat, e.g. steam -> 100 C. Is an offset from biome ambient temp.
    private HashMap<Block, Integer> blockHeats = new HashMap<Block, Integer>();

    public void registerBlockHeat(Block b, int i) {
        blockHeats.put(b, i);
    }
    public int getBlockHeat(Block b) {
        if(blockHeats.containsKey(b)) {
            return blockHeats.get(b);
        }
        else {
            return 0; //By default: same temperature as biome ambient
        }
    }

    // ---- Entry classes ----
    /* 
     * Basically a functor: Contains some data and a routine to carry out when its criteria are met.
     * DoBehavior is called on the target block *through* this class but caused *by* another - there
     * isn't any central heat manager enforcing heat behavior globally, rather, certain blocks will
     * cause this behavior in other blocks and entities adjacent.
     */

    //Temperature is in thousandths of a degrees celsius.
    public void TryBlockBehaviorAt (World world, int x, int y, int z, IHeatLogic heatSource) {
        ArrayList<BlockHeatBehavior> listbhb = blockBehavior.get(world.getBlock(x, y, z));
        if (listbhb != null) {
            for (BlockHeatBehavior bhb : listbhb) {
                bhb.doBehavior(world, x, y, z, heatSource);
            }
        }
    }

    //More Block ID dependent code.
    public void AddBlockBehavior (Block block, BlockHeatBehavior bhb)
    {
        if (blockBehavior.get(block) == null)
        {
            blockBehavior.put(block, new ArrayList<BlockHeatBehavior>());
        }

        blockBehavior.get(block).add(bhb);
    }
}
