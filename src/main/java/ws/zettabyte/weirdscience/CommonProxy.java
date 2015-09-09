package ws.zettabyte.weirdscience;

import java.util.HashMap;

import ws.zettabyte.zettalib.client.gui.CommonIcons;
import ws.zettabyte.zettalib.client.gui.GUIBuilder;
import ws.zettabyte.zettalib.client.gui.widgets.WidgetAmountBar;
import ws.zettabyte.zettalib.client.gui.widgets.WidgetContainer;
import ws.zettabyte.zettalib.client.gui.widgets.WidgetFluidBar;
import ws.zettabyte.zettalib.client.gui.widgets.WidgetSimple;
import ws.zettabyte.zettalib.client.gui.widgets.WidgetSlot;
import ws.zettabyte.zettalib.inventory.IDescriptiveInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler {
	GUIBuilder testInv;
	public CommonProxy() {
		testInv = new GUIBuilder();
		int margin = 1;
		int currentHeight = 0;
		
		WidgetContainer inventoryPanel = new WidgetContainer();
		inventoryPanel.setWidth(100);
		inventoryPanel.setHeight(100);
		testInv.addWidget(inventoryPanel);
		inventoryPanel.centerX();
		inventoryPanel.setY(10);

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
		
		int realX = slotTest1.getXRelative();

		WidgetSlot slotTest2 = new WidgetSlot(inventoryPanel, "out1");
		slotTest2.setX(realX - 10);
		slotTest2.setY(currentHeight);
		slotTest2.setLayer(3);
		
		WidgetSlot slotTest3 = new WidgetSlot(inventoryPanel, "out2");
		slotTest3.setX(realX + 10);
		slotTest3.setY(currentHeight);
		slotTest3.setLayer(3);
		
		WidgetContainer tank = new WidgetContainer();
		tank.setX(4);
		tank.setY(4);
		tank.setHeight(72);
		tank.setWidth(26);
		tank.setLayer(4);
		
		WidgetFluidBar tankBar = new WidgetFluidBar("exhaust");
		tankBar.setDirection(WidgetAmountBar.EXPAND_DIR.UP);
		tankBar.setHeight(66);
		tankBar.setWidth(20);
		tankBar.setLayer(0);
		tankBar.setX(3);
		tankBar.setY(3);
		
		tankBar.setParent(tank);
		tank.addChild(tankBar);

		WidgetSimple tankBack = new WidgetSimple(tank);
		tankBack.setHeight(72);
		tankBack.setWidth(26);
		tankBack.setLayer(-1);
		tankBack.setArt(CommonIcons.tank1Background);
		
		WidgetSimple tankFront = new WidgetSimple(tank);
		tankFront.setHeight(72);
		tankFront.setWidth(26);
		tankFront.setLayer(1);
		tankFront.setArt(CommonIcons.tank1Overlay);
		
		testInv.addWidget(tank);
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
				return testInv.buildContainer(tileEntity, player.inventory);
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement ( int ID, EntityPlayer player, World world, int x, int y, int z ) {
		if(ID == 0) {
			TileEntity tileEntity = world.getTileEntity(x, y, z);
			if(tileEntity instanceof IDescriptiveInventory) {
				return testInv.buildScreen(tileEntity, player.inventory);
			}
		}
		return null;
	}
}