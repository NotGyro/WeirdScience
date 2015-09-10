package ws.zettabyte.zettalib.client.render;

import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class SpriteFluid implements ISprite, ISpriteImage {
	protected FluidStack fluid;
	protected final FluidTank tank;
	protected TextureManager texman;
	protected IIcon icon;
	public SpriteFluid(FluidStack f) {
		tank = null;
		fluid = f;
        icon = f.getFluid().getIcon(f);
	}

	public SpriteFluid(FluidTank f) {
		tank = f;
		rebuild();        
	}

	@Override
	public void draw(IRenders2D context, double x, double y, double width,
			double height) {
	}
	protected void rebuild() {
		if(tank != null) fluid = tank.getFluid();
		if(fluid == null) return;
		if(fluid.getFluid() == null) return;
		if(fluid.getFluid().getIcon(fluid) == null) return;
        icon = fluid.getFluid().getIcon(fluid);
	}
	@Override
	public int getNativeWidth() {
		rebuild();
		if(icon == null) return 0;
		return icon.getIconWidth();
	}

	@Override
	public int getNativeHeight() {
		rebuild();
		if(icon == null) return 0;
		return icon.getIconHeight();
	}

	@Override
	public void drawWithUVOffset(IRenders2D context, double x, double y,
			double width, double height, double uStart, double vStart,
			double uWidth, double vHeight) {
		if(tank != null) fluid = tank.getFluid();
		if(fluid == null) return;
		if(fluid.getFluid() == null) return;
		if(fluid.getFluid().getIcon(fluid) == null) return;
        icon = fluid.getFluid().getIcon(fluid);
        texman = context.getMC().getTextureManager();
        //Important wizardry: this line was not easy to figure out.
        texman.bindTexture(texman.getResourceLocation(fluid.getFluid().getSpriteNumber()));
        
        //Technically this is a weird sort of bounds-checking.
        uStart = Math.max(icon.getMinU(), icon.getMinU()+uStart);
        vStart = Math.max(icon.getMinV(), icon.getMinV()+vStart);
        uStart = Math.min(icon.getMaxU(), uStart);
        vStart = Math.min(icon.getMaxV(), vStart);
        
        /*In this case our uWidth and vHeight arguments are the portion _of our sprite_ to use, not of its
         * underlying image. So, scale our width and height by this width and height. */
        uWidth = Math.min((icon.getMaxU() - icon.getMinU()),
        		(icon.getMaxU() - icon.getMinU())*uWidth);
        vHeight = Math.min((icon.getMaxV() - icon.getMinV()),
        		(icon.getMaxV() - icon.getMinV())*vHeight);
        //Make sure we don't go out of our outer bounds. 
        if((uStart + uWidth) > icon.getMaxU()) uWidth = icon.getMaxU() - uStart;
        if((vStart + vHeight) > icon.getMaxV()) vHeight = icon.getMaxV() - vStart;

        context.drawTexturedRect(x, y, uStart, vStart, uWidth, vHeight, width, height);
	}

}
