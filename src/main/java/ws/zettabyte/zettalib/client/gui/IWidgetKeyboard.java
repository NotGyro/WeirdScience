package ws.zettabyte.zettalib.client.gui;

public interface IWidgetKeyboard {
	boolean isInputCaptured();
	void click(int btn, int mouseX, int mouseY);
	boolean keyTyped(char keyChar, int keyInt);
	void setFocused(boolean b);
	void setCanLoseFocus(boolean b);
}
