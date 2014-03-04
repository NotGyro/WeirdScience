package zettabyte.weirdscience.core.interfaces;

import net.minecraft.client.renderer.texture.IconRegister;

public interface ISubItem extends IRegistrable {
	void setTextureName(String textureName);
	String getTextureName();

	String getUnlocalizedName();
	void setUnlocalizedName(String name);
	
	int getAssociatedMeta();
	void setAssociatedMeta(int setTo);
	
	void registerIcons(IconRegister reg);
	
	boolean isInCreativeTab();
}
