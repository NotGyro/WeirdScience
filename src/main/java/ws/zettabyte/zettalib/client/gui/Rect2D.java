package ws.zettabyte.zettalib.client.gui;

public class Rect2D {
	protected Vert2D pos = new Vert2D();
	protected Vert2D bounds = new Vert2D();
	
	public Rect2D () { };
	public Rect2D (Vert2D p, Vert2D b) { pos = p; bounds = b; };
	public Rect2D (int posX, int posY, int width, int height) { 
		pos = new Vert2D(posX, posY);
		bounds = new Vert2D(width, height);
	};
	
	public Vert2D getPos() { return pos; };
	public Vert2D getSize() { return bounds; };
	public int getX() { return pos.getX(); };
	public int getY() { return pos.getY(); };
	public int getWidth() { return bounds.getX(); };
	public int getHeight() { return bounds.getY(); };
	
	public void setPos(Vert2D p) { pos = p; };
	public void setSize(Vert2D s) { bounds = s; };
	public void setX(int X) { pos.setX(X); };
	public void setY(int Y) { pos.setY(Y); };
	public void setWidth(int W) { bounds.setX(W); };
	public void setHeight(int H) { bounds.setY(H); };
}
