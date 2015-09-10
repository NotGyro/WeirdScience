package ws.zettabyte.zettalib.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class SpriteResourceLocation implements ISprite {

	protected final ResourceLocation tex;

	public SpriteResourceLocation(ResourceLocation l) {
		tex = l;
	}
	

	@Override
	public void draw(IRenders2D context, double x, double y, double width,
			double height) {
        context.getMC().renderEngine.bindTexture(tex);
        //int x = (width - xSize) / 2;
        //int y = (height - ySize) / 2;
        context.drawWholeTexturedRect(x, y, width, height);
	}

	//protected int w = -1;
	//protected int h = -1;
	/**
	 * Alternate constructor for when you know what width and height an icon will be ahead of time.
	//
	public SpriteResourceLocation(ResourceLocation l, int iconWidth, int iconHeight) {
		this(l);
		w = iconWidth;
		h = iconHeight;
	}
	/*
	@Override
	public int getNativeWidth() {
		return w;
	}

	@Override
	public int getNativeHeight() {
		return h;
	}*/

}
