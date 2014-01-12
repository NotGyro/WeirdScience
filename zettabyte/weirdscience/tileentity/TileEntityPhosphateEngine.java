package zettabyte.weirdscience.tileentity;

import java.util.Random;
import static java.lang.System.out;


import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

import net.minecraftforge.common.ForgeDirection;

//import cofh.api.energy.TileEnergyHandler;
//import cofh.api.tileentity.IEnergyInfo;
//import cofh.api.energy.IEnergyHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import cpw.mods.fml.common.FMLCommonHandler;

public class TileEntityPhosphateEngine extends TileEntity implements //IEnergyHandler, IEnergyInfo,
		ISidedInventory {
	private static final int[] accessibleSlots = new int[] {0,1};

    /**
     * The ItemStacks that hold the items currently being used in the furnace
     */
    private ItemStack[] engineItemStacks = new ItemStack[2];

    private final Random itemDropRand = new Random(); //Randomize item drop direction.
    
    public int rfPerTick;
    public int rfPerDirt;
    
	public TileEntityPhosphateEngine() {
		this(20, 20, 80);
	}

	private int energy;
	private int energyCap;
    
	public TileEntityPhosphateEngine(int rfpd, int rfpt, int cap) {
		super();
		energy = 0;
		this.rfPerDirt = rfpd;
		this.rfPerTick = rfpt;
		energyCap = cap;
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
		if (this.engineItemStacks[slotID] != null) {
            ItemStack itemstack;

            if (this.engineItemStacks[slotID].stackSize <= amount) {
                itemstack = this.engineItemStacks[slotID];
                this.engineItemStacks[slotID] = null;
                return itemstack;
            }
            else {
                itemstack = this.engineItemStacks[slotID].splitStack(amount);

                if (this.engineItemStacks[slotID].stackSize == 0) {
                	this.engineItemStacks[slotID] = null;
                }

                return itemstack;
            }
        }
        else {
            return null;
        }
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slotID) {
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
	public void setInventorySlotContents(int slotID, ItemStack itemstack) {
        this.engineItemStacks[slotID] = itemstack;

        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
        {
        	itemstack.stackSize = this.getInventoryStackLimit();
        }

	}

	@Override
	public String getInvName() {
		return "PhosphateEngine";
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
		if(this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this){ //Are we bugging out or no?
			return false;
		}
		else {
			if (entityplayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D){
				return true;
			}
			else {
				return false;
			}
		}
	}
	
	public void openChest() {}

	public void closeChest() {}

	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemstack) {
       /* int i = itemstack.getItem().itemID;
		if (itemstack.getItem() instanceof ItemBlock && Block.blocksList[i] != null)
        {
            Block block = Block.blocksList[i];
			if(block == net.minecraft.block.BlockDirt.dirt) { //Is the item to insert our fuel?
				if(slotID == 0) { //Are we the fuel slot?
					return true;
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
        }
		else
		{
			return false;
		}*/
		if((itemstack.getItem().itemID == 3) && (slotID == 0))	{
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
        	return true;
        }
        else {
        	return false;
        }
	}
/*
	@Override
	public int getEnergyPerTick() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxEnergyPerTick() {
		return 0;
	}

	@Override
	public int getEnergy() {
		return storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergy() {
		return storage.getMaxEnergyStored();
	}

	public void setCapacity(int setTo) {
		storage.setCapacity(setTo);
	}
	
	public void setOutputRate(int setTo) {
		rfPerTick = setTo;
	}
	
	public void setEfficiency(int setTo) {
		rfPerDirt = setTo;
		storage.setMaxTransfer(rfPerDirt);
		storage.setMaxExtract(rfPerDirt);
		storage.setMaxReceive(rfPerDirt);
	}
*/
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		int dirtQuantity = nbt.getInteger("dirtQuantity");
		int sandQuantity = nbt.getInteger("sandQuantity");
		if(dirtQuantity > 0)
		{
			this.engineItemStacks[0] = new ItemStack(Item.itemsList[3], dirtQuantity);
		}
		if(sandQuantity > 0)
		{
			this.engineItemStacks[1] = new ItemStack(Item.itemsList[12], sandQuantity);
		}
		super.readFromNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {

		int dirtQuantity = 0;
		int sandQuantity = 0;
		if(this.engineItemStacks[0] != null)
		{
			dirtQuantity = engineItemStacks[0].stackSize;
		}
		if(this.engineItemStacks[1] != null)
		{
			sandQuantity = engineItemStacks[1].stackSize;
		}
		nbt.setInteger("dirtQuantity", dirtQuantity);
		nbt.setInteger("sandQuantity", sandQuantity);
		super.writeToNBT(nbt);
	}
	public Packet getDescriptionPacket() { //Very Complex And Difficult Network Code
	    NBTTagCompound nbt = new NBTTagCompound();
	    writeToNBT(nbt);
	    super.writeToNBT(nbt);
	    return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, nbt);
	}
	@Override
	public void updateEntity() //The meat of our block.
    {
		/*
		boolean flagInvChanged = false;
		boolean flagHasPower = this.getEnergyStored(ForgeDirection.UP) > 0;
		//Make sure we have fuel, somewhere to put waste products, and energy storage capacity.
		
		if (this.engineItemStacks[0] != null) {
            if ((this.engineItemStacks[0].stackSize >= 1) && (this.storage.getEnergyStored() < this.storage.getMaxEnergyStored())) {
            	
                int itemID = this.engineItemStacks[0].getItem().itemID;
                //Burn dirt.
            	if (this.engineItemStacks[0].getItem() instanceof ItemBlock) {
                    Block block = Block.blocksList[itemID];
        			//if(block == net.minecraft.block.BlockDirt.dirt) { //Is the item in slot 0 our fuel?
            		if(itemID == 3) { //Is the item in slot 0 our fuel?
        				this.engineItemStacks[0].stackSize--; //Decrement the stack size.
        				if(engineItemStacks[1] == null) { //Make sure we have a sand stack to add to.
        					engineItemStacks[1] = new ItemStack(12, 1, 0);
        				}
        				else if(engineItemStacks[1].stackSize >= this.getInventoryStackLimit()) {
        					//Drop excess waste
                            if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) { //prevent ghost item stupidity
	        					float xr = this.itemDropRand.nextFloat() * 0.8F + 0.1F;
	        					float yr = this.itemDropRand.nextFloat() * 0.8F + 0.1F;
	        					float zr = this.itemDropRand.nextFloat() * 0.8F + 0.1F;
	                            EntityItem entityItem = new EntityItem(this.worldObj, (double)((float)xCoord + xr), 
	                            		(double)((float)yCoord + yr), (double)((float)zCoord + zr), new ItemStack(12, 64, 0));

                            	worldObj.spawnEntityInWorld(entityItem);
                            }
                            engineItemStacks[1].stackSize = 1;
        				}
        				else {
        					engineItemStacks[1].stackSize++;
        				}
        				
        	            flagInvChanged = true;

                        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
        	            storage.modifyEnergyStored(rfPerDirt);
        	            out.println("Storage powered by:");
        	            out.println(rfPerDirt);
        	            out.println(", is now:");
        	            out.println(storage.getEnergyStored());
        	            out.println("Max:");
        	            out.println(this.storage.getMaxEnergyStored());
                      
                        }
        	            
        				flagHasPower = true;
        			}
        		}
                if (this.engineItemStacks[0].stackSize == 0) {
                	this.engineItemStacks[0] = null;
                }
            }
        }
		if (flagHasPower) {
            //out.println("FlagHasPower Block.");
			for (int direction = 0; direction < 6; ++direction) {
				//int toExtract = rfPerTick;
				//if(this.storage.getEnergyStored() == 0) {
		        //    out.println("1");
				//	break;
				//}
				//else if(this.storage.getEnergyStored() < toExtract) {
		        //    out.println("2");
				//	toExtract = this.storage.getEnergyStored();
				//}
				//int amount = EnergyHelper.chargeAdjacentEnergyHandler(this, direction, 50, false);
	            //out.println("Amount charge:");
	            //out.println(amount);
	            //if(amount > 0) {
	            //	this.storage.extractEnergy(toExtract, false);
	            //}
				
			}
		}
        if (flagInvChanged) {
            this.onInventoryChanged();
        }*/
    }
	@Override
	public void onInventoryChanged() {
		// TODO Auto-generated method stub
		
	}
/*
	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive,
			boolean simulate) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract,
			boolean simulate) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean canInterface(ForgeDirection from) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		// TODO Auto-generated method stub
		return 0;
	}
*/
}
