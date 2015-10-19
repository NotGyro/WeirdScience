package ws.zettabyte.zettalib.client.gui;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import ws.zettabyte.zettalib.client.gui.widgets.WidgetContainer;
import ws.zettabyte.zettalib.inventory.ContainerPlayerInv;
import ws.zettabyte.zettalib.inventory.FluidTankNamed;
import ws.zettabyte.zettalib.inventory.ICleanableContainer;
import ws.zettabyte.zettalib.inventory.IComponentContainer;
import ws.zettabyte.zettalib.inventory.IComponentReceiver;
import ws.zettabyte.zettalib.inventory.IDescriptiveInventory;
import ws.zettabyte.zettalib.inventory.IInvComponent;
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
public class GUITemplate extends GUIManagerWidgets implements IGUI {
    
	//Not copied.
	//protected WidgetContainer rootWidget = new WidgetContainer();
	
	public enum GUIType {
		INV_FULL,
		INV_HOTBAR,
		INV_NONE
	}
	
	protected GUIType invType = GUIType.INV_FULL; //TODO: Options for this.

	protected ArrayList<IComponentReceiver> componentWidgets = new ArrayList<IComponentReceiver>();

	public GUITemplate() {
		super();
	}
	
	/**
	 * Anything that the GUIBuilder does to every widget in its tree will involve this function.
	 */
	protected static void recurseProcess(IGUIWidget parent, ArrayList<IComponentReceiver> list) {
		if(parent.getChildren() == null) {
			return;
		}
		for(IGUIWidget e : parent.getChildren()) {
			if(e instanceof IComponentReceiver) {
				list.add((IComponentReceiver) (e));
			}
			recurseProcess(e, list);
		}
	}
	/**
	 * Process all GUI elements which need information from obj,
	 * and re-position all of the Minecraft Slot objects such that they work properly
	 * with the container.
	 * 
	 * @param obj Whatever's providing the game logic backing our GUI (typically a Tile Entity)?
	 * @param gui The GUI to which we'll be linking various values from the object.
	 * @param componentWidgets An array to reuse. Totally optional, there to not churn so much memory.
	 */
	protected static void processComponents(Object obj, IGUI gui, ArrayList<IComponentReceiver> componentWidgets) {
		if(componentWidgets == null) {
			componentWidgets = new ArrayList<IComponentReceiver>(8);
		}
		else {
			componentWidgets.clear();
		}
		recurseProcess(gui.getRootWidget(), componentWidgets);
		if(obj instanceof IComponentContainer) {
			//We have components to process.
			IComponentContainer inv = (IComponentContainer)obj;
			for(IComponentReceiver widget : componentWidgets) {
				if(widget == null) continue;
				Iterable<String> searching = widget.getComponentsSought();
				if(searching == null) continue;
				//See if we have any of the components the GUI is looking for in this object.
				for(String name : searching) {
					IInvComponent c = inv.getComponent(name);
					if(c != null) {
						widget.provideComponent(c);
					}
				}
			}
		}
	}
	
	/**
	 * Process all GUI elements which need information from obj,
	 * and re-position all of the Minecraft Slot objects such that they work properly
	 * with the container.
	 * 
	 * @param obj Whatever's providing the game logic backing our GUI (typically a Tile Entity)?
	 * @param gui The GUI to which we'll be linking various values from the object.
	 */
	protected static void processComponents(Object obj, IGUI gui) {
		processComponents(obj, gui, null);
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
		Container container = buildContainer(obj, inventoryPlayer);
				
		//Todo: sensitive to inventory type.
		SmartScreenBase result = (SmartScreenBase) new ZettaScreen(container);
		
		for(IGUIWidget e : rootWidget.getChildren()) {
			result.addWidget(e.copy());
		}
		
		processComponents(obj, result, this.componentWidgets);
		
		if(container instanceof ICleanableContainer) {
			//Remove any slots not associated with the GUI.
			((ICleanableContainer)container).cleanupUnlinkedSlots();
		}
		
		result.setGuiID(this.getGuiID());

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
		//Todo: sensitive to inventory type.
		Container result = (Container) new ContainerPlayerInv((IDescriptiveInventory)obj, inventoryPlayer);
		return result;
	}
	
	protected int GuiID = -1;
	@Override
	public int getGuiID() {
		return GuiID;
	}

	@Override
	public void setGuiID(int id) {
		GuiID = id;
	}
}
