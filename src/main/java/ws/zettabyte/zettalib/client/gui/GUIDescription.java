package ws.zettabyte.zettalib.client.gui;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import ws.zettabyte.zettalib.client.gui.widgets.WidgetContainer;
import ws.zettabyte.zettalib.inventory.ContainerPlayerInv;
import ws.zettabyte.zettalib.inventory.IDescriptiveInventory;
import ws.zettabyte.zettalib.inventory.ItemSlot;

/**
 * One per TYPE OF GUI. Operates as a GUI factory, among other things.
 * @author Samuel "Gyro"
 *
 */
public class GUIDescription {
    
	//Not copied.
	protected WidgetContainer rootWidget = new WidgetContainer();
	
	public enum GUIType {
		INV_FULL,
		INV_HOTBAR,
		INV_NONE
	}
	
	protected GUIType invType = GUIType.INV_FULL;
	
	protected ArrayList<IGUIItemSlot> slotInfo = null;

	public GUIDescription() {
	    rootWidget.setWidth(176);
	    rootWidget.setHeight(166);
	}
    
	public void addWidget(IGUIWidget w) {
    	rootWidget.addChild(w);
    	w.setParent(rootWidget);
    	return;
    }
	
	protected static void recurseGetSlots(IGUIWidget parent, ArrayList<IGUIItemSlot> list) {
		if(parent.getChildren() == null) {
			return;
		}
		for(IGUIWidget e : parent.getChildren()) {
			if(e instanceof IGUIItemSlot) {
				list.add((IGUIItemSlot) (e));
			}
			recurseGetSlots(e, list);
		}
	}
	protected void buildSlotPositions(IDescriptiveInventory inv) {
		slotInfo = new ArrayList<IGUIItemSlot>();
		recurseGetSlots(rootWidget, slotInfo);
		for(IGUIItemSlot e : slotInfo) {
			if(e == null) continue;
			//Search for an appropriate itemSlot
			//Does this GUISlot know the slot number?
			int idx = e.getSlotIndex();
			String name = e.getName();
			if(idx != -1) {
				ItemSlot slot = inv.getSlot(idx);
				if(slot != null) {
					//Isn't it a little stupid that this is on the "Server-sided" part of the container?
					slot.xDisplayPosition = e.getX();
					slot.yDisplayPosition = e.getY();
					slot.guiInfo = e;
				}
			}
			//Does this GUISlot have a name?
			else if(name != null) {
				ItemSlot slot = inv.getSlot(name);
				if(slot != null) {
					//Isn't it a little stupid that this is on the "Server-sided" part of the container?
					slot.xDisplayPosition = e.getX();
					slot.yDisplayPosition = e.getY();
					slot.guiInfo = e;
				}
			}
		}
	}

	public SmartScreenBase constructScreen(IDescriptiveInventory inv, InventoryPlayer inventoryPlayer) {
		//Todo: sensitive to inventory type.
		SmartScreenBase result = (SmartScreenBase) new ZettaScreen(constructContainer(inv, inventoryPlayer));
		for(IGUIWidget e : rootWidget.getChildren()) {
			result.addWidget(e.copy());
		}
		return result;
	}
	public Container constructContainer(IDescriptiveInventory inv, InventoryPlayer inventoryPlayer) {
		if(slotInfo == null) buildSlotPositions(inv);
		//Todo: sensitive to inventory type.
		return (Container) new ContainerPlayerInv(inv, inventoryPlayer);
	}
}
