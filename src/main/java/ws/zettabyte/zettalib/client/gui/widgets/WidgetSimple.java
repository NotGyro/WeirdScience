package ws.zettabyte.zettalib.client.gui.widgets;

import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ws.zettabyte.zettalib.client.gui.GUIContext;
import ws.zettabyte.zettalib.client.gui.IGUIWidget;
import ws.zettabyte.zettalib.client.render.ISprite;
import ws.zettabyte.zettalib.client.render.SpriteResourceLocation;

/**
 * A widget that draws an image, stretching it to fill its entire bounding box.
 * @author Sam "Gyro" C.
 */
public class WidgetSimple extends WidgetContainer {
	
	float tintR = 1.0F;
	float tintG = 1.0F;
	float tintB = 1.0F; 
	float tintA = 1.0F;

	protected ISprite sprite;
	public WidgetSimple() {
	}

	public WidgetSimple(IGUIWidget p) {
		super(p);
	}

	@Override
	public void draw(GUIContext context) {
		//Setup layer
		context.screen.setZLevel(context.screen.getZLevel() 
				+ (getLayer() * context.zLevelPerLayer));
        sprite.draw(context.screen, getX(), getY(), getWidth(), getHeight());
	}

	@Override
	public void setSprite(ISprite art) {
		this.sprite = art;
	}
	public void setSprite(ResourceLocation art) {
		this.setSprite(new SpriteResourceLocation(art));
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
		((WidgetSimple)clone).setSprite(sprite);
		
		return clone;
	}
}
