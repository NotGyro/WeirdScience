package ws.zettabyte.zettalib.client.gui;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import ws.zettabyte.zettalib.client.gui.widgets.WidgetContainer;
import ws.zettabyte.zettalib.inventory.ContainerPlayerInv;
import ws.zettabyte.zettalib.inventory.FluidTankNamed;
import ws.zettabyte.zettalib.inventory.IDescriptiveInventory;
import ws.zettabyte.zettalib.inventory.INamedTankInfo;
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

	protected ArrayList<IGUIItemSlot> slotInfo = new ArrayList<IGUIItemSlot>();
	protected ArrayList<IGUITank> tankInfo = new ArrayList<IGUITank>();

	public GUIDescription() {
	    rootWidget.setWidth(176);
	    rootWidget.setHeight(166);
	}
    
	public void addWidget(IGUIWidget w) {
    	rootWidget.addChild(w);
    	w.setParent(rootWidget);
    	return;
    }
	
	protected static void recurseProcess(IGUIWidget parent, ArrayList<IGUIItemSlot> slotInfo, ArrayList<IGUITank> tankInfo) {
		if(parent.getChildren() == null) {
			return;
		}
		for(IGUIWidget e : parent.getChildren()) {
			if(e instanceof IGUIItemSlot) {
				slotInfo.add((IGUIItemSlot) (e));
			}
			else if(e instanceof IGUITank) {
				tankInfo.add((IGUITank) (e));
			}
			recurseProcess(e, slotInfo, tankInfo);
		}
	}
	protected void buildSlotPositions(Object obj) {
		slotInfo.clear();
		tankInfo.clear();
		recurseProcess(rootWidget, slotInfo, tankInfo);
		//Handle item slots
		if(obj instanceof IDescriptiveInventory) {
			IDescriptiveInventory inv = (IDescriptiveInventory)obj;
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
		if(obj instanceof INamedTankInfo) {
			INamedTankInfo invTanks = (INamedTankInfo)obj;
			for(IGUITank e : tankInfo) {
				if(e == null) continue;
				String name = e.getName();
				//Does this GUITank have a name?
				if(name != null) {
					FluidTankNamed tank = invTanks.getTank(name);
					if(tank != null) {
						e.provideTank(tank);
					}
				}
			}
		}
	}

	public SmartScreenBase constructScreen(Object inv, InventoryPlayer inventoryPlayer) {
		
		//Todo: sensitive to inventory type.
		SmartScreenBase result = (SmartScreenBase) new ZettaScreen(constructContainer(inv, inventoryPlayer));
		
		for(IGUIWidget e : rootWidget.getChildren()) {
			result.addWidget(e.copy());
		}
		
		return result;
	}
	public Container constructContainer(Object inv, InventoryPlayer inventoryPlayer) {
		buildSlotPositions(inv);
		
		//Todo: sensitive to inventory type.
		return (Container) new ContainerPlayerInv((IDescriptiveInventory)inv, inventoryPlayer);
	}
}
