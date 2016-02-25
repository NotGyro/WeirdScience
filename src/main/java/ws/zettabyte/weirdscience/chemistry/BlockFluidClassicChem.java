package ws.zettabyte.weirdscience.chemistry;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidClassicChem extends BlockFluidClassic {
	
	//Exclude vanilla / terrain IDs from default ID values.
	public BlockFluidClassicChem(Fluid fluid, Material m) {
		super(fluid, m);
	}
	public BlockFluidClassicChem(Fluid fluid) {
		this(fluid, Material.water);
	}
	
	public boolean entitiesInteract = true;
	public boolean isReactive = false;
	
	@Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        if(entitiesInteract & (getFluid() instanceof IBioactive)) {
        	IBioactive bioFluid = (IBioactive)getFluid();
        	if(entity instanceof EntityLivingBase) {
        		bioFluid.contactAffectCreature((EntityLivingBase)entity);
        	}
        }
    }
    public void addDisplacementEntry(Block b, boolean isDisplaced) {
    	this.displacements.put(b, isDisplaced);
    }

	@Override
	public void onNeighborBlockChange(World world, int x,
			int y, int z, Block other) {
		// TODO Auto-generated method stub
		super.onNeighborBlockChange(world, x, y, z, other);
		updateReaction(world, x, y, z);
	}
	
	public void updateReaction(World world, int x, int y, int z) {
		if(isReactive ) {
			Block adjBlock;
			for(EnumFacing dir : EnumFacing.VALID_DIRECTIONS) {
				adjBlock = world.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
				if(adjBlock != null) {
					tryReaction(world, x, y, z, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, adjBlock);
				}
			}
		}
	}
	public void tryReaction(World world, int x, int y, int z, int xO, int yO, int zO, Block b) { }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
    	super.onBlockAdded(world, x, y, z);
		updateReaction(world, x, y, z);
    }
}