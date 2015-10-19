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
public class SmartScreenBase extends GuiContainer implements IRenders2D, IGUI {
	protected IGUIWidget currentMouseOver = null;
	protected GUIContext ctx;
	
	private int eventButton;
	private long lastMouseEvent;
	
	protected Vert2D mousePos = new Vert2D();
	
	protected GUIManagerWidgets managerWidgets = new GUIManagerWidgets();
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
	}
    
    /**
     * Centers the GUI screen in the game window.
     */
	protected void center(){
		//NOTE: Width and height refer to the screen in this context. As in, the whole screen.
        int k = (this.width - managerWidgets.getRootWidget().getWidth()) / 2;
        int l = (this.height - managerWidgets.getRootWidget().getHeight()) / 2;

        managerWidgets.getRootWidget().setX(k);
        managerWidgets.getRootWidget().setY(l);
	}
    
    @Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_,
			int p_146979_2_) {
		super.drawGuiContainerForegroundLayer(p_146979_1_, p_146979_2_);
		drawTooltips();
	}

	/**
     * A method invoked by our parent class' drawScreen() method. 
     * Inventory slots and items will be drawn on top of anything drawn here.
     * TODO: figure out what the arguments imply.
     */
	@Override
	protected void drawGuiContainerBackgroundLayer(float a, int b, int c) {
    	this.center();
        this.drawDefaultBackground();
        managerWidgets.drawWidgets(ctx);
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
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
     * Overridden to prevent annoying "must be 256x256" things.
     */
	@Override
    public void drawTexturedRect(double x, double y, 
    		double uStart, double vStart, double uWidth, double vHeight, 
    		double width, double height) {
		RenderUtils.drawTexturedRect(x, y, (double)this.zLevel, uStart, vStart, uWidth, vHeight, width, height);
    }

	@Override
	public Minecraft getMC() {
		return this.mc;
	}

    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, width, height.
     * Draws the whole whole source image in the defined area, from u 0, v 0 to u 1, v 1.
     */
	@Override
    public void drawWholeTexturedRect(double x, double y, double width, double height) {
    	this.drawTexturedRect(x, y, 0.0D,  0.0D, 1.0D, 1.0D, width, height);
    }
	@Override
	public void drawGradientRectangle(int p_73733_1_, int p_73733_2_,
			int p_73733_3_, int p_73733_4_, int p_73733_5_, int p_73733_6_) {
		this.drawGradientRect(p_73733_1_, p_73733_2_, p_73733_3_, p_73733_4_, p_73733_5_, p_73733_6_);
	}
	@Override
	public void drawHorzLine(int p_73730_1_, int p_73730_2_, int p_73730_3_, int p_73730_4_) {
		this.drawHorizontalLine(p_73730_1_, p_73730_2_, p_73730_3_, p_73730_4_);
	}
	@Override
	public void drawVertLine(int p_73728_1_, int p_73728_2_, int p_73728_3_, int p_73728_4_) {
		this.drawVerticalLine(p_73728_1_, p_73728_2_, p_73728_3_, p_73728_4_);
	}

	@Override
    public void drawRectangle(float x1, float y1, float x2, float y2, float r, float g, float b, float a) {
        RenderUtils.drawRectangle(x1, y1, x2, y2, (double)this.zLevel, r, g, b, a);
    }
	
	@Override
	protected void mouseMovedOrUp(int mouseX, int mouseY,
			int which) {
		super.mouseMovedOrUp(mouseX, mouseY, which);
		
		this.managerWidgets.mouseMovedOrUp(mousePos, which);
	}
	protected ArrayList<IGUIWidget> tooltipWidgetList = new ArrayList<IGUIWidget>(64);
	
	protected void drawTooltips() {
		center();
        managerWidgets.drawTooltips(this, mousePos);
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
		this.managerWidgets.keyTyped(p_73869_1_, p_73869_2_);
		super.keyTyped(p_73869_1_, p_73869_2_);
	}

	@Override
	public boolean addWidget(IGUIWidget w) {
		return this.managerWidgets.addWidget(w);
	}

	@Override
	public IGUIWidget getRootWidget() {
		return this.managerWidgets.getRootWidget();
	}

	@Override
	public void drawTooltipText(List l, int x, int y, FontRenderer fr) {
		this.drawHoveringText(l, x-this.guiLeft, y-this.guiTop, fr);
	}

	protected int GuiID = -1;
	@Override
	public int getGuiID() {
		return GuiID;
	}

	@Override
	public void setGuiID(int id) {
		GuiID = id;
	}
}
