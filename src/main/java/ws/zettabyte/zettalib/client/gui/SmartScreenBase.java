package ws.zettabyte.zettalib.client.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ws.zettabyte.zettalib.client.gui.widgets.WidgetContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
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
 * @author Sam "Gyro" Cutlip
 *
 */
public abstract class SmartScreenBase extends GuiContainer {
	protected IGUIWidget rootWidget;
	protected IGUIWidget currentMouseOver = null;
	protected GUIContext ctx;
	
	protected Rect2D guiArea;
	protected ArrayList<IGUIWidget> drawList = new ArrayList<IGUIWidget>(128);
	
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
    public SmartScreenBase(Container container) {
		super(container);
	    ctx = new GUIContext(this);
	    rootWidget = new WidgetContainer();
	    rootWidget.setWidth(176);
	    rootWidget.setHeight(166);
	    
	    guiArea = rootWidget.getRelativeBounds();
	}
    /**
     * Centers the GUI screen in the game window.
     */
	protected void center(){
		//NOTE: Width and height refer to the screen in this context. As in, the whole screen.
        int k = (this.width - this.guiArea.getWidth()) / 2;
        int l = (this.height - this.guiArea.getHeight()) / 2;

        rootWidget.setX(k);
        rootWidget.setY(l);
	}
	
	/**
	 * First, lists all widgets attached to the screen, by performing a recursive query
	 * to get the children of the root widget.
	 * Then, sorts all widgets in order of their layer. 
	 * Lastly, calls widget.draw(); on each of them.
	 */
    public void drawWidgets(int a, int b, float c) {
    	this.center();
        this.drawDefaultBackground();
        drawList.clear();
        addChildrenToDrawList(rootWidget);
        Collections.sort(drawList, compareLayer);
        for(IGUIWidget e : drawList) {
        	e.draw(ctx);
        }
    }
    
    /**
     * A method invoked by our parent class' drawScreen() method. 
     * 
     * Inventory slots and items will be drawn on top of anything drawn here.
     * 
     * TODO: figure out what the arguments imply.
     */
	@Override
	protected void drawGuiContainerBackgroundLayer(float a, int b, int c) {
		drawWidgets(c,b,a);
	}
	
	/**
	 * Used for recursively querying our widgets.
	 */
    protected void addChildrenToDrawList(IGUIWidget w) {
    	if(w == null) return;
    	if(w.getChildren() == null) return;
    	for(IGUIWidget e : w.getChildren()) {
    		if(e.isVisible()) {
	    		drawList.add(e);
	    		addChildrenToDrawList(e);
    		}
    	}
    }
    
    /**
     * Adds a widget to this screen's root widget,
     * providing it as a child to our rootWidget and also
     * calling setParent on w.
     * 
     * 
     * @param w
     * @return Were we able to add this widget (true), or is it a duplicate (false)?
     */
	public boolean addWidget(IGUIWidget w) {
    	boolean ret = rootWidget.addChild(w);
    	if(ret) w.setParent(rootWidget);
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

	/**
	 * Gets the root widget, the parent farthest up the widget tree of this screen's
	 * hierarchy. Recursively walking through all children of this object and all of
	 * their children and etc... will touch on every single widget involved in this
	 * screen.
	 */
	public IGUIWidget getRootWidget() { return rootWidget; }
    
    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
     * 
     * Overridden to prevent annoying "must be 256x256" things.
     */
    public void drawTexturedRect(double x, double y, 
    		double uStart, double vStart, double uWidth, double vHeight, 
    		double width, double height) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((x + 0), 
        		(y + height), 
        		(double)this.zLevel, 
        		uStart, 
        		vStart + vHeight );
        tessellator.addVertexWithUV((x + width), 
        		(y + height), 
        		(double)this.zLevel, 
        		uStart + uWidth, 
        		vStart + vHeight );
        tessellator.addVertexWithUV((x + width), 
        		(y + 0), 
        		(double)this.zLevel, 
        		uStart + uWidth, 
        		vStart );
        tessellator.addVertexWithUV((x + 0), 
        		(y + 0), 
        		(double)this.zLevel, 
        		uStart + 0.0F,
        		vStart );
        tessellator.draw();
    }

    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, width, height.
     * 
     * Draws the whole whole source image in the defined area. -
     * from u 0, v 0 to u 1, v 1.
     */
    public void drawWholeTexturedRect(double x, double y, double width, double height) {
    	this.drawTexturedRect(x, y, 0.0D,  0.0D, 1.0D, 1.0D, width, height);
    }

	public Minecraft getMC() {
		return this.mc;
	}
}
