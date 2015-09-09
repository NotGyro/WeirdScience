package ws.zettabyte.zettalib.client.gui;

import ws.zettabyte.zettalib.inventory.FluidTankNamed;

public interface IGUITank extends IGUIWidget {
	String getComponentName(); //null is a valid return value
	void provideTank(FluidTankNamed tank);
}
