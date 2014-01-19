package zettabyte.weirdscience.chemistry;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fluids.Fluid;

public abstract class BioactiveFluid extends Fluid implements IBioactive {
	public BioactiveFluid(String fluidName) {
		super(fluidName);
	}

	abstract public boolean contactAffectCreature(EntityLivingBase affected);

	abstract public boolean drinkAffectCreature(EntityLivingBase affected);

	abstract public boolean bloodstreamAffectCreature(EntityLivingBase affected);

	abstract public boolean breatheAffectCreature(EntityLivingBase affected);

	abstract public boolean canContactAffectCreature();

	abstract public boolean canDrinkAffectCreature();

	abstract public boolean canBloodstreamAffectCreature();

	abstract public boolean canBreatheAffectCreature();
}
