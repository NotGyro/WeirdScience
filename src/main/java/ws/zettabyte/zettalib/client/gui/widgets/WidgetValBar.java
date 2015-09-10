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
import ws.zettabyte.zettalib.inventory.SimpleInvComponent;
/**
 * A widget that tracks a Forge FluidTank class, rendering the contained Fluid's icon in a rectangle whose 
 * size is proportional to how full the tank is (tank.stack.getAmount() / tank.getCapacity()).
 * @author Sam "Gyro" C.
 *
 */
public class WidgetValBar extends WidgetAmountBar implements IComponentReceiver {
	protected final String name;
	protected SimpleInvComponent<Float> valueSource = null;
	//private final ResourceLocation tex = new ResourceLocation("weirdscience", "textures/blocks/smog.png"); //TODO: Not this
	
	protected ArrayList<String> comps = new ArrayList<String>(1);
	
	public WidgetValBar(String n) {
		super();
		name = n;
		if(n != null) comps.add(n);
	}

	public WidgetValBar(String n, IGUIWidget p) {
		super(p);
		name = n;
		if(n != null) comps.add(n);
	}

	@Override
	public void provideComponent(IInvComponent comp) {
		if(comp == null) return;
		if(valueSource != null) return;
		if(!(comp instanceof SimpleInvComponent<?>)) return;
		valueSource = (SimpleInvComponent<Float>)comp;
	}

	@Override
	protected float getValue() {
		if(valueSource == null)	return 0;
		return valueSource.val;
	}

	@Override
	public IGUIWidget newThis() {
		return new WidgetValBar(name);
	}

	@Override
	public IGUIWidget copy() {
		IGUIWidget clone = super.copy();
		if(valueSource instanceof IInvComponent) ((WidgetValBar)clone).provideComponent((IInvComponent)valueSource);
		((WidgetValBar)clone).setDirection(getDirection());
		return clone;
	}

	@Override
	public Iterable<String> getComponentsSought() {
		return comps;
	}

	@Override
	public void draw(GUIContext context) {
		if(valueSource == null) return;
		super.draw(context);
	}
	
	
	
}
