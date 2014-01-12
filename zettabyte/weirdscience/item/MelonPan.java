package zettabyte.weirdscience.item;

import zettabyte.weirdscience.CreativeTabWeirdScience;
import zettabyte.weirdscience.WeirdScience;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;

public class MelonPan extends ItemFood {
	private static final int hungerRestore = 3;
	public static final int craftCount = 1;
	public static Object[] recipe = {Item.bread, Item.melon};
	
	public MelonPan(int par1) {
		super(par1, hungerRestore, false);
		
		setCreativeTab(WeirdScience.tabWeirdScience);
		setUnlocalizedName("melonpan");
	}
	
	public void registerIcons(IconRegister reg) {
		if (this.itemID == WeirdScience.melonPan.itemID) {
			this.itemIcon = reg.registerIcon("weirdscience:melonpan");
		}
	}

}
