package ws.zettabyte.weirdscience.block;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import ws.zettabyte.weirdscience.tileentity.TileEntityFuelBurner;
import ws.zettabyte.weirdscience.core.baseclasses.BlockContainerBase;

public class BlockFuelBurner extends BlockContainerBase
{
    public BlockFuelBurner(Configuration config, String name, Material material) {
		super(config, name, material);
		// TODO Auto-generated constructor stub
	}

	public BlockFuelBurner(Configuration config, String name) {
		super(config, name);
		// TODO Auto-generated constructor stub
	}

	public BlockFuelBurner(Material material) {
		super(material);
		// TODO Auto-generated constructor stub
	}

	@Override
    public TileEntity createNewTileEntity (World world)
    {
        return new TileEntityFuelBurner();
    }

}
