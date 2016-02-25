package ws.zettabyte.zettalib.thermal.registryentries;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.BlockPos;
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
    public boolean canPerform(World world, BlockPos pos) {
        IBlockState b = world.getBlockState(pos);
        if(b == null) return false;
        if((b != Blocks.furnace) && (b != Blocks.lit_furnace)) return false;
        return true;
    }

    @Override
    public boolean doBehavior(World world, BlockPos pos, IHeatLogic heatSource) {
        if(!canPerform(world,pos)) return false;

        TileEntity te = world.getTileEntity(pos);
        if(te == null) return false;
        if(!(te instanceof TileEntityFurnace)) return false;
        TileEntityFurnace furnace = (TileEntityFurnace) te;
        /* Why would you suddenly and for no reason make this private..?
        //TODO: reflection code to get this working again
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
        }*/
        return false;
    }
}
