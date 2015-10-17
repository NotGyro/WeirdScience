package ws.zettabyte.zettalib.client.gui.widgets;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import ws.zettabyte.zettalib.client.gui.GUIContext;
import ws.zettabyte.zettalib.client.gui.IGUIWidget;
import ws.zettabyte.zettalib.client.gui.IGUIWidgetText;
import ws.zettabyte.zettalib.client.gui.IGUIWidgetKeyboard;
import ws.zettabyte.zettalib.client.gui.Rect2D;

/**
 * Mojang does this very well, so here's a relative-position wrapper around their implementation.
 * @author Sam "Gyro" C.
 *
 */
public class WidgetTextField extends GuiTextField implements IGUIWidget,
		IGUIWidgetText, IGUIWidgetKeyboard {

	protected Rect2D bounds = new Rect2D();
	protected IGUIWidget parent;
	protected int layer = 0;
	protected boolean visible = true;
	protected Field frField;
	protected FontRenderer font;
	
	public final static int ENTER_KEY_CODE = 28;
	
	public WidgetTextField() {
		super(null, 0, 0, 0, 0);
		/*
		 * Spooky scary reflection code.
		 */
		for(Field f : GuiTextField.class.getDeclaredFields()) {
			if(f.getType().isAssignableFrom(FontRenderer.class)) {
				frField = f;
				frField.setAccessible(true);
				break;
			}
		}
		this.setEnabled(true);
	}

	protected void setFontRenderer(FontRenderer f) {
		try {
			frField.set(this, f);
			font = f;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void draw(GUIContext context) {
		this.width = bounds.getWidth();
		this.height = bounds.getHeight();
		this.xPosition = this.getX();
		this.yPosition = this.getY();
		if(context.screen.getFR() == null) return;
		setFontRenderer(context.screen.getFR());
		
		this.setHeight(font.FONT_HEIGHT);
		
		this.drawTextBox();
	}

	@Override
	public Rect2D getRelativeBounds() {
		return bounds;
	}
	

	@Override
	public void setBounds(Rect2D b) {
		bounds = b.copy();
	}

	@Override
	public boolean addChild(IGUIWidget child) {
		return false;
	}

	@Override
	public ArrayList<IGUIWidget> getChildren() { return null; }
	
	@Override
	public IGUIWidget getParent() { return parent; }
	@Override
	public void setParent(IGUIWidget p) { parent = p; }

	@Override
	public int getLayerRelative() {
		return layer;
	}
	@Override
	public void setLayer(int l) { layer = l; }

	@Override
	public boolean isVisible() {
		return this.visible;
	}

	protected WidgetTextField newThis() {
		return (WidgetTextField) new WidgetTextField();
	}

	@Override
	public IGUIWidget copy() {
		WidgetTextField wtf = (WidgetTextField) newThis();
		
		wtf.setParent(null);
		
		wtf.getRelativeBounds().setX(getXRelative());
		wtf.getRelativeBounds().setY(getYRelative());
		wtf.setWidth(this.getWidth());
		wtf.setHeight(this.getHeight());
		
		wtf.setLayer(layer);
		wtf.setVisible(isVisible());
		wtf.setText(this.getText());
		
		wtf.setMaxStringLength(this.getMaxStringLength());
		
		return wtf;
	}
	
	@Override
	public boolean getHasTooltip(boolean verbose) { return false; }
	@Override
	public boolean getHasTooltip() { return false; }
	@Override
	public void addTooltip(String text) { }
	@Override
	public List getTooltips(boolean verbose) { return null; }

	@Override
	public boolean isInputCaptured() {
		return this.isFocused();
	}

	@Override
	public void click(int btn, int mouseX, int mouseY) {
		this.mouseClicked(mouseX, mouseY, btn);
	}

	@Override
	public boolean keyTyped(char keyChar, int keyInt) {
		if(keyInt == ENTER_KEY_CODE) {
			this.enterHit();
		}
		return textboxKeyTyped(keyChar, keyInt);
	}

	@Override
	public int getWidth() {
		return bounds.getWidth();
	}

	@Override
	public void setWidth(int w) {
		bounds.setWidth(w);
		this.width = w;
	}
	
	protected void enterHit() {
		this.setCanLoseFocus(true);
		this.setFocused(false);
	}

}
