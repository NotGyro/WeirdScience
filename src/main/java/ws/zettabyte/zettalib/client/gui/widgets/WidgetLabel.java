package ws.zettabyte.zettalib.client.gui.widgets;

import java.util.ArrayList;
import java.util.List;

import ws.zettabyte.zettalib.client.gui.GUIContext;
import ws.zettabyte.zettalib.client.gui.IGUIWidget;

public class WidgetLabel extends WidgetContainer {
	protected String text;
	public WidgetLabel() {
	}

	public WidgetLabel(IGUIWidget p) {
		super(p);
	}
	
	protected String getTextDraw() {
		if(text == null) return "";
		return "§f" + text;
	}
	
	public void setText(String t) {
		text = t;
	}

	@Override
	public void draw(GUIContext context) {
		super.draw(context);
		context.screen.drawString(getTextDraw(), this.getX(), this.getY(), (int)(context.screen.getZLevel() + (context.zLevelPerLayer*getLayer())));
	}

	@Override
	public boolean addChild(IGUIWidget child) { return false; }
	@Override
	public ArrayList<IGUIWidget> getChildren() { return null; }

	@Override
	protected IGUIWidget newThis() {
		return new WidgetLabel();
	}

	@Override
	public IGUIWidget copy() {
		WidgetLabel clone = (WidgetLabel)super.copy();
		clone.setText(this.text);
		return clone;
	}
	
	@Override
	public boolean getHasTooltip(boolean verbose) { return false; }
	@Override
	public boolean getHasTooltip() { return false; }
	@Override
	public void addTooltip(String text) { }
	@Override
	public List getTooltips(boolean verbose) { return null; }
}