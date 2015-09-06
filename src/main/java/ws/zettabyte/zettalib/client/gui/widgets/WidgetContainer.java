package ws.zettabyte.zettalib.client.gui.widgets;

import java.util.ArrayList;

import ws.zettabyte.zettalib.client.gui.GUIContext;
import ws.zettabyte.zettalib.client.gui.IGUIArt;
import ws.zettabyte.zettalib.client.gui.IGUIWidget;
import ws.zettabyte.zettalib.client.gui.OffsetRect2D;
import ws.zettabyte.zettalib.client.gui.Rect2D;

/**
 * Contains other GUI widgets, is not drawn.
 * @author Sam "Gyro" Cutlip
 *
 */
public class WidgetContainer implements IGUIWidget {
	protected OffsetRect2D bounds = new OffsetRect2D();
	protected IGUIWidget parent = null;
	
	protected ArrayList<IGUIWidget> children = new ArrayList<IGUIWidget>(4);
	protected int layer = 0;
	
	protected boolean visible = true;

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
	public void draw(GUIContext context) {
		/*for(IGUIWidget e : children) {
			e.draw(context);
		}*/
	}
	/*
	@Override
	public void toDraw(GUIContext context, ArrayList<IGUIWidget> list) {
		if(isVisible()) {
			list.add(this);
			for(IGUIWidget e : children) {
				e.toDraw(context, list);
			}
		}
	}*/

	@Override
	public Rect2D getBounds() {
		// TODO Auto-generated method stub
		return bounds;
	}

	@Override
	public void setBounds(Rect2D b) {
		bounds.setX(b.getX());
		bounds.setY(b.getY());
		bounds.setWidth(b.getWidth());
		bounds.setHeight(b.getHeight());
	}

	@Override
	public void setArt(IGUIArt art) {
		// TODO Auto-generated method stub
	}

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
	// TODO Auto-generated method stub

	public void setParent(IGUIWidget p) {
		parent = p;
		bounds.setParent(p.getPos());
	}

	@Override
	public int getLayer() { return layer; }

	@Override
	public void setLayer(int l) { layer = l; }

	//Despite the fact that a container itself is not drawn, child elements are drawn recurisvely, so we need this.
	@Override
	public boolean isVisible() { return visible; }

	@Override
	public void setVisible(boolean v) { visible = v; }
	@Override
	public void setTint(float R, float G, float B, float A) { }

}
