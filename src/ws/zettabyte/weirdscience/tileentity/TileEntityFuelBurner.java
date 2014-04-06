package ws.zettabyte.weirdscience.tileentity;

import ws.zettabyte.zettalib.thermal.BasicHeatLogic;
import ws.zettabyte.zettalib.thermal.MasterHeatManager;
import ws.zettabyte.zettalib.tileentity.TileEntityGenerator;
import cofh.util.BlockCoord;

public class TileEntityFuelBurner extends TileEntityGenerator
{
    BasicHeatLogic heatLogic = new BasicHeatLogic();

    @Override
    protected void init ()
    {
        super.init();
        //Set up variables for our heat logic.
        BlockCoord position = new BlockCoord(xCoord, yCoord, zCoord);
        heatLogic.setPosition(position);
        heatLogic.setWorld(worldObj);
        //Register it.
        MasterHeatManager.getInstance().getHandlerForWorld(worldObj).RegisterHeatBlock(heatLogic, xCoord, yCoord, zCoord);
        //Set base heat to ambient.
        heatLogic.setHeat(MasterHeatManager.getInstance().getHandlerForWorld(worldObj).getAmbientHeatAt(xCoord, yCoord, zCoord));
    }
}
