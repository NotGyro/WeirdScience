package ws.zettabyte.zettalib.inventory;

/**
 * Anything that should be provided with a component of a certain name -
 * particularly, this is used to provide GUI elements with 
 * @author Sam "Gyro" C.
 *
 */
public interface IComponentReceiver {
	Iterable<String> getComponentsSought();
	void provideComponent(IInvComponent comp);
}
