package zettabyte.weirdscience.client.gui;


import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import cofh.gui.GuiBase;
import cofh.gui.slot.SlotOutput;
import cofh.gui.slot.SlotSpecificItem;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import zettabyte.weirdscience.tileentity.TileEntityPhosphateEngine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPhosphateEngine extends GuiBase {
    private static final ResourceLocation engineGuiTextures = new ResourceLocation("weirdscience:textures/gui/genericcontainer.png");
	public GuiPhosphateEngine (InventoryPlayer inventoryPlayer, TileEntityPhosphateEngine tileEntity) {
		//Instantiate the container.
		super(new ContainerPhosphateEngine(inventoryPlayer, tileEntity));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		//draw text and stuff here
		//the parameters for drawString are: string, x, y, color
		fontRenderer.drawString("Tiny", 8, 6, 4210752);
		//draws "Inventory" or your regional equivalent
		fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		//draw your Gui here, only thing you need to change is the path
		this.mc.getTextureManager().bindTexture(engineGuiTextures);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(engineGuiTextures);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
}
