package ws.zettabyte.weirdscience.machine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import ws.zettabyte.zettalib.block.IMetaActive;
import ws.zettabyte.zettalib.block.TileEntityBase;
import ws.zettabyte.zettalib.block.TileEntityInventoryBase;
import ws.zettabyte.zettalib.fluid.BlockGas;
import ws.zettabyte.zettalib.initutils.Conf;
import ws.zettabyte.zettalib.initutils.Configgable;
import ws.zettabyte.zettalib.inventory.FluidTankNamed;
import ws.zettabyte.zettalib.inventory.IDescriptiveInventory;
import ws.zettabyte.zettalib.inventory.IInvComponent;
import ws.zettabyte.zettalib.inventory.ItemSlot;
import ws.zettabyte.zettalib.inventory.SimpleInvComponent;
import ws.zettabyte.zettalib.inventory.SlotInput;
import ws.zettabyte.zettalib.inventory.SlotInputOnly;
import ws.zettabyte.zettalib.inventory.SlotOutput;
import ws.zettabyte.weirdscience.WeirdScience;

@Configgable(section="Machine")
public class TECatalyticEngine extends TileEntityInventoryBase implements
		ISidedInventory, IDescriptiveInventory {
	//---- Inventory ----
	protected ItemSlot slotInput = new SlotInput(this, 0, "fuel");
	protected ItemSlot slotOutput1 = new SlotOutput(this, 1, "out1");
	protected ItemSlot slotOutput2 = new SlotOutput(this, 2, "out2");
	
	protected ArrayList<ItemSlot> slotsOut = new ArrayList<ItemSlot>(2);
	//---- end of inventory ----
    
    //Fractional RF per fuel values are achievable: rfPerStack = 1 and an itemstack of 16 fuel is 1/16 RF per fuel.
    public static class FuelInfo { 
    	public ItemStack fuel;
    	//RF per burn = (quantityPerBurn * rfPerItem * the engine's multiplier) rounded up
    	public int rfPerStack= 1;
    	//Millibuckets (mb)
    	//public int exhaustPerItem = 80;
    	public boolean metadataSensitive = true;
    	//public BlockGas blockExhaust = WeirdScience.blockSmog;
    	public FluidStack fluidExhaust = new FluidStack(WeirdScience.fluidSmog, 80);
    	public ArrayList<ItemStack> byproducts = new ArrayList<ItemStack>(2);
    }
    
    protected static HashMap<Item, FuelInfo> fuels = new HashMap<Item, FuelInfo>(3);
	
    protected SimpleInvComponent<Float> burnProgress = new SimpleInvComponent<Float>("progress");
	//---- Conf ----
    //@Conf(name="Catalytic Engine: Max dirt burned per cycle", def="16")
    //public static int quantityPerBurn; //Amount of dirt to attempt to consume at once.

    @Conf(name="Catalytic Engine: Ticks between cycles", def="8")
    public static int ticksPerBurn; //Time between ticks where we burn dirt. To reduce lag.

    @Conf(name="Catalytic Engine: Ticks between  passive exhaust ejection attempts", def="2")
    public static int ticksPerExhaust; //How long until we try to spawn smog?
    @Conf(name="Catalytic Engine: Rate of passive exhaust ejection", def="128", comment="Set to 0 to disable passive exhaust.")
    public static int rateExhaustOut; //How long until we try to spawn smog?

    @Conf(name="Catalytic Engine: do exhaust", def="true")
    public static boolean doExhaust;
    @Conf(name="Catalytic Engine: internal tank size", def="4000", comment="How much exhaust the engine can store before it gets vented.")
    public static int wasteCapacity;
    
    @Conf(name="Catalytic Engine: Explosion strength", def="4.0")
    public static double explosionStrength;
    
    public static FuelInfo dirtFuel = new FuelInfo();
    static {
    	dirtFuel.fuel = new ItemStack(Blocks.dirt, 8);
    	dirtFuel.rfPerStack = 4;

    	dirtFuel.metadataSensitive = false;
    	
    	dirtFuel.byproducts.add(new ItemStack(Blocks.sand, 2));
    	dirtFuel.byproducts.add(new ItemStack(Blocks.gravel, 1));
    	dirtFuel.fluidExhaust = new FluidStack(WeirdScience.fluidSmog, 512);
    	
    	addFuel(dirtFuel);
    };
    
    //---- end of conf ----

    private int ticksUntilBurn = ticksPerBurn;
    private int ticksUntilBurnClientside = ticksPerBurn;
    private int ticksUntilExhaust = ticksPerExhaust;
    private boolean wasRunningLastBurn = false;
	protected Random itemDropRand = new Random();

    protected FluidTankNamed fluidTank;
    protected ArrayList<FluidTankNamed> tanks = new ArrayList<FluidTankNamed>(1);
	private int ticksLastBurn = 127;
    
    
	public TECatalyticEngine() {
		super();
		slots.add(slotInput);
		slots.add(slotOutput1);
		slots.add(slotOutput2);
		slotsOut.add(slotOutput1);
		slotsOut.add(slotOutput2);
		
		fluidTank = new FluidTankNamed(wasteCapacity);
		fluidTank.setName("exhaust");
		fluidTank.setTile(this);
		tanks.add(fluidTank);
	}
	
	public static void addFuel(FuelInfo f) {
		fuels.put(f.fuel.getItem(), f);
	}
	
	@Override
	public String getInventoryName() {
		return "CatalyticEngine";
	}

	public boolean isItemFuel(Item item) {
		return fuels.containsKey(item);
	}
	@Override
	public boolean isItemValidForSlot(int s, ItemStack stack) {
    	//Only allow inserting into the input slot, and only allow
		//fuel to be inserted.
		if (isItemFuel(stack.getItem()) && (s == 0)) {
			return super.isItemValidForSlot(s, stack);
		}
		return false;
	}

	//Beefy engine stuff:

    @Override
    public void updateEntity () //The meat of our block.
    {
        super.updateEntity();
        boolean flagInvChanged = false;
        if (!worldObj.isRemote) {
            //Burn logic:
            //Are we still waiting to burn fuel?
            if (this.ticksUntilBurn > 0) {
                --this.ticksUntilBurn;
            }
            else {
                //If we are not waiting, update the entity.
                int toBurn = 0;
                //Make sure we have fuel, somewhere to put waste products, and energy storage capacity.
                ItemStack fuelStack = slotInput.getStack();
                if (fuelStack != null) {
                    if ((fuelStack.stackSize >= 1) 
                    		&& isItemFuel(fuelStack.getItem())) {
                        FuelInfo inf = fuels.get(fuelStack.getItem());
                        //Consume fuel
                        if(inf.fuel.stackSize <= fuelStack.stackSize) {
                        	flagInvChanged = true;
                        	ticksLastBurn = 0;
                        	fuelStack.stackSize -= inf.fuel.stackSize;
                        	if(doExhaust) receiveExhaust(inf.fluidExhaust.copy());
                            //Null the stack if there's nothing in it.
                            if (fuelStack.stackSize <= 0) {
                            	slotInput.setStackForce(null);
                            	/*
                            	 * TODO: Refactor this tile entity to be smarter about using helpful functions from
                            	 * slot classes.
                            	 */
                            }
                        	for(int i = 0; i < inf.byproducts.size(); ++i) {
                        		recieveByproduct(inf.byproducts.get(i).copy());
                        	}
                            ticksUntilBurn += ticksPerBurn; //Reset the timer, but only if we did anything.
                        }
                    }
                }
    	        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    	        markDirty();
            }
        }
        if (this.ticksUntilExhaust > 0) {
            --this.ticksUntilExhaust;
            if(this.ticksUntilExhaust < 0) ticksUntilExhaust = 0;
        }
        //Is this behavior enabled?
        if(doExhaust && (rateExhaustOut > 0)){
        	//Are the conditions met to vent?
            if ((!worldObj.isRemote) && (fluidTank.getFluidAmount() > rateExhaustOut)
            		&& (ticksLastBurn > (ticksPerBurn+ticksPerExhaust)*4)){
            	//Has the countdown finished?
                if (this.ticksUntilExhaust <= 0) {
		            Block fBlock = fluidTank.getFluid().getFluid().getBlock();
		            if (fBlock instanceof BlockGas) {
		            	BlockGas ourWaste = (BlockGas)fBlock;
		                for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
		                    if (dir != ForgeDirection.UP) {
		                        if (fluidTank.getFluid().amount < rateExhaustOut) {
		                            break;
		                        }
		                    	fluidTank.getFluid().amount -= (rateExhaustOut - ourWaste.pushIntoBlock(worldObj, xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ, rateExhaustOut));
		                    }
		                }
		            }
		            ticksUntilExhaust += ticksPerExhaust; //Reset the timer, but only if we did anything.
		            flagInvChanged = true;
                }
            }
        }
        burnProgress.val = 1.0F - Math.min(((float)ticksUntilBurn) / ((float)ticksPerBurn), 1.0F);
        if(burnProgress.val > 0.8F) burnProgress.val = 1.0F;
        if(worldObj.isRemote) {
	        Block b = worldObj.getBlock(xCoord, yCoord, zCoord);
	        if(b instanceof IMetaActive) {
	        	((IMetaActive)b).setActiveStatus((ticksLastBurn < 20), worldObj, xCoord, yCoord, zCoord);
	        }
        }
        ticksLastBurn++;
        if(flagInvChanged || (this.ticksUntilBurn > 0)) {
	        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	        markDirty();
        }
    }
    
    public void recieveByproduct(ItemStack stack) {
    	for(int i = 0; i < slotsOut.size(); ++i) {
    		ItemSlot s = slotsOut.get(i);
    		if(s.getStack() == null) {
    			s.setStackForce(stack);
    			return;
    		}
    		else if(s.getStack().isItemEqual(stack)) {
    			int total = s.getStack().stackSize + stack.stackSize;
    			s.getStack().stackSize = total;
    			if(total > getInventoryStackLimit()) {
    				//Keep the new ItemStack.
    				s.getStack() .stackSize = total - getInventoryStackLimit();
    				if(s.getStack() .stackSize <= 0) {
    	    			s.setStackForce(null);
    				}
    				
    				stack.stackSize = getInventoryStackLimit();
    			}
    			else {
    				return;
    			}
    		}
    	}
    	//If we couldn't find any place for it, then...
    	ejectWaste(stack);
    }
    //Send an item flying.
    private void ejectWaste(ItemStack stack) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) { //prevent ghost item stupidity
        	float xr = this.itemDropRand.nextFloat() * 0.8F + 0.1F;
        	float yr = this.itemDropRand .nextFloat() * 0.8F + 0.1F;
        	float zr = this.itemDropRand.nextFloat() * 0.8F + 0.1F;
        	EntityItem ent = new EntityItem(this.worldObj, 
        			(double) ((float) xCoord + xr), (double) ((float) yCoord + yr), (double) ((float) zCoord + zr), 
        			stack);

        	worldObj.spawnEntityInWorld(ent);
        }
    }
    public void receiveExhaust (FluidStack exhaustStack) {
        if ((exhaustStack == null) || (exhaustStack.amount == 0)) {
            //If we're just receiving a null value, do nothing with it.
            return;
        }
        else {
        	exhaustStack.amount -= fluidTank.fill(exhaustStack, true);
            //Is there still smog left over? If so, we could not fit it into the tank. Exhaust into the adjacent air.
            if (exhaustStack.amount > 0) {
                Block fBlock = exhaustStack.getFluid().getBlock();
                if (fBlock instanceof BlockGas) {
                	BlockGas ourWaste = (BlockGas)fBlock;
                    for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                        if (dir != ForgeDirection.UP) {
                        	exhaustStack.amount = ourWaste.pushIntoBlock(worldObj, xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ, exhaustStack.amount);
                            if (exhaustStack.amount <= 0) {
                                break;
                            }
                        }
                    }
                }
                //If this isn't a fluid we can handle sanely...
                else {
                    //Don't punish the player for it.
                	return;
                }
            }
            //Is there STILL smog left over? If so, explode violently.
            if (exhaustStack.amount > 0) {
                if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) { //prevent general stupidity
                    worldObj.createExplosion(null, xCoord, yCoord, zCoord, (float)explosionStrength, true);
                }
            }
        }
    }
    
	//---- NBT stuff ----
	@Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        //Read how far we are from doing another engine tick.
		ticksLastBurn = nbt.getShort("LastBurnTime");
        ticksUntilBurn = nbt.getShort("BurnTime");

        fluidTank.readFromNBT(nbt);
    }

	@Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        //Write time until next engine burn tick.
		nbt.setShort("BurnTime", (short)this.ticksUntilBurn);
		nbt.setShort("LastBurnTime", (short)this.ticksLastBurn);
        //Write our internal fluid tank (which stores smog)
        fluidTank.writeToNBT(nbt);
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
	
	@Override
	public Iterable<ItemSlot> getSlots() {
		return slots;
	}

	
	@Override
	protected void buildComponentList() {
		super.buildComponentList();
		for(FluidTankNamed t : tanks) {
			fullComponentList.add(t);
		}
		fullComponentList.add(burnProgress);
	}
}
