package ws.zettabyte.weirdscience.core.baseclasses;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import ws.zettabyte.weirdscience.core.interfaces.ISubBlock;
import ws.zettabyte.weirdscience.core.interfaces.IWeirdScienceBlock;

public class BlockBase extends Block implements IWeirdScienceBlock {
	
	protected String englishName;
	
	//Exclude vanilla / terrain IDs from default ID values.
	protected static final int blockIDSearchLowerBound = 256;
	
	public String harvestType = "pickaxe";
	public int harvestLevel = 1;
	protected ItemStack itemDropped;
	protected int droppedRandomBonus;
	
	public BlockBase(Configuration config, String name, Material material)
	{
		/* 
		 * Default material set to rock.
		 */
		super(material);
		englishName = name;
		this.setBlockName("block" + "." + name.replace(" ", "")); //A default value. Absolutely acceptable to not keep it.
	
	}

	public BlockBase(Configuration config, String name) {
		/* 
		 * For this version of the constructor, the default block ID is set to the result of 
		 * FindFreeBlockID(), which should be a valid and available identifier if all goes well.
		 * 
		 * Ironically much safer than the much faster versions of the constructor that specify
		 * an ID, since this finds a free block ID rather than erroring if the requested block
		 * ID is not free.
		 */
		this(config, name, Material.rock);
	}
	public BlockBase(Material material) {
		super(material);
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
        //canBlockGrass = !m.getCanBlockGrass();
    }
	@Override
	public ArrayList<ISubBlock> getSubBlocks() {
		//By default, no metadata-based sub-blocks.
		return null;
	}

	@Override
	public int getHarvestLevel(int subBlockMeta) {
		//By default, no metadata-based sub-blocks.
		return harvestLevel;
	}

	@Override
	public String getHarvestType(int subBlockMeta) {
		//By default, no metadata-based sub-blocks.
		return harvestType;
	}

	@Override
	public ISubBlock getSubBlock(int meta) {
		//By default, no metadata-based sub-blocks.
		return null;
	}
	
	@Override
	public boolean InCreativeTab() { 
		return true;
	}
	@Override
	public int quantityDropped(Random random) {
		// TODO Auto-generated method stub
		return this.quantityDroppedWithBonus(0, random);
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World p_getDrops_1_,
			int p_getDrops_2_, int p_getDrops_3_, int p_getDrops_4_,
			int p_getDrops_5_, int p_getDrops_6_) {
		// TODO Auto-generated method stub
		if(this.itemDropped != null)
		{
			ArrayList<ItemStack> result = new ArrayList<ItemStack>(1);
			result.add(itemDropped);
			return result;
		}
		else
		{
			return super.getDrops(p_getDrops_1_,
					p_getDrops_2_, p_getDrops_3_, p_getDrops_4_,
					p_getDrops_5_, p_getDrops_6_);
		}
	}
	@Override
	public int quantityDroppedWithBonus(int bonus, Random random) {
		if(this.itemDropped != null) {
			return itemDropped.stackSize + random.nextInt(bonus + droppedRandomBonus);
		}
		else {
			return super.quantityDroppedWithBonus(bonus+ droppedRandomBonus, random);
		}
	}
	@Override
	public int quantityDropped(int meta, int fortune, Random random) {
		return this.quantityDroppedWithBonus(fortune, random);
	}
	@Override
    public int damageDropped(int par1)
    {
		if(this.itemDropped != null) {
			return itemDropped.getItemDamage();
		}
		else {
			return super.damageDropped(par1);
		}
    }
	public ItemStack getItemDropped() {
		return itemDropped;
	}
	public void setItemDropped(ItemStack itemDropped) {
		this.itemDropped = itemDropped;
	}
	public int getDroppedRandomBonus() {
		return droppedRandomBonus;
	}
	public void setDroppedRandomBonus(int droppedRandomBonus) {
		this.droppedRandomBonus = droppedRandomBonus;
	}

}
