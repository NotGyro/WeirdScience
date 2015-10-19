package ws.zettabyte.zettalib.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.Tessellator;

public final class RenderUtils {
    public static void drawTexturedRect(double x, double y, double z, 
    		double uStart, double vStart, double uWidth, double vHeight, 
    		double width, double height) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((x + 0), 
        		(y + height), 
        		z, 
        		uStart, 
        		vStart + vHeight );
        tessellator.addVertexWithUV((x + width), 
        		(y + height), 
        		z, 
        		uStart + uWidth, 
        		vStart + vHeight );
        tessellator.addVertexWithUV((x + width), 
        		(y + 0), 
        		z, 
        		uStart + uWidth, 
        		vStart );
        tessellator.addVertexWithUV((x + 0), 
        		(y + 0), 
        		z, 
        		uStart,
        		vStart );
        tessellator.draw();
    }
    public static void drawRectangle(float x1, float y1, float x2, float y2, double z, float r, float g, float b, float a) {
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
        tessellator.addVertex((double)x1, (double)y2, z);
        tessellator.addVertex((double)x2, (double)y2, z);
        tessellator.addVertex((double)x2, (double)y1, z);
        tessellator.addVertex((double)x1, (double)y1, z);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        //GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
