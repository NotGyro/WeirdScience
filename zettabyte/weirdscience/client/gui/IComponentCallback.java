package zettabyte.weirdscience.client.gui;

/* This can be generalized into a component system, I think.
 * However, since the niche for implementation-independent
 * abstraction is filled by interfaces in the Minecraft
 * modding scene, I don't really see the point - so it'll
 * be used mostly for GUI purposes.  
 */
public interface IComponentCallback {
	/*Used for things such as sliders, dropboxes, etc.*/
	void ReceiveValue(int val);	
	/* On simple elements like buttons, returns 0.
	 * Returns down state on toggleables.
	 */
	int getCurrentIntValue();
	
	/*Used for things such as text entry boxes.*/
	void ReceiveValue(String val);
	String getCurrentStringValue();
	
	String getName();
	void setName(String nameTo);
}
