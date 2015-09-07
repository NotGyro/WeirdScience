package ws.zettabyte.zettalib.client.gui;

import java.util.ArrayList;

public interface IGUIWidget {
	void draw(GUIContext context); //TODO: Add arguments as we figure out what we're doing.
	/**
	 * Returns true if this widget should "intercept" clicks - read: If the GUI should stop looking
	 * for further elements after hitting this one.
	 */
	default boolean takesMouse() { return false; };
	
	default void onClickDown(MouseButton button) {};
	default void onClickUp(MouseButton button) {};
	
	default void onMouseOver() {};
	default void onMouseOff() {};
	
	Rect2D getBounds();
	default Vert2D getPos() { return getBounds().getPos(); };
	default Vert2D getSize() { return getBounds().getSize(); };
	default int getX() { return getPos().getX(); };
	default int getY() { return getPos().getY(); };
	default int getWidth() { return getSize().getX(); };
	default int getHeight() { return getSize().getY(); };

	void setBounds(Rect2D bounds);
	default void setPos(Vert2D pos) { getBounds().setPos(pos); };
	default void setSize(Vert2D size) { getBounds().setSize(size); };
	default void setX(int X) { getPos().setX(X); };
	default void setY(int Y) { getPos().setY(Y); };
	default void setWidth(int W) { getSize().setX(W); };
	default void setHeight(int H) { getSize().setY(H); };
	
	void setTint(float R, float G, float B, float A);
	
	/**
	 * 
	 * @return Is this a valid operation?
	 */
	boolean addChild(IGUIWidget child);
	ArrayList<IGUIWidget> getChildren();
	IGUIWidget getParent();
	/**
	 * 
	 * @return Is this a valid operation?
	 */
	void setParent(IGUIWidget parent);
	
	int getLayer();
	void setLayer(int l);
	
	boolean isVisible();
	void setVisible(boolean v);
}