package ws.zettabyte.weirdscience;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


public class CreativeTabWeirdScience extends CreativeTabs
{
	public CreativeTabWeirdScience(int id, String name) {
		super(id, name);
	}
	@Override
	@SideOnly(Side.CLIENT)
    public ItemStack getIconItemStack() {
    	return new ItemStack(net.minecraft.init.Items.potionitem, 1, 16454);
	}
	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() {
		return net.minecraft.init.Items.potionitem;
	}
};