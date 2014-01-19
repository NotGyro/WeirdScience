package zettabyte.weirdscience.fluid;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import zettabyte.weirdscience.chemistry.BioactiveFluid;

public class FluidSmog extends BioactiveFluid {

	public FluidSmog(String fluidName) {
		super(fluidName);
	}

	@Override
	public boolean contactAffectCreature(EntityLivingBase affected) {
		return false;
	}

	@Override
	public boolean drinkAffectCreature(EntityLivingBase affected) {
		return breatheAffectCreature(affected);
	}

	@Override
	public boolean bloodstreamAffectCreature(EntityLivingBase affected) {
		return breatheAffectCreature(affected);
	}

	@Override
	public boolean breatheAffectCreature(EntityLivingBase affected) {
		if(affected != null) {
			affected.addPotionEffect(new PotionEffect(Potion.poison.id, 250, 1));
			affected.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 250, 1));
		}
		return false;
	}

	@Override
	public boolean canContactAffectCreature() {
		return false;
	}

	@Override
	public boolean canDrinkAffectCreature() {
		return true;
	}

	@Override
	public boolean canBloodstreamAffectCreature() {
		return true;
	}

	@Override
	public boolean canBreatheAffectCreature() {
		return true;
	}

}
