package ws.zettabyte.weirdscience.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.common.util.RotationHelper;
import ws.zettabyte.zettalib.block.BlockContainerBase;
import ws.zettabyte.zettalib.block.IInfoTileEntity;
import ws.zettabyte.zettalib.initutils.ICreativeTabInfo;

public class BlockCatalyticEngine extends BlockContainerBase implements
		ICreativeTabInfo, IInfoTileEntity {
	public BlockCatalyticEngine(Material material) {
		super(material);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		// TODO Auto-generated method stub
		return new TileEntityCatalyticEngine();
	}

	@Override
	public Class getTileEntityType() {
		// TODO Auto-generated method stub
		return TileEntityCatalyticEngine.class;
	}
	
    @SideOnly(Side.CLIENT)
    public IIcon sidesIcon;
    @SideOnly(Side.CLIENT)
    public IIcon topIcon;
    @SideOnly(Side.CLIENT)
    public IIcon frontIcon;
    @SideOnly(Side.CLIENT)
    public IIcon frontIconPowered;
    
    
    //TODO: abstract this furnace-like side stuff out.
	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
		if (side == 1) {
			return topIcon;
		} else if (side == 0) {
			return sidesIcon;
		} else if (side == getFacing(meta)) {
			// Is it powered?
			if (isOperating(meta)) {
				return frontIconPowered;
			} else {
				return frontIcon;
			}
		} else {
			return sidesIcon;
		}
	}
	
    @SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		frontIcon = iconRegister.registerIcon("weirdscience:genericmachine5");
		sidesIcon = iconRegister.registerIcon("weirdscience:genericmachine");
		topIcon = iconRegister.registerIcon("weirdscience:genericmachine3");
		frontIconPowered = iconRegister
				.registerIcon("weirdscience:genericmachine5_active");
	}

	public boolean isOperating(int meta) {
		return ((meta & 8) > 0);
	}

	private int getFacing(int meta) {
		return meta & ~8;
	}

	@Override
	public boolean rotateBlock(World worldObj, int x, int y, int z,
			ForgeDirection axis) {
		// Dumb hacks ahoy. Should really find a better (but still non-verbose)
		// way to do this.
		return RotationHelper.rotateVanillaBlock(Blocks.furnace, worldObj,
				x, y, z, axis);
	}
	@Override
	public ForgeDirection[] getValidRotations(World worldObj, int x, int y,
			int z) {
		// Dumb hacks ahoy. Should really find a better (but still non-verbose)
		// way to do this.
		return RotationHelper.getValidVanillaBlockRotations(Blocks.furnace);
	}

	@Override
	public String getDebugInfo(IBlockAccess world, int x, int y, int z,
			int metadata) {
		String info = "Facing " + ForgeDirection.VALID_DIRECTIONS[getFacing(metadata)] + "\n";
		info += "According to metadata, this block is ";
		if(isOperating(metadata)) { info += "active.";} else { info += "inactive.";}
		info += "\n";
		return info + super.getDebugInfo(world, x, y, z, metadata);
	}
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z,
			EntityLivingBase placer, ItemStack thisItemStack) {
		int quadrant = (int) ((placer.rotationYaw / 360.0F) + 0.5F);

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
}
