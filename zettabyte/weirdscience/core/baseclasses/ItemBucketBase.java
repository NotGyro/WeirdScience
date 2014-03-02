package zettabyte.weirdscience.core.baseclasses;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.IFluidContainerItem;
import zettabyte.weirdscience.core.interfaces.IWeirdScienceBlock;
import zettabyte.weirdscience.core.interfaces.IWeirdScienceItem;

public class ItemBucketBase extends ItemBucket implements IWeirdScienceItem, IFluidContainerItem {
	
	protected String englishName;
	
	//Exclude viable block IDs from default ID values.
	protected static final int emptyBucketID = 69;
	
    /** Mojang hates us for making this private the first time around. */
    protected Block contained;
	
	public Block getContained() {
		return contained;
	}

	protected Fluid fluid;
	
	public Fluid getFluid() {
		return fluid;
	}

	public void setFluid(Fluid fluid) {
		this.fluid = fluid;
	}

	public ItemBucketBase(Configuration config, String name, int defaultID, Block bucketedBlock) {
		/* 
		 * Real version of the constructor. Ultimately all other versions of the constructor turn into this.
		 * In this line, config looks up the block ID with a setting based upon the name argument.
		 */
		super(config.getItem(name + " item ID", defaultID).getInt(), bucketedBlock.blockID);
		englishName = name;
		setUnlocalizedName("item" + name.replace(" ", "")); //A default value. Absolutely acceptable to not keep it.
		
		contained = bucketedBlock;
		
		if(bucketedBlock instanceof IFluidBlock) {
			fluid = ((IFluidBlock)bucketedBlock).getFluid();
		}
	}

	public ItemBucketBase(Configuration config, String name, Block bucketedBlock) {
		/* 
		 * For this version of the constructor, the default item ID is set to the result of 
		 * FindFreeItemID(), which should be a valid and available identifier if all goes well.
		 * 
		 * Ironically much safer than the much faster versions of the constructor that specify
		 * an ID, since this finds a free block ID rather than erroring if the requested block
		 * ID is not free.
		 */
		this(config, name, ItemBase.FindFreeItemID(), bucketedBlock);
	}
	
	public ItemBucketBase(int id, Block bucketedBlock) {
		//Dumb version of the constructor. Use this if you want to make life harder for yourself.
		super(id, bucketedBlock.blockID);
		if(bucketedBlock instanceof IFluidBlock) {
			fluid = ((IFluidBlock)bucketedBlock).getFluid();
		}
		contained = bucketedBlock;
	}

	@Override
	public String getEnglishName() {
		// TODO Auto-generated method stub
		return englishName;
	}

	@Override
	public String getGameRegistryName() {
		// TODO Auto-generated method stub
		return getUnlocalizedName();
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public IWeirdScienceBlock getSubItem(int meta) {
		// No sub items by default.
		return null;
	}

	@Override
	public ArrayList<IWeirdScienceItem> getSubItems() {
		// No sub items by default.
		return null;
	}

	@Override
	public boolean InCreativeTab() {
		// Is in creative tab by default.
		return true;
	}
	
	@Override
	public FluidStack getFluid(ItemStack container) {
		if(fluid != null) {
			return new FluidStack(fluid, 1000);
		}
		else {
			return null;
		}
	}

	@Override
	public int getCapacity(ItemStack container) {
		// TODO Auto-generated method stub
		return 1000;
	}

	@Override
	public int fill(ItemStack container, FluidStack resource, boolean doFill) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
		if(fluid == null) {
			return null;
		}
		//Cannot drain a whole bucket
		if(maxDrain < 1000) {
			return null;
		}
		FluidStack drained = new FluidStack(fluid, 1000);
		if(doDrain) {
			//Empty the bucket.
			//container.itemID = emptyBucketID;
			//container.stackSize = 1;
			//container.setItemDamage(0);
		}
		return drained;
	}

}
