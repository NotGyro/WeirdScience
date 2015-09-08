package ws.zettabyte.zettalib.inventory;

import net.minecraft.entity.player.EntityPlayer;

/*
 * Like IDescriptiveInventory, but for fluid tanks rather than item slots.
 */
public interface INamedTankInfo {
	Iterable<FluidTankNamed> getTanks();
	
	/**
	 * Get by name.
	 */
	default FluidTankNamed getTank(String name) { 
		for(FluidTankNamed e : this.getTanks()) {
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
