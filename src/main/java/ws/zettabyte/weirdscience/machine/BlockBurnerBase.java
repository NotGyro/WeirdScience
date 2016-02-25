package ws.zettabyte.weirdscience.machine;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.RotationHelper;
import ws.zettabyte.zettalib.block.BlockContainerBase;
import ws.zettabyte.zettalib.block.RotationTools;
import ws.zettabyte.zettalib.thermal.IIgnitable;

/**
 * Created by Sam "Gyro" C. on 12/29/2015.
 */
public abstract class BlockBurnerBase extends BlockContainerBase implements IIgnitable {
    public BlockBurnerBase(Material material) {
        super(material);
    }

    @Override
    public boolean ignite(World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x,y,z);
        if(te != null) {
            //Should always be true, but this is modded Minecraft:
            if(te instanceof TEBurnerBase) {
                TEBurnerBase burner = (TEBurnerBase) te;
                burner.ignite();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isBurning(World world, int x, int y, int z) {
        return false; //TODO
    }
}
