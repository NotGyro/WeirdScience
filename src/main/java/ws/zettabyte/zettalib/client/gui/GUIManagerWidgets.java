package ws.zettabyte.zettalib.client.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import ws.zettabyte.zettalib.client.gui.widgets.WidgetContainer;
import ws.zettabyte.zettalib.client.render.IRenders2D;
import ws.zettabyte.zettalib.client.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

/**
 * A basic Minecraft-friendly Screen class that can handle our more dynamic
 * Widget system. 
 * 
 * TODO: Will probably refactor this entirely. I'll extract an interface,
 * at the very least.
 * 
 * @author Sam "Gyro" C.
 */
public class GUIManagerWidgets {
	protected IGUIWidget rootWidget;
	protected IGUIWidget currentMouseOver = null;
	protected IGUIWidgetKeyboard capture = null;
	
	protected ArrayList<IGUIWidget> drawList = new ArrayList<IGUIWidget>(128);
	private int eventButton;
	private long lastMouseEvent;
	//protected Vert2D mousePos; //= new Vert2D();
	
	protected float zLevel;
	
	protected static Comparator<IGUIWidget> compareLayer = new Comparator<IGUIWidget>() {
		@Override
	    public int compare(IGUIWidget w1, IGUIWidget w2) {
	      //ascending order
	      return ((Integer)w1.getLayer()).compareTo((Integer)w2.getLayer());
	    }
	};
	
	/**
	 * Constructs a screen from the container given.
	 * 
	 * Also, the root widget's width and height are set to
	 * w: 176 and h: 166 by default, which is the standard
	 * size of GUI screens in Minecraft.
	 */
    public GUIManagerWidgets() {
	    //ctx = new GUIContext(this);
	    rootWidget = new WidgetContainer();
	    rootWidget.setWidth(176);
	    rootWidget.setHeight(166);
	}
    
	
	/**
	 * First, lists all widgets attached to the screen, by performing a recursive query
	 * to get the children of the root widget.
	 * Then, sorts all widgets in order of their layer. 
	 * Lastly, calls widget.draw(); on each of them.
	 */
    public void drawWidgets(GUIContext ctx) {
        drawList.clear();
        rootWidget.getWidgetsFlat(drawList);
        Collections.sort(drawList, compareLayer);
        for(IGUIWidget e : drawList) {
        	e.draw(ctx);
        }
    }

	/**
	 * Adds a widget to the root node of this GUIBuilder.
	 * 
	 * All added widgets and their children will be copied,
	 * and their copies will be passed to a GUI Screen class.
	 * 
	 * @return Was the widget successfully added?
	 */
	public boolean addWidget(IGUIWidget w) {
    	boolean ret = rootWidget.addChild(w);
    	if (ret) w.setParent(rootWidget);
    	return ret;
    }

	/**
	 * Sets the Z-level at which OpenGL should render our GUI.
	 */
	public void setZLevel(float z) {
    	this.zLevel = z;
    }

	/**
	 * Gets the Z-level at which OpenGL should render our GUI.
	 */
	public float getZLevel() {
    	return this.zLevel;
    }

	/* (non-Javadoc)
	 * @see ws.zettabyte.zettalib.client.gui.IGUI#getRootWidget()
	 */
	public IGUIWidget getRootWidget() { return rootWidget; }

	private Rect2D boundsTemp = new Rect2D(0,0,0,0);
	protected boolean getIntersects(IGUIWidget w, Vert2D point) {
		//Optimization stuff: must allocate less.
		boundsTemp.setWidth(w.getWidth());
		boundsTemp.setHeight(w.getHeight());
		boundsTemp.setX(w.getX());
		boundsTemp.setY(w.getY());
		return boundsTemp.contains(point);
	}
	protected ArrayList<IGUIWidget> getIntersectingList(IGUIWidget w, Vert2D point, ArrayList<IGUIWidget> list) {
		if(getIntersects(w, point)){
			list.add(w);
			if(w.getChildren() != null) {
				for(IGUIWidget e : w.getChildren()) {
					if(e != null) getIntersectingList(e, point, list);
				}
			}
		}
		return list;
	}
	/* This does not actually get called when the mouse has merely moved.
	 * Here, we use it to try to hand control to textboxes n' such. 
	 * (non-Javadoc)
	 * @see net.minecraft.client.gui.inventory.GuiContainer#mouseMovedOrUp(int, int, int)
	 */
	protected void defocusWidget() {
		if(capture != null) {
			capture.setCanLoseFocus(true);
			capture.setFocused(false);
			capture = null; 
		}
	}

	protected void mouseMovedOrUp(Vert2D mousePos,
			int which) {
		if(which == -1) return;
		if(capture != null) {
			capture.click(which, mousePos.getX(), mousePos.getY());
			if(!capture.isInputCaptured()) {
				defocusWidget();
			}
			else if (!getIntersects(capture, mousePos)) { 
				defocusWidget();
			}
		}
		if(capture != null) return;
		ArrayList<IGUIWidget> list = new ArrayList<IGUIWidget>(64);
		list = getIntersectingList(rootWidget, mousePos, list);
		for(IGUIWidget e : list) {
			if(e instanceof IGUIWidgetKeyboard) {
				IGUIWidgetKeyboard wk = (IGUIWidgetKeyboard)e;
				wk.click(which, mousePos.x, mousePos.y);
				capture = wk;
				wk.setFocused(true);
				wk.setCanLoseFocus(false);
				break;
			}
		}
	}
	protected ArrayList<IGUIWidget> tooltipWidgetList = new ArrayList<IGUIWidget>(64);
	
	protected void drawTooltips(IRenders2D ctx, Vert2D mousePos) {
        tooltipWidgetList.clear();
        tooltipWidgetList = getIntersectingList(rootWidget, mousePos, tooltipWidgetList);
		for(IGUIWidget e : tooltipWidgetList) {
			if(e.getHasTooltip()) {
				if(e.getTooltips() != null) {
					//Our position messes up the mousepos for reasons unknown.
					this.drawTooltip(e.getTooltips(), mousePos.getX(), mousePos.getY(), ctx);
				}
			}
		}
	}
	
	protected void drawTooltip(List l, int x, int y, IRenders2D ctx) { 
		//Our position messes up the mousepos for reasons unknown.
		ctx.drawTooltipText(l,x, y, ctx.getFR());
	};
	
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		if(this.capture != null) {
			this.capture.keyTyped(p_73869_1_, p_73869_2_);
			if(!capture.isInputCaptured()) {
				capture = null;
			}
		}
	}
}
