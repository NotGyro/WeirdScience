package zettabyte.weirdscience.core.baseclasses;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import zettabyte.weirdscience.core.interfaces.IWeirdScienceBlock;

public abstract class BlockContainerBase extends BlockContainer implements IWeirdScienceBlock {
	
	protected String englishName;
	
	//Exclude vanilla / terrain IDs from default ID values.
	protected static final int blockIDSearchLowerBound = 256;
	
	protected TileEntity protoTileEntity;
	
	public BlockContainerBase(Configuration config, String name, int defaultID, Material material) {
		/* 
		 * Real version of the constructor. Ultimately all other versions of the constructor turn into this.
		 * In this line, config looks up the block ID with a setting based upon the name argument.
		 */
		super(config.getBlock(name + " block ID", defaultID).getInt(), material);
		englishName = name;
		setUnlocalizedName("block" + name.replace(" ", "")); //A default value. Absolutely acceptable to not keep it.
	}
	public BlockContainerBase(Configuration config, String name, int defaultID) {
		/* 
		 * Default material set to rock.
		 */
		this(config, name, defaultID, Material.rock);
	}

	public BlockContainerBase(Configuration config, String name) {
		/* 
		 * For this version of the constructor, the default block ID is set to the result of 
		 * FindFreeBlockID(), which should be a valid and available identifier if all goes well.
		 * 
		 * Ironically much safer than the much faster versions of the constructor that specify
		 * an ID, since this finds a free block ID rather than erroring if the requested block
		 * ID is not free.
		 */
		this(config, name, FindFreeBlockID());
	}
	
	public BlockContainerBase(Configuration config, String name, Material material) {
		/* 
		 * A variant of BlockBase(Configuration config, String name) with Material specified.
		 * Probably the constructor of choice for an easy development cycle (i.e. lines of
		 * code to init a block will be lowest here).
		 */
		this(config, name, FindFreeBlockID(), material);
	}
	
	public BlockContainerBase(int id, Material material) {
		//Dumb version of the constructor. Use this if you want to make life harder for yourself.
		super(id, material);
        this.isBlockContainer = true;
	}

	//A slow, painful process. Don't use this outside of init.
	protected static int FindFreeBlockID() {
		for(int i = blockIDSearchLowerBound; i < 4096 /* block list size */; ++i) {
			if(blocksList[i] == null) {
				return i;
			}
		}
		//Should never be reached in practice.
        throw new IllegalArgumentException("No free block IDs above " + blockIDSearchLowerBound + " available upon inspection in Weird Science's BlockBase.FindFreeBlockID().");
	}

	@Override
	public String getEnglishName() {
		return englishName;
	}

	@Override
	public String getGameRegistryName() {
		return this.getUnlocalizedName();
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
    public Material getMaterial() {
        return this.blockMaterial;
    }
    
    public void setMaterial(Material m) {
    	//Deep dark voodoo. If you get a security exception, here it is. I'm sorry, I did it all for the greater good.
    	Field field;
		try {
			//Get the field of the block class.
			field = Block.class.getField("blockMaterial");
	        field.setAccessible(true);
	
	        //Modify the field to not be final.
	        Field modifiersField = Field.class.getDeclaredField("modifiers");
	        modifiersField.setAccessible(true);
	        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
	
	        field.set(this, m);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		//Make sure that the entries to canBlockGrass are still valid.
        canBlockGrass[blockID] = !m.getCanBlockGrass();
    }
	@Override
	public ArrayList<IWeirdScienceBlock> getSubBlocks() {
		//By default, no metadata-based sub-blocks.
		return null;
	}

	@Override
	public int getHarvestLevel(int subBlockMeta) {
		//By default, no metadata-based sub-blocks.
		return 1;
	}

	@Override
	public String getHarvestType(int subBlockMeta) {
		//By default, no metadata-based sub-blocks.
		return "pickaxe";
	}

	@Override
	public IWeirdScienceBlock getSubBlock(int meta) {
		//By default, no metadata-based sub-blocks.
		return null;
	}
	
	@Override
	public boolean InCreativeTab() { 
		return true;
	}
	
	@Override
	abstract public TileEntity createNewTileEntity(World world);
}
