package zettabyte.weirdscience.core.chemistry;

import net.minecraft.entity.EntityLivingBase;

/**
 * Implement this interface on fluids and items that can apply some effect on a living entity.
 * Pretty general, right? Still, useful for the weird things Weird Science does.
 * 
 * @author Gyro
 * 
 */
public interface IBioactive {
	/**
     * Affect a creature via touch, skin contact, etc.
     * Returns if it is possible to do this.
     * 
     * @return
     */
	boolean contactAffectCreature(EntityLivingBase affected);
	/**
     * Affect a creature via ingestion.
     * 
     * @return
     */
	boolean drinkAffectCreature(EntityLivingBase affected);
	/**
     * Affect a creature via injection.
     * 
     * @return
     */
	boolean bloodstreamAffectCreature(EntityLivingBase affected);
	/**
     * Affect a creature via inhalation.
     * 
     * @return
     */
	boolean breatheAffectCreature(EntityLivingBase affected);
	
	
	boolean canContactAffectCreature();
	boolean canDrinkAffectCreature();
	boolean canBloodstreamAffectCreature();
	boolean canBreatheAffectCreature();
}
