package zettabyte.weirdscience.client.gui.widgets;

import java.util.Iterator;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import zettabyte.weirdscience.client.gui.RenderHelper;

public class SimpleWidget extends BaseWidget {
	
	protected Icon icon;
	protected int width, height;
	
	protected static final GuiRenderer renderer = new GuiRenderer();
	
	public SimpleWidget() {
		super();
	}
	
	public SimpleWidget(Icon img) {
		super();
		this.icon = img;
	}

	public Icon getIcon() {
		return icon;
	}
	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	@Override
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	@Override
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
	@Override
	public void Draw(GuiScreen context) {
		if(getVisible()) {
			super.Draw(context);
			renderer.drawScaledTexturedModelRectFromIcon(x, y, getLayerAbsolute(), icon, width, height);
		}
	}
}
