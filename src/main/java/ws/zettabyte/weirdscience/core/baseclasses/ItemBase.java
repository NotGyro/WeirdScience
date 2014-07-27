package ws.zettabyte.weirdscience.core.baseclasses;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import ws.zettabyte.weirdscience.core.interfaces.ISubItem;
import ws.zettabyte.weirdscience.core.interfaces.IWeirdScienceItem;

public class ItemBase extends Item implements IWeirdScienceItem {
	
	protected String englishName;
	
	//Exclude viable block IDs from default ID values.
	protected static final int itemIDSearchLowerBound = 4096;
	
	protected int furnaceFuelValue = 0;

	public ItemBase(Configuration config, String name) {
		super();
		englishName = name;
		setUnlocalizedName(name.replace(" ", "")); //A default value. Absolutely acceptable to not keep it.
	}

	public ItemBase(String name) {
		super();
		englishName = name;
	}
	
	public ItemBase() {
		super();
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
	public ISubItem getSubItem(int meta) {
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
