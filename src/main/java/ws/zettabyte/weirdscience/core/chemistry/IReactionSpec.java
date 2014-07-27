package ws.zettabyte.weirdscience.core.chemistry;

import net.minecraftforge.fluids.Fluid;

public interface IReactionSpec
{
    Fluid getSolvent ();

    Object getSolute ();

    Object getSolventTarget ();

    Object getSoluteTarget ();

    boolean isSolventAffected ();

    boolean isSoluteAffected ();

    int getSolventMin ();

    int getSoluteMin ();

    /**
    * @return null if there is no lower bound.
    */
    Integer getHeatLowerBound ();

    /**
    * @return null if there is no upper bound.
    */
    Integer getHeatUpperBound ();

    /**
    * @return Positive for an exothermic reaction, negative for an endothermic reaction.
    * This is a consumption value for endothermic reactions: If it can't be supplied
    * with the necessary heat, it can't occur.
    */
    int getHeatChangeProduced ();
}