package ws.zettabyte.zettalib.inventory;

import net.minecraft.entity.player.EntityPlayer;

/**
 * An IComponentContainer can be queried for any (named) object which should
 * be provided to an associated GUI or other logic tile.
 * @author Sam "Gyro" C.
 *
 */
public interface IComponentContainer {

	Iterable<IInvComponent> getComponents();
	
	/**
	 * Get by name.
	 */
	default IInvComponent getComponent(String name) { 
		for(IInvComponent e : this.getComponents()) {
			if(e != null) {
				if(e.getComponentName() != null) {
					if(e.getComponentName().equals(name)) {
						return e;
					}
				}
			}
		}
		return null;
	};
	
	boolean canInteractWith(EntityPlayer player);
}
