package ws.zettabyte.zettalib.client.gui;

public class OffsetVert2D extends Vert2D {
	protected Vert2D parent = null;
	public OffsetVert2D() {
	}

	public OffsetVert2D(int X, int Y) {
		super(X, Y);
	}
	public OffsetVert2D(Vert2D p) {
		parent = p;
	}

	@Override
	public int getX() {
		if(parent == null) return super.getX();
		return super.getX() + parent.getX();
	}

	@Override
	public int getY() {
		if(parent == null) return super.getY();
		return super.getY() + parent.getY();
	}

	public void setParent(Vert2D p) {
		parent = p;
	}
	public Vert2D getParent() { return parent; }

	public int getXRelative() { return x; }
	public int getYRelative() { return y; }
}
