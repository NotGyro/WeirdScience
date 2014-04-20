package ws.zettabyte.weirdscience.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import ws.zettabyte.zettalib.interfaces.IRegistrable;
import ws.zettabyte.zettalib.thermal.BasicHeatLogic;
import ws.zettabyte.zettalib.thermal.MasterHeatManager;
import ws.zettabyte.zettalib.tileentity.TileEntityGenerator;
import cofh.util.BlockCoord;

public class TileEntityFuelBurner extends TileEntityGenerator implements IRegistrable
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

    @Override
    public void writeToNBT (NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        //Write heat
        nbt.setInteger("Temperature", heatLogic.getHeat());
    }

    @Override
    public void readFromNBT (NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        heatLogic.setHeat(nbt.getInteger("Temperature"));
    }

    @Override
    public String getEnglishName ()
    {
        return "Fuel Burner";
    }

    @Override
    public String getGameRegistryName ()
    {
        return "tileEntityFuelBurner";
    }

    @Override
    public boolean isEnabled ()
    {
        return true;
    }

    @Override
    public void onChunkUnload ()
    {
        super.onChunkUnload();
        MasterHeatManager.getInstance().getHandlerForWorld(worldObj).UnregisterHeatBlock(heatLogic, xCoord, yCoord, zCoord);
    }

    @Override
    public void onKill ()
    {
        super.onKill();
        MasterHeatManager.getInstance().getHandlerForWorld(worldObj).UnregisterHeatBlock(heatLogic, xCoord, yCoord, zCoord);
    }
}
