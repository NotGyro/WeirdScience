package ws.zettabyte.weirdscience.machine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
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
import net.minecraftforge.fluids.Fluid;
import ws.zettabyte.ferretlib.IDebugInfo;
import ws.zettabyte.ferretlib.ItemSlot;
import ws.zettabyte.ferretlib.block.TileEntityBase;
import ws.zettabyte.ferretlib.initutils.Conf;
import ws.zettabyte.ferretlib.initutils.Configgable;
import ws.zettabyte.weirdscience.WeirdScience;
import ws.zettabyte.weirdscience.gas.BlockGas;

@Configgable(section="Machine")
public class TileEntityCatalyticEngine extends TileEntityBase implements
		IDebugInfo, ISidedInventory, IInventory {
	//---- Inventory ----
	private ItemSlot slotInput = new ItemSlot();
	private ItemSlot slotOutput1 = new ItemSlot();
	private ItemSlot slotOutput2 = new ItemSlot();

	private final ItemSlot[] slots = {slotInput, slotOutput1, slotOutput2};
	private final ItemSlot[] slotsOut = {slotOutput1, slotOutput2};
	//---- end of inventory ----
    
    //Fractional RF per fuel values are achievable: rfPerStack = 1 and an itemstack of 16 fuel is 1/16 RF per fuel.
    public static class FuelInfo { 
    	public ItemStack fuel;
    	//RF per burn = (quantityPerBurn * rfPerItem * the engine's multiplier) rounded up
    	public int rfPerStack= 1;
    	//Millibuckets (mb)
    	public int exhaustPerItem = 80;
    	public boolean metadataSensitive = true;
    	public BlockGas blockExhaust = WeirdScience.blockSmog;
    	public Fluid fluidExhaust = WeirdScience.fluidSmog;
    	public ArrayList<ItemStack> byproducts = new ArrayList<ItemStack>(2);
    }
    
    protected static HashMap<Item, FuelInfo> fuels = new HashMap<Item, FuelInfo>(3);
	
	//---- Conf ----
    //@Conf(name="Catalytic Engine: Max dirt burned per cycle", def="16")
    //public static int quantityPerBurn; //Amount of dirt to attempt to consume at once.

    @Conf(name="Catalytic Engine: Ticks between cycles", def="5")
    public static int ticksPerBurn; //Time between ticks where we burn dirt. To reduce lag.

    @Conf(name="Catalytic Engine: Ticks between exhaust attempts", def="20")
    public static int ticksPerExhaust; //How long until we try to spawn smog?

    @Conf(name="Catalytic Engine: do exhaust", def="false")
    public static boolean doExhaust;
    @Conf(name="Catalytic Engine: internal tank size", def="2400", comment="How much exhaust the engine can store before it gets vented.")
    public static int exhaustAmount;
    
    public static FuelInfo dirtFuel = new FuelInfo();
    static {
    	dirtFuel.fuel = new ItemStack(Blocks.dirt, 8);
    	dirtFuel.rfPerStack = 4;
    	dirtFuel.exhaustPerItem = 80;
    	dirtFuel.metadataSensitive = false;
    	dirtFuel.blockExhaust = WeirdScience.blockSmog;
    	dirtFuel.fluidExhaust = WeirdScience.fluidSmog;
    	dirtFuel.byproducts.add(new ItemStack(Blocks.sand, 2));
    	dirtFuel.byproducts.add(new ItemStack(Blocks.gravel, 1));
    	addFuel(dirtFuel);
    };
    
    //---- end of conf ----

    private int ticksUntilBurn = ticksPerBurn;
    private boolean wasRunningLastBurn = false;
	protected Random itemDropRand = new Random();
    
    
	public TileEntityCatalyticEngine() {
		// TODO Auto-generated constructor stub
	}
	
	public static void addFuel(FuelInfo f) {
		fuels.put(f.fuel.getItem(), f);
	}
	
	// ---- inventory logic ----
	@Override
	public int getSizeInventory() {
		return 3;
	}

	@Override
	public ItemStack getStackInSlot(int s) {
		if(s < slots.length) {
			return slots[s].stack;
		}
		return null;
	}
	
	@Override
	public ItemStack decrStackSize(int s, int amount) {
		//Attempts to remove amount from slot # slotID. Returns a usable
		//itemstack taken out of the slot: Split or just yanked.
		if (this.slots[s] != null) {
            ItemStack itemstack;
            if (this.slots[s].stack.stackSize <= amount) {
                itemstack = this.slots[s].stack;
                this.slots[s].stack = null;
                return itemstack;
            }
            else {
                itemstack = this.slots[s].stack.splitStack(amount);
                return itemstack;
            }
		}
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int s) {
        return this.slots[s].stack;
	}

	@Override
	public void setInventorySlotContents(int s, ItemStack stack) {
        this.slots[s].stack = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
        	stack.stackSize = this.getInventoryStackLimit();
        }
        markDirty();
	}

	@Override
	public String getInventoryName() {
		return "CatalyticEngine";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return (player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) 
				<= 16.0D);
	}

	//More relevant to chests, I think..?
	@Override
	public void openInventory() { }

	@Override
	public void closeInventory() { }

	
	public boolean isItemFuel(Item item) {
		return fuels.containsKey(item);
	}
	@Override
	public boolean isItemValidForSlot(int s, ItemStack stack) {
    	//Only allow inserting into the input slot, and only allow
		//fuel to be inserted.
		return (isItemFuel(stack.getItem()) && (s == 0));
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
		// TODO: Rotation-sensitive stuff.
		return new int[]{0, 1, 2};
	}

	@Override
	public boolean canInsertItem(int s, ItemStack stack,
			int fromDirection) {
		return isItemValidForSlot(s, stack);
	}

	//TODO: Make this a property of the slots somehow.
	@Override
	public boolean canExtractItem(int s, ItemStack stack,
			int fromDirection) {
        return ((s == 1) || (s == 2));
	}
	// ---- end inventory logic ----

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
                ItemStack fuelStack = this.slots[0].stack;
                if (fuelStack != null) {
                    if ((fuelStack.stackSize >= 1) 
                    		&& isItemFuel(fuelStack.getItem())) {
                        FuelInfo inf = fuels.get(fuelStack.getItem());
                        //Consume fuel
                        if(inf.fuel.stackSize <= fuelStack.stackSize) {
                        	flagInvChanged = true;
                        	fuelStack.stackSize -= inf.fuel.stackSize;
                            //Null the stack if there's nothing in it.
                            if (fuelStack.stackSize <= 0) {
                            	this.slots[0].stack = null;
                            }
                        	for(int i = 0; i < inf.byproducts.size(); ++i) {
                        		recieveByproduct(inf.byproducts.get(i).copy());
                        	}
                            ticksUntilBurn += ticksPerBurn; //Reset the timer, but only if we did anything.
                        }
                    }
                }
            }
            if(flagInvChanged) {
                this.markDirty();
            }
        }
    }
    public void recieveByproduct(ItemStack stack) {
    	for(int i = 0; i < slotsOut.length; ++i) {
    		ItemSlot s = slotsOut[i];
    		if(s.stack == null) {
    			s.stack = stack;
    			return;
    		}
    		else if(s.stack.isItemEqual(stack)) {
    			int total = s.stack.stackSize + stack.stackSize;
    			s.stack.stackSize = total;
    			if(total > getInventoryStackLimit()) {
    				//Keep the new ItemStack.
    				s.stack.stackSize = total - getInventoryStackLimit();
    				if(s.stack.stackSize <= 0) {
    					s.stack = null;
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
    
	//NBT stuff
	@Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        //Read the item stacks.
        NBTTagList itemList = (NBTTagList)nbt.getTag("Items");
        NBTTagCompound nbttagcompound1 = null;
        for (int i = 0; i < itemList.tagCount(); ++i) {
        	nbttagcompound1 = (NBTTagCompound)itemList.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.slots.length) {
                slots[b0].stack = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
        //Read how far we are from doing another engine tick.
        ticksUntilBurn = nbt.getShort("BurnTime");

        //Read the internal fluid tank for smog storage
        /*if (!nbt.hasKey("Empty")) {
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(nbt);

            if (fluid != null) {
            	fluidTank = fluid;
            }
        }*/
    }

	@Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        //Write time until next engine burn tick.
        nbt.setShort("BurnTime", (short)this.ticksUntilBurn);
        //Write item stacks.
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < slots.length; ++i) {
            if (slots[i].stack != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.slots[i].stack.writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
        nbt.setTag("Items", nbttaglist);
        //Write our internal fluid tank (which stores smog)
        /*if (fluidTank != null) {
        	fluidTank.writeToNBT(nbt);
        }
        else {
        	nbt.setString("Empty", "");
        }*/
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
	public String getDebugInfo() {
		String inf1 = "nothing";
		if(slotInput.stack != null) {
			inf1 = slotInput.stack.getUnlocalizedName() + " x" + slotInput.stack.stackSize;
		}
		String inf2 = "nothing";
		if(slotOutput1.stack != null) {
			inf2 = slotOutput1.stack.getUnlocalizedName() + " x" + slotOutput1.stack.stackSize;
		}
		String inf3 = "nothing";
		if(slotOutput2.stack != null) {
			inf3 = slotOutput2.stack.getUnlocalizedName() + " x" + slotOutput2.stack.stackSize;
		}
		String finalinf = "Input slot contains " + inf1 + ".\n";
		//finalinf += "Output slot contains " + inf2 + ".\n";
		finalinf += "Output slot 1 contains " + inf2 + ".\n";
		finalinf += "Output slot 2 contains " + inf3 + ".\n";
		return finalinf;
	}

}
