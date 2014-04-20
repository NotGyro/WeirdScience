package ws.zettabyte.weirdscience.block;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import ws.zettabyte.weirdscience.tileentity.TileEntityFuelBurner;
import ws.zettabyte.zettalib.baseclasses.BlockContainerBase;

public class BlockFuelBurner extends BlockContainerBase
{
    public BlockFuelBurner(Configuration config, String name, int defaultID, Material material)
    {
        super(config, name, defaultID, material);
        // TODO Auto-generated constructor stub
    }

    public BlockFuelBurner(Configuration config, String name, int defaultID)
    {
        super(config, name, defaultID);
        // TODO Auto-generated constructor stub
    }

    public BlockFuelBurner(Configuration config, String name, Material material)
    {
        super(config, name, material);
        // TODO Auto-generated constructor stub
    }

    public BlockFuelBurner(Configuration config, String name)
    {
        super(config, name);
        // TODO Auto-generated constructor stub
    }

    public BlockFuelBurner(int id, Material material)
    {
        super(id, material);
        // TODO Auto-generated constructor stub
    }
    

    @Override
    public TileEntity createNewTileEntity (World world)
    {
        return new TileEntityFuelBurner();
    }

}
