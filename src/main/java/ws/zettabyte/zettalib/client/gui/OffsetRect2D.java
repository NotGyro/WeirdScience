package ws.zettabyte.zettalib.client.gui;

public class OffsetRect2D extends Rect2D {
	
	protected Vert2D parent = null;

	public OffsetRect2D() {
		this.pos = new OffsetVert2D();
		this.size = new Vert2D();
	}

	public OffsetRect2D(Vert2D p, Vert2D b) {
		super(p, b);
		if(p instanceof OffsetVert2D) {
			parent = ((OffsetVert2D) p).getParent();
		}
	}

	public OffsetRect2D(int posX, int posY, int width, int height) {
		super(posX, posY, width, height);
	}

	public void setParent(Vert2D p) {
		parent = p;
		if(pos instanceof OffsetVert2D) {
			((OffsetVert2D)pos).setParent(p);
		}
	}
	public Vert2D getParent() { return parent; };
}
