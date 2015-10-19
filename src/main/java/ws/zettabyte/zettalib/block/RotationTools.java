package ws.zettabyte.zettalib.block;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public final class RotationTools {

	/**
	 * Furnace-style rotations. In an ideal world we'd use values that make more sense with 
	 * __FDtoUseable2DMath(), but for cross-mod compat I'd like to use Vanilla's style of
	 * metadata to rotation mapping.
	 */
	public static  void initFourDirBlock(World world, int x, int y, int z,
			EntityLivingBase placer) {
		if(world.isRemote) return;
		float yaw = placer.rotationYaw;
		yaw += 45.0F;
		yaw %= 360;
		if(yaw < 0.0F) {
			yaw += 360.0F;
		}
		int quadrant = MathHelper.floor_double(yaw / 90.0F);

		// Modulo out any 360 degree dealies.
		//quadrant %= 4;

		/*
		 * public static final ForgeDirection[] VALID_DIRECTIONS = {DOWN, UP,
		 * NORTH, SOUTH, WEST, EAST}; 0 1 2 3 4 5
		 */
		// Facing north
		if (quadrant == 0) {
			world.setBlockMetadataWithNotify(x, y, z, 3, 1|2);
		}

		// Facing west
		else if (quadrant == 1) {
			world.setBlockMetadataWithNotify(x, y, z, 5, 1|2);
		}

		// Facing south
		else if (quadrant == 2) {
			world.setBlockMetadataWithNotify(x, y, z, 2, 1|2);
		}

		// Facing east
		else if (quadrant == 3) {
			world.setBlockMetadataWithNotify(x, y, z, 4, 1|2);
		}
	}

	public static int ForgeDirectionToIndex(ForgeDirection in) {
		//UP, NORTH, SOUTH, WEST, EAST
		if(in == ForgeDirection.DOWN) return 0;
		else if(in == ForgeDirection.UP) return 1;
		else if(in == ForgeDirection.NORTH) return 2;
		else if(in == ForgeDirection.SOUTH) return 3;
		else if(in == ForgeDirection.WEST) return 4;
		else if(in == ForgeDirection.EAST) return 5;
		else return -1;
	}
	protected static int __FDtoUseable2DMath(ForgeDirection in) {
		//UP, NORTH, SOUTH, WEST, EAST
		//Up-down is invalid for this function - however, they should have identity through this, so eh
		if(in == ForgeDirection.DOWN) return 4;
		else if(in == ForgeDirection.UP) return 5;
		
		//Counter-clockwise rotation from origin.
		if(in == ForgeDirection.EAST) return 0;
		else if(in == ForgeDirection.NORTH) return 1;
		else if(in == ForgeDirection.WEST) return 2;
		else if(in == ForgeDirection.SOUTH) return 3;
		
		return -1;
	}
	protected static ForgeDirection __FDfromUseable2DMath(int in) {
		if(in == 4) return ForgeDirection.DOWN;
		else if(in == 5) return ForgeDirection.UP;
		
		//Counter-clockwise rotation from origin.
		if(in == 0) return ForgeDirection.EAST;
		else if(in == 1) return ForgeDirection.NORTH;
		else if(in == 2) return ForgeDirection.WEST;
		else if(in == 3) return ForgeDirection.SOUTH;
		
		return ForgeDirection.UNKNOWN; //1.8 port problem spot
	}
	
	public static ForgeDirection getTranslatedSideFStyle(ForgeDirection side, ForgeDirection facing) {
		//Facing is presumed to be the "front" of a block. Metadata of 2 is presumed North.
		//UP, NORTH, SOUTH, WEST, EAST
		if(side == ForgeDirection.DOWN) return ForgeDirection.DOWN;
		else if(side == ForgeDirection.UP) return ForgeDirection.UP;

		int f = __FDtoUseable2DMath(facing);
		int s = __FDtoUseable2DMath(side);
		return __FDfromUseable2DMath(Math.abs(s+f) % 4);
	}
}
