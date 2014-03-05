package zettabyte.weirdscience.core.baseclasses;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import zettabyte.weirdscience.core.chemistry.IBioactive;
import zettabyte.weirdscience.core.interfaces.ISubBlock;
import zettabyte.weirdscience.core.interfaces.IWeirdScienceBlock;

public class BlockFluidClassicWS extends BlockFluidClassic implements IWeirdScienceBlock {
	
	//Exclude vanilla / terrain IDs from default ID values.
	protected static final int blockIDSearchLowerBound = 256;
	public BlockFluidClassicWS(Configuration config, String name, int defaultID, Material material, Fluid fluid) {
		/* 
		 * Real version of the constructor. Ultimately all other versions of the constructor turn into this.
		 * In this line, config looks up the block ID with a setting based upon the name argument.
		 */
		super(config.getBlock(name + " block ID", defaultID).getInt(), fluid, material);
		englishName = name;
	}
	public BlockFluidClassicWS(Configuration config, String name, int defaultID, Fluid fluid) {
		/* 
		 * Default material set to water.
		 */
		this(config, name, defaultID, Material.water, fluid);
		setUnlocalizedName("block" + name.replace(" ", "")); //A default value. Absolutely acceptable to not keep it.
	}

	public BlockFluidClassicWS(Configuration config, String name, Fluid fluid) {
		/* 
		 * For this version of the constructor, the default block ID is set to the result of 
		 * FindFreeBlockID(), which should be a valid and available identifier if all goes well.
		 * 
		 * Ironically much safer than the much faster versions of the constructor that specify
		 * an ID, since this finds a free block ID rather than erroring if the requested block
		 * ID is not free.
		 */
		this(config, name, BlockBase.FindFreeBlockID(), fluid);
	}
	
	public BlockFluidClassicWS(Configuration config, String name, Material material, Fluid fluid) {
		/* 
		 * A variant of BlockBase(Configuration config, String name) with Material specified.
		 * Probably the constructor of choice for an easy development cycle (i.e. lines of
		 * code to init a block will be lowest here).
		 */
		this(config, name, BlockBase.FindFreeBlockID(), material, fluid);
	}
	
	public BlockFluidClassicWS(int id, Fluid fluid, Material material) {
		//Dumb version of the constructor. Use this if you want to make life harder for yourself.
		super(id, fluid, material);
	}


	
	protected String englishName;
	boolean entitiesInteract = true;
	public boolean isEntitiesInteract() {
		return entitiesInteract;
	}

	public void setEntitiesInteract(boolean entitiesInteract) {
		this.entitiesInteract = entitiesInteract;
	}

	@Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        if(entitiesInteract & (getFluid() instanceof IBioactive)) {
        	IBioactive bioFluid = (IBioactive)getFluid();
        	if(entity instanceof EntityLivingBase) {
        		bioFluid.contactAffectCreature((EntityLivingBase)entity);
        	}
        }
    }
    public void addDisplacementEntry(int id, boolean isDisplaced) {
    	this.displacementIds.put(id, isDisplaced);
    }

	@Override
	public String getEnglishName() {
		return englishName;
	}

	@Override
	public String getGameRegistryName() {
		return getUnlocalizedName();
	}

	@Override
	public boolean isEnabled() {
		return true;
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
	public ArrayList<ISubBlock> getSubBlocks() {
		//By default, no metadata-based sub-blocks.
		return null;
	}

	@Override
	public int getHarvestLevel(int subBlockMeta) {
		//By default, no metadata-based sub-blocks.
		return 0;
	}

	@Override
	public String getHarvestType(int subBlockMeta) {
		//By default, no metadata-based sub-blocks.
		return null;
	}

	@Override
	public ISubBlock getSubBlock(int meta) {
		//By default, no metadata-based sub-blocks.
		return null;
	}
	
	@Override
	public boolean InCreativeTab() { 
		//TODO
		//True by default for debugging. Should be false by default in release?
		return true;
	}

}
