package ws.zettabyte.weirdscience.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.common.util.RotationHelper;
import ws.zettabyte.zettalib.block.BlockGeneric;
import ws.zettabyte.zettalib.block.CubeIconSet;
import ws.zettabyte.zettalib.block.RotationTools;

/**
 * Created by Sam "Gyro" C. on 12/21/2015.
 */
public class BlockIgniter extends BlockGeneric {
    @SideOnly(Side.CLIENT)
    public CubeIconSet icons = new CubeIconSet();

    public BlockIgniter(Material material) {
        super(material);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        //Porting danger spot
        ForgeDirection facing = ForgeDirection.VALID_DIRECTIONS[meta];

        return icons.getIconPistonStyle(ForgeDirection.VALID_DIRECTIONS[side], facing);
    }
    @Override
    public boolean rotateBlock(World worldObj, int x, int y, int z,
                               ForgeDirection axis) {
        // Dumb hacks ahoy. Should really find a better (but still non-verbose)
        // way to do this.
        return RotationHelper.rotateVanillaBlock(Blocks.dispenser, worldObj,
                x, y, z, axis);
    }
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z,
                                EntityLivingBase placer, ItemStack thisItemStack) {
        super.onBlockPlacedBy(world, x, y, z, placer, thisItemStack);
        if(world.isRemote) return;
        RotationTools.initSixDirBlock(world, x, y, z, placer);
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        icons.setAllSidesName("weirdscience:genericmachine");
        icons.setTextureName("weirdscience:genericmachine3", ForgeDirection.EAST);
        icons.registerBlockIcons(iconRegister);
    }
}
