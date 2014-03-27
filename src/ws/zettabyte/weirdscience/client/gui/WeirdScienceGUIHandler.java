package ws.zettabyte.weirdscience.client.gui;

import ws.zettabyte.weirdscience.tileentity.*;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class WeirdScienceGUIHandler implements IGuiHandler {
	@Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
            TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
            if(tileEntity instanceof TileEntityNitrateEngine){
                    return new ContainerNitrateEngine(player.inventory, (TileEntityNitrateEngine) tileEntity);
            }
            return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
            TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
            if(tileEntity instanceof TileEntityNitrateEngine){
                    return new GuiNitrateEngine(player.inventory, (TileEntityNitrateEngine) tileEntity);
            }
            return null;

    }

}
