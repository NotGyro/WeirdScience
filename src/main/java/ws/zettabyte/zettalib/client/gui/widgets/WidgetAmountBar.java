package ws.zettabyte.zettalib.client.gui.widgets;

import org.lwjgl.opengl.GL11;

import ws.zettabyte.zettalib.client.gui.GUIContext;
import ws.zettabyte.zettalib.client.gui.IGUIWidget;

/**
 * Anything that represents some value by the size of a graphic in a single direction.
 * 
 * In this case, the width and height of the widget represent the bounds it will fill, rather than what you see.
 * @author Sam "gyro" Cutlip
 *
 */
public abstract class WidgetAmountBar extends WidgetContainer {
	
	float tintR = 1.0F;
	float tintG = 1.0F;
	float tintB = 1.0F; 
	float tintA = 1.0F;

	public static enum EXPAND_DIR { 
		RIGHT,
		UP,
		LEFT,
		DOWN
	}
	//Which direction is the bar meant to expand to?
	protected EXPAND_DIR direction = EXPAND_DIR.RIGHT;
	
	public EXPAND_DIR getDirection() {
		return direction;
	}

	public void setDirection(EXPAND_DIR direction) {
		this.direction = direction;
	}

	public WidgetAmountBar() {
		super();
	}

	public WidgetAmountBar(IGUIWidget p) {
		super(p);
	}
	
	/**
	 * Get the value our bar's size is supposed to be tracking - should be from 0.0 to 1.0.
	 */
	protected abstract float getValue();

	protected int getDrawWidth() {
		if((direction == EXPAND_DIR.RIGHT) || (direction == EXPAND_DIR.LEFT)) {
			return (int)(((float)getWidth()) * getValue());
		}
		return this.getWidth();
	}
	protected int getDrawHeight() {
		if((direction == EXPAND_DIR.UP) || (direction == EXPAND_DIR.DOWN)) {
			return (int)((float)getHeight() * getValue());
		}
		return this.getHeight();
	}
	protected int getDrawX() {
		if(direction == EXPAND_DIR.LEFT) {
			//X Pos plus how much empty space is in the bar. (which is available space minus current space)
			return getX() + ( getWidth() - (int)((float)getWidth() * getValue()));
		}
		return getX();
	}
	protected int getDrawY() {
		if(direction == EXPAND_DIR.UP) {
			//Y Pos plus how much empty space is in the bar. (which is available space minus current space)
			return getY() + ( getHeight() - (int)((float)getHeight() * getValue()));
		}
		return getY();
	}

	@Override
	public void setTint(float R, float G, float B, float A) {
		super.setTint(R, G, B, A);
		
		tintR = R;
		tintG = G; 
		tintB = B; 
		tintA = A;
	}
	@Override
	public IGUIWidget copy() {
		IGUIWidget clone = super.copy();
		
		clone.setTint(tintR, tintG, tintB, tintA);
		((WidgetAmountBar)clone).setDirection(getDirection());
		return clone;
	}


}
