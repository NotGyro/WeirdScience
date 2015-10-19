package ws.zettabyte.zettalib.client.gui;

import java.util.HashMap;

import ws.zettabyte.zettalib.client.gui.GUITemplate.GUIType;
import ws.zettabyte.zettalib.inventory.IDescriptiveInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GUIRegistry implements IGuiHandler {

	protected HashMap<Integer, GUITemplate> guis = new HashMap<Integer, GUITemplate>();
	public GUIRegistry() {}
	
	
	protected boolean addGUI(GUITemplate gui) {
		if(guis.containsKey(gui.getGuiID())) return false;
		guis.put(gui.getGuiID(), gui);
		return true;
	}

	@Override
	public Object getServerGuiElement ( int ID, EntityPlayer player, World world, int x, int y, int z ) {
		if(!guis.containsKey(ID)) return null;
		GUITemplate current = guis.get(ID);
		Object inv = null;
		
		if(current.invType == GUITemplate.GUIType.INV_FULL) {
			TileEntity tileEntity = world.getTileEntity(x, y, z);
			if(tileEntity != null) {
				inv = tileEntity;
			}
		}
		return current.buildContainer(inv, player.inventory);
	}

	@Override
	public Object getClientGuiElement ( int ID, EntityPlayer player, World world, int x, int y, int z ) {
		if(!guis.containsKey(ID)) return null;
		GUITemplate current = guis.get(ID);
		Object inv = null;
		
		if(current.invType == GUITemplate.GUIType.INV_FULL) {
			TileEntity tileEntity = world.getTileEntity(x, y, z);
			if(tileEntity != null) {
				inv = tileEntity;
			}
		}
		return current.buildScreen(inv, player.inventory);
	}
}
