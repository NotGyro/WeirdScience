package ws.zettabyte.weirdscience.core.baseclasses;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.ForgeDirection;
import ws.zettabyte.weirdscience.core.interfaces.ISubBlock;
import ws.zettabyte.weirdscience.core.interfaces.IWeirdScienceBlock;

public abstract class BlockContainerBase extends BlockContainer implements IWeirdScienceBlock
{

    protected String englishName;

    //Exclude vanilla / terrain IDs from default ID values.
    protected static final int blockIDSearchLowerBound = 256;

    protected TileEntity protoTileEntity;

    public String harvestType = "pickaxe";
    public int harvestLevel = 1;

	
	public BlockContainerBase(Configuration config, String name, Material material)
	{
		/* 
		 * Default material set to rock.
		 */
		super(material);
		englishName = name;
		this.setBlockName("block" + "." + name.replace(" ", "")); //A default value. Absolutely acceptable to not keep it.
	
	}

	public BlockContainerBase(Configuration config, String name) {
		/* 
		 * For this version of the constructor, the default block ID is set to the result of 
		 * FindFreeBlockID(), which should be a valid and available identifier if all goes well.
		 * 
		 * Ironically much safer than the much faster versions of the constructor that specify
		 * an ID, since this finds a free block ID rather than erroring if the requested block
		 * ID is not free.
		 */
		this(config, name, Material.rock);
	}
	public BlockContainerBase(Material material) {
		super(material);
	}
	
    @Override
    public String getEnglishName ()
    {
        return englishName;
    }

    @Override
    public String getGameRegistryName ()
    {
        return this.getUnlocalizedName();
    }

    @Override
    public boolean isEnabled ()
    {
        return true;
    }

    public Material getMaterial ()
    {
        return this.blockMaterial;
    }

    public void setMaterial (Material m)
    {
        //Deep dark voodoo. If you get a security exception, here it is. I'm sorry, I did it all for the greater good.
        Field field;
        try
        {
            //Get the field of the block class.
            field = Block.class.getField("blockMaterial");
            field.setAccessible(true);

            //Modify the field to not be final.
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

            field.set(this, m);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        //Make sure that the entries to canBlockGrass are still valid.
        //canBlockGrass[blockID] = !m.getCanBlockGrass();
    }

    @Override
    public ArrayList<ISubBlock> getSubBlocks ()
    {
        //By default, no metadata-based sub-blocks.
        return null;
    }

    @Override
    public int getHarvestLevel (int subBlockMeta)
    {
        //By default, no metadata-based sub-blocks.
        return harvestLevel;
    }

    @Override
    public String getHarvestType (int subBlockMeta)
    {
        //By default, no metadata-based sub-blocks.
        return harvestType;
    }

    @Override
    public ISubBlock getSubBlock (int meta)
    {
        //By default, no metadata-based sub-blocks.
        return null;
    }

    @Override
    public boolean InCreativeTab ()
    {
        return true;
    }

    @Override
    public void onNeighborChange (IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ)
    {
        super.onNeighborChange(world, x, y, z, tileX, tileY, tileZ);
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null)
        {
            TileEntityBase b = (TileEntityBase) te;
            TileEntity te2 = world.getTileEntity(tileX, tileY, tileZ);
            //Check directionality
            for (int i = 0; i < 6; i++)
            {
                if ((((tileX - x) == ForgeDirection.VALID_DIRECTIONS[i].offsetX) && ((tileY - y) == ForgeDirection.VALID_DIRECTIONS[i].offsetY))
                        && ((tileZ - z) == ForgeDirection.VALID_DIRECTIONS[i].offsetZ))
                    b.updateAdjacency(te2, i);
            }
        }
    }

    @Override
	public TileEntity createNewTileEntity(World world, int meta) {
		// TODO Auto-generated method stub
		return null;
	}

    
	public TileEntity createNewTileEntity(World world) {
		// TODO Auto-generated method stub
		return createNewTileEntity(world, 0);
	}

    @Override
    public void onBlockPreDestroy (World world, int x, int y, int z, int oldmeta)
    {
        super.onBlockPreDestroy(world, x, y, z, oldmeta);

        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null)
        {
            if(te instanceof TileEntityBase)
            {
                TileEntityBase b = (TileEntityBase)te;
                b.onKill();
            }
        }
    }

}
