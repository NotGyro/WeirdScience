package ws.zettabyte.zettalib.client.render;

public class SpriteTiler implements ISprite {
	protected final ISpriteImage bg;
	public SpriteTiler(ISpriteImage b) {
		bg = b;
	}
	
	@Override
	public void draw(IRenders2D context, double x, double y,
			double width, double height) {
		//How many times will the texture repeat in each direction?
		if(bg.getNativeWidth() <= 0) return;
		if(bg.getNativeHeight() <= 0) return;
		
		double spriteW = bg.getNativeWidth();
		double spriteH = bg.getNativeHeight();
		
		double repeatW = width/spriteW;
		double repeatH = height/spriteH;

		//Tile the texture.
		for(int xCell = 0; xCell < Math.ceil(repeatW); ++xCell) {
			for(int yCell = 0; yCell < Math.ceil(repeatH); ++yCell) {
				double lEdge = spriteW*xCell;
				double tEdge = spriteH*yCell;
				double rEdge = spriteW*(xCell+1);
				double bEdge = spriteH*(yCell+1);
				
				if( rEdge > width ) rEdge = width;
				if(bEdge > height) bEdge = height;
				
				double tilePortionW = rEdge - lEdge;
				double tilePortionH = bEdge - tEdge;
				
		        bg.drawWithUVOffset(context, x + lEdge, y + tEdge, 
		        		tilePortionW, tilePortionH, 0.0D, 0.0D, 
		        		tilePortionW/spriteW, tilePortionH/spriteH);
			}
		}
	}
}
