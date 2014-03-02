package zettabyte.weirdscience.tileentity;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import zettabyte.weirdscience.core.ContentRegistry;
import zettabyte.weirdscience.core.fluid.BlockGasBase;
import zettabyte.weirdscience.core.interfaces.IConfiggable;
import zettabyte.weirdscience.core.interfaces.IRegistrable;
import cofh.api.energy.IEnergyHandler;
import cofh.api.tileentity.IEnergyInfo;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
//import static java.lang.System.out;

public class TileEntityNitrateEngine extends TileEntity implements IEnergyHandler, IEnergyInfo,
		ISidedInventory, IFluidHandler, IFluidTank, IConfiggable, IRegistrable {
	private static final int[] accessibleSlots = new int[] {0,1};

    /**
     * The ItemStacks that hold the items currently being used in the furnace
     */
    private ItemStack[] engineItemStacks = new ItemStack[2];

    private final Random itemDropRand = new Random(); //Randomize item drop direction.
    
    public static int rfPerTick;
    public static int rfPerDirt;
    public static int dirtPerBurn; //Amount of dirt to attempt to consume at once.
    public static int ticksPerBurn; //Time between ticks where we burn dirt. To reduce lag.
	private static int energy;
	private static int energyCap;
    public static float explosionStrength = 4.0F;
    protected static int wasteCapacity;
    protected static int ticksPerExhaust; //How long until we try to spawn smog?
    protected static int wasteProductionSpeed;
    public static BlockGasBase waste = null;
    
    private int ticksUntilBurn = ticksPerBurn;
    
	public TileEntityNitrateEngine() {
		ticksUntilBurn = ticksPerBurn;
		energy = 0;
	}
	
	protected FluidStack fluidTank;

	public static void setWaste(BlockGasBase b) {
		waste = b;
	}
	public void setWasteCapacity(int amt) {
		wasteCapacity = amt;
	}
	public void setTicksPerExhaust(int amt) {
		ticksPerExhaust = amt;
	}
	//Warning: This is a *per dirt* value.
	public void setWasteProductionSpeed(int amt) {
		wasteProductionSpeed = amt;
	}

	@Override
	public int getSizeInventory() {
		return this.engineItemStacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int slotID) {
        return this.engineItemStacks[slotID];
	}

	@Override
	public ItemStack decrStackSize(int slotID, int amount) {
		//Attempts to remove amount from slot # slotID. Returns a usable
		//itemstack taken out of the slot: Split or just yanked.
		if (this.engineItemStacks[slotID] != null) {
            ItemStack itemstack;

            if (this.engineItemStacks[slotID].stackSize <= amount) {
                itemstack = this.engineItemStacks[slotID];
                this.engineItemStacks[slotID] = null;
                return itemstack;
            }
            else {
                itemstack = this.engineItemStacks[slotID].splitStack(amount);

                return itemstack;
            }
        }
        else {
            return null;
        }
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slotID) {
		//Used to get items when block is broken.
		//(I think)
        if (this.engineItemStacks[slotID] != null) {
            ItemStack itemstack = this.engineItemStacks[slotID];
            this.engineItemStacks[slotID] = null;
            return itemstack;
        }
        else {
            return null;
        }
	}

	@Override
	public void setInventorySlotContents(int slotID, ItemStack itemstack) {
        this.engineItemStacks[slotID] = itemstack;

        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
        	itemstack.stackSize = this.getInventoryStackLimit();
        }
	}

	@Override
	public String getInvName() {
		return "NitrateEngine";
	}

	@Override
	public boolean isInvNameLocalized() {
		// TODO
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
        return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) { //Sanity checks!
		if (entityplayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 16.0D){
			return true; //The player is sufficiently close.
		}
		else {
			return false; //The player is too far away.
		}
	}
	
	public void openChest() {}

	public void closeChest() {}
	
	public boolean isItemFuel(Item item) {
		if(item.itemID == 3) {
			//Vanilla item ID / block ID #3 is dirt.
			//Using this rather than the class for Chisel compat.
			//(Chisel hijacks the same ID for a new class
			//for dirt)
			return true;
		}
		else {
			return false;
		}
	}
	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemstack) {
    	//Only allow inserting into the input slot, and only allow
		//fuel to be inserted.
		if(isItemFuel(itemstack.getItem()) && (slotID == 0)) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return accessibleSlots;
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack itemstack, int direction) {
		return isItemValidForSlot(slotID, itemstack);
	}

	@Override
	public boolean canExtractItem(int slotID, ItemStack itemstack, int direction) {
        if(slotID == 1) {
        	//Only allow removing from the output slot.
        	//Causes hoppers and item pipes to act clever.
        	return true;
        }
        else {
        	return false;
        }
	}
	
	//NBT stuff
	@Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        //Read the item stacks.
        NBTTagList nbttaglist = nbt.getTagList("Items");
        this.engineItemStacks = new ItemStack[this.getSizeInventory()];
        NBTTagCompound nbttagcompound1 = null;
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
        	nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.engineItemStacks.length) {
                this.engineItemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
        //Read how far we are from doing another engine tick.
        this.ticksUntilBurn = nbt.getShort("BurnTime");

        //Read the internal fluid tank for smog storage
        if (!nbt.hasKey("Empty")) {
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(nbt);

            if (fluid != null) {
            	fluidTank = fluid;
            }
        }

        //Get energy
        energy = nbt.getInteger("Energy");
    }

	@Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        //Write time until next engine burn tick.
        nbt.setShort("BurnTime", (short)this.ticksUntilBurn);
        //Write energy
        nbt.setInteger("Energy", this.energy);
        //Write item stacks.
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.engineItemStacks.length; ++i) {
            if (this.engineItemStacks[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.engineItemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
        nbt.setTag("Items", nbttaglist);
        //Write our internal fluid tank (which stores smog)
        if (fluidTank != null) {
        	fluidTank.writeToNBT(nbt);
        }
        else {
        	nbt.setString("Empty", "");
        }
    }
	public Packet getDescriptionPacket() { //Very Complex And Difficult Network Code
	    NBTTagCompound nbt = new NBTTagCompound();
	    writeToNBT(nbt);
	    super.writeToNBT(nbt);
	    return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, nbt);
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		// This is not a fluid tank.
		return 0;
	}
	//FLUID CODE:
	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) {
		if(resource.isFluidEqual(fluidTank)) {
			return drain(from, resource.amount, doDrain);
		}
		else {
			return null;
		}
	}
	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		// This is still not a fluid tank.
		return false;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[] { getInfo() };
	}

	@Override
	public FluidStack getFluid() {
		return fluidTank;
	}

	@Override
	public int getFluidAmount() {
		return fluidTank.amount;
	}

	@Override
	public int getCapacity() {
		return wasteCapacity;
	}

	@Override
	public FluidTankInfo getInfo() {
		return new FluidTankInfo(this);
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		return 0;
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		if (fluidTank == null) {
            return null;
        }

        int drained = maxDrain;
        if (fluidTank.amount < drained) {
            drained = fluidTank.amount;
        }

        FluidStack stack = new FluidStack(fluidTank, drained);
        if (doDrain)
        {
        	fluidTank.amount -= drained;
            if (fluidTank.amount <= 0) {
            	fluidTank = null;
            }
            FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(fluidTank, this.worldObj, this.xCoord, this.yCoord, this.zCoord, this));
        }
        return stack;
	}
	//ENERGY CODE:

	@Override
	public int getEnergyPerTick() {
		return rfPerDirt;
	}

	@Override
	public int getMaxEnergyPerTick() {
		return rfPerTick;
	}

	@Override
	public int getEnergy() {
		return energy;
	}

	@Override
	public int getMaxEnergy() {
		return energyCap;
	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive,
			boolean simulate) {
		// This isn't a battery.
		return 0;
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract,
			boolean simulate) {
		if(!simulate) { 
			if(energy < maxExtract) {
				maxExtract = energy;
				energy = 0;
				return maxExtract;
			}
			else {
				energy -= maxExtract;
				return maxExtract;
			}
		}
		else {
			if(energy < maxExtract) {
				return energy;
			}
			else {
				return maxExtract;
			}
		}
	}

	@Override
	public boolean canInterface(ForgeDirection from) {
		return true;
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		return energy;
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return energyCap;
	}
	//ENTITY UPDATE:
	@Override
	public void updateEntity() //The meat of our block.
    {
		//Burn logic:
		//Are we still waiting to burn fuel?
		boolean flagHasPower = energy > 0;
		int smogProduced = 0;
        if (this.ticksUntilBurn > 0) {
        	--this.ticksUntilBurn;
        }
        else {
        	//If we are not waiting, update the entity.
			boolean flagInvChanged = false;
			int toBurn = 0;
			//Make sure we have fuel, somewhere to put waste products, and energy storage capacity.
			
			if (this.engineItemStacks[0] != null) {
	            if ((this.engineItemStacks[0].stackSize >= 1) && (energy < energyCap)) {
	                int itemID = this.engineItemStacks[0].getItem().itemID;
	                //Burn dirt.
	            	if (this.engineItemStacks[0].getItem() instanceof ItemBlock) {
	                    Block block = Block.blocksList[itemID];
	        			//if(block == net.minecraft.block.BlockDirt.dirt) { //Is the item in slot 0 our fuel?
	            		if(itemID == 3) { //Is the item in slot 0 our fuel?
	            			//Decide how much fuel to burn.
	            			toBurn = Math.min(dirtPerBurn, this.engineItemStacks[0].stackSize); //Either eat dirtPerBurn fuel or the entire stack.
	        				this.engineItemStacks[0].stackSize -= toBurn;
	        				if(engineItemStacks[1] == null) { //Make sure we have a sand stack to add to.
	        					engineItemStacks[1] = new ItemStack(12, toBurn, 0);
	        				}
	        				else if((engineItemStacks[1].stackSize + toBurn) > this.getInventoryStackLimit()) {
	        					//Drop excess waste
	                            if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) { //prevent ghost item stupidity
		        					float xr = this.itemDropRand.nextFloat() * 0.8F + 0.1F;
		        					float yr = this.itemDropRand.nextFloat() * 0.8F + 0.1F;
		        					float zr = this.itemDropRand.nextFloat() * 0.8F + 0.1F;
		                            EntityItem entityItem = new EntityItem(this.worldObj, (double)((float)xCoord + xr), 
		                            		(double)((float)yCoord + yr), (double)((float)zCoord + zr), new ItemStack(12, getInventoryStackLimit(), 0));
	
	                            	worldObj.spawnEntityInWorld(entityItem);
	                            }
	                            engineItemStacks[1].stackSize = toBurn;
	        				}
	        				else {
	        					engineItemStacks[1].stackSize += toBurn;
	        				}
	        				
	        	            flagInvChanged = true;
	
	        	            energy += (rfPerDirt * toBurn);

	        				smogProduced = wasteProductionSpeed * toBurn;
	        	            
	        				flagHasPower = true;
	        			}
	        		}
	                if (this.engineItemStacks[0].stackSize <= 0) {
	                	this.engineItemStacks[0] = null;
	                }
	            }
	            ticksUntilBurn = ticksPerBurn; //Reset the timer, but only if we did anything.
			}
	        if (flagInvChanged) {
	            this.onInventoryChanged();
	        }
        }
		//And now, attempt to charge surrounding blocks.
		if (flagHasPower) {
			for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				TileEntity tileEntity = worldObj.getBlockTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
				if(tileEntity != null) {
					if(tileEntity instanceof IEnergyHandler) {
						((IEnergyHandler)tileEntity).receiveEnergy(dir.getOpposite(), Math.min(energy, rfPerTick), false);
						energy -= Math.min(energy, rfPerTick);
						if(energy == 0) {
							//Don't bother trying to output energy if we're out of it.
							break;
						}
					}
				}
			}
		}
		//Smog logic:
		if(waste != null) {
			if(smogProduced > 0) {
				if(fluidTank == null) {
					fluidTank = new FluidStack(waste.getFluidType(),0);
				}
				if(fluidTank.amount < wasteCapacity) {
					int amountToStore = Math.min((wasteCapacity - fluidTank.amount), smogProduced);
					fluidTank.amount += amountToStore;
					smogProduced -= amountToStore;
				}
				//Is there still smog left over? If so, we could not fit it into the tank. Exhaust into the adjacent air.
				if(smogProduced > 0) {
					for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
						if(dir != ForgeDirection.UP)
						{
							smogProduced = waste.pushIntoBlock(worldObj, xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ, smogProduced);
							if(smogProduced <= 0) {
								break;
							}
						}
					}
				}
				//Is there STILL smog left over? If so, explode violently.
				if(smogProduced > 0) {
			        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) { //prevent general stupidity
			        	worldObj.createExplosion(null, xCoord, yCoord, zCoord, explosionStrength, true);
			        }
				}
			}
			else {
				//TODO: Empty the tank into the atmosphere.
			}
		}
		//Attempt to dump tank into surrounding Forge fluid handlers.
		if(fluidTank != null) {
			for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				TileEntity tileEntity = worldObj.getBlockTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
				if(tileEntity != null) {
					if(tileEntity instanceof IFluidHandler) {
						FluidStack toDrain = new FluidStack(fluidTank.getFluid(), fluidTank.amount);
						drain(((IFluidHandler)tileEntity).fill(dir.getOpposite(), toDrain, true), true);
						
						if(fluidTank == null) {
							break;
						}
					}
				}
			}
		}
    }
	@Override
	public void onInventoryChanged() {
		// TODO Do we need to do anything with this?
	}

	@Override
	public void doConfig(Configuration config, ContentRegistry cr) {
		this.rfPerDirt = config.get("Nitrate Engine", "RF generated per dirt", 20).getInt();
		this.rfPerTick = config.get("Nitrate Engine", "RF transfer rate", 20).getInt();
		energyCap = config.get("Nitrate Engine", "Capacity of internal buffer", 200).getInt();
	    dirtPerBurn = 32; //Amount of dirt to attempt to consume at once.
	    ticksPerBurn = 20; //Time between ticks where we burn dirt. To reduce lag.
	    wasteProductionSpeed = config.get("Nitrate Engine", "Smog produced per dirt burned in milibuckets", 16).getInt();
		ticksUntilBurn = ticksPerBurn;
		wasteCapacity = config.get("Nitrate Engine", "Internal smog tank capacity", wasteProductionSpeed * 2).getInt();
	}

	@Override
	public String getEnglishName() {
		// TODO Auto-generated method stub
		return "NitrateEngine";
	}

	@Override
	public String getGameRegistryName() {
		// TODO Auto-generated method stub
		return "engineNitrate";
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
