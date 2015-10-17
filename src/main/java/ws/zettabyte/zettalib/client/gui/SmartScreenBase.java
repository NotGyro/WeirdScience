package ws.zettabyte.zettalib.client.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import ws.zettabyte.zettalib.client.gui.widgets.WidgetContainer;
import ws.zettabyte.zettalib.client.render.IRenders2D;
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
 *
 */
public abstract class SmartScreenBase extends GuiContainer implements IRenders2D, IGUI {
	protected IGUIWidget rootWidget;
	protected IGUIWidget currentMouseOver = null;
	protected GUIContext ctx;
	protected IGUIWidgetKeyboard capture = null;
	
	//protected Rect2D guiArea;
	protected ArrayList<IGUIWidget> drawList = new ArrayList<IGUIWidget>(128);
	private int eventButton;
	private long lastMouseEvent;
	//private int mouseX;
	//private int mouseY;
	protected Vert2D mousePos = new Vert2D();
	
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
	    //if(this.guiLeft != 0) rootWidget.setX(this.guiLeft);
	    //if(this.guiTop != 0) rootWidget.setY(this.guiTop);
	    
	    //guiArea = rootWidget.getRelativeBounds();
	}
    /**
     * Centers the GUI screen in the game window.
     */
	protected void center(){
		//NOTE: Width and height refer to the screen in this context. As in, the whole screen.
        int k = (this.width - this.rootWidget.getWidth()) / 2;
        int l = (this.height - this.rootWidget.getHeight()) / 2;

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
        getWidgetsFlat(rootWidget, drawList);
        Collections.sort(drawList, compareLayer);
        for(IGUIWidget e : drawList) {
        	e.draw(ctx);
        }
    }
    
    @Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_,
			int p_146979_2_) {
		super.drawGuiContainerForegroundLayer(p_146979_1_, p_146979_2_);

		drawTooltips();
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
    protected void getWidgetsFlat(IGUIWidget w, List<IGUIWidget> list) {
    	if(w == null) return;
    	if(w.getChildren() == null) return;
    	for(IGUIWidget e : w.getChildren()) {
    		if(e.isVisible()) {
    			list.add(e);
    			getWidgetsFlat(e, list);
    		}
    	}
    }
    
    /* (non-Javadoc)
	 * @see ws.zettabyte.zettalib.client.gui.IGUI#addWidget(ws.zettabyte.zettalib.client.gui.IGUIWidget)
	 */
	@Override
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

	/* (non-Javadoc)
	 * @see ws.zettabyte.zettalib.client.gui.IGUI#getRootWidget()
	 */
	@Override
	public IGUIWidget getRootWidget() { return rootWidget; }
    
    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
     * 
     * Overridden to prevent annoying "must be 256x256" things.
     */
	@Override
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
        		uStart,
        		vStart );
        tessellator.draw();
    }

    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, width, height.
     * 
     * Draws the whole whole source image in the defined area. -
     * from u 0, v 0 to u 1, v 1.
     */
	@Override
    public void drawWholeTexturedRect(double x, double y, double width, double height) {
    	this.drawTexturedRect(x, y, 0.0D,  0.0D, 1.0D, 1.0D, width, height);
    }

	@Override
	public Minecraft getMC() {
		return this.mc;
	}
	
	@Override
	public void drawGradientRectangle(int p_73733_1_, int p_73733_2_,
			int p_73733_3_, int p_73733_4_, int p_73733_5_, int p_73733_6_) {
		this.drawGradientRect(p_73733_1_, p_73733_2_, p_73733_3_, p_73733_4_, p_73733_5_, p_73733_6_);
	}
	
	@Override
	public void drawHorzLine(int p_73730_1_, int p_73730_2_, int p_73730_3_,
			int p_73730_4_) {
		this.drawHorizontalLine(p_73730_1_, p_73730_2_, p_73730_3_, p_73730_4_);
	}
	
	@Override
	public void drawVertLine(int p_73728_1_, int p_73728_2_, int p_73728_3_,
			int p_73728_4_) {
		this.drawVerticalLine(p_73728_1_, p_73728_2_, p_73728_3_, p_73728_4_);
	}

	@Override
    public void drawRectangle(float x1, float y1, float x2, float y2, float r, float g, float b, float a) {
        float swap;

        if (x1 < x2) {
            swap = x1;
            x1 = x2;
            x2 = swap;
        }

        if (y1 < y2) {
            swap = y1;
            y1 = y2;
            y2 = swap;
        }
        
        Tessellator tessellator = Tessellator.instance;
        //GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        //OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(r, g, b, a);
        tessellator.startDrawingQuads();
        tessellator.addVertex((double)x1, (double)y2, this.getZLevel());
        tessellator.addVertex((double)x2, (double)y2, this.getZLevel());
        tessellator.addVertex((double)x2, (double)y1, this.getZLevel());
        tessellator.addVertex((double)x1, (double)y1, this.getZLevel());
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        //GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
	@Override
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		// TODO Auto-generated method stub
		super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
	}
	@Override
	protected void mouseClickMove(int p_146273_1_, int p_146273_2_,
			int p_146273_3_, long p_146273_4_) {
		// TODO Auto-generated method stub
		super.mouseClickMove(p_146273_1_, p_146273_2_, p_146273_3_, p_146273_4_);
	}

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
	@Override
	protected void mouseMovedOrUp(int mouseX, int mouseY,
			int which) {
		super.mouseMovedOrUp(mouseX, mouseY, which);
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
				wk.click(which, mouseX, mouseY);
				capture = wk;
				wk.setFocused(true);
				wk.setCanLoseFocus(false);
				break;
			}
		}
	}
	protected ArrayList<IGUIWidget> tooltipWidgetList = new ArrayList<IGUIWidget>(64);
	
	protected void drawTooltips() {
	    //if(this.guiLeft != 0) rootWidget.setX(this.guiLeft);
	    //if(this.guiTop != 0) rootWidget.setY(this.guiTop);
		center();
        tooltipWidgetList.clear();
        tooltipWidgetList = getIntersectingList(rootWidget, mousePos, tooltipWidgetList);
		for(IGUIWidget e : tooltipWidgetList) {
			if(e.getHasTooltip()) {
				if(e.getTooltips() != null) {
					//Our position messes up the mousepos for reasons unknown.
					this.drawHoveringText(e.getTooltips(), mousePos.getX()-this.guiLeft, mousePos.getY()-this.guiTop, fontRendererObj);
				}
			}
		}
	}
	@Override
	public void handleMouseInput() {
		mousePos.setX(Mouse.getEventX() * this.width / this.mc.displayWidth);
        mousePos.setY(this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1);
        int btn = Mouse.getEventButton();
        super.handleMouseInput();
    }
	@Override
	public void drawString(String str, int x, int y, int z) {
		this.drawString(fontRendererObj, str, x, y, z);
	}
	
	public FontRenderer getFR() {
		return this.fontRendererObj;
	}
	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		if(this.capture == null) {
			super.keyTyped(p_73869_1_, p_73869_2_);
		}
		else {
			this.capture.keyTyped(p_73869_1_, p_73869_2_);
			if(!capture.isInputCaptured()) {
				capture = null;
			}
		}
	}
	
	private void debugType() {
		ArrayList<IGUIWidget> l = new ArrayList<IGUIWidget>(32);
		getWidgetsFlat(rootWidget, l);
		for(IGUIWidget e : l) {
			if(e instanceof IGUIWidgetText) {
				System.out.println("X: " + e.getX() + " Y: " + e.getY() + " W: " + e.getWidth() + " H: " + e.getHeight());
			}
		}
	}
}
