package ws.zettabyte.zettalib.client.gui;

public class Vert2D {
	protected int x, y;
	public Vert2D () { };
	public Vert2D (int X, int Y) { x = X; y = Y; };
	public int getX() { return x; }; 
	public int getY() { return y; }; 
	public void setX(int X) { x = X; };
	public void setY(int Y) { y = Y; };
	public void set(int X, int Y) { setX(X); setY(Y); };
}
