package zettabyte.weirdscience.util;


/* Below is a bunch of code copied and pasted from CoFHLib,
 * which is legally, ethically, and technically correct as the library was
 * released under the LGPL, and the authors, on its github page, recommend
 * shadowing code (that is, doing exactly this).
 */

import net.minecraft.world.World;

public class TimeTracker {

	private long lastMark = Long.MIN_VALUE;
	private final long delay = -1;

	public boolean hasDelayPassed(World world, int delay) {

		long currentTime = world.getTotalWorldTime();

		if (currentTime < lastMark) {
			lastMark = currentTime;
			return false;
		} else if (lastMark + delay <= currentTime) {
			lastMark = currentTime;
			return true;
		} else {
			return false;
		}

	}

	public void markTime(World world) {

		lastMark = world.getTotalWorldTime();
	}

}
