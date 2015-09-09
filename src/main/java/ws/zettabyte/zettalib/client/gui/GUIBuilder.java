package ws.zettabyte.zettalib.client.gui;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import ws.zettabyte.zettalib.client.gui.widgets.WidgetContainer;
import ws.zettabyte.zettalib.inventory.ContainerPlayerInv;
import ws.zettabyte.zettalib.inventory.FluidTankNamed;
import ws.zettabyte.zettalib.inventory.IDescriptiveInventory;
import ws.zettabyte.zettalib.inventory.INamedTankInfo;
import ws.zettabyte.zettalib.inventory.ItemSlot;

/**
 * The standard method of producing ZettaLib GUIs. Add Widgets to it, 
 * and then call its constructScreen and constructContainer methods as appropriate.
 * 
 * One GUIBuilder per GUI screen, not one per GUI screen instance or one per mod.
 * 
 * @author Samuel "Gyro"
 *
 */
public class GUIBuilder {
    
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

	public GUIBuilder() {
	    rootWidget.setWidth(176);
	    rootWidget.setHeight(166);
	}
	
	/**
	 * Adds a widget to the root node of this GUIBuilder.
	 * 
	 * All added widgets and their children will be copied,
	 * and their copies will be passed to a GUI Screen class.
	 * 
	 * @return Was the widget successfully added?
	 */
	public boolean addWidget(IGUIWidget w) {
    	boolean ret = rootWidget.addChild(w);
    	if (ret) w.setParent(rootWidget);
    	return ret;
    }
	
	/**
	 * Anything that the GUIBuilder does to every widget in its tree should happen here.
	 * 
	 * TODO: Refactor to use a component system rather than separate types of widgets to process.
	 */
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
	/**
	 * Process all GUI elements which need information from obj,
	 * and re-position all of the Minecraft Slot objects such that they work properly
	 * with the container.
	 * 
	 * TODO: Refactor to use component system, split up a bit.
	 * 
	 * @param obj Whatever's providng the game logic backing our GUI (typically a Tile Entity).
	 */
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
				String name = e.getComponentName();
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

	/**
	 * Produces an instance of this GUI, with widgets from our widget tree
	 * and with the appropriate properties in terms of things like Container
	 * classes.
	 * 
	 * This function's output should be valid input for IGuiHandler.getClientGuiElement()
	 * 
	 * If obj is an IInventory, it will also call buildContainer().
	 * 
	 * @param obj Whatever's providing the game logic backing our GUI (typically a Tile Entity).
	 * @param inventoryPlayer The player opening this GUI.
	 */
	public GuiScreen buildScreen(Object obj, InventoryPlayer inventoryPlayer) {
		
		//Todo: sensitive to inventory type.
		SmartScreenBase result = (SmartScreenBase) new ZettaScreen(buildContainer(obj, inventoryPlayer));
		
		for(IGUIWidget e : rootWidget.getChildren()) {
			result.addWidget(e.copy());
		}
		
		return (GuiScreen)result;
	}
	
	/**
	 * Produces an instance of the server-side element for this GUI.
	 * 
	 * This function's output should be valid input for IGuiHandler.getServerGuiElement()
	 * 
	 * @param obj Whatever's providing the game logic backing our GUI (typically a Tile Entity).
	 * @param inventoryPlayer The player opening this GUI.
	 */
	public Container buildContainer(Object obj, InventoryPlayer inventoryPlayer) {
		buildSlotPositions(obj);
		
		//Todo: sensitive to inventory type.
		return (Container) new ContainerPlayerInv((IDescriptiveInventory)obj, inventoryPlayer);
	}
}
