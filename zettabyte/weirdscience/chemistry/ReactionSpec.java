package zettabyte.weirdscience.chemistry;

import net.minecraftforge.fluids.Fluid;

//Extremely simplified from the complexity of real chemistry.
public class ReactionSpec {

	//The fluid which is the primary driver of this reaction.
	public Fluid solvent;
	//Valid types: Item, Block, Fluid
	public Object solute;
	
	//Valid types: Item, Block, Fluid. What the solvent becomes after reaction.
	public Object solventTarget = null;
	//Valid types: Item, Block, Fluid. What the solute becomes after reaction.
	public Object soluteTarget = null;
	
	public int solventMin = 0; //Minimum solvent quantity for the reaction to take place.
	public int soluteMin = 0; //Minimum solvent quantity for the reaction to take place.
	
	public ReactionSpec() {}
	
}
