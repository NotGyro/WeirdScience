package ws.zettabyte.zettalib.client.gui.widgets;

import ws.zettabyte.zettalib.client.gui.CommonIcons;
import ws.zettabyte.zettalib.client.gui.IGUIWidget;

public class WidgetSlot extends WidgetSimple {
	
	protected WidgetDummySlot innerSlot = null;

	public WidgetSlot(IGUIWidget p, String n, int idx) {
		super();
		
		//Init:
		this.setArt(CommonIcons.slotItem);
		this.setWidth(18); 
		this.setHeight(18);
		
		this.setVisible(true);
		
		innerSlot = new WidgetDummySlot(n, idx);
		
		//Offset for rendering.
		innerSlot.setX(1); innerSlot.setY(1);
		innerSlot.setLayer(layer + 1);
		
		this.addChild(innerSlot);
		innerSlot.setParent(this);
		
		if(p != null) {
			p.addChild(this);
			this.setParent(p);
		}
	}
	public WidgetSlot(String n, int idx) {
		this(null, n, idx);
	}
	public WidgetSlot(String n) {
		this(n, -1);
	}
	public WidgetSlot(int idx) {
		this(null, null, idx);
	}
	public WidgetSlot(IGUIWidget p, String n) {
		this(p, n, -1);
	}
	public WidgetSlot(IGUIWidget p, int idx) {
		this(p, null, idx);
	}
	

	public void setName(String n) {
		innerSlot.name = n;
	}
	public void setIDX(int idx) {
		innerSlot.idx = idx;
	}
	@Override
	public void setLayer(int l) {
		super.setLayer(l);
		innerSlot.setLayer(l);
	}
}
