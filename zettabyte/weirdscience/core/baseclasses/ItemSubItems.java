package zettabyte.weirdscience.core.baseclasses;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.common.Configuration;
import zettabyte.weirdscience.core.interfaces.ISubItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSubItems extends ItemBase {
	protected ArrayList<ISubItem> subItems = new ArrayList<ISubItem>(8);

	public void addSubItem(ISubItem b) {
		//Set our meta to what its position in the array will be.
		b.setAssociatedMeta(subItems.size());
		subItems.add(b);
	}

	@Override
	public ISubItem getSubItem(int damage) {
		// No sub items by default.
		return subItems.get(damage);
	}

	@Override
	public ArrayList<ISubItem> getSubItems() {
		// No sub items by default.
		return subItems;
	}

	

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister reg) {
		for(ISubItem b : subItems) {
			b.setIcon(reg.registerIcon(b.getTextureName()));
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack i) {
		if(subItems.get(i.getItemDamage()) != null) {
			return subItems.get(i.getItemDamage()).getUnlocalizedName();
		}
		return null;
	}
	@Override
    public Icon getIconFromDamage(int i) {
		if(subItems.get(i) != null) {
			return subItems.get(i).getIcon();
		}
		return null;
    }
	
	@Override
    public boolean getHasSubtypes()
    {
        return true;
    }

	//For the creative tab.
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int ourID, CreativeTabs cTab,
			List subItemsReturn) {
		for (int ix = 0; ix < subItems.size(); ix++) {
			if(subItems.get(ix).isInCreativeTab()) {
				subItemsReturn.add(new ItemStack(this.itemID, 1, subItems.get(ix).getAssociatedMeta()));
			}
		}
		//super.getSubItems(par1, cTab, subItemsReturn);
	}

	
	public ItemSubItems(Configuration config, String name, int defaultID) {
		super(config, name, defaultID);
		setHasSubtypes(true);
	}

	public ItemSubItems(Configuration config, String name) {
		super(config, name);
		setHasSubtypes(true);
	}

	public ItemSubItems(int id) {
		super(id);
		setHasSubtypes(true);
	}

}
