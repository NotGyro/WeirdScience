package zettabyte.weirdscience.client.gui.widgets;

import zettabyte.weirdscience.client.gui.ICallbackObject;

public interface IClickable {
	//Has the player pressed the mouse button on top of this GUI element?
	void Press(int buttonID, ICallbackObject context);
	//Has the player released the mouse button on top of this GUI element?
	void ReleaseOn(int buttonID, ICallbackObject context);
	//Has the player released the mouse button elsewhere after clicking on this?
	void ReleaseOff(int buttonID, ICallbackObject context);
	
	//Has the button been pressed and not yet released?
	boolean getPressed(int buttonID);
	
	//For use with callback system.
	String getCallbackName();
	void setCallbackName(String name);
}
