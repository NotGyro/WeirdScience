package ws.zettabyte.weirdscience;

import java.util.HashMap;

import ws.zettabyte.zettalib.client.gui.GUIDescription;
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
		int top = 20;
		int currentHeight = top;

		WidgetSlot slotTest1 = new WidgetSlot("fuel");
		slotTest1.setX(91);
		slotTest1.setY(currentHeight);
		slotTest1.setLayer(3);
		testInv.addWidget(slotTest1);
		
		currentHeight += slotTest1.getHeight();
		currentHeight += margin;
		
		WidgetSimple slotAlart = new WidgetSimple();
		slotAlart.setHeight(16);
		slotAlart.setWidth(22);
		slotAlart.setX(89);
		slotAlart.setY(currentHeight);
		slotAlart.setLayer(3);
		slotAlart.setArt(new ResourceLocation("weirdscience", "textures/gui/iconDanger.png"));
		testInv.addWidget(slotAlart);
		
		currentHeight += slotAlart.getHeight();
		currentHeight += margin;
		

		WidgetSlot slotTest2 = new WidgetSlot("out1");
		slotTest2.setX(81);
		slotTest2.setY(currentHeight);
		slotTest2.setLayer(3);
		testInv.addWidget(slotTest2);
		
		WidgetSlot slotTest3 = new WidgetSlot("out2");
		slotTest3.setX(101);
		slotTest3.setY(currentHeight);
		slotTest3.setLayer(3);
		testInv.addWidget(slotTest3);
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