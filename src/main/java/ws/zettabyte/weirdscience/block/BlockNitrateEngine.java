package ws.zettabyte.weirdscience.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
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
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.RotationHelper;
import net.minecraftforge.fluids.Fluid;
import ws.zettabyte.weirdscience.WeirdScience;
import ws.zettabyte.weirdscience.tileentity.TileEntityNitrateEngine;
import ws.zettabyte.zettalib.baseclasses.BlockContainerBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockNitrateEngine extends BlockContainerBase implements
		IBlockMetaPower {

	int teCapacity = 0;
	int tePerTick = 0;
	int tePerDirt = 0;

	private final Random itemDropRand = new Random(); // Randomize item drop
														// direction.

	public static Fluid waste = null;

	@SideOnly(Side.CLIENT)
	public Icon frontIcon;
	@SideOnly(Side.CLIENT)
	public Icon frontIconPowered;
	@SideOnly(Side.CLIENT)
	public Icon topIcon;
	@SideOnly(Side.CLIENT)
	public Icon sidesIcon;

	/**
	 * Args: side, metadata
	 */
    @SideOnly(Side.CLIENT)
	@Override
	public Icon getIcon(int side, int meta) {
		if (side == 1) {
			return topIcon;
		} else if (side == 0) {
			return sidesIcon;
		} else if (side == (meta & ~8)) {
			// Is it powered?
			if ((meta & 8) > 0) {
				return frontIconPowered;
			} else {
				return frontIcon;
			}
		} else {
			return sidesIcon;
		}
	}

	protected static void initRotate(BlockNitrateEngine b) {
		//BlockHelper.rotateType[b.blockID] = BlockHelper.RotationType.CHEST;
	}

	@Override
	public ForgeDirection[] getValidRotations(World worldObj, int x, int y,
			int z) {
		// Dumb hacks ahoy. Should really find a better (but still non-verbose)
		// way to do this.
		return RotationHelper.getValidVanillaBlockRotations(Block.furnaceIdle);
	}

	@Override
	public boolean rotateBlock(World worldObj, int x, int y, int z,
			ForgeDirection axis) {
		// Dumb hacks ahoy. Should really find a better (but still non-verbose)
		// way to do this.
		return RotationHelper.rotateVanillaBlock(Block.furnaceIdle, worldObj,
				x, y, z, axis);
	}

	public BlockNitrateEngine(Configuration config, String name, int defaultID,
			Material material) {
		super(config, name, defaultID, material);
		initRotate(this);
	}

	public BlockNitrateEngine(Configuration config, String name, int defaultID) {
		super(config, name, defaultID);
		initRotate(this);
	}

	public BlockNitrateEngine(Configuration config, String name,
			Material material) {
		super(config, name, material);
		initRotate(this);
	}

	public BlockNitrateEngine(Configuration config, String name) {
		super(config, name);
		initRotate(this);
	}

	public boolean hasComparatorInputOverride() {
		return true;
	}

    @SideOnly(Side.CLIENT)
	@Override
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z,
			int side) {
		return this.getIcon(side, world.getBlockMetadata(x, y, z));
	}

    @SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister iconRegister) {
		frontIcon = iconRegister.registerIcon("weirdscience:genericmachine5");
		sidesIcon = iconRegister.registerIcon("weirdscience:genericmachine");
		topIcon = iconRegister.registerIcon("weirdscience:genericmachine3");
		frontIconPowered = iconRegister
				.registerIcon("weirdscience:genericmachine5_active");
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z,
			EntityLivingBase placer, ItemStack thisItemStack) {
		int quadrant = (int) ((placer.rotationYaw * 4.0F / 360.0F) + 0.5F);

		// Modulo out any 360 degree dealies.
		quadrant %= 4;

		/*
		 * public static final ForgeDirection[] VALID_DIRECTIONS = {DOWN, UP,
		 * NORTH, SOUTH, WEST, EAST}; 0 1 2 3 4 5
		 */
		// Facing south
		if (quadrant == 0) {
			world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		}

		// Facing west
		else if (quadrant == 1) {
			world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		}

		// Facing north
		else if (quadrant == 2) {
			world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		}

		// Facing east
		else if (quadrant == 3) {
			world.setBlockMetadataWithNotify(x, y, z, 4, 2);
		}
	}

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
	 * If hasComparatorInputOverride returns true, the return value from this is
	 * used instead of the redstone signal strength when this block inputs to a
	 * comparator.
	 */
	public int getComparatorInputOverride(World world, int x, int y, int z,
			int par5) {
		return Container.calcRedstoneFromInventory((IInventory) world
				.getBlockTileEntity(x, y, z));
	}

	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (te != null) {
			if (te instanceof TileEntityNitrateEngine) {
				TileEntityNitrateEngine tileentity = (TileEntityNitrateEngine) te;
				for (int slotiter = 0; slotiter < tileentity.getSizeInventory(); ++slotiter) {
					ItemStack itemstack = tileentity.getStackInSlot(slotiter);
					if (itemstack != null) {
						float xr = this.itemDropRand.nextFloat() * 0.8F + 0.1F;
						float yr = this.itemDropRand.nextFloat() * 0.8F + 0.1F;
						float zr = this.itemDropRand.nextFloat() * 0.8F + 0.1F;
						EntityItem entityItem = new EntityItem(world,
								(double) ((float) x + xr),
								(double) ((float) y + yr),
								(double) ((float) z + zr), itemstack);
						world.spawnEntityInWorld(entityItem);
					}
				}
			}
		}
		super.breakBlock(world, x, y, z, par5, par6);
	}

	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int metadata, float par1, float par2,
			float par3) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (tileEntity == null || player.isSneaking()) {
			return false;
		}
		player.openGui(WeirdScience.instance, 0, world, x, y, z);
		return true;
	}

	@Override
	public void recievePowerOn(World world, int x, int y, int z) {
		// Bitmask bit 8 to on
		world.setBlockMetadataWithNotify(x, y, z,
				world.getBlockMetadata(x, y, z) | 8, 2);

	}

	@Override
	public void recievePowerOff(World world, int x, int y, int z) {
		/*
		 * Bitmask bit 8 to off by &ing it with the bitwise complement of 8
		 * (which is to say ~8).
		 */
		world.setBlockMetadataWithNotify(x, y, z,
				world.getBlockMetadata(x, y, z) & ~8, 2);
	}
}
