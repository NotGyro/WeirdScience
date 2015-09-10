package ws.zettabyte.zettalib.client.gui;

import ws.zettabyte.zettalib.message.IMessageMiddleman;
import ws.zettabyte.zettalib.message.MiddlemanTransparent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;

/**
 * Everything that a Widget would need to sanely draw itself.
 * @author Sam "Gyro" C.
 */
public class GUIContext {
	public SmartScreenBase screen;
	GUIContext(SmartScreenBase scr) {screen = scr;};
	
	//public int mouseX = 0;
	//public int mouseY = 0;
	
    public float zLevelPerLayer = 0.2F;
}
