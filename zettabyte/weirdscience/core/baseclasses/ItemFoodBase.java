package zettabyte.weirdscience.core.baseclasses;

import java.util.ArrayList;

import net.minecraft.item.ItemFood;
import net.minecraftforge.common.Configuration;
import zettabyte.weirdscience.core.interfaces.IWeirdScienceBlock;
import zettabyte.weirdscience.core.interfaces.IWeirdScienceItem;

public class ItemFoodBase extends ItemFood implements IWeirdScienceItem {
	
	protected String englishName;
	
	//Exclude viable block IDs from default ID values.
	

	public static int defaultHungerRestore = 1;
	public static float defaultSaturation = 0.5f;

	public ItemFoodBase(Configuration config, String name, int defaultID) {
		/* 
		 * Real version of the constructor. Ultimately all other versions of the constructor turn into this.
		 * In this line, config looks up the block ID with a setting based upon the name argument.
		 */
		super(config.getItem(name + " item ID", defaultID).getInt(), 
				config.getItem(name + " properties", name + " hunger restore", defaultHungerRestore).getInt(defaultHungerRestore), 
				(float)config.get(name + " properties", name + " saturation", defaultSaturation).getDouble(defaultSaturation), false);
		englishName = name;
		setUnlocalizedName("item" + name.replace(" ", "")); //A default value. Absolutely acceptable to not keep it.
	}

	public ItemFoodBase(Configuration config, String name) {
		/* 
		 * For this version of the constructor, the default item ID is set to the result of 
		 * FindFreeItemID(), which should be a valid and available identifier if all goes well.
		 * 
		 * Ironically much safer than the much faster versions of the constructor that specify
		 * an ID, since this finds a free block ID rather than erroring if the requested block
		 * ID is not free.
		 */
		this(config, name, ItemBase.FindFreeItemID());
	}
	

	public ItemFoodBase(Configuration config, String name, int defaultID, int restore, float sat) {
		super(config.getItem(name + " item ID", defaultID).getInt(), restore, sat, false);
		englishName = name;
		setUnlocalizedName("item" + name.replace(" ", ""));
	}

	public ItemFoodBase(Configuration config, String name, int restore, float sat) {
		this(config, name, ItemBase.FindFreeItemID(), restore, sat);
	}
	
	public ItemFoodBase(int id) {
		//Dumb version of the constructor. Use this if you want to make life harder for yourself.
		super(id, defaultHungerRestore, defaultSaturation, false);
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
}
