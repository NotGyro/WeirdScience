package ws.zettabyte.zettalib.client.gui.widgets;

import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ws.zettabyte.zettalib.client.gui.GUIContext;
import ws.zettabyte.zettalib.client.gui.IGUIWidget;

/**
 * Just a static image which is drawn.
 * @author Sam "Gyro" Cutlip
 *
 */
public class WidgetSimple extends WidgetContainer {
	
	float tintR = 1.0F;
	float tintG = 1.0F;
	float tintB = 1.0F; 
	float tintA = 1.0F;

	protected ResourceLocation tex;
	public WidgetSimple() {
	}

	public WidgetSimple(IGUIWidget p) {
		super(p);
	}

	@Override
	public void draw(GUIContext context) {
		// TODO Auto-generated method stub
		super.draw(context);
		//Setup layer
		context.screen.setZLevel(context.screen.getZLevel() 
				+ (getLayer() * context.zLevelPerLayer));
        //ITextureObject texture = context.mc.renderEngine.getTexture(tex);
        GL11.glColor4f(tintR, tintG, tintB, tintA);
        context.screen.mc.renderEngine.bindTexture(tex);
        //int x = (width - xSize) / 2;
        //int y = (height - ySize) / 2;
        context.screen.drawWholeTexturedRect(getX(), getY(), getWidth(), getHeight());
	}

	public void setArt(ResourceLocation art) {
		this.tex = art;
	}

	@Override
	public void setTint(float R, float G, float B, float A) {
		// TODO Auto-generated method stub
		super.setTint(R, G, B, A);
		tintR = R;
		tintG = G; 
		tintB = B; 
		tintA = A;
	}
	@Override
	public IGUIWidget newThis() {
		return new WidgetSimple();
	}
	
	@Override
	public IGUIWidget copy() {
		IGUIWidget clone = super.copy();
		clone.setTint(tintR, tintG, tintB, tintA);
		((WidgetSimple)clone).setArt(tex);
		
		return clone;
	}
}
