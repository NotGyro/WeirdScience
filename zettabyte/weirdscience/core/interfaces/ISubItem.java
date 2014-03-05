package zettabyte.weirdscience.core.interfaces;

import net.minecraft.util.Icon;

public interface ISubItem extends IRegistrable {
	void setTextureName(String textureName);
	String getTextureName();
	
	void setIcon(Icon toSet);
	Icon getIcon();

	String getUnlocalizedName();
	void setUnlocalizedName(String name);
	
	int getAssociatedMeta();
	void setAssociatedMeta(int setTo);
	
	boolean isInCreativeTab();
}
