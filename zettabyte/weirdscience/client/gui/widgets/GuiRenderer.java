package zettabyte.weirdscience.client.gui.widgets;

/* Below is a bunch of code copied and pasted from CoFHLib's GuiBase.java,
 * which is legally, ethically, and technically correct as the library was
 * released under the LGPL, and the authors, on its github page, recommend
 * shadowing code (that is, doing exactly this).
 * 
 * The code has been modified somewhat, however, for the purposes of our
 * Gui system, which is structured somewhat differently.
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import zettabyte.weirdscience.client.gui.RenderHelper;
import zettabyte.weirdscience.util.StringHelper;
import cpw.mods.fml.common.Loader;

public class GuiRenderer {

	protected static final float layerToZFactor = 0.000976563f;
	public static float layerToZ(int layer) {
		return (float)layer * layerToZFactor;
	}
	/**
	 * Simple method used to draw a fluid of arbitrary size.
	 */
	public void drawFluid(int x, int y, int layer, FluidStack fluid, int width, int height) {

		if (fluid == null || fluid.getFluid() == null) {
			return;
		}
		RenderHelper.setBlockTextureSheet();
		RenderHelper.setColor3ub(fluid.getFluid().getColor(fluid));

		drawTiledTexture(x, y, layer, fluid.getFluid().getIcon(fluid), width, height);
	}

	public void drawTiledTexture(int x, int y, int layer, Icon icon, int width, int height) {

		int i = 0;
		int j = 0;

		int drawHeight = 0;
		int drawWidth = 0;

		for (i = 0; i < width; i += 16) {
			for (j = 0; j < height; j += 16) {
				drawWidth = Math.min(width - i, 16);
				drawHeight = Math.min(height - j, 16);
				drawScaledTexturedModelRectFromIcon(x + i, y + j, layer, icon, drawWidth, drawHeight);
			}
		}
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0F);
	}

	public void drawIcon(Icon icon, int x, int y, int layer, int spriteSheet) {

		if (spriteSheet == 0) {
			RenderHelper.setBlockTextureSheet();
		} else {
			RenderHelper.setItemTextureSheet();
		}
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0F);
		drawScaledTexturedModelRectFromIcon(x, y, layer, icon, 16, 16);
	}

	public void drawSizedTexturedModalRect(int x, int y, int layer, int u, int v, int width, int height, float texW, float texH) {

		float texU = 1 / texW;
		float texV = 1 / texH;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(x + 0, y + height, layerToZ(layer), (u + 0) * texU, (v + height) * texV);
		tessellator.addVertexWithUV(x + width, y + height, layerToZ(layer), (u + width) * texU, (v + height) * texV);
		tessellator.addVertexWithUV(x + width, y + 0, layerToZ(layer), (u + width) * texU, (v + 0) * texV);
		tessellator.addVertexWithUV(x + 0, y + 0, layerToZ(layer), (u + 0) * texU, (v + 0) * texV);
		tessellator.draw();
	}

	public void drawScaledTexturedModelRectFromIcon(int x, int y, int layer, Icon icon, int width, int height) {

		if (icon == null) {
			return;
		}
		double minU = icon.getMinU();
		double maxU = icon.getMaxU();
		double minV = icon.getMinV();
		double maxV = icon.getMaxV();

		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(x + 0, y + height, layerToZ(layer), minU, minV + (maxV - minV) * height / 16F);
		tessellator.addVertexWithUV(x + width, y + height, layerToZ(layer), minU + (maxU - minU) * width / 16F, minV + (maxV - minV) * height / 16F);
		tessellator.addVertexWithUV(x + width, y + 0, layerToZ(layer), minU + (maxU - minU) * width / 16F, minV);
		tessellator.addVertexWithUV(x + 0, y + 0, layerToZ(layer), minU, minV);
		tessellator.draw();
	}
/*
	public void drawTooltip(List<String> list) {

		drawTooltipHoveringText(list, mouseX, mouseY, fontRenderer);
		tooltip.clear();
	}*/
	
	/*
	protected void drawTooltipHoveringText(List list, int x, int y, int layer, FontRenderer font) {

		if (list == null || list.isEmpty()) {
			return;
		}
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		int k = 0;
		Iterator iterator = list.iterator();

		while (iterator.hasNext()) {
			String s = (String) iterator.next();
			int l = font.getStringWidth(s);

			if (l > k) {
				k = l;
			}
		}
		int i1 = x + 12;
		int j1 = y - 12;
		int k1 = 8;

		if (list.size() > 1) {
			k1 += 2 + (list.size() - 1) * 10;
		}
		if (i1 + k > this.width) {
			i1 -= 28 + k;
		}
		if (j1 + k1 + 6 > this.height) {
			j1 = this.height - k1 - 6;
		}
		layerToZ(layer) = 300.0F;
		itemRenderer.zLevel = 300.0F;
		int l1 = -267386864;
		this.drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
		this.drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
		this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
		this.drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
		this.drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
		int i2 = 1347420415;
		int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
		this.drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
		this.drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
		this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
		this.drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);

		for (int k2 = 0; k2 < list.size(); ++k2) {
			String s1 = (String) list.get(k2);
			font.drawStringWithShadow(s1, i1, j1, -1);

			if (k2 == 0) {
				j1 += 2;
			}
			j1 += 10;
		}
		layerToZ(layer) = 0.0F;
		itemRenderer.zLevel = 0.0F;
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
	} */

}
