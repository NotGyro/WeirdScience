package zettabyte.weirdscience.client.gui.widgets;

public interface IClickable {
	//Has the player pressed the mouse button on top of this GUI element?
	void Press();
	//Has the player released the mouse button on top of this GUI element?
	void ReleaseOn();
	//Has the player released the mouse button elsewhere after clicking on this?
	void ReleaseOff();
	
	//Has the button been pressed and not yet released?
	boolean getPressed();
}
