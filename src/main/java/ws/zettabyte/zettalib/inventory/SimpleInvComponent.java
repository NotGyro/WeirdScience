package ws.zettabyte.zettalib.inventory;

public class SimpleInvComponent<T> implements IInvComponent {

	public T val;
	
	public final String name;
	public SimpleInvComponent(String n) {
		name = n;
	}

	@Override
	public String getComponentName() {
		return name;
	}
}
