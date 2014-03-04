package zettabyte.weirdscience.core.chemistry;
/**
 * 
 * @author Gyro
 * Any object which can be registered with the Content Registry that can receive
 * reaction specs.
 * 
 * Used so that reactions can be passed to, say, the Acid block and in this process
 * the Mixer will also get them because it gets everything.
 */
public interface IReactionReceiver {
	/**
	 * 
	 * @param react
	 * The reaction to register
	 * @return
	 * If this reaction is applicable to this object (i.e. was registered with it)
	 */
	boolean registerReaction(IReactionSpec react);
}
