package ws.zettabyte.zettalib.client.gui.widgets;

import java.util.ArrayList;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTank;

import org.lwjgl.opengl.GL11;

import ws.zettabyte.zettalib.client.gui.GUIContext;
import ws.zettabyte.zettalib.client.gui.IGUIWidget;
import ws.zettabyte.zettalib.client.render.SpriteFluid;
import ws.zettabyte.zettalib.client.render.SpriteTiler;
import ws.zettabyte.zettalib.inventory.FluidTankNamed;
import ws.zettabyte.zettalib.inventory.IComponentReceiver;
import ws.zettabyte.zettalib.inventory.IInvComponent;
/**
 * A widget that tracks a Forge FluidTank class, rendering the contained Fluid's icon in a rectangle whose 
 * size is proportional to how full the tank is (tank.stack.getAmount() / tank.getCapacity()).
 * @author Sam "Gyro" C.
 *
 */
public class WidgetFluidBar extends WidgetAmountBar implements IComponentReceiver {
	protected final String name;
	protected FluidTank currentTank = null;
	//protected SpriteTiler sprite = null;
	//private final ResourceLocation tex = new ResourceLocation("weirdscience", "textures/blocks/smog.png"); //TODO: Not this
	
	protected ArrayList<String> comps = new ArrayList<String>(1);
	
	public WidgetFluidBar(String n) {
		super();
		name = n;
		if(n != null) comps.add(name);
	}

	public WidgetFluidBar(String n, IGUIWidget p) {
		super(p);
		name = n;
		if(n != null) comps.add(name);
	}

	@Override
	public void provideComponent(IInvComponent comp) {
		if(comp == null) return;
		if(!(comp instanceof FluidTank)) return;
		currentTank = (FluidTank)comp;
	}

	@Override
	protected float getValue() {
		if(currentTank == null)	return 0;
		if(currentTank.getFluidAmount() == 0) return 0;
		return  (float)currentTank.getFluidAmount() / (float)currentTank.getCapacity();
	}

	@Override
	public void draw(GUIContext context) {
		if(currentTank == null) return;
		if(this.sprite == null) sprite = new SpriteTiler(new SpriteFluid(currentTank));
		super.draw(context);
	}

	@Override
	public IGUIWidget newThis() {
		return new WidgetFluidBar(name);
	}

	@Override
	public IGUIWidget copy() {
		IGUIWidget clone = super.copy();
		((WidgetFluidBar)clone).provideComponent((IInvComponent) currentTank);
		((WidgetFluidBar)clone).setDirection(getDirection());
		return clone;
	}

	@Override
	public Iterable<String> getComponentsSought() {
		return comps;
	}
	
}
