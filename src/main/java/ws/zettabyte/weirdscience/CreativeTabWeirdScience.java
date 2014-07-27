package ws.zettabyte.weirdscience;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;


public class CreativeTabWeirdScience extends CreativeTabs
{
	public CreativeTabWeirdScience(int id, String name) {
		super(id, name);
	}
    public ItemStack getIconItemStack() {
    	return new ItemStack(net.minecraft.item.Item.potion, 1, 16454);
	}
};