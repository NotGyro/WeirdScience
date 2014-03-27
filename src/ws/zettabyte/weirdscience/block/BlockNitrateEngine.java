package zettabyte.weirdscience.block;


import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.fluids.Fluid;
import zettabyte.weirdscience.WeirdScience;
import zettabyte.weirdscience.core.baseclasses.BlockContainerBase;
import zettabyte.weirdscience.tileentity.TileEntityNitrateEngine;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockNitrateEngine extends BlockContainerBase {

	public BlockNitrateEngine(Configuration config, String name,
			int defaultID, Material material) {
		super(config, name, defaultID, material);
		// TODO Auto-generated constructor stub
	}

	public BlockNitrateEngine(Configuration config, String name, int defaultID) {
		super(config, name, defaultID);
		// TODO Auto-generated constructor stub
	}

	public BlockNitrateEngine(Configuration config, String name,
			Material material) {
		super(config, name, material);
		// TODO Auto-generated constructor stub
	}

	public BlockNitrateEngine(Configuration config, String name) {
		super(config, name);
		// TODO Auto-generated constructor stub
	}

    public boolean hasComparatorInputOverride() {
        return true;
    }

    int teCapacity = 0;
    int tePerTick = 0;
    int tePerDirt = 0;
    @SideOnly(Side.CLIENT)
    public Icon sidesIcon;
    @SideOnly(Side.CLIENT)
    public Icon topIcon;
    public Icon bottomIcon;
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
        return this.getIcon(side, 0);
    }
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int metadata) {
    	if(side == 1) {
    		return topIcon;
    	}
    	else if(side == 0) {
    		return bottomIcon;
    	}
    	else {
    		return sidesIcon;
    	}
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {	
    	sidesIcon = iconRegister.registerIcon("weirdscience:genericmachine5");
    	bottomIcon = iconRegister.registerIcon("weirdscience:genericmachine");
    	topIcon = iconRegister.registerIcon("weirdscience:genericmachine3");
    }
    
    public static Fluid waste = null;

    public static void setWaste(Fluid w) {
    	waste = w;
    }
	@Override
	public TileEntity createNewTileEntity(World world) {
		TileEntityNitrateEngine TE = new TileEntityNitrateEngine();
		TE.setWaste(waste);
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
		TileEntityNitrateEngine tileentity = (TileEntityNitrateEngine)world.getBlockTileEntity(x, y, z);

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
