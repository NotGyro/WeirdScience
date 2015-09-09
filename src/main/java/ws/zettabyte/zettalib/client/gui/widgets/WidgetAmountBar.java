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
	
	/**
	 * Which direction scales proportionately with our input value?
	 * In other words, if we have a fluid tank slowly filling up,
	 * in which direction should our widget be getting larger?
	 */
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
	 * 
	 * A value of 1.0 means that a bar expanding to the left will display as getWidth() units wide,
	 * whereas a value of 0.2 means our bar will be getWidth()*0.2, etc...
	 */
	protected abstract float getValue();

	protected float getDrawWidth() {
		if((direction == EXPAND_DIR.RIGHT) || (direction == EXPAND_DIR.LEFT)) {
			return (float)getWidth() * getValue();
		}
		return this.getWidth();
	}
	protected float getDrawHeight() {
		if((direction == EXPAND_DIR.UP) || (direction == EXPAND_DIR.DOWN)) {
			return (float)getHeight() * getValue();
		}
		return this.getHeight();
	}
	protected float getDrawX() {
		if(direction == EXPAND_DIR.LEFT) {
			//X Pos plus how much empty space is in the bar. (which is available space minus current space)
			return (float)getX() + ( (float)getWidth() - (float)getWidth() * getValue());
		}
		return getX();
	}
	protected float getDrawY() {
		if(direction == EXPAND_DIR.UP) {
			//Y Pos plus how much empty space is in the bar. (which is available space minus current space)
			return (float)getY() + ((float)getHeight() - (float)getHeight() * getValue());
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
