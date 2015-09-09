package ws.zettabyte.zettalib.inventory;

import net.minecraft.entity.player.EntityPlayer;
/**
 * An IComponentContainer can be queried for any (named) object which should
 * be provided to an associated GUI.
 * @author Sam "Gyro" Cutlip
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
				if(e.getName() != null) {
					if(e.getName().equals(name)) {
						return e;
					}
				}
			}
		}
		return null;
	};
	
	boolean canInteractWith(EntityPlayer player);
}
