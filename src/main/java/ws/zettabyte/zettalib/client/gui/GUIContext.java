package ws.zettabyte.zettalib.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GUIContext {
	public ZettaScreen screen;
	//public final Rect2D screenRect;
	GUIContext(ZettaScreen scr) {screen = scr;};
	
	public int mouseX = 0;
	public int mouseY = 0;
	
    public float zLevelPerLayer = 0.2F;
}
