package ws.zettabyte.zettalib.client.render;

/**
 * Any sprite for which an offset to its usual UV would make sense.
 * @author Sam "Gyro" C.
 *
 */
public interface ISpriteUV extends ISprite {
	void drawWithUVOffset(IRenders2D context, double x, double y, double width, double height, double uStart, double vStart, double uWidth, double vHeight);
	
	default void draw(IRenders2D context, double x, double y, double width, double height) { 
		drawWithUVOffset(context, x, y, width, height, 0.0D, 0.0D, 1.0D, 1.0D); 
	};
}
