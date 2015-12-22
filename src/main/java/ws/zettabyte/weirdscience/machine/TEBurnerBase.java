package ws.zettabyte.weirdscience.machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import ws.zettabyte.zettalib.block.TileEntityInventoryBase;
import ws.zettabyte.zettalib.initutils.Conf;
import ws.zettabyte.zettalib.initutils.Configgable;
import ws.zettabyte.zettalib.inventory.IDescriptiveInventory;
import ws.zettabyte.zettalib.inventory.IInvComponent;
import ws.zettabyte.zettalib.thermal.IHasHeatLogic;
import ws.zettabyte.zettalib.thermal.IHeatLogic;
import ws.zettabyte.zettalib.thermal.SimpleHeatLogic;

import java.util.ArrayList;

/**
 * Created by Sam "Gyro" C. on 12/21/2015.
 *
public class TECatalyticEngine  {
 */
@Configgable(section="Machine")
public abstract class TEBurnerBase extends TileEntityInventoryBase implements
        ISidedInventory, IDescriptiveInventory, IHasHeatLogic {
    protected SimpleHeatLogic heat = new SimpleHeatLogic();


    @Conf(name="Burners: Ticks between trying heat transfer logic", def="20", comment="WARNING: As of right now burner tick rate actually affects heat dissipation rate.")
    public static int ticksPerBalance; //How many ticks between attempts to balance heat?
    private int ticksUntilBalance = ticksPerBalance;

    @Override
    public IHeatLogic getHeatLogic() {
        return heat;
    }

    protected ArrayList<IInvComponent> fullComponentList = new ArrayList<IInvComponent>(); //new ArrayList<IInvComponent>(8);

    public TEBurnerBase() {
        heat.te = this;
        fullComponentList.add(heat);
        trySetupHeat();
    }
    @Override
    public Iterable<IInvComponent> getComponents() {
        return fullComponentList;
    }
    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    //---- NBT stuff ----
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        heat.readFromNBT(nbt);
        heat.initialized = true;
        ticksUntilBalance = nbt.getShort("BalanceTime");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        heat.writeToNBT(nbt);
        nbt.setShort("BalanceTime", (short)this.ticksUntilBalance);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
    }

    //------------- Update behavior -----------
    protected void trySetupHeat() {
        //Try to set our heat level to ambient biome temperature.
        if((worldObj != null) && !heat.initialized){
            //if(worldObj.isRemote) return;
            heat.setupAmbientHeat(worldObj, xCoord, yCoord, zCoord);
            this.markDirty();
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    @Override
    public void updateEntity () {
        super.updateEntity();
        trySetupHeat();

        if (!worldObj.isRemote) {
            //Burn logic:
            //Are we still waiting to burn fuel?
            if (this.ticksUntilBalance > 0) {
                --this.ticksUntilBalance;
            } else {
                heat.process();
                ArrayList<IHeatLogic> testAgainst = new ArrayList<IHeatLogic>(3);
                for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                    TileEntity t = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
                    if (t != null) {
                        if (t instanceof IHeatLogic) {
                            testAgainst.add((IHeatLogic) t);
                        }
                    }
                }

                if (testAgainst.size() != 0) {
                    this.heat.doBalance(testAgainst);
                }
                //if ((worldObj.getTotalWorldTime() % heat.getTicksPerPassiveLoss()) == 0) {
                //    this.heat.doPassiveLoss();
                //}
                this.heat.doPassiveLoss();

                if (heat.isDirty()) {
                    this.markDirty();
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                }
                //Reset our timer
                this.ticksUntilBalance = this.ticksPerBalance;
            }
        }
    }
}
