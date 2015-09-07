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
	
    public SmartScreenBase(Container container) {
		super(container);
	    ctx = new GUIContext(this);
	    rootWidget = new WidgetContainer();
	    rootWidget.setWidth(176);
	    rootWidget.setHeight(166);
	    
	    guiArea = rootWidget.getBounds();
	}
	public void center(){
		//NOTE: Width and height refer to the screen in this context. As in, the whole screen.
        int k = (this.width - this.guiArea.getWidth()) / 2;
        int l = (this.height - this.guiArea.getHeight()) / 2;

        rootWidget.setX(k);
        rootWidget.setY(l);
	}

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
	/*
    @Override
    public void drawScreen(int a, int b, float c) {
		drawWidgets(a,b,c);		
    	this.zLevel = 10;
    	this.drawGuiContainerBackgroundLayer(c,b,a);
    	this.drawGuiContainerForegroundLayer(a, b);
    }*/
	@Override
	protected void drawGuiContainerBackgroundLayer(float a, int b, int c) {
		drawWidgets(c,b,a);
	}
	
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
    
	public void addWidget(IGUIWidget w) {
    	rootWidget.addChild(w);
    	w.setParent(rootWidget);
    }
	
	public void setZLevel(float z) {
    	this.zLevel = z;
    }
	
	public float getZLevel() {
    	return this.zLevel;
    }
    
	public IGUIWidget getRootWidget() { return rootWidget; }
    
    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
     * 
     * Overridden to prevent annoying "must be 256x256" things.
     */
    public void drawTexturedRect(int x, int y, 
    		double uStart, double vStart, double uWidth, double vHeight, 
    		int width, int height) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(x + 0), 
        		(double)(y + height), 
        		(double)this.zLevel, 
        		uStart, 
        		vStart + vHeight );
        tessellator.addVertexWithUV((double)(x + width), 
        		(double)(y + height), 
        		(double)this.zLevel, 
        		uStart + uWidth, 
        		vStart + vHeight );
        tessellator.addVertexWithUV((double)(x + width), 
        		(double)(y + 0), 
        		(double)this.zLevel, 
        		uStart + uWidth, 
        		vStart );
        tessellator.addVertexWithUV((double)(x + 0), 
        		(double)(y + 0), 
        		(double)this.zLevel, 
        		uStart + 0.0F,
        		vStart );
        tessellator.draw();
    }

    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, width, height.
     * 
     * Draws the whole whole source image in the defined area.
     */
    public void drawWholeTexturedRect(int x, int y, int width, int height) {
    	this.drawTexturedRect(x, y, 0.0D,  0.0D, 1.0D, 1.0D, width, height);
    }

    @Override
    public void drawTexturedModelRectFromIcon(int x, int y, IIcon icon, int width, int height) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + height), (double)this.zLevel, (double)icon.getMinU(), (double)icon.getMaxV());
        tessellator.addVertexWithUV((double)(x + width), (double)(y + height), (double)this.zLevel, (double)icon.getMaxU(), (double)icon.getMaxV());
        tessellator.addVertexWithUV((double)(x + width), (double)(y + 0), (double)this.zLevel, (double)icon.getMaxU(), (double)icon.getMinV());
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)this.zLevel, (double)icon.getMinU(), (double)icon.getMinV());
        tessellator.draw();
    }

	public Minecraft getMC() {
		return this.mc;
	}

}
