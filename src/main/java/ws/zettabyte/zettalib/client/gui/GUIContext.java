package ws.zettabyte.zettalib.client.gui;

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
	
    public float zLevelPerLayer = 0.2F;
}
