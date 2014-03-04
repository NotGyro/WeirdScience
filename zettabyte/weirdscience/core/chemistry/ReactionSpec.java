package zettabyte.weirdscience.core.chemistry;

import net.minecraftforge.fluids.Fluid;

//Extremely simplified from the complexity of real chemistry.
public class ReactionSpec implements IReactionSpec {

	//The fluid which is the primary driver of this reaction.
	public Fluid solvent;
	//Valid types: Item, Block, Fluid
	public Object solute;
	
	//Valid types: Item, Block, Fluid. What the solvent becomes after reaction.
	public Object solventTarget = null;
	//Valid types: Item, Block, Fluid. What the solute becomes after reaction.
	public Object soluteTarget = null;

	//Turn solvent into solventTarget?
	public boolean solventAffected = true;
	//Turn solute into soluteTarget?
	public boolean soluteAffected = true;
	
	public int solventMin = 0; //Minimum solvent quantity for the reaction to take place.
	public int soluteMin = 0; //Minimum solvent quantity for the reaction to take place.

	public ReactionSpec() {}
	public ReactionSpec(Fluid solv, Object sol, Object targ1, Object targ2) {
		solvent = solv;
		solute = sol;
		solventAffected = false;
		soluteAffected = false;
		if(targ1 != null) {
			solventTarget = targ1;
			solventAffected = true;
		}
		if(targ2 != null) {
			soluteTarget = targ2;
			soluteAffected = true;
		}
	}

	@Override
	public Fluid getSolvent() {
		return solvent;
	}

	@Override
	public Object getSolute() {
		return solute;
	}

	@Override
	public Object getSolventTarget() {
		return solventTarget;
	}

	@Override
	public Object getSoluteTarget() {
		return soluteTarget;
	}

	@Override
	public boolean isSolventAffected() {
		return solventAffected;
	}

	@Override
	public boolean isSoluteAffected() {
		return soluteAffected;
	}

	@Override
	public int getSolventMin() {
		return solventMin;
	}

	@Override
	public int getSoluteMin() {
		return soluteMin;
	}
	
}
