package ws.zettabyte.zettalib.client.gui;

public interface IGUI {

	/**
	 * Adds a widget to this screen's root widget,
	 * providing it as a child to our rootWidget and also
	 * calling setParent on w.
	 * 
	 * 
	 * @param w
	 * @return Were we able to add this widget (true), or is it a duplicate (false)?
	 */
	public boolean addWidget(IGUIWidget w);

	/**
	 * Gets the root widget, the parent farthest up the widget tree of this screen's
	 * hierarchy. Recursively walking through all children of this object and all of
	 * their children and etc... will touch on every single widget involved in this
	 * screen.
	 */
	public IGUIWidget getRootWidget();

	public int getGuiID();
	public void setGuiID(int id);
}