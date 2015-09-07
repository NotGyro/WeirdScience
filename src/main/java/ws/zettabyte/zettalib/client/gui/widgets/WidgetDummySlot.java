package ws.zettabyte.zettalib.client.gui.widgets;

import ws.zettabyte.zettalib.client.gui.GUIContext;
import ws.zettabyte.zettalib.client.gui.IGUIItemSlot;
import ws.zettabyte.zettalib.client.gui.IGUIWidget;
import ws.zettabyte.zettalib.client.gui.OffsetRect2D;
import ws.zettabyte.zettalib.client.gui.Rect2D;
import ws.zettabyte.zettalib.client.gui.Vert2D;

public class WidgetDummySlot implements IGUIItemSlot {
	public int idx = -1; // By default: "I dunno"
	public String name = null; // By default: "I dunno"
	
	protected OffsetRect2D bounds = new OffsetRect2D();
	protected IGUIWidget parent = null;
	protected int layer = -1;

	public WidgetDummySlot() {
		bounds.setWidth(16);
		bounds.setHeight(16);
	}
	public WidgetDummySlot(String n, int i) {
		this();
		idx = i;
		name = n;
	}
	public WidgetDummySlot(String n) {
		this(n, -1);
	}
	public WidgetDummySlot(int i) {
		this(null, i);
	}
	
	@Override
	public void draw(GUIContext context) { }

	@Override
	public Rect2D getBounds() {
		return bounds;
	}

	@Override
	public void setBounds(Rect2D bounds) {	} //Unsupported

	@Override
	public IGUIWidget getParent() { return parent; }

	@Override
	public void setParent(IGUIWidget p) {
		parent = p;
		if(p != null) {
			bounds.setParent(p.getPos());
		}
		else {
			bounds.setParent(null);
		}
	}

	@Override
	public int getLayer() {
		// Most likely does nothing.
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
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getSlotIndex() {
		return idx;
	}
	
	@Override 
	public IGUIWidget copy() {
		IGUIWidget clone = new WidgetDummySlot();
		
		clone.setParent(null);
		
		clone.getBounds().setX(bounds.getXRelative());
		clone.getBounds().setY(bounds.getYRelative());
		clone.getBounds().setWidth(bounds.getWidth());
		clone.getBounds().setHeight(bounds.getHeight());
		
		clone.setLayer(layer);
		
		return clone;
	}

}
