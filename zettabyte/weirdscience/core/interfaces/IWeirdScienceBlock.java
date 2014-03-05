package zettabyte.weirdscience.core.interfaces;

import java.util.ArrayList;

import net.minecraft.block.material.Material;

public interface IWeirdScienceBlock extends IRegistrable {
	/**
	 * Returns null if there is no valid sub-block for this metadata. 
	 */
	ISubBlock getSubBlock(int meta);
	/**
	 * Must appear in the same numerical order as the metadata for the
	 * functional sub-blocks that exist for this block & block ID.
	 * Returns null if there are no sub-blocks.
	 */
	ArrayList<ISubBlock> getSubBlocks();
	int getHarvestLevel(int subBlockMeta);
	String getHarvestType(int subBlockMeta);
    void setMaterial(Material m);
    
    //Class<? extends ItemBlock> getItemBlock();
    
    boolean InCreativeTab();
}
