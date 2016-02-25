package ws.zettabyte.zettalib.client.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.lwjgl.opengl.GL11;

import ws.zettabyte.zettalib.client.gui.widgets.WidgetContainer;
import ws.zettabyte.zettalib.client.gui.widgets.WidgetSimple;
import ws.zettabyte.zettalib.client.render.SpriteResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.client.Minecraft;

@SideOnly(Side.CLIENT)
public class ZettaScreen extends SmartScreenBase {
	
	protected ItemStack heldStack = null;
	
	protected WidgetSimple bg;
    
	public ZettaScreen(Container container) {
		super(container);
		bg = new WidgetSimple(getRootWidget());

		bg.setX(0);
		bg.setY(0);
		bg.setWidth(176);
		bg.setHeight(166);
		bg.setLayer(1);
		bg.setSprite(new SpriteResourceLocation(CommonIcons.backgroundInv));
	}
	
	//TODO: Inventory logic.
}
