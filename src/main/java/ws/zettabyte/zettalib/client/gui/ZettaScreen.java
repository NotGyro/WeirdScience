package ws.zettabyte.zettalib.client.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.lwjgl.opengl.GL11;

import ws.zettabyte.zettalib.client.gui.widgets.WidgetContainer;
import ws.zettabyte.zettalib.client.gui.widgets.WidgetSimple;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;

@SideOnly(Side.CLIENT)
public class ZettaScreen extends GuiScreen {
	public int GUI_ID;
	protected IGUIWidget rootWidget;
	protected IGUIWidget currentMouseOver = null;
	protected GUIContext ctx;
    
	protected ArrayList<IGUIWidget> drawList = new ArrayList<IGUIWidget>(128);
	
	protected static Comparator<IGUIWidget> compareLayer = new Comparator<IGUIWidget>() {
		@Override
	    public int compare(IGUIWidget w1, IGUIWidget w2) {
	      //ascending order
	      return ((Integer)w1.getLayer()).compareTo((Integer)w2.getLayer());
	    }
	};
	
	public ZettaScreen() {
    	ctx = new GUIContext(this);
    	rootWidget = new WidgetContainer();
    	Rect2D screenArea = new Rect2D(0, 0, 176, 166);
    	rootWidget.setBounds(screenArea);
	}
	
    @Override
    public void drawScreen(int par1, int par2, float par3) {
        this.drawDefaultBackground();
        drawList.clear();
        addChildrenToDrawList(rootWidget);
        Collections.sort(drawList, compareLayer);
        for(IGUIWidget e : drawList) {
        	e.draw(ctx);
        }
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
    //@Override
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
    //@Override
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
}
