package zettabyte.weirdscience.client.gui.widgets;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiScreen;

public interface IGuiWidget {
	void Draw(GuiScreen context);
	//SetPosition is always relative to the parent widget.
	void setPosition(int x, int y);

	//Gets the position relative to a parent widget.
	int getPosX();
	int getPosY();

	//Gets the position relative to the screen origin.
	int getAbsPosX();
	int getAbsPosY();
	
	//For non-standard elements, just returns the bounding box.
	int getWidth();
	int getHeight();
	
	//Gets and sets our arbitrary layer.
	int getLayer();
	int getLayerAbsolute();
	void setLayer(int layer);
	
	//Sets a widget for this one to track / draw with.
	void setParent(IGuiWidget adoptive);
	
	//Gets the widget that this is tracking and drawing with.
	IGuiWidget getParent();
	
	//Draw this? 
	boolean getVisible();
	void setVisible(boolean setTo);
	
	//Does what you would expect, blah blah, I am writing comments.
	//It is assumed that addChild calls setParent.
	void addChild(IGuiWidget adoptee);
	
	void removeChild(IGuiWidget toRemove);
	
	ArrayList<IGuiWidget> getChildren();
	
	/* What do we do when a draggable widget has been dragged-and-dropped onto this one?
	 * Returns whether or not anything is done with the widget recieved.
	 */
	boolean ReceiveDragged(IGuiWidget dragged);
	
	//Do we pay attention to this widget when clicking and for mouseover highlight?
	boolean isMouseSolid();
	void onMouseOver();
	void onMouseOff();
	
	//Is this point inside the widget? (coordinates in screen-space)
	boolean IntersectPoint(int x, int y);
}
