package ws.zettabyte.weirdscience.core.baseclasses;

import java.util.ArrayList;

import net.minecraft.item.ItemFood;
import net.minecraftforge.common.config.Configuration;
import ws.zettabyte.weirdscience.core.interfaces.ISubItem;
import ws.zettabyte.weirdscience.core.interfaces.IWeirdScienceItem;

public class ItemFoodBase extends ItemFood implements IWeirdScienceItem {
	
	protected String englishName;

	protected int furnaceFuelValue = 0;
	
	//Exclude viable block IDs from default ID values.
	

	public static int defaultHungerRestore = 1;
	public static float defaultSaturation = 0.5f;

	//public ItemFoodBase(Configuration config, String name) {
	//	/* 
	//	 * Real version of the constructor. Ultimately all other versions of the constructor turn into this.
	//	 * In this line, config looks up the block ID with a setting based upon the name argument.
	//	 */
	//	super(config.getItem(name + " item ID", defaultID).getInt(), 
	//			config.getItem(name + " properties", name + " hunger restore", defaultHungerRestore).getInt(defaultHungerRestore), 
	//			(float)config.get(name + " properties", name + " saturation", defaultSaturation).getDouble(defaultSaturation), false);
	//	englishName = name;
	//	setUnlocalizedName(name.replace(" ", "")); //A default value. Absolutely acceptable to not keep it.
	//}
	public ItemFoodBase() {
		super(defaultHungerRestore, defaultSaturation, false);
	}
	
	@Override
	public String getEnglishName() {
		return englishName;
	}

	@Override
	public String getGameRegistryName() {
		return getUnlocalizedName();
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public ISubItem getSubItem(int damage) {
		// No sub items by default.
		return null;
	}

	@Override
	public ArrayList<ISubItem> getSubItems() {
		// No sub items by default.
		return null;
	}

	@Override
	public boolean InCreativeTab() {
		// Is in creative tab by default.
		return true;
	}
	
	@Override
	public int getFurnaceFuelValue() {
		return furnaceFuelValue;
	}
	
	public void setFurnaceFuelValue(int furnaceFuelValue) {
		this.furnaceFuelValue = furnaceFuelValue;
	}

}
