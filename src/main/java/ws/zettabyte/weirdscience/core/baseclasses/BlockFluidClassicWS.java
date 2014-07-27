package ws.zettabyte.weirdscience.core.baseclasses;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import ws.zettabyte.weirdscience.core.chemistry.IBioactive;
import ws.zettabyte.weirdscience.core.interfaces.ISubBlock;
import ws.zettabyte.weirdscience.core.interfaces.IWeirdScienceBlock;

public class BlockFluidClassicWS extends BlockFluidClassic implements IWeirdScienceBlock {
	
	//Which blocks are displaced by this one?
	protected HashMap<Block, Boolean> displacementBlocks = new HashMap<Block, Boolean>();
	
	public BlockFluidClassicWS(String name, Material material, Fluid fluid) {
		/* 
		 * Real version of the constructor. Ultimately all other versions of the constructor turn into this.
		 * In this line, config looks up the block ID with a setting based upon the name argument.
		 */
		super(fluid, material);
		englishName = name;
	}
	
	public BlockFluidClassicWS(Material material, Fluid fluid) {
		/* 
		 * A variant of BlockBase(Configuration config, String name) with Material specified.
		 * Probably the constructor of choice for an easy development cycle (i.e. lines of
		 * code to init a block will be lowest here).
		 */
		super(fluid, material);
	}

	public BlockFluidClassicWS(Fluid fluid) {
		//Dumb version of the constructor. Use this if you want to make life harder for yourself.
		super(fluid, Material.water);
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
    public void addDisplacementEntry(Block b, boolean isDisplaced) {
    	this.displacementBlocks.put(b, isDisplaced);
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
