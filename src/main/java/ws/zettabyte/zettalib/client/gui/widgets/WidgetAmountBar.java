package ws.zettabyte.zettalib.client.gui.widgets;

import org.lwjgl.opengl.GL11;

import ws.zettabyte.zettalib.client.gui.GUIContext;
import ws.zettabyte.zettalib.client.gui.IGUIWidget;
import ws.zettabyte.zettalib.client.render.SpriteFluid;
import ws.zettabyte.zettalib.client.render.SpriteTiler;

/**
 * Anything that represents some value by the size of a graphic in a single direction.
 * 
 * In this case, the width and height of the widget represent the bounds it will fill, rather than what you see.
 * @author Sam "gyro" C.
 *
 */
public abstract class WidgetAmountBar extends WidgetSimple {
	
	float tintR = 1.0F;
	float tintG = 1.0F;
	float tintB = 1.0F; 
	float tintA = 1.0F;

	//public static boolean forceDisableInterpolate = false;
	public boolean interpolate = true;
	protected long timeLastChange = 0;
	protected long timeLastChange2 = 0;
	protected float rate = 0.04F;
	protected static final float secondsLastChangeDefault = 1.0F;
	protected float secondsLastChange = secondsLastChangeDefault;
	protected long timeLastDraw = 0;

	protected float mostRecentValue = -1.0F;
	protected float drawValue = 0.0F;
	
	//Difference the last time the value changed.
	protected float delta = 0.0F;
	boolean steady = true;

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
	protected void initTime() {
		timeLastChange = System.currentTimeMillis();
		timeLastChange2 = System.currentTimeMillis();
		timeLastDraw = System.currentTimeMillis();
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
	
	//Protected void to ensure tanks don't animate filling up if you join a server and immediately open a full one.
	protected void firstDraw() {
		mostRecentValue = getValue();
		drawValue = getValue();
		initTime();
	}
	
	//Interpolate
	protected float getValueDraw() {
		//if((!interpolate) || forceDisableInterpolate) return getValue();
		long timeNow = System.currentTimeMillis();
		if(mostRecentValue < 0) {
			firstDraw();
		}
		else {
			if(getValue() != mostRecentValue) {
				timeLastChange2 = timeLastChange;
				timeLastChange = timeNow;
				secondsLastChange = ((float)(timeLastChange-timeLastChange2))/(1000.0F);
				//Negative if our value is falling.
				delta = getValue() - mostRecentValue;	
				if(!steady) {		
					//If we have been in a resting state, don't mess with our rate of change estimate.
					//Which is to say, ignore the amount of time it took to see a change if we've obviously
					//been in a state where nothing should be happening for a while, and reuse our old value.
					rate = delta / secondsLastChange;
				}
				mostRecentValue = getValue();
				steady = false;
			}
			if(!steady) {
				float secondsDraw = ((float)(timeNow-timeLastDraw))/(1000.0F);
				track(secondsDraw);
			}
		}
		timeLastDraw = timeNow;
		return drawValue;
	}
	protected void track(float secondsDraw) {
		drawValue += rate * secondsDraw; //units per second times seconds equals units
		if((drawValue >= getValue()) && (rate > 0)) {
			drawValue = getValue();
			steady = true;
		}
		if((drawValue <= getValue()) && (rate < 0)) {
			drawValue = getValue();
			steady = true;
		}
	}

	protected float getDrawWidth() {
		if((direction == EXPAND_DIR.RIGHT) || (direction == EXPAND_DIR.LEFT)) {
			return (float)getWidth() * getValueDraw();
		}
		return this.getWidth();
	}
	protected float getDrawHeight() {
		if((direction == EXPAND_DIR.UP) || (direction == EXPAND_DIR.DOWN)) {
			return (float)getHeight() * getValueDraw();
		}
		return this.getHeight();
	}
	protected float getDrawX() {
		if(direction == EXPAND_DIR.LEFT) {
			//X Pos plus how much empty space is in the bar. (which is available space minus current space)
			return (float)getX() + ( (float)getWidth() - (float)getWidth() * getValueDraw());
		}
		return getX();
	}
	protected float getDrawY() {
		if(direction == EXPAND_DIR.UP) {
			//Y Pos plus how much empty space is in the bar. (which is available space minus current space)
			return (float)getY() + ((float)getHeight() - (float)getHeight() * getValueDraw());
		}
		return getY();
	}

	@Override
	public void draw(GUIContext context) {
		if(sprite == null) return;
		context.screen.setZLevel(context.screen.getZLevel() 
				+ (getLayer() * context.zLevelPerLayer));
        sprite.draw(context.screen, getDrawX(), getDrawY(), getDrawWidth(), getDrawHeight());
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
