package ws.zettabyte.zettalib.thermal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

/**
 * A singleton (formerly known as "HeatBehaviorRegistry," name cut down for the sake of convenience)
 * providing a registry of simple in-world heat behaviors and some static utility functions for heat stuff.
 * @author Sam "Gyro" C.
 *
 */
public class Heat
{
    //Protected constructor, for laffs.
    protected Heat() { }

    protected Heat instance = new Heat();

    public Heat getInstance () {
        return instance;
    }

    /* 
     * Basically a functor: Contains some data and a routine to carry out when its criteria are met.
     * DoBehavior is called on the target block *through* this class but caused *by* another - there
     * isn't any central heat manager enforcing heat behavior globally, rather, certain blocks will
     * cause this behavior in other blocks and entities adjacent.
     */
    protected class BlockHeatBehavior {
        public int upperTemp, lowerTemp = 0;

        //Returns how much heat has been "consumed" in an endothermic process (negative for produced in exothermic)
        public int DoBehavior (World world, int x, int y, int z, int temperature) {
            return 0;
        } 
    }

    /* 
     * Basically a functor: Contains some data and a routine to carry out when its criteria are met.
     * DoBehavior is called on the target entity *through* this class but caused *by* another - there
     * isn't any central heat manager enforcing heat behavior globally, rather, certain blocks will
     * cause this behavior in other blocks and entities adjacent.
     */
    protected class EntityHeatBehavior
    {
        public int upperTemp, lowerTemp = 0;

        //Returns how much heat has been "consumed" in an endothermic process (negative for produced in exothermic)
        public int DoBehavior (Entity ent, int temperature)
        {
            return 0;
        }
    }

    /* 
     * ID-dependent code that will have to be changed for 1.7.x, watch out.
     * 
     * (p.s. Java now has the distinction of being more temperamental than C++, 
     * in which vector<BlockHeatBehavior>* blockBehavior = new vector<BlockHeatBehavior>[4096]; would totally work.
     */
    private HashMap<Block, ArrayList<BlockHeatBehavior>> blockBehavior = new HashMap<Block, ArrayList<BlockHeatBehavior>>();

    //Temperature is in degrees celsius.
    public int TryBlockBehaviorAt (World world, int x, int y, int z, int temperature)
    {
        ArrayList<BlockHeatBehavior> listbhb = blockBehavior.get(world.getBlock(x, y, z));
        if (listbhb != null)
        {
            for (BlockHeatBehavior bhb : listbhb)
            {
                //Are we within the range for this behavior?
                if ((bhb.lowerTemp < temperature) && (bhb.upperTemp > temperature))
                {
                    return bhb.DoBehavior(world, x, y, z, temperature);
                }
            }
        }
        //Default, fall-through condition is to return 0.
        return 0;
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

    //Mapping of entity ID to heat behavior list.
    HashMap<UUID, ArrayList<EntityHeatBehavior>> entityBehavior = new HashMap<UUID, ArrayList<EntityHeatBehavior>>(8);

    //Temperature is in degrees celsius.
    public int TryEntityBehavior (Entity ent, int temperature)
    {
        ArrayList<EntityHeatBehavior> listbehv = entityBehavior.get(ent.getUniqueID());
        if (listbehv != null)
        {
            for (EntityHeatBehavior behv : listbehv)
            {
                //Are we within the range for this behavior?
                if ((behv.lowerTemp < temperature) && (behv.upperTemp > temperature))
                {
                    return behv.DoBehavior(ent, temperature);
                }
            }
        }
        //Default, fall-through condition is to return 0.
        return 0;
    }

    //Entity ID dependent code.
    public void AddEntityBehavior (Entity ent, EntityHeatBehavior behv)
    {
        if (entityBehavior.get(ent.getUniqueID()) == null)
        {
            entityBehavior.put(ent.getUniqueID(), (new ArrayList<EntityHeatBehavior>(1)));
        }

        entityBehavior.get(ent.getUniqueID()).add(behv);
    }
    
    public static void doBalance(IHeatLogic a, IHeatLogic b) {
    	
    }
}
