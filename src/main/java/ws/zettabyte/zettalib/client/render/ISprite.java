package ws.zettabyte.zettalib.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * An ISprite represents any 2D object that can be drawn at a specified X and Y position
 * across a specified width and height.
 * 
 * ISprites have no reference to the rendering context or the state of what they're meant
 * to indicate - they're just a drawable thing you can draw at your X and Y.
 * 
 * @author Sam "Gyro" C.
 *
 */
public interface ISprite {
	void draw(IRenders2D context, double x, double y, double width, double height);
	
	default void drawWithTint(IRenders2D context, double x, double y, double width, double height, 
			float R, float G, float B, float A) { 
		draw(context, x, y, width, height); 
	};
}
