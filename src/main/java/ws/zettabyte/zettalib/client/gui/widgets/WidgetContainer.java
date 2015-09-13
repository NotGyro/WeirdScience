package ws.zettabyte.zettalib.client.gui.widgets;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import ws.zettabyte.zettalib.client.gui.GUIContext;
import ws.zettabyte.zettalib.client.gui.IGUIWidget;
import ws.zettabyte.zettalib.client.gui.Rect2D;

/**
 * A simple base implementation of an IGUIWidget, which supports all of its methods.
 * Also useful as a parent node for other widgets.
 * 
 * This Widget does not have any visual representation, and calling draw() on it
 * does nothing.
 * @author Sam "Gyro" C.
 *
 */
public class WidgetContainer implements IGUIWidget {
	/**
	 * Where is our widget, and how big is it?
	 */
	protected Rect2D bounds = new Rect2D();
	protected IGUIWidget parent = null;
	
	protected ArrayList<IGUIWidget> children = new ArrayList<IGUIWidget>(4);
	protected int layer = 0;
	
	protected boolean visible = true;
	
	protected ArrayList<String> tooltips = null;
	protected boolean showTooltip = false;

	public WidgetContainer() {}
	
	/**
	 * Registers @param p as the parent widget of this one, and
	 * also registers this widget as a child of p.
	 */
	public WidgetContainer(IGUIWidget p) { 
		setParent(p);
		p.addChild(this);
	}

	@Override
	public void draw(GUIContext context) { }

	/**
	 * Where is our widget, and how big is it?
	 */
	@Override
	public Rect2D getRelativeBounds() {
		return bounds;
	}
	
	/**
	 * @return false if the widget already has this one as a child.
	 */
	@Override
	public boolean addChild(IGUIWidget child) { 
		if(children.contains(child)) {
			return false;
		}
		children.add(child); 
		return true;
	}
	
	@Override
	public ArrayList<IGUIWidget> getChildren() { return children; }
	@Override
	public IGUIWidget getParent() { return parent; }
	@Override
	public void setParent(IGUIWidget p) { parent = p; }
	
	@Override
	public int getLayerRelative() {
		return layer;
	}
	@Override
	public void setLayer(int l) { layer = l; }
	
	@Override
	public boolean isVisible() { return visible; }
	@Override
	public void setVisible(boolean v) { visible = v; }
	@Override
	public void setTint(float R, float G, float B, float A) { }

	/**
	 * Horrible hack on language limitations. MUST be implemented on every child of WidgetContainer. 
	 * @return MUST be a new instance of the type of Widget the function is implemented on, otherwise copy() will break badly.
	 */
	protected IGUIWidget newThis() {
		return (IGUIWidget) new WidgetContainer();
	}

	/**
	 * Create an exact duplicate of this widget.
	 * Parent will not be set. We recursively call copy() on all children of this widget
	 * and then add them to our copy, setting our copy to their parent.
	 */
	@Override 
	public IGUIWidget copy() {
		IGUIWidget clone = newThis();
		
		clone.setParent(null);
		
		clone.getRelativeBounds().setX(getXRelative());
		clone.getRelativeBounds().setY(getYRelative());
		clone.getRelativeBounds().setWidth(bounds.getWidth());
		clone.getRelativeBounds().setHeight(bounds.getHeight());
		
		clone.setLayer(layer);
		clone.setVisible(isVisible());

		if(this.tooltips != null) {
			for(String tooltip : this.tooltips) {
				clone.addTooltip(tooltip);
			}
		}
		clone.setHasTooltip(showTooltip);
		
		//Do recursion
		for(IGUIWidget e : children) {
			IGUIWidget c = e.copy();
			clone.addChild(c);
			c.setParent(clone);
		}
		return clone;
	}

	@Override
	public void setBounds(Rect2D b) {
		bounds = b.copy();
	}

	@Override
	public boolean getHasTooltip(boolean verbose) {
		return getHasTooltip();
	}

	@Override
	public boolean getHasTooltip() {
		return ((tooltips != null) && showTooltip);
	}

	@Override
	public void setHasTooltip(boolean b) {
		IGUIWidget.super.setHasTooltip(b);
		showTooltip = true;
	}

	@Override
	public void addTooltip(String text) {
		if(tooltips == null) tooltips = new ArrayList<String>(2);
		tooltips.add(text);
	}

	@Override
	public List getTooltips(boolean verbose) {
		if(!showTooltip) {
			return null;
		}
		return tooltips;
	}
	
	
}
