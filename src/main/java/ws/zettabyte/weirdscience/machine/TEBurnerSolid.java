package ws.zettabyte.weirdscience.machine;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import ws.zettabyte.zettalib.initutils.Conf;
import ws.zettabyte.zettalib.initutils.Configgable;
import ws.zettabyte.zettalib.inventory.ItemSlot;
import ws.zettabyte.zettalib.inventory.SimpleInvComponent;
import ws.zettabyte.zettalib.inventory.SlotInput;
import ws.zettabyte.zettalib.thermal.HeatRegistry;

import java.util.ArrayList;

/**
 * Created by Sam "Gyro" Cutlip on 12/29/2015.
 */
@Configgable(section="Machine")
public class TEBurnerSolid extends TEBurnerBase {

    protected ArrayList<ItemSlot> fuelSlots = new ArrayList<ItemSlot>(9);
    /**
     * How many ticks the associated slot will continue burning for.
     */
    public int[] burnRemain = new int[9];
    /**
     * How many ticks our last consumed item will / had burned for.
     */
    public int[] prevBurnTime = new int[9];
    /**
     * How many of these slots have "remaining fuel" (read: non-zero burnRemain)?
     */
    protected short activeCount = 0;

    @Conf(name="Burners: How hot the solid fuel burner has to be in order to self-ignite", def="200")
    protected static int ignitionHeat = 200;

    @Conf(name="Burners: mC (thousandths of degrees celsius) per furnace fuel value for Solid Fuel Burner", def="25")
    protected static int mcPerFuelTick = 25;

    //For our friends in GUI land:
    protected ArrayList<SimpleInvComponent<Float>> remainingDisplay = new ArrayList<SimpleInvComponent<Float>>(9);


    protected void setupSlots() {
        for(int i = 0; i < 9; ++i) {
            ItemSlot slot = new ItemSlot(this, i, "fuel" + i);
            fuelSlots.add(slot);
            slots.add(slot);

            SimpleInvComponent<Float> sInv = new SimpleInvComponent<Float>("slotFuel" + i);
            sInv.val = new Float(0.0F);
            remainingDisplay.add(sInv);

            this.fullComponentList.add(slot);
            this.fullComponentList.add(sInv);
        }
    }
    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setShort("ActiveSlotCount", activeCount); //Save the count
        for(int i = 0; i < 9; ++i) {
            nbt.setInteger("RemainingBurn" + i, burnRemain[i]);
            nbt.setInteger("PrevRemainingBurn" + i, prevBurnTime[i]);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        activeCount = nbt.getShort("ActiveSlotCount"); //read the count
        for(int i = 0; i < 9; ++i) {
            burnRemain[i] =  nbt.getInteger("RemainingBurn" + i);
            prevBurnTime[i] =  nbt.getInteger("PrevRemainingBurn" + i);
        }
    }

    public TEBurnerSolid() {
        super();
        setupSlots();
    }

    @Override
    public void updateEntity () {
        /* TEBurnerBase.updateEntity() handles a bunch of tick-counting logic and will call doBurnTick. However we need
         * to count ticks remaining for fuel in the fuel slots. Here: */
        if(activeCount > 0) {
            int tempUp = 0;
            for (int i = 0; i < 9; ++i) {
                //Is this, before our process, a still-burning slot?
                if (burnRemain[i] > 0) {
                    burnRemain[i] -= 1 * HeatRegistry.getInstance().burnSpeedMult;
                    //A fuel slot "goes out".
                    if (burnRemain[i] <= 0) activeCount -= 1;
                    //If they all go out, it is no longer burning.
                    //if (activeCount <= 0) burning = false;

                    tempUp += mcPerFuelTick * HeatRegistry.getInstance().burnSpeedMult; /*We have just lost a tick of burn time, add the
                                                              * conversion ratio to our temperature.*/
                }
            }
            if (!worldObj.isRemote) {
                if (tempUp != 0) {
                    heat.modifyHeat(tempUp); //Increase heat by cumulative fuel-time converted to heat.
                }
            }
            //Are there no embers in this hearth?
            if (activeCount <= 0) {
                activeCount = 0; //Prevent shenanigans
                //We are technically no longer burning, then.
                //burning = false;
                // ^ Commented out: Engine stays warm until the Burn Tick happens.
            }
            refreshProgressValues();
        }
        //We can now try the burn tick.
        super.updateEntity();
    }

    @Override
    protected void doBurnTick() {
        if(!this.canBurn()) return; //We need to ignite this thing before we can do anything of the sort.
        for(int i = 0; i < 9; ++i) tryBurnSlot(i);
        //If fuel-time ran out and there is no more fuel to burn, the fire goes out. Check for that.
        verifyBurning();
        //this.markDirty();
    }

    protected void tryBurnSlot(int sl) {
        //Only consume fuel if the previous fuel in the slot has finished burning.
        if(burnRemain[sl] == 0) {
            if(fuelSlots.get(sl) != null) {
                ItemSlot slot = fuelSlots.get(sl);
                if(slot.getStack() != null) {
                    ItemStack stack = slot.getStack();

                    /*We cannot deal with burning, say, a stack of lava buckets, so if our item has a container and
                    * we have more than one of our item, discontinue this plan. */
                    if(stack.getItem().hasContainerItem(stack) && (stack.stackSize != 1)) return;

                    int bTime = TileEntityFurnace.getItemBurnTime(stack);
                    if(bTime > 0) {
                        //Congrats, we can burn this item.
                        //Setup time properly.
                        activeCount += 1;
                        burnRemain[sl] = bTime;
                        prevBurnTime[sl] = bTime;
                        if(!this.worldObj.isRemote) {
                            //Do removal / replacement logic on burnable item
                            if (stack.getItem().getContainerItem(stack) != null) {
                                //Buckets and such
                                ItemStack replacementStack = stack.getItem().getContainerItem(stack);
                                slot.setStackForce(replacementStack);
                            } else {
                                //Coal, wood, etc.
                                slot.decrStackSize(1);
                            }
                            this.markDirty();
                        }
                    }
                }
            }
        }
    }

    protected boolean verifyBurning() {
        activeCount = 0;
        for(int i = 0; i < 9; ++i) {
            if(burnRemain[i] != 0) {
                activeCount += 1;
            }
        }
        burning = (activeCount > 0);
        return burning;
    }

    protected void refreshProgressValues() {
        for(int i = 0; i < 9; ++i) {
            if(this.prevBurnTime[i] == 0) {
                this.remainingDisplay.get(i).val = 0.0F;
            }
            else {
                this.remainingDisplay.get(i).val = ((float)this.burnRemain[i]) / ((float)this.prevBurnTime[i]);
            }
        }
    }

    @Override
    public void ignite() {
        super.ignite();
        verifyBurning(); //If we try to use an F&S or an igniter on an empty burner, don't consider this active.
    }

    //------- Inventory stuff --------
    @Override
    public String getInventoryName() {
        return "SolidFuelBurner";
    }

    @Override
    public boolean canExtractItem(int s, ItemStack stack, int fromDirection) {
        //Only allow extracting from bottom.
        if(EnumFacing.VALID_DIRECTIONS[fromDirection] != EnumFacing.DOWN) {
            return false;
        }
        else {
            return super.canExtractItem(s, stack, fromDirection);
        }
    }

    @Override
    public boolean canInsertItem(int s, ItemStack stack, int fromDirection) {
        //Is it a valid furnace fuel?
        if(stack != null) {
            int bTime = TileEntityFurnace.getItemBurnTime(stack);
            if(bTime <= 0) return false; //If no, reject it.
        }
        //If it is a valid furnace fuel, try but still respect whitelists / blacklists / etc
        return super.canInsertItem(s, stack, fromDirection);
    }

    protected boolean canBurn() {
        if(this.burning == true) return true;
        else if(this.heat.getHeat() > (this.ignitionHeat * 1000)) return true;
        return false;
    }
}
