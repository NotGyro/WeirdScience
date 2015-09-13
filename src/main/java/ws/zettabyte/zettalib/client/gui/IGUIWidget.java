package ws.zettabyte.zettalib.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;

import ws.zettabyte.zettalib.client.render.ISprite;
/**
 * An element of a GUI which has a mutable upper-left-hand corner position, a mutable width and height 
 * for a bounding box, and which can be incorporated into a tree of other IGUIWidgets.
 * 
 * The position of an IGUIWidget is relative to its parent's position, which in turn is relative to its
 * parent's position, and so on all the way up. That is, calling setX(10) will mean that this widget is
 * 10 units to the right of its parent's position. Calling getX() after that will probably not yield 10.
 * Calling getXRelative(), on the other hand, will definitely yield 10. That's what the get_Relative()
 * functions are for: providing the position in local (rather than world) space.
 * 
 * Width and height are absolutes and do not bend to the hierarchy. Calling parent.setWidth(x) will not
 * alter the output of child.getWidth(); in any way (except possibly in some odd special cases).
 * @author Sam "Gyro" C.
 *
 */
public interface IGUIWidget {
	void draw(GUIContext context); //TODO: Add arguments as we figure out what we're doing.
	/**
	 * Returns true if this widget should "intercept" clicks - read: If the GUI should stop looking
	 * for further elements after hitting this one.
	 */
	default boolean takesMouse() { return false; };
	
	default void onClickDown(MouseButton button) {};
	default void onClickUp(MouseButton button) {};
	
	/**
	 * Has the mouse moved over this widget's bounding box?
	 */
	default void onMouseOver() {};
	/**
	 * Has the mouse moved out of this widget's bounding box?
	 */
	default void onMouseOff() {};
	
	Rect2D getRelativeBounds();
	default Vert2D getPos() {
		return new Vert2D(getX(), getY());
	};
	default int getX() {
		if(getParent() == null) return getXRelative();
		return (getXRelative() + getParent().getX());
	};
	default int getY() {
		if(getParent() == null) return getYRelative();
		return getYRelative() + getParent().getY();
	};
	
	default Vert2D getPosRelative() { return getRelativeBounds().getPos(); };
	default Vert2D getSize() { return getRelativeBounds().getSize(); };
	default int getXRelative() {return getRelativeBounds().getX(); };
	default int getYRelative() {return getRelativeBounds().getY(); };
	default int getWidth() { return getSize().getX(); };
	default int getHeight() { return getSize().getY(); };
	
	default Rect2D getBounds() { return new Rect2D(getPos(), getSize());}

	void setBounds(Rect2D bounds);
	default void setPos(Vert2D pos) { getRelativeBounds().setPos(pos); };
	default void setSize(Vert2D size) { getRelativeBounds().setSize(size); };
	default void setX(int X) { getRelativeBounds().setX(X); };
	default void setY(int Y) { getRelativeBounds().setY(Y); };
	default void setWidth(int W) { getSize().setX(W); };
	default void setHeight(int H) { getSize().setY(H); };

	/**
	 * Re-color the widget's visual representation. Not supported by all widgets.
	 * 
	 * In most cases these values are handed right to GL11.glColor4f.
	 */
	default void setTint(float R, float G, float B, float A) {};
	
	/**
	 * @return Is this a valid operation?
	 * Should be false if this is a duplicate entry.
	 */
	boolean addChild(IGUIWidget child);
	
	/**
	 * @return All widgets attached to this one as children.
	 * (NOTE: null is a valid return value, here. Some classes will not support this function.
	 */
	ArrayList<IGUIWidget> getChildren();
	
	/**
	 * @return The widget to which this one is attached as a child.
	 * (NOTE: null is a valid return value, here - it's actually a common one. Root nodes will always return null.)
	 */
	IGUIWidget getParent();
	
	void setParent(IGUIWidget parent);

	default int getLayer() {
		if(getParent() == null) return getLayerRelative();
		return getLayerRelative() + getParent().getLayer();
	};
	int getLayerRelative();
	void setLayer(int l);
	
	boolean isVisible();
	void setVisible(boolean v);

	/**
	 * Create an exact duplicate of this widget.
	 * Parent will not be set. We recursively call copy() on all children of this widget
	 * and then add them to our copy, setting our copy to their parent.
	 */
	IGUIWidget copy();

	//Centers this widget within the space of its parent widget.
	default void center() {
		centerX();
		centerY();
	}
	//Centers this widget within the space of its parent widget.
	default void centerX() {
		if(this.getParent() == null) return;
		
        int k = Math.abs(this.getParent().getWidth() - this.getWidth()) / 2;
        setX(k);
	}
	//Centers this widget within the space of its parent widget.
	default void centerY() {
		if(this.getParent() == null) return;
        int l = Math.abs(this.getParent().getHeight() - this.getHeight()) / 2;
        setY(l);
	}
	default void setSprite(ISprite s) { };

	default boolean getHasTooltip(boolean verbose) { return false; }
	default boolean getHasTooltip() { return getHasTooltip(false); }
	default void setHasTooltip(boolean b) {}
	/**
	 * These should be unlocalized values
	 * @param text
	 */
	default void addTooltip(String text) {}
	default List getTooltips( boolean verbose) { return null; }
	default List getTooltips() { return getTooltips(false); }
}