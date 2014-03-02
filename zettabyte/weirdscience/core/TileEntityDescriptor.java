package zettabyte.weirdscience.core;

import net.minecraft.tileentity.TileEntity;

public class TileEntityDescriptor {
	public String name;
	public Class<? extends TileEntity> teClass;
	
	public TileEntityDescriptor(String n, Class<? extends TileEntity> cla) {
		name = n;
		teClass = cla;
	}
}
