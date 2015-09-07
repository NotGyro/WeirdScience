package ws.zettabyte.zettalib.client.gui;

import java.util.ArrayList;

//Basically a dummy that tells the container "An item slot is here!"
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
