package ws.zettabyte.zettalib.client.gui;

import java.util.ArrayList;

/**
 * A dummy widget that tells our GUI system "An item slot is here!", so that
 * the item slot can be repositioned in the container.
 * @author Sam "Gyro" Cutlip
 * 
 * TODO: Refactor this into the component system.
 * 
 * This will still exist as a subtype of IGUIComponent, but it will change.
 */
public interface IGUIItemSlot extends IGUIWidget {
	String getName(); //null is a valid return value
	int getSlotIndex(); //-1 for unknown.

	//Not applicable in this case:   |
	//                               V
	@Override
	default boolean addChild(IGUIWidget child) { return false; };
	@Override
	default ArrayList<IGUIWidget> getChildren() { return null; };
}
