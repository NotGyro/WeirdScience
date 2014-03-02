package zettabyte.weirdscience.block;


import java.lang.reflect.Field;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import zettabyte.weirdscience.tileentity.TileEntityBloodEngine;

public class BlockBloodEngine extends BlockMetaTank {
	
	protected static final String fuelName= "blood";

	public BlockBloodEngine(Configuration config, String name,
			int defaultID, Material material) {
		super(config, name, defaultID, material);
	}

	public BlockBloodEngine(Configuration config, String name, int defaultID) {
		super(config, name, defaultID);
	}

	public BlockBloodEngine(Configuration config, String name,
			Material material) {
		super(config, name, material);
	}

	public BlockBloodEngine(Configuration config, String name) {
		super(config, name);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityBloodEngine(englishName, fuelName);
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int metadata, float par1, float par2, float par3) {
		TileEntityBloodEngine tileEntity = (TileEntityBloodEngine)world.getBlockTileEntity(x, y, z);
	    if (tileEntity == null || player.isSneaking()) {
	            return false;
	    }
		ItemStack playerItem = player.inventory.getCurrentItem();

		//Check to see if the player is holding something that can be filled with a fluid.
		if (playerItem != null) {
			//Is the player's item THE ONE TRUE ITEM we're lookin' for?
			if (playerItem.getItem() instanceof ItemBucket) {
				ItemBucket bucket = (ItemBucket)playerItem.getItem();
				//Do deep black Worst Practices hoodoo because Notch didn't design anything to be extended or generalized
				//Lucky for us the performance impact of reflection is negligable in this case since there won't be 
				//instances of right clicking on an engine with a bucket very often.
				int dumpedBlockID = 0;
				try {
					Field f = ItemBucket.class.getDeclaredField("isFull"); //NoSuchFieldException if there is no isFull
					f.setAccessible(true);
					dumpedBlockID = (Integer) f.get(bucket);
				} catch (Exception e) {
					e.printStackTrace();
				}
				//Does this bucket contain a fluid block?
				if(blocksList[dumpedBlockID] instanceof IFluidBlock) {
					Fluid fluidTry = ((IFluidBlock)blocksList[dumpedBlockID]).getFluid();
					//Is it blood?
					if(fluidTry.getName() == fuelName) {
						if(FillTank(fluidTry, tileEntity) && !player.capabilities.isCreativeMode) {
							player.inventory.setInventorySlotContents(player.inventory.currentItem, FluidContainerRegistry.EMPTY_BUCKET);
						}
						return true;
					}
				}
				//Now that we've tried one wonky kind of reflection, try another for TCon compat.
				if(playerItem.getItem().getUnlocalizedName().contentEquals("item.tconstruct.bucket")) {
					//Deeper voodoo begins here.
					try {
						Class tcontent = Class.forName("tconstruct.common.TContent");
						Field f = tcontent.getDeclaredField("fluidBlocks");
						Block[] fluidBlockArry = (Block[]) f.get(null);
						if(fluidBlockArry[playerItem.getItemDamage()].getUnlocalizedName().contentEquals("tile.liquid.blood")) {
							Fluid fluidTry = FluidRegistry.getFluid("blood");
							if(FillTank(fluidTry, tileEntity) && !player.capabilities.isCreativeMode) {
								player.inventory.setInventorySlotContents(player.inventory.currentItem, FluidContainerRegistry.EMPTY_BUCKET);
							}
							return true;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		//The player is not holding a blood bucket.
		//player.openGui(WeirdScience.instance, guiID, world, x, y, z);
		//return true;
		return false;
	}
	
	public boolean FillTank(Fluid f, TileEntityBloodEngine tileEntity) {
		//Get the amount of fluid we're trying to put in the TE.
		FluidStack toTry = new FluidStack(f, 1000);
		//First simulate the filling to make sure we can fit a whole bucket of blood in the engine.
		if(tileEntity.fill(ForgeDirection.UP, toTry, false) == 1000) {
			//Then, do it for real.
			tileEntity.fill(ForgeDirection.UP, toTry, true);
			//If they are not god they do not get infinite blood bucket.
			return true;
		}
		return false;
	}

}
