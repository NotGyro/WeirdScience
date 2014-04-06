package ws.zettabyte.weirdscience.tileentity;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import ws.zettabyte.weirdscience.block.IBlockMetaPower;
import ws.zettabyte.zettalib.ContentRegistry;
import ws.zettabyte.zettalib.SolidFuelInfo;
import ws.zettabyte.zettalib.fluid.BlockGasBase;
import ws.zettabyte.zettalib.interfaces.IConfiggable;
import ws.zettabyte.zettalib.interfaces.IDeferredInit;
import ws.zettabyte.zettalib.interfaces.IRegistrable;
import ws.zettabyte.zettalib.interfaces.ISolidFuelInfo;
import ws.zettabyte.zettalib.tileentity.TileEntitySolidFueled;
import cofh.api.energy.IEnergyHandler;
import cofh.api.tileentity.IEnergyInfo;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

//import static java.lang.System.out;

public class TileEntityNitrateEngine extends TileEntitySolidFueled implements IEnergyHandler, IEnergyInfo, ISidedInventory, IFluidHandler, IFluidTank, IConfiggable, IRegistrable, IDeferredInit
{
    private static final int[] accessibleSlots = new int[] { 0, 1 };

    /**
    * The ItemStacks that hold the items currently being used in the furnace
    */
    private ItemStack[] engineItemStacks = new ItemStack[2];

    private final Random itemDropRand = new Random(); //Randomize item drop direction.

    public static int rfPerTick;
    public static int rfPerDirt;
    public static int quantityPerBurn; //Amount of dirt to attempt to consume at once.
    public static int ticksPerBurn; //Time between ticks where we burn dirt. To reduce lag.
    //private static int energy;
    private static int staticEnergyCap;
    public static float explosionStrength = 4.0F;
    protected static int wasteCapacity;
    protected static int ticksPerExhaust; //How long until we try to spawn smog?
    protected static int exhaustPerDirt;
    protected static int exhaustPerGrass;
    protected static int exhaustPerMycelium;

    public static Fluid waste = null;
    private static ArrayList<ISolidFuelInfo> staticFuelInfo = new ArrayList<ISolidFuelInfo>(3);

    //Fuel config.
    static boolean enableDirt = true;
    static SolidFuelInfo dirtFuel;
    static boolean enableGrassyDirt = true;
    static SolidFuelInfo grassyDirtFuel;
    static boolean enableMycelium = true;
    static SolidFuelInfo myceliumFuel;

    //Burning Natura nether dirts / sands / etc.
    static boolean enableNaturaCompat = true;
    static boolean enableTaintedSoil = true;
    static boolean enableHeatSand = true;
    static SolidFuelInfo taintedSoilFuel;
    static SolidFuelInfo heatSandFuel;
    protected static int exhaustPerHeatSand;
    protected static int exhaustPerTaintedSoil;

    private int ticksUntilBurn = ticksPerBurn;
    private boolean wasRunningLastBurn = false;

    public TileEntityNitrateEngine()
    {
        super();
        setEnergyCapacity(staticEnergyCap);
        setEnergyTransferRate(rfPerTick);
        ticksUntilBurn = ticksPerBurn;
        energy = 0;
        //Tell our instance everything it needs to know.
        fuelInfo.addAll(staticFuelInfo);
    }

    //Adds a fuel.
    @Override
    public void addSolidFuelInfo (ISolidFuelInfo isf)
    {
        if (isf != null)
        {
            staticFuelInfo.add(isf);
        }
    }

    protected FluidStack fluidTank;

    public static void setWaste (Fluid b)
    {
        waste = b;
    }

    public void setWasteCapacity (int amt)
    {
        wasteCapacity = amt;
    }

    public void setTicksPerExhaust (int amt)
    {
        ticksPerExhaust = amt;
    }

    @Override
    public int getSizeInventory ()
    {
        return this.engineItemStacks.length;
    }

    @Override
    public ItemStack getStackInSlot (int slotID)
    {
        return this.engineItemStacks[slotID];
    }

    @Override
    public ItemStack decrStackSize (int slotID, int amount)
    {
        //Attempts to remove amount from slot # slotID. Returns a usable
        //itemstack taken out of the slot: Split or just yanked.
        if (this.engineItemStacks[slotID] != null)
        {
            ItemStack itemstack;

            if (this.engineItemStacks[slotID].stackSize <= amount)
            {
                itemstack = this.engineItemStacks[slotID];
                this.engineItemStacks[slotID] = null;
                return itemstack;
            }
            else
            {
                itemstack = this.engineItemStacks[slotID].splitStack(amount);

                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing (int slotID)
    {
        //Used to get items when block is broken.
        //(I think)
        if (this.engineItemStacks[slotID] != null)
        {
            ItemStack itemstack = this.engineItemStacks[slotID];
            this.engineItemStacks[slotID] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents (int slotID, ItemStack itemstack)
    {
        this.engineItemStacks[slotID] = itemstack;

        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
        {
            itemstack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public String getInvName ()
    {
        return "NitrateEngine";
    }

    @Override
    public boolean isInvNameLocalized ()
    {
        // TODO
        return false;
    }

    @Override
    public int getInventoryStackLimit ()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer (EntityPlayer entityplayer)
    { //Sanity checks!
        if (entityplayer.getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D) <= 16.0D)
        {
            return true; //The player is sufficiently close.
        }
        else
        {
            return false; //The player is too far away.
        }
    }

    public void openChest ()
    {
    }

    public void closeChest ()
    {
    }

    public boolean isItemFuel (Item item)
    {
        if (canBurn(new ItemStack(item)) != null)
        {
            //Vanilla item ID / block ID #3 is dirt.
            //Using this rather than the class for Chisel compat.
            //(Chisel hijacks the same ID for a new class
            //for dirt)
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean isItemWaste (Item item)
    {
        for (ISolidFuelInfo fuel : staticFuelInfo)
        {
            if (fuel.getByproduct().getItem().getUnlocalizedName().contentEquals(item.getUnlocalizedName()))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isItemValidForSlot (int slotID, ItemStack itemstack)
    {
        //Only allow inserting into the input slot, and only allow
        //fuel to be inserted.
        if (isItemFuel(itemstack.getItem()) && (slotID == 0))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public int[] getAccessibleSlotsFromSide (int side)
    {
        return accessibleSlots;
    }

    @Override
    public boolean canInsertItem (int slotID, ItemStack itemstack, int direction)
    {
        return isItemValidForSlot(slotID, itemstack);
    }

    @Override
    public boolean canExtractItem (int slotID, ItemStack itemstack, int direction)
    {
        if (slotID == 1)
        {
            //Only allow removing from the output slot.
            //Causes hoppers and item pipes to act clever.
            return true;
        }
        else
        {
            return false;
        }
    }

    //NBT stuff
    @Override
    public void readFromNBT (NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        //Read the item stacks.
        NBTTagList nbttaglist = nbt.getTagList("Items");
        this.engineItemStacks = new ItemStack[this.getSizeInventory()];
        NBTTagCompound nbttagcompound1 = null;
        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.engineItemStacks.length)
            {
                this.engineItemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
        //Simple behavior for performance reasons: If there's fuel in the slot, assume the engine was running.
        if (this.engineItemStacks[0] != null)
        {
            this.wasRunningLastBurn = true;
        }
        //...otherwise, assume it was not.
        else
        {
            this.wasRunningLastBurn = false;
        }
        //Read how far we are from doing another engine tick.
        this.ticksUntilBurn = nbt.getShort("BurnTime");

        //Read the internal fluid tank for smog storage
        if (!nbt.hasKey("Empty"))
        {
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(nbt);

            if (fluid != null)
            {
                fluidTank = fluid;
            }
        }
    }

    @Override
    public void writeToNBT (NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        //Write time until next engine burn tick.
        nbt.setShort("BurnTime", (short) this.ticksUntilBurn);
        //Write energy
        //Write item stacks.
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.engineItemStacks.length; ++i)
        {
            if (this.engineItemStacks[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                this.engineItemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
        nbt.setTag("Items", nbttaglist);
        //Write our internal fluid tank (which stores smog)
        if (fluidTank != null)
        {
            fluidTank.writeToNBT(nbt);
        }
        else
        {
            nbt.setString("Empty", "");
        }
    }

    public Packet getDescriptionPacket ()
    { //Very Complex And Difficult Network Code
        NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, nbt);
    }

    @Override
    public int fill (ForgeDirection from, FluidStack resource, boolean doFill)
    {
        // This is not a fluid tank.
        return 0;
    }

    //FLUID CODE:
    @Override
    public FluidStack drain (ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        if (resource.isFluidEqual(fluidTank))
        {
            return drain(from, resource.amount, doDrain);
        }
        else
        {
            return null;
        }
    }

    @Override
    public FluidStack drain (ForgeDirection from, int maxDrain, boolean doDrain)
    {
        return drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill (ForgeDirection from, Fluid fluid)
    {
        // This is still not a fluid tank.
        return false;
    }

    @Override
    public boolean canDrain (ForgeDirection from, Fluid fluid)
    {
        return true;
    }

    @Override
    public FluidTankInfo[] getTankInfo (ForgeDirection from)
    {
        return new FluidTankInfo[] { getInfo() };
    }

    @Override
    public FluidStack getFluid ()
    {
        return fluidTank;
    }

    @Override
    public int getFluidAmount ()
    {
        return fluidTank.amount;
    }

    @Override
    public int getCapacity ()
    {
        return wasteCapacity;
    }

    @Override
    public FluidTankInfo getInfo ()
    {
        return new FluidTankInfo(this);
    }

    @Override
    public int fill (FluidStack resource, boolean doFill)
    {
        return 0;
    }

    @Override
    public FluidStack drain (int maxDrain, boolean doDrain)
    {
        if (fluidTank == null)
        {
            return null;
        }

        int drained = maxDrain;
        if (fluidTank.amount < drained)
        {
            drained = fluidTank.amount;
        }

        FluidStack stack = new FluidStack(fluidTank, drained);
        if (doDrain)
        {
            fluidTank.amount -= drained;
            if (fluidTank.amount <= 0)
            {
                fluidTank = null;
            }
            FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(fluidTank, this.worldObj, this.xCoord, this.yCoord, this.zCoord, this));
        }
        return stack;
    }

    //ENERGY CODE:

    @Override
    public int getEnergyPerTick ()
    {
        return rfPerDirt;
    }

    @Override
    public int getMaxEnergyPerTick ()
    {
        return rfPerTick;
    }

    @Override
    public int getEnergy ()
    {
        return energy;
    }

    @Override
    public int getMaxEnergy ()
    {
        return staticEnergyCap;
    }

    @Override
    public int receiveEnergy (ForgeDirection from, int maxReceive, boolean simulate)
    {
        // This isn't a battery.
        return 0;
    }

    @Override
    public int extractEnergy (ForgeDirection from, int maxExtract, boolean simulate)
    {
        if (!simulate)
        {
            if (energy < maxExtract)
            {
                maxExtract = energy;
                energy = 0;
                return maxExtract;
            }
            else
            {
                energy -= maxExtract;
                return maxExtract;
            }
        }
        else
        {
            if (energy < maxExtract)
            {
                return energy;
            }
            else
            {
                return maxExtract;
            }
        }
    }

    @Override
    public boolean canInterface (ForgeDirection from)
    {
        return true;
    }

    @Override
    public int getEnergyStored (ForgeDirection from)
    {
        return energy;
    }

    @Override
    public int getMaxEnergyStored (ForgeDirection from)
    {
        return staticEnergyCap;
    }

    //ENTITY UPDATE:
    @Override
    public void updateEntity () //The meat of our block.
    {
        super.updateEntity();
        boolean flagInvChanged = false;
        if (!worldObj.isRemote)
        {
            //Burn logic:
            //Are we still waiting to burn fuel?
            boolean flagHasPower = energy > 0;
            int smogProduced = 0;
            if (this.ticksUntilBurn > 0)
            {
                --this.ticksUntilBurn;
            }
            else
            {
                //If we are not waiting, update the entity.
                int toBurn = 0;
                //Make sure we have fuel, somewhere to put waste products, and energy storage capacity.

                if (this.engineItemStacks[0] != null)
                {
                    if (this.engineItemStacks[0].stackSize >= 1)
                    {
                        //Do the burny thing.
                        int deltaItems = doBurn(this.engineItemStacks[0], quantityPerBurn);
                        this.engineItemStacks[0].stackSize -= deltaItems;
                        flagHasPower = (deltaItems != 0);
                        flagInvChanged = flagHasPower;
                        if (this.engineItemStacks[0].stackSize <= 0)
                        {
                            this.engineItemStacks[0] = null;
                        }
                        if (deltaItems != 0)
                        {
                            TurnBlockOn();
                        }
                        else
                        {
                            TurnBlockOff();
                        }
                    }
                    else
                    {
                        TurnBlockOff();
                    }
                    ticksUntilBurn = ticksPerBurn; //Reset the timer, but only if we did anything.
                }
                else
                {
                    TurnBlockOff();
                }
            }
            //And now, attempt to charge surrounding blocks.
            if (flagHasPower)
            {
                this.powerAdjacent();
            }
            //Attempt to dump tank into surrounding Forge fluid handlers.
            if (fluidTank != null)
            {
                ForgeDirection dir;
                IFluidHandler adjFluidHandler;
                for (int i = 0; i < 6; ++i)
                {
                    dir = ForgeDirection.VALID_DIRECTIONS[i];
                    adjFluidHandler = this.adjFluidHandlers[i];
                    if (adjFluidHandler != null)
                    {
                        FluidStack toDrain = new FluidStack(fluidTank.getFluid(), fluidTank.amount);
                        drain(adjFluidHandler.fill(dir.getOpposite(), toDrain, true), true);

                        if (fluidTank == null)
                        {
                            break;
                        }
                    }
                }
            }
        }

        if (flagInvChanged)
        {
            this.onInventoryChanged();
        }
    }

    private void TurnBlockOff ()
    {
        if (wasRunningLastBurn == true)
        {
            Block block = Block.blocksList[worldObj.getBlockId(xCoord, yCoord, zCoord)];
            if (block instanceof IBlockMetaPower)
            {
                ((IBlockMetaPower) block).recievePowerOff(worldObj, xCoord, yCoord, zCoord);
            }
        }
        wasRunningLastBurn = false;
    }

    private void TurnBlockOn ()
    {
        if (wasRunningLastBurn == false)
        {
            Block block = Block.blocksList[worldObj.getBlockId(xCoord, yCoord, zCoord)];
            if (block instanceof IBlockMetaPower)
            {
                ((IBlockMetaPower) block).recievePowerOn(worldObj, xCoord, yCoord, zCoord);
            }
        }
        wasRunningLastBurn = true;
    }

    @Override
    public void receiveByproduct (ItemStack byproductStack)
    {
        if (byproductStack == null)
        {
            //If we're just receiving a null value, do nothing with it.
            return;
        }
        else if (engineItemStacks[1] == null)
        { //Make sure we have a sand stack to add to.
            engineItemStacks[1] = byproductStack;
        }
        else
        {
            //Only do the simple behavior if we've got two item stacks of the same type and added items will fit.
            if ((engineItemStacks[1].itemID == byproductStack.itemID) && ((engineItemStacks[1].stackSize + byproductStack.stackSize) > this.getInventoryStackLimit()))
            {
            }
            else
            {
                //...Otherwise, remove everything from that slot and put our byproduct stack in there.
                ejectWaste();
                engineItemStacks[1] = byproductStack.copy();
            }
        }
    }

    public void ejectWaste ()
    {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
        { //prevent ghost item stupidity
            float xr = this.itemDropRand.nextFloat() * 0.8F + 0.1F;
            float yr = this.itemDropRand.nextFloat() * 0.8F + 0.1F;
            float zr = this.itemDropRand.nextFloat() * 0.8F + 0.1F;
            EntityItem entityItem = new EntityItem(this.worldObj, (double) ((float) xCoord + xr), (double) ((float) yCoord + yr), (double) ((float) zCoord + zr), engineItemStacks[1].copy());

            worldObj.spawnEntityInWorld(entityItem);
            engineItemStacks[1] = null;
        }
    }

    //else {
    // engineItemStacks[1].stackSize += toBurn;
    //}

    /*
    * Emit deadly smog.
    * This particular implementation of recieveExhaust() is only smart enough to operate
    * with fluids whose block extends BlockGasBase.
    */
    @Override
    public void receiveExhaust (FluidStack exhaustStack)
    {
        if ((exhaustStack == null) || (exhaustStack.amount == 0))
        {
            //If we're just receiving a null value, do nothing with it.
            return;
        }
        else
        {
            int smogProduced = exhaustStack.amount;
            if (fluidTank == null)
            {
                fluidTank = exhaustStack.copy();
                fluidTank.amount = 0;
            }
            //Stash some smog in the fluid tank.
            if (fluidTank.amount < wasteCapacity)
            {
                int amountToStore = Math.min((wasteCapacity - fluidTank.amount), smogProduced);
                fluidTank.amount += amountToStore;
                smogProduced -= amountToStore;
            }
            //Is there still smog left over? If so, we could not fit it into the tank. Exhaust into the adjacent air.
            if (smogProduced > 0)
            {
                int fBlock = exhaustStack.getFluid().getBlockID();
                if (Block.blocksList[fBlock] instanceof BlockGasBase)
                {
                    BlockGasBase ourWaste = (BlockGasBase) Block.blocksList[fBlock];
                    for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
                    {
                        if (dir != ForgeDirection.UP)
                        {
                            smogProduced = ourWaste.pushIntoBlock(worldObj, xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ, smogProduced);
                            if (smogProduced <= 0)
                            {
                                break;
                            }
                        }
                    }
                }
                //If this isn't a fluid we can handle sanely...
                else
                {
                    //Don't punish the player for it.
                    smogProduced = 0;
                }
            }
            //Is there STILL smog left over? If so, explode violently.
            if (smogProduced > 0)
            {
                if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
                { //prevent general stupidity
                    worldObj.createExplosion(null, xCoord, yCoord, zCoord, explosionStrength, true);
                }
            }
        }
    }

    @Override
    public void onInventoryChanged ()
    {
        super.onInventoryChanged();
        // TODO Do we need to do anything with this?
    }

    @Override
    public void doConfig (Configuration config, ContentRegistry cr)
    {
        staticEnergyCap = config.get(getEnglishName(), "Capacity of internal buffer", 2000).getInt();
        quantityPerBurn = 32; //Amount of dirt to attempt to consume at once.
        ticksPerBurn = 20; //Time between ticks where we burn dirt. To reduce lag.
        rfPerTick = config.get(getEnglishName(), "RF transfer rate", 20).getInt();
        enableNaturaCompat = config.get(getEnglishName(), "Enable Natura fuels", true).getBoolean(true);

        enableDirt = config.get(getEnglishName(), "Enable dirt as fuel", true).getBoolean(true);
        enableGrassyDirt = config.get(getEnglishName(), "Enable grassy dirt as fuel", true).getBoolean(true);
        enableMycelium = config.get(getEnglishName(), "Enable mycelium as fuel", true).getBoolean(true);

        if (enableDirt)
        {
            dirtFuel = new SolidFuelInfo();
            dirtFuel.energyPer = config.get(getEnglishName(), "RF generated per dirt", 2).getInt();
            exhaustPerDirt = config.get(getEnglishName(), "Smog produced per dirt burned in milibuckets", 16).getInt();
        }
        if (enableGrassyDirt)
        {
            grassyDirtFuel = new SolidFuelInfo();
            grassyDirtFuel.energyPer = config.get(getEnglishName(), "RF generated per grassy dirt", 3).getInt();
            exhaustPerGrass = config.get(getEnglishName(), "Smog produced per grassy dirt burned in milibuckets", 16).getInt();
        }
        if (enableMycelium)
        {
            myceliumFuel = new SolidFuelInfo();
            myceliumFuel.energyPer = config.get(getEnglishName(), "RF generated per mycelium", 5).getInt();
            exhaustPerMycelium = config.get(getEnglishName(), "Smog produced per mycelium burned in milibuckets", 16).getInt();
        }
        if (enableNaturaCompat)
        {
            enableTaintedSoil = config.get(getEnglishName(), "Enable tainted soil as fuel", true).getBoolean(true);
            enableHeatSand = config.get(getEnglishName(), "Enable heat sand as fuel", true).getBoolean(true);
            if (enableTaintedSoil)
            {
                taintedSoilFuel = new SolidFuelInfo();
                taintedSoilFuel.energyPer = config.get(getEnglishName(), "RF generated per Tainted Soil", 4).getInt();
                exhaustPerTaintedSoil = config.get(getEnglishName(), "Smog produced per Tainted Soil burned in milibuckets", 32).getInt();
            }
            if (enableHeatSand)
            {
                heatSandFuel = new SolidFuelInfo();
                heatSandFuel.energyPer = config.get(getEnglishName(), "RF generated per heat sand", 16).getInt();
                exhaustPerHeatSand = config.get(getEnglishName(), "waste produced per heat sand burned in milibuckets", 2).getInt();
            }
        }
        ticksUntilBurn = ticksPerBurn;
        wasteCapacity = config.get(getEnglishName(), "Internal smog tank capacity", (exhaustPerDirt * quantityPerBurn) * 2).getInt();
    }

    @Override
    public String getEnglishName ()
    {
        // TODO Auto-generated method stub
        return "NitrateEngine";
    }

    @Override
    public String getGameRegistryName ()
    {
        // TODO Auto-generated method stub
        return "engineNitrate";
    }

    @Override
    public boolean isEnabled ()
    {
        return true;
    }

    @Override
    public void DeferredInit (ContentRegistry cr)
    {
        if (dirtFuel != null)
        {
            //In case somebody overrides the terrain block classes like a weirdo, this is here.
            dirtFuel.ourFuel = new ItemStack(Item.itemsList[Block.dirt.blockID]);
            dirtFuel.byproduct = new ItemStack(Item.itemsList[Block.sand.blockID]);
            dirtFuel.exhaust = new FluidStack(waste, exhaustPerDirt);
            staticFuelInfo.add(dirtFuel);
        }
        if (grassyDirtFuel != null)
        {
            //In case somebody overrides the terrain block classes like a weirdo, this is here.
            grassyDirtFuel.ourFuel = new ItemStack(Item.itemsList[Block.grass.blockID]);
            grassyDirtFuel.byproduct = new ItemStack(Item.itemsList[Block.sand.blockID]);
            grassyDirtFuel.exhaust = new FluidStack(waste, exhaustPerDirt);
            staticFuelInfo.add(grassyDirtFuel);
        }
        if (myceliumFuel != null)
        {
            //In case somebody overrides the terrain block classes like a weirdo, this is here.
            myceliumFuel.ourFuel = new ItemStack(Item.itemsList[Block.mycelium.blockID]);
            myceliumFuel.byproduct = new ItemStack(Item.itemsList[Block.sand.blockID]);
            myceliumFuel.exhaust = new FluidStack(waste, exhaustPerDirt / 2);
            staticFuelInfo.add(myceliumFuel);
        }

        if (enableNaturaCompat)
        {
            int tsBlockID = 0;
            int hsBlockID = 0;
            if (GameRegistry.findBlock("Natura", "soil.tainted") != null)
            {
                tsBlockID = GameRegistry.findBlock("Natura", "soil.tainted").blockID;
                System.out.println(GameRegistry.findBlock("Natura", "soil.tainted").blockID);
            }
            if (GameRegistry.findBlock("Natura", "heatsand") != null)
            {
                hsBlockID = GameRegistry.findBlock("Natura", "heatsand").blockID;
                System.out.println(GameRegistry.findBlock("Natura", "heatsand").blockID);
            }
            if ((taintedSoilFuel != null) && (tsBlockID != 0))
            {
                System.out.println("yoyoyo");
                taintedSoilFuel.ourFuel = new ItemStack(Item.itemsList[tsBlockID]);
                taintedSoilFuel.byproduct = new ItemStack(Item.itemsList[Block.slowSand.blockID]);
                taintedSoilFuel.exhaust = new FluidStack(waste, exhaustPerTaintedSoil);
                staticFuelInfo.add(taintedSoilFuel);
            }
            if ((heatSandFuel != null) && (hsBlockID != 0))
            {
                heatSandFuel.ourFuel = new ItemStack(Item.itemsList[hsBlockID]);
                heatSandFuel.byproduct = new ItemStack(Item.itemsList[Block.slowSand.blockID]);
                heatSandFuel.exhaust = new FluidStack(waste, exhaustPerHeatSand);
                heatSandFuel.byproductMult = 0.5f;
                staticFuelInfo.add(heatSandFuel);
            }
        }
    }

}
