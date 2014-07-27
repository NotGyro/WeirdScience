package ws.zettabyte.weirdscience.inventory;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/*An abstraction for Minecraft's slot type.
 *Intended to be easy and generalized to
 *configure and use with tile entities.
 *Intended for use with our GUI system -
 *namely, the wrapper over Container.
 */

/*
 * In practice these will most likely be in a static array on the TileEntity.
 */
public class SlotData {
	
	protected ArrayList<Item> whitelist;
	protected ArrayList<Item> blacklist;
	protected int capacity;
	
	//For mapping of this slot to the Tile Entity it is used with.
	protected int slotID;
	
	//For mapping this slot to the GUI. 
	protected String name; 

	public SlotData() {
		whitelist = new ArrayList<Item>(8); //Start with 8 as a capacity hint
		blacklist = new ArrayList<Item>(8); //for performance reasons maybe.
											//(Must profile, who knows?)
	}
	/*
	 * This enum can be:
	 * 
	 * InputRestricted (refers to machine interface, players can still remove items), 
	 * InputOutput, OutputOnly, 
	 * OutputRestricted (much like InputRestricted, players can still put items in there 
	 * but automation and shift-click cannot). 
	 */
	public static enum AccessType {
		//I'd really rather not implement these right now. :effort:
		//INPUT_RESTRICTED,
		INPUT_OUTPUT,
		OUTPUT_ONLY//,
		//OUPUT_RESTRICTED
	};
	protected AccessType accessType;
	

	public static enum FilterType {
		NOFILTER,
		WHITELISTED,
		BLACKLISTED
	};
	
	protected FilterType filterType;

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getSlotID() {
		return slotID;
	}

	public void setSlotID(int slotID) {
		this.slotID = slotID;
	}

	public AccessType getAccessType() {
		return accessType;
	}

	public void setAccessType(AccessType accessType) {
		this.accessType = accessType;
	}

	public FilterType getFilterType() {
		return filterType;
	}

	public void setFilterType(FilterType filterType) {
		this.filterType = filterType;
	}
	
	public void addBlacklistItem(Item item) {
		blacklist.add(item);
	}
	
	public void addWhitelistItem(Item item) {
		whitelist.add(item);
	}
	public boolean isItemBlacklisted(Item item) {
		return blacklist.contains(item);
	}
	public boolean isItemWhitelisted(Item item) {
		return whitelist.contains(item);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Check to see if this type of item can be placed in this slot.
	 */
	public boolean Accepts(ItemStack item){
		if(accessType == AccessType.INPUT_OUTPUT) {
			if(filterType == FilterType.NOFILTER){
				//If this is an input slot and there is no filter, of course it will accept the item.
				return true;
			}
			else if(filterType == FilterType.BLACKLISTED) {
				//If it's blacklisted and the blacklist contains our item...
				if(blacklist.contains(item.getItem())) {
					return false;
				}
				return true;
			}
			else if(filterType == FilterType.WHITELISTED) {
				//If it's whitelisted and the whitelist contains our item...
				if(whitelist.contains(item.getItem())) {
					return true;
				}
				return false;
			}
		}
		//Slots that aren't input slots do not accept anything.
		return false;
	}
}
