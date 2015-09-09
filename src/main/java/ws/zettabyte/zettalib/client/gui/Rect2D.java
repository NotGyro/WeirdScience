package ws.zettabyte.zettalib.client.gui;

/**
 * A class for defining a 2D rectangle.
 * The position (X and Y) represents the 
 * upper-left-hand corner.
 * 
 * @author Sam "Gyro" Cutlip
 */
public class Rect2D {
	protected Vert2D pos;
	protected Vert2D size;
	
	public Rect2D () { pos = new Vert2D(); size = new Vert2D();};
	public Rect2D (Vert2D p, Vert2D b) { pos = p; size = b; };
	public Rect2D (int posX, int posY, int width, int height) { 
		pos = new Vert2D(posX, posY);
		size = new Vert2D(width, height);
	};
	
	public Vert2D getPos() { return pos; };
	public Vert2D getSize() { return size; };
	public int getX() { return pos.getX(); };
	public int getY() { return pos.getY(); };
	public int getWidth() { return size.getX(); };
	public int getHeight() { return size.getY(); };
	
	public void setPos(Vert2D p) { 
		setX(p.getX());
		setY(p.getY());
		};
	public void setSize(Vert2D s) { 
		setWidth(s.getX());
		setHeight(s.getY()); 
		};
	public void setX(int X) { pos.setX(X); };
	public void setY(int Y) { pos.setY(Y); };
	public void setWidth(int W) { size.setX(W); };
	public void setHeight(int H) { size.setY(H); };

	public int getCenterX() {
		return getPos().getX() + (getWidth() / 2);
	};
	public int getCenterY() { 
		return getPos().getY() + (getHeight() / 2);
	};
	
	public Vert2D getCenter() { 
		return new Vert2D(getCenterX(), getCenterY());
	};
	
	public Rect2D copy() {
		return new Rect2D(pos.copy(), size.copy());
	}
}
