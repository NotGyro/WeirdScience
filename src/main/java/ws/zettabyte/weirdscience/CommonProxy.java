package ws.zettabyte.weirdscience;

import java.util.HashMap;

import ws.zettabyte.zettalib.client.gui.GUIDescription;
import ws.zettabyte.zettalib.client.gui.OffsetRect2D;
import ws.zettabyte.zettalib.client.gui.widgets.WidgetContainer;
import ws.zettabyte.zettalib.client.gui.widgets.WidgetSimple;
import ws.zettabyte.zettalib.client.gui.widgets.WidgetSlot;
import ws.zettabyte.zettalib.inventory.IDescriptiveInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler {
	GUIDescription testInv;
	public CommonProxy() {
		testInv = new GUIDescription();
		int margin = 1;
		int currentHeight = 0;
		
		WidgetContainer inventoryPanel = new WidgetContainer();
		inventoryPanel.setWidth(100);
		inventoryPanel.setHeight(100);
		testInv.addWidget(inventoryPanel);
		inventoryPanel.centerX();
		inventoryPanel.centerY();

		WidgetSlot slotTest1 = new WidgetSlot(inventoryPanel, "fuel");
		slotTest1.setLayer(3);
		slotTest1.centerX();
		
		currentHeight = slotTest1.getHeight() + margin;
		
		WidgetSimple slotAlart = new WidgetSimple(inventoryPanel);
		slotAlart.setHeight(16);
		slotAlart.setWidth(22);
		slotAlart.centerX();
		slotAlart.setY(currentHeight);
		slotAlart.setLayer(3);
		slotAlart.setArt(new ResourceLocation("weirdscience", "textures/gui/iconDanger.png"));
		
		currentHeight += slotAlart.getHeight();
		currentHeight += margin;
		
		int realX = ((OffsetRect2D)slotTest1.getBounds()).getXRelative();

		WidgetSlot slotTest2 = new WidgetSlot(inventoryPanel, "out1");
		slotTest2.setX(realX - 10);
		slotTest2.setY(currentHeight);
		slotTest2.setLayer(3);
		
		WidgetSlot slotTest3 = new WidgetSlot(inventoryPanel, "out2");
		slotTest3.setX(realX + 10);
		slotTest3.setY(currentHeight);
		slotTest3.setLayer(3);
	}
	// Client stuff
	public void registerRenderers() {
		// TODO: Nothing. This is the server-side class.
	}
	public void registerSound() {
		// TODO: Nothing. This is the server-side class.
	}

	@Override
	public Object getServerGuiElement ( int ID, EntityPlayer player, World world, int x, int y, int z ) {
		if(ID == 0) {
			TileEntity tileEntity = world.getTileEntity(x, y, z);
			if(tileEntity instanceof IDescriptiveInventory) {
				return testInv.constructContainer(((IDescriptiveInventory)tileEntity), player.inventory);
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement ( int ID, EntityPlayer player, World world, int x, int y, int z ) {
		if(ID == 0) {
			TileEntity tileEntity = world.getTileEntity(x, y, z);
			if(tileEntity instanceof IDescriptiveInventory) {
				return testInv.constructScreen(((IDescriptiveInventory)tileEntity), player.inventory);
			}
		}
		return null;
	}
}