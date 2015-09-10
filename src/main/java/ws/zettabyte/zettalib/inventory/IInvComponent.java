package ws.zettabyte.zettalib.inventory;

/* TODO: Unify the conveyance, from TileEntities to GUIs, of tanks, progress bars,
 * energy storages, etc... onto this interface, IComponentReceiver, and IComponentContainer.
 * 
 * Item slots are a maybe. */
public interface IInvComponent {
	String getComponentName();
}
