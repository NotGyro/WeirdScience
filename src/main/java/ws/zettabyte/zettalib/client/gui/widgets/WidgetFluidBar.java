package ws.zettabyte.zettalib.client.gui.widgets;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;

import org.lwjgl.opengl.GL11;

import ws.zettabyte.zettalib.client.gui.GUIContext;
import ws.zettabyte.zettalib.client.gui.IGUITank;
import ws.zettabyte.zettalib.client.gui.IGUIWidget;
import ws.zettabyte.zettalib.inventory.FluidTankNamed;
/**
 * A widget that tracks a Forge FluidTank class, rendering the contained Fluid's icon in a rectangle whose 
 * size is proportional to how full the tank is (tank.stack.getAmount() / tank.getCapacity()).
 * @author Sam "Gyro" Cutlip
 *
 */
public class WidgetFluidBar extends WidgetAmountBar implements IGUITank {
	protected final String name;
	protected FluidTankNamed currentTank = null;
	//private final ResourceLocation tex = new ResourceLocation("weirdscience", "textures/blocks/smog.png"); //TODO: Not this
	
	public WidgetFluidBar(String n) {
		super();
		name = n;
	}

	public WidgetFluidBar(String n, IGUIWidget p) {
		super(p);
		name = n;
	}

	@Override
	public String getComponentName() {
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
		if(currentTank == null) return;
		if(currentTank.getFluid() == null) return;
		//if(FluidRegistry.isFluidRegistered()) return;
		//Setup layer
		context.screen.setZLevel(context.screen.getZLevel() 
				+ (getLayer() * context.zLevelPerLayer));
        //ITextureObject texture = context.mc.renderEngine.getTexture(tex);
        GL11.glColor4f(tintR, tintG, tintB, tintA);

        //Important wizardry: this stuff was not easy to find.
        TextureManager texman = context.screen.mc.getTextureManager();
        IIcon icon = currentTank.getFluid().getFluid().getIcon(currentTank.getFluid());
        texman.bindTexture(texman.getResourceLocation(currentTank.getFluid().getFluid().getSpriteNumber()));
        
        //Below this is more code but really figuring out that Texture Manager Sprite Number stuff was the hard bit.
        if(icon == null) return;
        
        //Figure out what we have available.
        double spriteW = icon.getIconWidth();
        double spriteH = icon.getIconHeight();
        double minU = icon.getMinU();
        double minV = icon.getMinV();
		double maxU = icon.getMaxU() - icon.getMinU();
		double maxV = icon.getMaxV() - icon.getMinV();
		
		//How many times will the texture repeat in each direction?
		double repeatW = getDrawWidth()/spriteW;
		double repeatH = getDrawHeight()/spriteH;

		//Tile the texture.
		for(int x = 0; x < Math.ceil(repeatW); ++x) {
			for(int y = 0; y < Math.ceil(repeatH); ++y) {
				double tileWidth = spriteW;
				double tileHeight = spriteH;
				if((tileWidth*(x+1)) > getDrawWidth() ) {
					tileWidth -= ((tileWidth*(x+1)) - getDrawWidth());
				}
				if((tileHeight*(y+1)) > getDrawHeight() ) {
					tileHeight -= ((tileHeight*(y+1)) - getDrawHeight());
				}
				double tilePortionW = tileWidth / spriteW;
				double tilePortionH = tileHeight / spriteH;
				
		        context.screen.drawTexturedRect(getDrawX()+(x*spriteW), getDrawY()+(y*spriteH), 
		        		icon.getMinU(), icon.getMinV(), 
		        		(icon.getMaxU() - icon.getMinU()) * tilePortionW, 
		        		(icon.getMaxV() - icon.getMinV()) * tilePortionH,
		        		tileWidth, tileHeight);
			}
		}
	}

	@Override
	protected IGUIWidget newThis() {
		return new WidgetFluidBar(name);
	}

	@Override
	public IGUIWidget copy() {
		IGUIWidget clone = super.copy();
		((WidgetFluidBar)clone).provideTank(currentTank);
		((WidgetFluidBar)clone).setDirection(getDirection());
		return clone;
	}
	
}
