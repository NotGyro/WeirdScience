package zettabyte.weirdscience.core.baseclasses;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import zettabyte.weirdscience.core.interfaces.IWeirdScienceBlock;
import zettabyte.weirdscience.core.interfaces.IWeirdScienceItem;

public class ItemBase extends Item implements IWeirdScienceItem {
	
	protected String englishName;
	
	//Exclude viable block IDs from default ID values.
	protected static final int itemIDSearchLowerBound = 4096;
	
	public ItemBase(Configuration config, String name, int defaultID) {
		/* 
		 * Real version of the constructor. Ultimately all other versions of the constructor turn into this.
		 * In this line, config looks up the block ID with a setting based upon the name argument.
		 */
		super(config.getItem(name + " item ID", defaultID).getInt());
		englishName = name;
		setUnlocalizedName("item" + name.replace(" ", "")); //A default value. Absolutely acceptable to not keep it.
	}

	public ItemBase(Configuration config, String name) {
		/* 
		 * For this version of the constructor, the default item ID is set to the result of 
		 * FindFreeItemID(), which should be a valid and available identifier if all goes well.
		 * 
		 * Ironically much safer than the much faster versions of the constructor that specify
		 * an ID, since this finds a free block ID rather than erroring if the requested block
		 * ID is not free.
		 */
		this(config, name, FindFreeItemID());
	}
	
	public ItemBase(int id) {
		//Dumb version of the constructor. Use this if you want to make life harder for yourself.
		super(id);
	}

	@Override
	public String getEnglishName() {
		// TODO Auto-generated method stub
		return englishName;
	}

	@Override
	public String getGameRegistryName() {
		// TODO Auto-generated method stub
		return getUnlocalizedName();
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public IWeirdScienceBlock getSubItem(int meta) {
		// No sub items by default.
		return null;
	}

	@Override
	public ArrayList<IWeirdScienceItem> getSubItems() {
		// No sub items by default.
		return null;
	}

	@Override
	public boolean InCreativeTab() {
		// Is in creative tab by default.
		return true;
	}
	
	protected static int FindFreeItemID() {
		int i = itemIDSearchLowerBound;
		for(i = itemIDSearchLowerBound; i < 32000 /* max item ID */; ++i) {
			if(itemsList[i] == null) {
				return i;
			}
		}
		i = itemIDSearchLowerBound;
		//Should never be reached in practice.
        throw new RuntimeException("No free item IDs above " + itemIDSearchLowerBound + " available upon inspection in Weird Science's ItemBase.FindFreeItemID().");
	}

}
