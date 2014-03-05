package zettabyte.weirdscience.core.interfaces;

import java.util.ArrayList;

public interface IWeirdScienceItem extends IRegistrable {
	//Returns null if there is no valid sub-items for this damage value. 
	ISubItem getSubItem(int meta);
	/* Must appear in the same numerical order as the damage values for the 
	 * functional sub-items that exist for this item & item ID.
	 * Returns null if there are no sub-items.
	 */
	ArrayList<ISubItem> getSubItems();
    
    boolean InCreativeTab();
}
