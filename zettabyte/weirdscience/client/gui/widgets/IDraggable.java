package zettabyte.weirdscience.client.gui.widgets;

public interface IDraggable {
	//Returns whether or not the widget can be picked up right now. 
	boolean PickUp();
	//Pass it a null slot if you're just moving it about on the screen.
	void Drop(int x, int y, IGuiWidget slot);
	//Move the draggable around.
	void Move(int x, int y);
}
