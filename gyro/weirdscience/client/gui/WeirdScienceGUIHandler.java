package gyro.weirdscience.client.gui;

import gyro.weirdscience.tileentity.*;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class WeirdScienceGUIHandler implements IGuiHandler {
	@Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
            TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
            if(tileEntity instanceof TileEntityPhosphateEngine){
                    return new ContainerPhosphateEngine(player.inventory, (TileEntityPhosphateEngine) tileEntity);
            }
            return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
            TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
            if(tileEntity instanceof TileEntityPhosphateEngine){
                    return new GuiPhosphateEngine(player.inventory, (TileEntityPhosphateEngine) tileEntity);
            }
            return null;

    }

}
