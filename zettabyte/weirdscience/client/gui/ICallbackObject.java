package zettabyte.weirdscience.client.gui;


/* Anything that can receive or yield out values
 * via Component Callbacks.
 */
public interface ICallbackObject {
	IComponentCallback getComponentCallback(String nameFind);
	void addComponentCallback(IComponentCallback comp);
}
