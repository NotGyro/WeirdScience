package zettabyte.weirdscience.client.gui.widgets;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiScreen;

public interface IGUIWidget {
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
	void setLayer(int layer);
	
	//Sets a widget for this one to track / draw with.
	void setParent(IGUIWidget adoptive);
	
	//Gets the widget that this is tracking and drawing with.
	IGUIWidget getParent();
	
	//Does what you would expect, blah blah, I am writing comments.
	//It is assumed that addChild calls setParent.
	void addChild(IGUIWidget adoptee);
	
	ArrayList<IGUIWidget> getChildren();
}
