package zettabyte.weirdscience.core;

import net.minecraft.block.StepSound;

/**
 * Extends StepSound to provide a generic, mod/vanilla-nonspecific version, as default StepSound can only be used with Vanilla assets
 * Additionally it allows for separate break/step/placement sounds, if you want
 * 
 * @author Shados
 *
 */
public class WeirdStepSound extends StepSound {
	public static String breakSoundName;
	public static String placeSoundName;
	public static String stepSoundName;
	
	public WeirdStepSound(String breakSoundName, String placeSoundName, String stepSoundName, float volume, float pitch) {
		super(stepSoundName, volume, pitch);
		this.breakSoundName = breakSoundName;
		this.placeSoundName = placeSoundName;
		this.stepSoundName = stepSoundName;
	}
	@Override
	public String getBreakSound() {
		return breakSoundName;
	}
	@Override
	public String getStepSound() {
		return stepSoundName;
	}
	@Override
	public String getPlaceSound() {
		return placeSoundName;
	}
}
