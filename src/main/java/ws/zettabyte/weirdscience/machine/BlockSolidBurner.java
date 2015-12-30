package ws.zettabyte.weirdscience.machine;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import ws.zettabyte.weirdscience.CommonProxy;
import ws.zettabyte.weirdscience.WeirdScience;

import java.util.Random;

/**
 * Created by Sam "Gyro" Cutlip on 12/29/2015.
 */
public class BlockSolidBurner extends BlockBurnerBase {

    private static Random itemDamageRand = new Random();
    public BlockSolidBurner(Material material) {
        super(material);
    }

    @Override
    public Class<? extends TileEntity> getTileEntityType() {
        return TEBurnerSolid.class;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TEBurnerSolid();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        TEBurnerSolid donationEntity = (TEBurnerSolid)tileEntity;
        if (tileEntity == null || player.isSneaking()) {
            return false;
        }
        ItemStack playerItem = player.inventory.getCurrentItem();

        //Check to see if the player is holding something that can be filled with a fluid.
        if (playerItem != null) {
            if (playerItem.getItem() == Items.flint_and_steel) {
                //If it is a flint and steel, we can light our burner.
                this.ignite(world, x, y, z);
                playerItem.attemptDamageItem(1, itemDamageRand);
                return true;
            }
        }
        player.openGui(WeirdScience.instance, CommonProxy.solidBurner.getGuiID(), world, x, y, z);
        return true;
    }
}
