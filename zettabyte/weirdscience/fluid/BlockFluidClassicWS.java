package zettabyte.weirdscience.fluid;

import zettabyte.weirdscience.WeirdScience;
import zettabyte.weirdscience.chemistry.IBioactive;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidClassicWS extends BlockFluidClassic {
	boolean entitiesInteract = true;
	public boolean isEntitiesInteract() {
		return entitiesInteract;
	}

	public void setEntitiesInteract(boolean entitiesInteract) {
		this.entitiesInteract = entitiesInteract;
	}

	public BlockFluidClassicWS(int id, Fluid fluid, Material material) {
		super(id, fluid, material);
	}

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
    public void addDisplacementEntry(int id, boolean isDisplaced) {
    	this.displacementIds.put(id, isDisplaced);
    }
	

}
