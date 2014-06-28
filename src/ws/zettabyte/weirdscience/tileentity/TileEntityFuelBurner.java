package ws.zettabyte.weirdscience.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import ws.zettabyte.zettalib.interfaces.IRegistrable;
import ws.zettabyte.zettalib.tileentity.TileEntityGenerator;

public class TileEntityFuelBurner extends TileEntityGenerator implements IRegistrable
{
    protected int internalTemp = 0;
    @Override
    protected void init ()
    {
        super.init();
    }

    @Override
    public void writeToNBT (NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        //Write heat
        nbt.setInteger("Temperature", internalTemp);
    }

    @Override
    public void readFromNBT (NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        internalTemp = nbt.getInteger("Temperature");
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
    }

    @Override
    public void onKill ()
    {
        super.onKill();
    }
}
