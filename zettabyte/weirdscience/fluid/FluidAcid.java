package zettabyte.weirdscience.fluid;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fluids.Fluid;
import zettabyte.weirdscience.core.chemistry.IBioactive;
import zettabyte.weirdscience.core.interfaces.IRegistrable;

public class FluidAcid extends Fluid implements IBioactive, IRegistrable {

	public int damage = 4;
	public FluidAcid(String fluidName) {
		super(fluidName);
	}

	@Override
	public boolean contactAffectCreature(EntityLivingBase affected) {
		affected.attackEntityFrom(DamageSource.magic, (float)damage);
		return true;
	}

	@Override
	public boolean drinkAffectCreature(EntityLivingBase affected) {
		//Don't drink acid.
		affected.attackEntityFrom(DamageSource.magic, (float)damage*2);
		return true;
	}

	@Override
	public boolean bloodstreamAffectCreature(EntityLivingBase affected) {
		//Holy shit what are you doing?
		affected.attackEntityFrom(DamageSource.magic, (float)damage*4);
		return true;
	}

	@Override
	public boolean breatheAffectCreature(EntityLivingBase affected) {
		//Doesn't seem possible, but eh.
		//If there are going to be Space Station 13 style shenanigans wherein we put acid in somebody's
		//oxygen tank, we should start laying the foundations for that now.
		return bloodstreamAffectCreature(affected);
	}

	@Override
	public boolean canContactAffectCreature() {
		return true;
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

	@Override
	public String getEnglishName() {
		return this.getLocalizedName();
	}

	@Override
	public String getGameRegistryName() {
		return this.getUnlocalizedName();
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
