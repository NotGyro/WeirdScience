package ws.zettabyte.weirdscience.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.common.util.RotationHelper;
import ws.zettabyte.weirdscience.CommonProxy;
import ws.zettabyte.weirdscience.WeirdScience;
import ws.zettabyte.zettalib.block.BlockContainerBase;
import ws.zettabyte.zettalib.block.IInfoTileEntity;
import ws.zettabyte.zettalib.block.IMetaActive;
import ws.zettabyte.zettalib.block.CubeIconSet;
import ws.zettabyte.zettalib.block.RotationTools;
import ws.zettabyte.zettalib.initutils.ICreativeTabInfo;

public class BlockCatalyticEngine extends BlockContainerBase implements
		ICreativeTabInfo, IInfoTileEntity, IMetaActive {
	
    @SideOnly(Side.CLIENT)
    public CubeIconSet iconsInactive = new CubeIconSet();
    public CubeIconSet iconsActive = new CubeIconSet();
    
	public BlockCatalyticEngine(Material material) {
		super(material);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TECatalyticEngine();
	}

	@Override
	public Class getTileEntityType() {
		return TECatalyticEngine.class;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
		if(isOperating(meta)){
			return iconsActive.getIconFurnaceStyle(ForgeDirection.VALID_DIRECTIONS[side], 
					ForgeDirection.VALID_DIRECTIONS[getFacing(meta)]);
		}
		else {
			return iconsInactive.getIconFurnaceStyle(ForgeDirection.VALID_DIRECTIONS[side], 
					ForgeDirection.VALID_DIRECTIONS[getFacing(meta)]);
		}
	}
	
    @SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
    	iconsInactive.setAllSidesName("weirdscience:genericmachine");
    	iconsInactive.setTextureName("weirdscience:genericmachine5", ForgeDirection.EAST);
    	iconsInactive.setTextureName("weirdscience:genericmachine3", ForgeDirection.UP);

    	iconsInactive.setTextureName("weirdscience:genericmachine4", ForgeDirection.NORTH);
    	
    	iconsActive.makeCopy(iconsInactive);
    	iconsActive.setTextureName("weirdscience:genericmachine5_active", ForgeDirection.EAST);

    	iconsInactive.registerBlockIcons(iconRegister);
    	iconsActive.registerBlockIcons(iconRegister);
    	
		/*frontIcon = iconRegister.registerIcon("weirdscience:genericmachine5");
		sidesIcon = iconRegister.registerIcon("weirdscience:genericmachine");
		topIcon = iconRegister.registerIcon("weirdscience:genericmachine3");
		frontIconPowered = iconRegister
				.registerIcon("weirdscience:genericmachine5_active");*/
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
	public void onBlockPlacedBy(World world, int x, int y, int z,
			EntityLivingBase placer, ItemStack thisItemStack) {
		super.onBlockPlacedBy(world, x, y, z, placer, thisItemStack);
		if(world.isRemote) return;
		RotationTools.initFourDirBlock(world, x, y, z, placer);
	}
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int metadata, float par1, float par2,
			float par3) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity == null || player.isSneaking()) {
			return false;
		}
		player.openGui(WeirdScience.instance, CommonProxy.catalyticInv.getGuiID(), world, x, y, z);
		return true;
	}

	@Override
	public void setActiveStatus(boolean status, World world, int x,
			int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);
		
		if(isOperating(meta) == status) return;
		if(status) {
			world.setBlockMetadataWithNotify(x, y, z, 
				this.getFacing(meta)|8, 2);
		}
		else {
			world.setBlockMetadataWithNotify(x, y, z, 
					this.getFacing(meta), 2);
		}
	}

	@Override
	public boolean getActiveStatus(World world, int x, int y, int z) {
		return isOperating(world.getBlockMetadata(x, y, z));
	}
}
