package ws.zettabyte.weirdscience.tileentity;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.fluids.FluidStack;
import ws.zettabyte.weirdscience.block.IBlockMetaPower;
import ws.zettabyte.zettalib.ContentRegistry;
import ws.zettabyte.zettalib.SolidFuelInfo;
import ws.zettabyte.zettalib.interfaces.IConfiggable;
import ws.zettabyte.zettalib.interfaces.IDeferredInit;
import ws.zettabyte.zettalib.interfaces.IRegistrable;
import ws.zettabyte.zettalib.interfaces.ISolidFuelInfo;
import ws.zettabyte.zettalib.tileentity.TileEntitySolidFueled;

/*
 * A tile entity is essentially a bit of extra behavior and information that is associated with a block in the world.
 * When looking at a block, the "Block" class is not instantiated on a per-block basis, instead, an instance of the 
 * Block class is used to do things to any block with that ID.
 * 
 * Without a Tile Entity, all of the information you can put into a block amounts to its ID and four bits of metadata.
 * With a Tile Entity, you can fit as much information as you want into a block, plus lots and lots of lag.
 * (so use them sparingly and don't build your walls out of engines)
 * (actually that would be cool and totally worth it, please build your walls out of engines)
 */
public class TileEntityGunpowderEngine extends TileEntitySolidFueled implements
		IConfiggable, IRegistrable, IDeferredInit, ISidedInventory {

	// Static settings to apply to every instance of the Tile Entity.
	public static int rfPerTick;
	public static int quantityPerBurn; // Amount of fuel to attempt to consume at once.
	public static int ticksPerBurn; // Time between ticks where we burn fuel. To reduce lag.
	private static int staticEnergyCap;
	private static boolean setsFires = true;
	public static int radius = 4; 

	private static ArrayList<ISolidFuelInfo> staticFuelInfo = new ArrayList<ISolidFuelInfo>( 3);
	private static Random rand = new Random();
	public static Item thermite = null;

	// Setting values to carry over between doConfig and DeferredInit
	public static int rfPerThermite = 0;
	public static boolean enableThermite = false;
	public static float thermiteFires = 0.0f;

	// Instance-specific values.
	private int ticksUntilBurn = 0;
	private ItemStack fuelStack = null;
	private boolean wasRunningLastBurn;

	// Make sure local values are synced up with static values:
	public TileEntityGunpowderEngine() {
		super();
		fuelInfo.addAll(staticFuelInfo);
		this.energyCap = staticEnergyCap;
		this.transferRate = rfPerTick;
	}

	@Override
	public void receiveByproduct(ItemStack byproductStack) {
		// Do nothing. Not that kind of engine.
	}

	@Override
	public void receiveExhaust(FluidStack exhaustStack) {
		// Do nothing. Not that kind of engine.
	}

	// Called by our very own game registry and config systems.
	@Override
	public String getEnglishName() {
		return "Blast Engine";
	}

	@Override
	public String getGameRegistryName() {
		return "engineGunpowder";
	}

	// Placeholder, maybe we will eventually use this for something
	@Override
	public boolean isEnabled() {
		return true;
	}

	/*
	 * Called automatically by our very own game registry and config systems.
	 * This method is called on a short-lived instance of the Tile Entity that
	 * will never be used in the world, for the purpose of doing configuration
	 * on static fields.
	 */
	@Override
	public void doConfig(Configuration config, ContentRegistry cr) {
		// Get the energy cap which is universal for all TileEntityGunpowderEngines.
		// Then, we will set the specific energy cap of the instances from this value.
		staticEnergyCap = config.get(getEnglishName(),
				"Capacity of internal energy buffer", 2000).getInt();
		setsFires = config.get(getEnglishName(), "Sets fires", true).getBoolean(true);
		quantityPerBurn = 32; // Amount of dirt to attempt to consume at once.
		ticksPerBurn = 20; // Time between ticks where we burn dirt. To reduce lag.
		// Which is to say: Every X ticks, we will do the engine logic. Where X = ticksPerBurn
		rfPerTick = config.get(getEnglishName(), "RF transfer rate", 20) .getInt();
		radius = rfPerTick = config.get(getEnglishName(), "Burn radius", 4) .getInt(4);

		ticksUntilBurn = ticksPerBurn;

		boolean enableGunpowder = config.get(getEnglishName(),
				"Enable gunpowder as fuel", true).getBoolean(true);
		if (enableGunpowder) {
			SolidFuelInfo gunpowderInfo = new SolidFuelInfo();
			gunpowderInfo.ourFuel = new ItemStack(Item.gunpowder);
			// Remember:                          config secton,   setting name,     default     default
			gunpowderInfo.energyPer = config.get(getEnglishName(), "RF per Gunpowder", 80).getInt(80);
			/*
			 * Bit of a hack. SolidFuelInfo is very much specific to the Nitrate
			 * Engines I designed it for, and I haven't figured out how I should
			 * refactor it quite yet. For now, we'll use the "byproductMult"
			 * float variable to contain info about how much fire each different
			 * fuel will produce.
			 */
			gunpowderInfo.byproductMult = (float) config.get(getEnglishName(),
					"Gunpowder fire factor", 4.0).getDouble(4.0d);
			staticFuelInfo.add(gunpowderInfo);
		}
		boolean enableBlazePowder = config.get(getEnglishName(),
				"Enable blaze powder as fuel", true).getBoolean(true);
		if (enableBlazePowder) {
			SolidFuelInfo blazeInfo = new SolidFuelInfo();
			blazeInfo.ourFuel = new ItemStack(Item.blazePowder);
			blazeInfo.energyPer = config.get(getEnglishName(),
					"RF per Blaze Powder", 80).getInt(80);
			blazeInfo.byproductMult = (float) config.get(getEnglishName(),
					"Blaze Powder fire factor", 8.0).getDouble(8.0d);
			staticFuelInfo.add(blazeInfo);
		}
		enableThermite = config.get(getEnglishName(), "Enable Thermite as fuel", true).getBoolean(true);
		if (enableThermite) { 
			rfPerThermite = config.get(getEnglishName(), "RF per Blaze Powder", 80).getInt(80);
			thermiteFires = (float) config.get(getEnglishName(), "Blaze Powder fire factor", 8.0).getDouble(8.0d);
		}
	}

	// All mods have added all items. Do cool init stuff now.
	@Override
	public void DeferredInit(ContentRegistry cr) {
		if (enableThermite) {
			SolidFuelInfo thermInfo = new SolidFuelInfo();
			thermInfo.energyPer = rfPerThermite;
			thermInfo.byproductMult = thermiteFires;
			if (thermite != null) {
				thermInfo.ourFuel = new ItemStack(thermite);
				staticFuelInfo.add(thermInfo);
			}
		}
	}

	// A bunch of ugly boilerplate I'd abstract away in a second if Java had
	// multiple inheritance follows.

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int slotID) {
		if (slotID == 0) {
			return fuelStack;
		} else {
			return null;
		}
	}

	@Override
	public ItemStack decrStackSize(int slotID, int amount) {
		// Sanity check
		if (slotID != 0) {
			return null;
		}
		// Attempts to remove amount from slot # slotID. Returns a usable
		// itemstack taken out of the slot: Split or just yanked.
		if (fuelStack != null) {
			ItemStack itemstack;

			if (fuelStack.stackSize <= amount) {
				itemstack = fuelStack;
				fuelStack = null;
				return itemstack;
			} else {
				// Creates a new item stack split from ours, of size amount
				itemstack = fuelStack.splitStack(amount);
				return itemstack;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slotID) {
		// Sanity check
		if (slotID != 0) {
			return null;
		}
		// Used to get items when block is broken.
		// (I think)
		if (fuelStack != null) {
			ItemStack itemstack = fuelStack;
			fuelStack = null;
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int slotID, ItemStack itemstack) {
		// Sanity check
		if (slotID != 0) {
			return;
		}

		fuelStack = itemstack;
		if (fuelStack != null
				&& fuelStack.stackSize > this.getInventoryStackLimit()) {
			fuelStack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public String getInvName() {
		return "GunpowderEngine";
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
	public boolean isUseableByPlayer(EntityPlayer entityplayer) { // Sanity
																	// checks!
		if (entityplayer.getDistanceSq((double) this.xCoord + 0.5D,
				(double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D) <= 16.0D) {
			return true; // The player is sufficiently close.
		} else {
			return false; // The player is too far away.
		}
	}

	public void openChest() {
	}

	public void closeChest() {
	}

	public boolean isItemFuel(Item item) {
		// Uses the canBurn functionality from our parent class,
		// TileEntitySolidFueled
		if (canBurn(new ItemStack(item)) != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemstack) {
		if (isItemFuel(itemstack.getItem()) && (slotID == 0)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		// Slot 0 is accessible from all sides.
		return new int[] { 0 };
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack itemstack, int direction) {
		return isItemValidForSlot(slotID, itemstack);
	}

	@Override
	public boolean canExtractItem(int slotID, ItemStack itemstack, int direction) {
		if (slotID == 0) {
			// We can extract from and insert to the same slot.
			// This is fine because there is a single slot for the machine.
			// In a machine with input and result slots, consider only allowing
			// extraction from result slots and insertion to input slots, to
			// allow
			// reasonable automation.
			return true;
		} else {
			return false;
		}
	}

	// NBT stuff: Minecraft uses this data structure for serializing many things
	// to file and to network.
	// Our parent class handles energy just fine, but here, we must implement
	// saving/loading of fuel.
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		// Read the item stack as a sub-tag of nbt
		NBTTagCompound nbttagcompound1 = nbt.getCompoundTag("Fuel");
		if (nbttagcompound1 != null) {
			fuelStack = ItemStack.loadItemStackFromNBT(nbttagcompound1);
		}
		// Simple behavior for performance reasons: If there's fuel in the slot,
		// assume the engine was running.
		if (fuelStack != null) {
			this.wasRunningLastBurn = true;
		}
		// ...otherwise, assume it was not.
		else {
			this.wasRunningLastBurn = false;
		}
		// Read how far we are from doing another engine tick.
		this.ticksUntilBurn = nbt.getShort("BurnTime");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		// Write time until next engine burn tick.
		nbt.setShort("BurnTime", (short) this.ticksUntilBurn);
		// Write item stacks.
		NBTTagCompound fuelSubTag = new NBTTagCompound();
		if (fuelStack != null) {
			fuelStack.writeToNBT(fuelSubTag);
		}
		nbt.setTag("Fuel", fuelSubTag);
	}

	public Packet getDescriptionPacket() { // Very Complex And Difficult Network Code
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, nbt);
	}

	// The meat of our engine logic.
	// ENTITY UPDATE:
	@Override
	public void updateEntity() // The meat of our block.
	{
		super.updateEntity();
		boolean flagInvChanged = false;
		//worldObj.isRemote is a boolean set to true on client.
		//We need to run this code only on server-side,
		//because Minecraft is very naive about sidedness.
		//It does none of it for you.
		if (!worldObj.isRemote) {
			// Burn logic:
			// Are we still waiting to burn fuel?
			boolean flagHasPower = energy > 0;
			int smogProduced = 0;
			if (this.ticksUntilBurn > 0) {
				--this.ticksUntilBurn;
			} else {
				// If we are not waiting, update the entity.
				int toBurn = 0;
				// Make sure we have fuel, somewhere to put waste products, and
				// energy storage capacity.
				if (fuelStack != null) {
					if (fuelStack.stackSize >= 1) {
						// Do the burny thing.
						int deltaItems = doBurn(fuelStack, quantityPerBurn);
						
						setFires(canBurn(fuelStack), deltaItems);
						//Delta, here, is the mathematical meaning of the word
						//(change in thing)
						fuelStack.stackSize -= deltaItems;
						flagHasPower = (deltaItems != 0);
						flagInvChanged = flagHasPower;
						if (fuelStack.stackSize <= 0) {
							fuelStack = null;
						}
						//Update our block as to the current running state of the engine.
						if (deltaItems != 0) {
							TurnBlockOn();
						} else {
							TurnBlockOff();
						}
					} else {
						TurnBlockOff();
					}
					ticksUntilBurn = ticksPerBurn; // Reset the timer, but only
													// if we did anything.
				} else {
					TurnBlockOff();
				}
			}
			// And now, attempt to charge surrounding blocks.
			if (flagHasPower) {
				this.powerAdjacent();
			}
		}

		//This is really important: Here, inventory info is syncced.
		//You get desync if you comment this out.
		if (flagInvChanged) {
			this.onInventoryChanged();
		}
	}
	
	//Yessssss.
	//Attempt to ignite surrounding blocks.
	private void setFires(ISolidFuelInfo fuel, int quantityBurned) {
		//Is this behavior enabled via config?
		if(setsFires) {
			if(quantityBurned > 0) {
				//Chance per block, a percentage.
				//Magic number 2.0f here. Refactor how?
				int cpb = (int) ((2.0f * fuel.getByproductMult()) * (float)quantityBurned);
				ChunkCoordinates coordCheck = new ChunkCoordinates(0, 0, 0);
				ChunkCoordinates coordOurs = new ChunkCoordinates(xCoord, yCoord, zCoord);
				//Iterate through surrounding blocks within the bounding box, attempting to set them on fire.
				for(int x = -radius; x <= radius; ++x) {
					for(int y = -radius; y <= radius; ++y) {
						for(int z = -radius; z <= radius; ++z) {
							coordCheck.set(x+xCoord, y+yCoord, z+zCoord);
							//Check to see if this block is ACTUALLY within the radius.
							int distanceSquared = (int) coordCheck.getDistanceSquaredToChunkCoordinates(coordOurs);
							if(distanceSquared <= (radius * radius)) {
								//Check to see if the block under this one is not air
								if(worldObj.getBlockId(x+xCoord, (y+yCoord)-1, z+zCoord) != 0) {
									//Check to see if THIS block is air
									if(worldObj.getBlockId(x+xCoord, y+yCoord, z+zCoord) == 0) {
										//Roll the dice. Adjust to scale off with distance.
										if(rand.nextInt(100) <= (cpb - (distanceSquared * 2))) {
											//Set it to fire.
											worldObj.setBlock(x+xCoord, y+yCoord, z+zCoord, Block.fire.blockID);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	//Do metadata things to our block, changing its textures based on if the engine is on or off.
	private void TurnBlockOff() {
		if (wasRunningLastBurn == true) {
			Block block = Block.blocksList[worldObj.getBlockId(xCoord, yCoord,
					zCoord)];
			if (block instanceof IBlockMetaPower) {
				((IBlockMetaPower) block).recievePowerOff(worldObj, xCoord,
						yCoord, zCoord);
			}
		}
		wasRunningLastBurn = false;
	}

	//Do metadata things to our block, changing its textures based on if the engine is on or off.
	private void TurnBlockOn() {
		if (wasRunningLastBurn == false) {
			Block block = Block.blocksList[worldObj.getBlockId(xCoord, yCoord,
					zCoord)];
			if (block instanceof IBlockMetaPower) {
				((IBlockMetaPower) block).recievePowerOn(worldObj, xCoord,
						yCoord, zCoord);
			}
		}
		wasRunningLastBurn = true;
	}
	
	//A route to 
}
