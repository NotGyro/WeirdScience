package ws.zettabyte.weirdscience.core.interfaces;

import net.minecraft.client.renderer.texture.IIconRegister;

public interface ISubBlock extends IRegistrable {
	void setTextureName(String textureName, int side);
	String getTextureName(int side);

	String getUnlocalizedName();
	void setUnlocalizedName(String name);
	
	int getAssociatedMeta();
	void setAssociatedMeta(int setTo);
	
	void registerIcons(IIconRegister reg);

	void setHarvestLevel(int levelHarvest);
	void setHarvestType(String nameType);

	int getHarvestLevel();
	String getHarvestType();
	
	float getHardness();
	void setHardness(float hardness);
	
	boolean isInCreativeTab();
}
