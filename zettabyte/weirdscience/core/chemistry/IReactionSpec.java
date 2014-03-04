package zettabyte.weirdscience.core.chemistry;

import net.minecraftforge.fluids.Fluid;

public interface IReactionSpec {
	Fluid getSolvent();

	Object getSolute();

	Object getSolventTarget();

	Object getSoluteTarget();

	boolean isSolventAffected();

	boolean isSoluteAffected();

	int getSolventMin();

	int getSoluteMin();
}
