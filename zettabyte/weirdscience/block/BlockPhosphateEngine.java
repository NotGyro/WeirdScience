package zettabyte.weirdscience.block;


import java.util.Random;

import zettabyte.weirdscience.WeirdScience;
import zettabyte.weirdscience.tileentity.TileEntityPhosphateEngine;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;

public class BlockPhosphateEngine extends BlockContainer {

	public BlockPhosphateEngine(int id, Material material) {
		super(id, material);
		this.setCreativeTab(WeirdScience.tabWeirdScience);
	}

    public boolean hasComparatorInputOverride() {
        return true;
    }

    int teCapacity = 0;
    int tePerTick = 0;
    int tePerDirt = 0;

	public void setCapacity(int setTo) {
		teCapacity = setTo;
	}
	public void setOutputRate(int setTo) {
		tePerTick = setTo;
	}
	public void setEfficiency(int setTo) {
		tePerDirt = setTo;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		TileEntityPhosphateEngine TE = new TileEntityPhosphateEngine(tePerDirt, tePerTick, teCapacity);
		return TE;
	}
    /**
     * If hasComparatorInputOverride returns true, the return value from this is used instead of the redstone signal
     * strength when this block inputs to a comparator.
     */
    public int getComparatorInputOverride(World world, int x, int y, int z, int par5) {
        return Container.calcRedstoneFromInventory((IInventory)world.getBlockTileEntity(x, y, z));
    }
    private final Random itemDropRand = new Random(); //Randomize item drop direction.
	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
		TileEntityPhosphateEngine tileentity = (TileEntityPhosphateEngine)world.getBlockTileEntity(x, y, z);

		if (tileentity != null) {
			for (int slotiter = 0; slotiter < tileentity.getSizeInventory(); ++slotiter) {
				ItemStack itemstack = tileentity.getStackInSlot(slotiter);
				if (itemstack != null) {
					float xr = this.itemDropRand.nextFloat() * 0.8F + 0.1F;
					float yr = this.itemDropRand.nextFloat() * 0.8F + 0.1F;
					float zr = this.itemDropRand.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityItem = new EntityItem(world, (double)((float)x + xr), (double)((float)y + yr), 
                    		(double)((float)z + zr), itemstack);
                    world.spawnEntityInWorld(entityItem);
				}
			}
		}
        super.breakBlock(world, x, y, z, par5, par6);
    }
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int metadata, float par1, float par2, float par3) {
	    TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
	    if (tileEntity == null || player.isSneaking()) {
	            return false;
	    }
		player.openGui(WeirdScience.instance, 0, world, x, y, z);
	    return true;
	}
}
