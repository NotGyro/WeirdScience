package ws.zettabyte.weirdscience.core;

import net.minecraft.util.IIcon;
import ws.zettabyte.weirdscience.core.interfaces.ISubItem;

public class SubItem implements ISubItem {

	protected IIcon icon = null;
	protected String texName = "";
	protected String name = "";
	protected String englishName = "";
	protected int assocMeta = 0;
	protected int furnaceFuelValue = 0;
	
	public SubItem() {}
	
	public SubItem(String engName, String textureName) { 
		englishName = engName;
		texName = textureName;
		name = "item." + englishName.replace(" ", "");
	}
	
	@Override
	public void setTextureName(String textureName) {
		texName = textureName;
	}

	@Override
	public String getTextureName() {
		return texName;
	}

	@Override
	public void setIcon(IIcon toSet) {
		icon = toSet;
	}

	@Override
	public IIcon getIcon() {
		return icon;
	}

	@Override
	public String getUnlocalizedName() {
		return name;
	}

	@Override
	public void setUnlocalizedName(String nname) {
		name = nname;
	}

	@Override
	public int getAssociatedMeta() {
		return assocMeta;
	}

	@Override
	public void setAssociatedMeta(int setTo) {
		assocMeta = setTo;

	}

	@Override
	public boolean isInCreativeTab() {
		return true;
	}

	@Override
	public String getEnglishName() {
		return englishName;
	}

	@Override
	public String getGameRegistryName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public boolean isEnabled() {
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
