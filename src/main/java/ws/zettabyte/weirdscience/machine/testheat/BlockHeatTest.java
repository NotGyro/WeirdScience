package ws.zettabyte.weirdscience.machine.testheat;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.RotationHelper;
import ws.zettabyte.weirdscience.CommonProxy;
import ws.zettabyte.weirdscience.WeirdScience;
import ws.zettabyte.zettalib.block.BlockContainerBase;
import ws.zettabyte.zettalib.block.IInfoTileEntity;
import ws.zettabyte.zettalib.block.IMetaActive;
import ws.zettabyte.zettalib.initutils.ICreativeTabInfo;

public class BlockHeatTest extends BlockContainerBase implements
		ICreativeTabInfo, IInfoTileEntity {
	public BlockHeatTest(Material material) {
		super(material);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TEHeatTest();
	}

	@Override
	public Class getTileEntityType() {
		return TEHeatTest.class;
	}

	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int metadata, float par1, float par2,
			float par3) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity == null || player.isSneaking()) {
			return false;
		}
		player.openGui(WeirdScience.instance, CommonProxy.testHeat.getGuiID(), world, x, y, z);
		return true;
	}
}
