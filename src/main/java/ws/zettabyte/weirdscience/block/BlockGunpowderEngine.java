package ws.zettabyte.weirdscience.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.common.util.RotationHelper;
import ws.zettabyte.weirdscience.tileentity.TileEntityGunpowderEngine;
import ws.zettabyte.weirdscience.tileentity.TileEntityNitrateEngine;
import ws.zettabyte.weirdscience.core.baseclasses.BlockContainerBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


//A copy-and-paste from BlockNitrateEngine.
//Soon I will abstract this functionality out
//to a BlockContainerRotatable or something like that.
public class BlockGunpowderEngine extends BlockContainerBase implements
		IBlockMetaPower {

	public BlockGunpowderEngine(Configuration config, String name,
			Material material) {
		super(config, name, material);
		// TODO Auto-generated constructor stub
	}

	public BlockGunpowderEngine(Configuration config, String name) {
		super(config, name);
		// TODO Auto-generated constructor stub
	}

	public BlockGunpowderEngine(Material material) {
		super(material);
		// TODO Auto-generated constructor stub
	}

	int teCapacity = 0;
	int tePerTick = 0;
	int tePerDirt = 0;

	private final Random itemDropRand = new Random(); // Randomize item drop
														// direction.

	@SideOnly(Side.CLIENT)
	public IIcon frontIcon;
	@SideOnly(Side.CLIENT)
	public IIcon frontIconPowered;
	@SideOnly(Side.CLIENT)
	public IIcon topIcon;
	@SideOnly(Side.CLIENT)
	public IIcon sidesIcon;

	/**
	 * Args: side, metadata
	 */
    @SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int meta) {
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

	protected static void initRotate(BlockGunpowderEngine b) {
		//BlockHelper.rotateType[b.blockID] = BlockHelper.RotationType.CHEST;
	}

	@Override
	public ForgeDirection[] getValidRotations(World worldObj, int x, int y,
			int z) {
		// Dumb hacks ahoy. Should really find a better (but still non-verbose)
		// way to do this.
		return RotationHelper.getValidVanillaBlockRotations(Blocks.furnace);
	}

	@Override
	public boolean rotateBlock(World worldObj, int x, int y, int z,
			ForgeDirection axis) {
		// Dumb hacks ahoy. Should really find a better (but still non-verbose)
		// way to do this.
		return RotationHelper.rotateVanillaBlock(Blocks.furnace, worldObj,
				x, y, z, axis);
	}

	public boolean hasComparatorInputOverride() {
		return true;
	}

    @SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z,
			int side) {
		return this.getIcon(side, world.getBlockMetadata(x, y, z));
	}

    @SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		//TODO: Visually differentiate this from the Nitrate Engine.
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

	@Override
	public TileEntity createNewTileEntity(World world) {
		TileEntityGunpowderEngine TE = new TileEntityGunpowderEngine();
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
				.getTileEntity(x, y, z));
	}

	//Toss away all item stacks on block break.
	
	@Override
	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int par5) {
		TileEntity te = world.getTileEntity(x, y, z);
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
		super.onBlockDestroyedByPlayer(world, x, y, z, par5);
	}

	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int metadata, float par1, float par2,
			float par3) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity == null || player.isSneaking()) {
			return false;
		}
		//TODO: A GUI for this block.
		//player.openGui(WeirdScience.instance, 0, world, x, y, z);
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
