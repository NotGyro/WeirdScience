package ws.zettabyte.zettalib.client.render;

public class SpriteSolidColor implements ISprite {
	public float r, g, b;
	//Opaque by default
	public float a = 1.0F;
	public SpriteSolidColor() {	}

	@Override
	public void draw(IRenders2D context, double x, double y, double width,
			double height) {
		context.drawRectangle((float)x, (float)y, (float)(x+width), (float)(y+height), r, g, b, a);
	}

}
