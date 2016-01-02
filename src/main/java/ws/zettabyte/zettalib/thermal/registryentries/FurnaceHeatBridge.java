package ws.zettabyte.zettalib.thermal.registryentries;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;
import ws.zettabyte.zettalib.initutils.Configgable;
import ws.zettabyte.zettalib.thermal.HeatRegistry;
import ws.zettabyte.zettalib.thermal.IHeatLogic;

/**
 * Created by Sam "Gyro" C. on 1/2/2016.
 */
public class FurnaceHeatBridge extends BlockHeatBehavior {
    public FurnaceHeatBridge() {
    }

    public static int maxFurnaceBurnTime = 800;
    public static int ratio = 4*HeatRegistry.burnSpeedMult; //ratio heat units to one furnace burn tick

    @Override
    public boolean canPerform(World world, int x, int y, int z) {
        Block b = world.getBlock(x,y,z);
        if(b == null) return false;
        if((b != Blocks.furnace) && (b != Blocks.lit_furnace)) return false;
        return true;
    }

    @Override
    public boolean doBehavior(World world, int x, int y, int z, IHeatLogic heatSource) {
        if(!canPerform(world,x,y,z)) return false;

        TileEntity te = world.getTileEntity(x,y,z);
        if(te == null) return false;
        if(!(te instanceof TileEntityFurnace)) return false;
        TileEntityFurnace furnace = (TileEntityFurnace) te;

        if(furnace.furnaceBurnTime > this.maxFurnaceBurnTime) return false; //Do not go over max.

        if(heatSource == null) return false;

        int availHeat = heatSource.getHeat() - heatSource.getAmbientHeat();

        if(availHeat <= 0) return false;
        if(availHeat <= (furnace.furnaceBurnTime*ratio)) return false;

        int diff = availHeat - (furnace.furnaceBurnTime*ratio);
        int transferable = Math.min(Math.min(diff, availHeat), heatSource.getHeatTransferRate());
        if((transferable/ratio) > furnace.furnaceBurnTime) {
            if(furnace.currentItemBurnTime == 0) {
                furnace.currentItemBurnTime = maxFurnaceBurnTime;
            }
            furnace.furnaceBurnTime += transferable/ratio;
            heatSource.modifyHeat(-transferable);
            return true;
        }
        else {
            return false;
        }
    }
}
