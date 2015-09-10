package ws.zettabyte.zettalib.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;

/**
 * Anything we can pass to a sprite's draw() function and expect to do something useful.
 * @author Sam "Gyro" C.
 * TODO: Figure out what the Minecraft equivalents of these functions do so I can properly name the parameters. 
 */
public interface IRenders2D {

    void drawTexturedRect(double x, double y, 
    		double uStart, double vStart, double uWidth, double vHeight, 
    		double width, double height);

    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, width, height.
     * 
     * Draws the whole whole source image in the defined area. -
     * from u 0, v 0 to u 1, v 1.
     */
    default void drawWholeTexturedRect(double x, double y, double width, double height) {
    	this.drawTexturedRect(x, y, 0.0D,  0.0D, 1.0D, 1.0D, width, height);
    }

    /**
     * Draws a rectangle with a vertical gradient between the specified colors.
     */
    void drawGradientRectangle(int p_73733_1_, int p_73733_2_, int p_73733_3_, int p_73733_4_, int p_73733_5_, int p_73733_6_);
    
    void drawHorzLine(int p_73730_1_, int p_73730_2_, int p_73730_3_, int p_73730_4_);
    
    void drawVertLine(int p_73728_1_, int p_73728_2_, int p_73728_3_, int p_73728_4_);
    
    /**
     * Draws a solid color rectangle with the specified coordinates and color. Args: x1, y1, x2, y2, color
     */
    void drawRectangle(float x1, float y1, float x2, float y2, float r, float g, float b, float a);
    
    /**
     * Renders the specified text to the screen, center-aligned.
     */
    void drawCenteredString(FontRenderer fr, String str, int x, int y, int z);

    /**
     * Renders the specified text to the screen.
     */
    void drawString(FontRenderer fr, String str, int x, int y, int z);

    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height.
     * 
     * @param u is actually how many pixels across a 256x256 texture we are.
     * @param v is actually how many pixels down a 256x256 texture we are.
     */
    void drawTexturedModalRect(int x, int y, int u, int v, int width, int height);
    void drawTexturedModelRectFromIcon(int x, int y, IIcon icon, int width, int height);
    
    
	Minecraft getMC();
}
