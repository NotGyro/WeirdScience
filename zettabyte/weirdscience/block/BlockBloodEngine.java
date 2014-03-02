package zettabyte.weirdscience.block;


import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import zettabyte.weirdscience.core.baseclasses.BlockContainerBase;
import zettabyte.weirdscience.core.baseclasses.ItemBucketBase;
import zettabyte.weirdscience.tileentity.TileEntityFluidEngine;

public class BlockBloodEngine extends BlockContainerBase {

	public BlockBloodEngine(Configuration config, String name,
			int defaultID, Material material) {
		super(config, name, defaultID, material);
		// TODO Auto-generated constructor stub
	}

	public BlockBloodEngine(Configuration config, String name, int defaultID) {
		super(config, name, defaultID);
		// TODO Auto-generated constructor stub
	}

	public BlockBloodEngine(Configuration config, String name,
			Material material) {
		super(config, name, material);
		// TODO Auto-generated constructor stub
	}

	public BlockBloodEngine(Configuration config, String name) {
		super(config, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		
		TileEntityFluidEngine TE = new TileEntityFluidEngine("blood", "Hemoionic Dynamo");
		return TE;
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int metadata, float par1, float par2, float par3) {
		TileEntityFluidEngine tileEntity = (TileEntityFluidEngine)world.getBlockTileEntity(x, y, z);
	    if (tileEntity == null || player.isSneaking()) {
	            return false;
	    }
		ItemStack playerItem = player.inventory.getCurrentItem();

		//Check to see if the player is holding something that can be filled with a fluid.
		if (playerItem != null) {
			//Is the player's item THE ONE TRUE ITEM we're lookin' for?
			if (playerItem.getItem() instanceof ItemBucketBase) {
				if(((ItemBucketBase)playerItem.getItem()).getFluid().getName() == "blood") {
					//Get the amount of fluid we're trying to put in the TE.
					FluidStack toTry = new FluidStack(((ItemBucketBase)playerItem.getItem()).getFluid(), 1000);
					//First simulate the filling to make sure we can fit a whole bucket of blood in the engine.
					if(tileEntity.fill(ForgeDirection.UP, toTry, false) == 1000) {
						//Then, do it for real.
						tileEntity.fill(ForgeDirection.UP, toTry, true);
						//If they are not god they do not get infinite blood bucket.
						if(!player.capabilities.isCreativeMode) {
							player.inventory.setInventorySlotContents(player.inventory.currentItem, FluidContainerRegistry.EMPTY_BUCKET);
						}
					}
					return true;
				}
			}
		}
		//The player is not holding a blood bucket.
		//player.openGui(WeirdScience.instance, guiID, world, x, y, z);
		//return true;
		return false;
	}
}
