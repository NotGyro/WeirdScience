package ws.zettabyte.zettalib.client.gui.widgets;

import ws.zettabyte.zettalib.client.gui.GUIContext;
import ws.zettabyte.zettalib.client.gui.IGUIItemSlot;
import ws.zettabyte.zettalib.client.gui.IGUIWidget;
import ws.zettabyte.zettalib.client.gui.Rect2D;
import ws.zettabyte.zettalib.client.gui.Vert2D;

/**
 * A widget that is neither displayed nor possible to interact with directly -
 * instead, this widget is a marker, read by other classes and used to place
 * Slots from Minecraft's inventory container system.
 * 
 * A name of null means "unknown" or "do not search on name" and an index of
 * -1 means "unknown" or "do not search on index."
 * 
 * DummySlots which cannot be matched to a slot on the inventory do not
 * throw an exception - they just don't work, and the actual position of
 * the slot logic in the GUI is likely to be at 0,0. TODO: Log a warning
 * rather than failing silently on this.
 * 
 * @author Sam "Gyro" Cutlip
 *
 */
public class WidgetDummySlot implements IGUIItemSlot {
	public int idx = -1;
	public String name = null;
	
	protected Rect2D bounds = new Rect2D();
	protected IGUIWidget parent = null;
	protected int layer = -1;

	/**
	 * Sets its size to 16x16.
	 */
	public WidgetDummySlot() {
		bounds.setWidth(16);
		bounds.setHeight(16);
	}
	/**
	 * @param n Name of slot component, to match against an inventory.
	 * @param i Index of slot, to match against an inventory.
	 */
	public WidgetDummySlot(String n, int i) {
		this();
		idx = i;
		name = n;
	}
	/**
	 * @param n Name of slot component, to match against an inventory.
	 */
	public WidgetDummySlot(String n) {
		this(n, -1);
	}
	/**
	 * @param i Index of slot, to match against an inventory.
	 */
	public WidgetDummySlot(int i) {
		this(null, i);
	}
	
	@Override
	public void draw(GUIContext context) { }

	@Override
	public Rect2D getRelativeBounds() {
		return bounds;
	}

	@Override
	public void setBounds(Rect2D bounds) {	} //Unsupported

	@Override
	public IGUIWidget getParent() { return parent; }

	@Override
	public void setParent(IGUIWidget p) {
		parent = p;
	}

	@Override
	public int getLayerRelative() {
		return layer;
	}
	
	@Override
	public void setLayer(int l) {
		// Most likely does nothing.
		layer = l;
	}

	@Override
	public boolean isVisible() {
		return false;
	}

	@Override
	public void setVisible(boolean v) {	}

	/**
	 * @return The name of our slot component, to be matched against slots on an inventory.
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @return The index of our slot, to be matched against slots on an inventory.
	 */
	@Override
	public int getSlotIndex() {
		return idx;
	}
	
	@Override 
	public IGUIWidget copy() {
		IGUIWidget clone = new WidgetDummySlot();
		
		clone.setParent(null);
		
		clone.getRelativeBounds().setX(getXRelative());
		clone.getRelativeBounds().setY(getYRelative());
		clone.getRelativeBounds().setWidth(bounds.getWidth());
		clone.getRelativeBounds().setHeight(bounds.getHeight());
		
		clone.setLayer(layer);
		
		return clone;
	}

}
