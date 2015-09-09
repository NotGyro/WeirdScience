package ws.zettabyte.zettalib.client.gui;

import net.minecraft.util.ResourceLocation;
/**
 * A static collection of all of the icons likely to be used over and over again
 * by Minecraft GUIs. Includes item slots, an inventory background for inventories
 * where you can access the full player inventory, a background for accessing only
 * the hotbar, and a background graphic that resembles the standard Minecraft look
 * but provides no access to the player's inventory at all.
 * @author Sam "Gyro" Cutlip
 *
 */
public final class CommonIcons {
	public static final ResourceLocation slotItem 
	= new ResourceLocation("zettalib", "textures/gui/slot.png");
	
	public static final ResourceLocation backgroundInv 
	= new ResourceLocation("zettalib", "textures/gui/background_playerinv.png");

	public static final ResourceLocation backgroundHotbar 
	= new ResourceLocation("zettalib", "textures/gui/background_hotbar.png");
	
	public static final ResourceLocation backgroundBlank 
	= new ResourceLocation("zettalib", "textures/gui/background_blank.png");
}
