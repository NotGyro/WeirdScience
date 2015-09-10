package ws.zettabyte.zettalib.client.gui.widgets;

import ws.zettabyte.zettalib.client.gui.CommonIcons;
import ws.zettabyte.zettalib.client.gui.IGUIWidget;
import ws.zettabyte.zettalib.client.render.SpriteResourceLocation;
/**
 * A widget that represents an inventory slot, with the default Minecraft inventory slot icon as
 * its background, that automatically creates a WidgetDummySlot as its child to mesh with Minecraft's
 * inventory logic.
 * 
 * Size defaults to (18, 18) and the offset of the dummy slot defaults to (1, 1). 
 * @author Sam "Gyro" C.
 *
 */
public class WidgetSlot extends WidgetSimple {
	
	protected WidgetDummySlot innerSlot = null;

	/** 
	 * @param p A parent widget. null is a valid value.
	 * 
	 * For the other arguments, @see WidgetDummySlot(String n, int idx)
	 */
	public WidgetSlot(IGUIWidget p, String n, int idx) {
		super();
		
		//Init:
		this.setSprite(new SpriteResourceLocation(CommonIcons.slotItem));
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
	/** @see WidgetDummySlot(String n, int idx) */
	public WidgetSlot(String n, int idx) {
		this(null, n, idx);
	}
	/** @see WidgetDummySlot(String n) */
	public WidgetSlot(String n) {
		this(n, -1);
	}
	/** @see WidgetDummySlot(int idx) */
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
