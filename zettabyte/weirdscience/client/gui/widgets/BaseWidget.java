package zettabyte.weirdscience.client.gui.widgets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import net.minecraft.client.gui.GuiScreen;


/* 
 * Pretty much a dummy class. Can be used as a movable vertex to move child widgets around.
 * Implements all basic 
 */
public class BaseWidget implements IGuiWidget {
	
	protected ArrayList<IGuiWidget> children;
	protected IGuiWidget parent;
	
	protected int x, y;
	
	protected int layer;
	protected boolean visible;
	
	public BaseWidget() { 
		children = new ArrayList<IGuiWidget>();
		parent = null;
	}
	
	public boolean getVisible() {
		return visible;
	}
	
	public void setVisible(boolean setTo) {
		visible = setTo;
	}
	
	@Override
	public void Draw(GuiScreen context) {
		if(getVisible()) {
			ListIterator<IGuiWidget> iter = children.listIterator(); 
			while(iter.hasNext()) {
				IGuiWidget child = iter.next();
				child.Draw(context);
				
			}
		}

	}

	@Override
	public void setPosition(int xN, int yN) {
		x = xN;
		y = yN;
	}

	@Override
	public int getPosX() {
		return x;
	}

	@Override
	public int getPosY() {
		return y;
	}

	@Override
	public int getAbsPosX() {
		/* As our position is an offset from our parent widget's position,
		 * goes recursively through the chain of GUI widgets to get position
		 * in world.
		 */
		if(parent != null) {
			return x + parent.getAbsPosX();
		}
		else {
			return x;
		}
	}

	@Override
	public int getAbsPosY() {
		/* As our position is an offset from our parent widget's position,
		 * goes recursively through the chain of GUI widgets to get position
		 * in world.
		 */
		if(parent != null) {
			return y + parent.getAbsPosY();
		}
		else {
			return y;
		}
	}

	@Override
	public int getWidth() {
		return 0;
	}

	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public int getLayer() {
		return layer;
	}
	
	@Override
	public int getLayerAbsolute() {
		if(parent != null) {
			return layer + parent.getLayerAbsolute();
		}
		else {
			return layer;
		}
	}

	@Override
	public void setLayer(int newlayer) {
		layer = newlayer;
	}

	@Override
	public void setParent(IGuiWidget adoptive) {
		//Make sure to clean up references if we're switching.
		if(parent != null) {
			parent.removeChild(this);
		}
		parent = adoptive;

	}

	@Override
	public IGuiWidget getParent() {
		return parent;
	}

	@Override
	public void addChild(IGuiWidget adoptee) {
		children.add(adoptee);
	}

	@Override
	public ArrayList<IGuiWidget> getChildren() {
		return children;
	}
	
	@Override
	public void removeChild(IGuiWidget adoptee) {
		children.remove(adoptee);
	}
	
	@Override
	public boolean ReceiveDragged(IGuiWidget dragged) {
		return false; //By default, do nothing with the widget.
	}

	@Override
	public boolean isMouseSolid() {
		return false;
	}

	@Override
	public void onMouseOver() {
	}

	@Override
	public void onMouseOff() {		
	}

	@Override
	public boolean IntersectPoint(int x, int y) {
		return false;
	}
}
