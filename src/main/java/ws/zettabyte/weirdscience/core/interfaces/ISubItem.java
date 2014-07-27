package ws.zettabyte.weirdscience.core.interfaces;

import net.minecraft.util.IIcon;

public interface ISubItem extends IRegistrable {
	void setTextureName(String textureName);
	String getTextureName();
	
	void setIcon(IIcon toSet);
	IIcon getIcon();

	String getUnlocalizedName();
	void setUnlocalizedName(String name);
	
	int getAssociatedMeta();
	void setAssociatedMeta(int setTo);
	
	boolean isInCreativeTab();
	
	int getFurnaceFuelValue();
}
