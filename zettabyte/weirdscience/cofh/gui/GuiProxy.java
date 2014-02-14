package zettabyte.weirdscience.cofh.gui;

import net.minecraft.client.gui.inventory.GuiContainer;

public class GuiProxy {

	/**
	 * Soft proxy reference
	 */
	public static boolean shouldShowTooltip(GuiContainer gui) {

		return false; //gui.manager.shouldShowTooltip();
	}

}
