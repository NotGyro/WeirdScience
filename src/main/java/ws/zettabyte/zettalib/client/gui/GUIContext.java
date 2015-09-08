package ws.zettabyte.zettalib.client.gui;

import ws.zettabyte.zettalib.message.IMessageMiddleman;
import ws.zettabyte.zettalib.message.MiddlemanTransparent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;

public class GUIContext {
	public SmartScreenBase screen;
	//public final Rect2D screenRect;
	GUIContext(SmartScreenBase scr) {screen = scr;};
	
	public int mouseX = 0;
	public int mouseY = 0;
	
    public float zLevelPerLayer = 0.2F;
    
    //public IMessageMiddleman messenger = new MiddlemanTransparent();
}
