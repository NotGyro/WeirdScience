package ws.zettabyte.zettalib.client.gui.widgets;

import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import ws.zettabyte.zettalib.client.gui.GUIContext;
import ws.zettabyte.zettalib.client.gui.IGUITank;
import ws.zettabyte.zettalib.client.gui.IGUIWidget;
import ws.zettabyte.zettalib.inventory.FluidTankNamed;

public class WidgetFluidBar extends WidgetAmountBar implements IGUITank {

	protected final String name;
	protected FluidTankNamed currentTank = null;
	private final ResourceLocation tex = new ResourceLocation("weirdscience", "textures/blocks/smog.png"); //TODO: Not this
	
	public WidgetFluidBar(String n) {
		super();
		name = n;
	}

	public WidgetFluidBar(String n, IGUIWidget p) {
		super(p);
		name = n;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public void provideTank(FluidTankNamed tank) {
		if(tank != null) currentTank = tank;
	}

	@Override
	protected float getValue() {
		if(currentTank == null)	return 0;
		if(currentTank.getFluidAmount() == 0) return 0;
		return  (float)currentTank.getFluidAmount() / (float)currentTank.getCapacity();
	}

	@Override
	public void draw(GUIContext context) {
		super.draw(context);
		//Setup layer
		context.screen.setZLevel(context.screen.getZLevel() 
				+ (getLayer() * context.zLevelPerLayer));
        //ITextureObject texture = context.mc.renderEngine.getTexture(tex);
        GL11.glColor4f(tintR, tintG, tintB, tintA);
        context.screen.mc.renderEngine.bindTexture(tex);
        //int x = (width - xSize) / 2;
        //int y = (height - ySize) / 2;
        int x = getDrawX();
        int y = getDrawY();
        int w = getDrawWidth();
        int h = getDrawHeight();
        float debugV = getValue();
        context.screen.drawWholeTexturedRect(x, y, w, h);
	}

	@Override
	protected IGUIWidget newThis() {
		return new WidgetFluidBar(name);
	}

	@Override
	public IGUIWidget copy() {
		IGUIWidget clone = super.copy();
		((WidgetFluidBar)clone).provideTank(currentTank);
		return clone;
	}
	
}
