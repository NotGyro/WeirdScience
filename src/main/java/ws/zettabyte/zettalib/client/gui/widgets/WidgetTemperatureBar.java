package ws.zettabyte.zettalib.client.gui.widgets;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTank;

import org.lwjgl.opengl.GL11;

import ws.zettabyte.zettalib.client.gui.GUIContext;
import ws.zettabyte.zettalib.client.gui.IGUIWidget;
import ws.zettabyte.zettalib.client.render.SpriteFluid;
import ws.zettabyte.zettalib.client.render.SpriteSolidColor;
import ws.zettabyte.zettalib.client.render.SpriteTiler;
import ws.zettabyte.zettalib.inventory.FluidTankNamed;
import ws.zettabyte.zettalib.inventory.IComponentReceiver;
import ws.zettabyte.zettalib.inventory.IInvComponent;
import ws.zettabyte.zettalib.thermal.IHeatLogic;
/**
 * A widget that tracks a IHeatLogic, (by default) rendering the bar in a color with 
 * red proportional to the heat in degrees celsius and green inversely proportional
 * to it.
 * @author Sam "Gyro" C.
 *
 */
public class WidgetTemperatureBar extends WidgetAmountBar implements IComponentReceiver {
	protected String name;
	protected IHeatLogic heat = null;
	
	protected ArrayList<String> tempTooltip = new ArrayList<String>(1);
	protected ArrayList<String> comps = new ArrayList<String>(1);
	
	public WidgetTemperatureBar(String n) {
		super();
		name = n;
		if(n != null) comps.add(name);
	}

	public WidgetTemperatureBar(String n, IGUIWidget p) {
		super(p);
		name = n;
		if(n != null) comps.add(name);
	}
	
	public void setSoughtComponent(String s) {
		name = s;
	}

	@Override
	public void provideComponent(IInvComponent comp) {
		if(comp == null) return;
		if(!(comp instanceof IHeatLogic)) return;
		heat = (IHeatLogic)comp;
	}

	@Override
	protected float getValue() {
		if(heat == null)	return 0;
		if(heat.getHeat() == 0) return 0;
		return  (float)(heat.getHeat() - 32) / 160.0F; //TODO: Saner min / max values.
	}

	@Override
	public void draw(GUIContext context) {
		if(heat == null) return;
		SpriteSolidColor ssc = null;
		if(this.sprite == null) {
			ssc = new SpriteSolidColor();
			ssc.a = 1.0F;
			ssc.b = 0.0F;

			ssc.r = this.getValue();
			ssc.g = 1.0F - this.getValue();
			
			this.sprite = ssc;
		}
		else if(this.sprite instanceof SpriteSolidColor) {
			ssc = (SpriteSolidColor)this.sprite;
			ssc.a = 1.0F;
			ssc.b = 0.0F;

			ssc.r = this.getValue();
			ssc.g = 1.0F - this.getValue();
		}
		super.draw(context);
	}

	@Override
	public IGUIWidget newThis() {
		return new WidgetTemperatureBar(name);
	}

	@Override
	public IGUIWidget copy() {
		IGUIWidget clone = super.copy();
		((WidgetTemperatureBar)clone).provideComponent((IInvComponent) heat);
		((WidgetTemperatureBar)clone).setDirection(getDirection());
		return clone;
	}

	@Override
	public Iterable<String> getComponentsSought() {
		return comps;
	}
	protected String getHeatTooltip() {
		if(this.heat != null) {
			if(this.heat.getHeat() != 0) {
				return "" + heat.getHeat() + " C";
			}
		}
		return "0 C";
	}

	@Override
	public boolean getHasTooltip(boolean verbose) {
		return true;
	}

	@Override
	public boolean getHasTooltip() {
		return true;
	}

	@Override
	public List getTooltips(boolean verbose) {
		tempTooltip.clear();
		tempTooltip.add(getHeatTooltip());
		return tempTooltip;
	}
	
	
}
