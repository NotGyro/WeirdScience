package zettabyte.weirdscience.util;

/* Below is a bunch of code copied and pasted from CoFHLib,
 * which is legally, ethically, and technically correct as the library was
 * released under the LGPL, and the authors, on its github page, recommend
 * shadowing code (that is, doing exactly this).
 */

import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;

public final class ServerHelper {

	private ServerHelper() {

	}

	public static final boolean isClientWorld(World world) {

		return world.isRemote;
	}

	public static final boolean isServerWorld(World world) {

		return !world.isRemote;
	}

	public static boolean isSinglePlayerServer() {

		return FMLCommonHandler.instance().getMinecraftServerInstance() != null;
	}

	public static boolean isMultiPlayerServer() {

		return !isSinglePlayerServer();
	}

}
