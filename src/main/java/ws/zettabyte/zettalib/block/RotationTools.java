package ws.zettabyte.zettalib.block;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;

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
		 * public static final EnumFacing[] VALID_DIRECTIONS = {DOWN, UP,
		 * NORTH, SOUTH, WEST, EAST}; 0 1 2 3 4 5
		 */
		// Facing south
		if (quadrant == 0) {
			world.setBlockMetadataWithNotify(x, y, z, 2, 1|2); //face the block North
		}

		// Facing west
		else if (quadrant == 1) {
			world.setBlockMetadataWithNotify(x, y, z, 5, 1|2);
		}

		// Facing north
		else if (quadrant == 2) {
			world.setBlockMetadataWithNotify(x, y, z, 3, 1|2);
		}

		// Facing east
		else if (quadrant == 3) {
			world.setBlockMetadataWithNotify(x, y, z, 4, 1|2);
		}
		System.out.println("Quadrant: " + quadrant);
		System.out.println("Meta: " + world.getBlockMetadata(x,y,z));
	}

	public static int EnumFacingToIndex(EnumFacing in) {
		//UP, NORTH, SOUTH, WEST, EAST
		if(in == EnumFacing.DOWN) return 0;
		else if(in == EnumFacing.UP) return 1;
		else if(in == EnumFacing.NORTH) return 2;
		else if(in == EnumFacing.SOUTH) return 3;
		else if(in == EnumFacing.WEST) return 4;
		else if(in == EnumFacing.EAST) return 5;
		else return -1;
	}
	protected static int __FDtoUseable2DMath(EnumFacing in) {
		//UP, NORTH, SOUTH, WEST, EAST
		//Up-down is invalid for this function - however, they should have identity through this, so eh
		if(in == EnumFacing.DOWN) return 4;
		else if(in == EnumFacing.UP) return 5;
		
		//Counter-clockwise rotation from origin.
		else if(in == EnumFacing.SOUTH) return 0;
		if(in == EnumFacing.EAST) return 1;
		else if(in == EnumFacing.NORTH) return 2;
		else if(in == EnumFacing.WEST) return 3;
		
		return -1;
	}
	protected static EnumFacing __FDfromUseable2DMath(int in) {
		if(in == 4) return EnumFacing.DOWN;
		else if(in == 5) return EnumFacing.UP;
		
		//Counter-clockwise rotation from origin.
		else if(in == 0) return EnumFacing.SOUTH;
		if(in == 1) return EnumFacing.EAST;
		else if(in == 2) return EnumFacing.NORTH;
		else if(in == 3) return EnumFacing.WEST;
		
		return EnumFacing.UNKNOWN; //1.8 port problem spot
	}
	
	public static EnumFacing getTranslatedSideFStyle(EnumFacing side, EnumFacing facing) {
		//Facing is presumed to be the "front" of a block. Metadata of 2 is presumed North.
		//DOWN, UP, NORTH, SOUTH, WEST, EAST
		if(side == EnumFacing.DOWN) return EnumFacing.DOWN;
		else if(side == EnumFacing.UP) return EnumFacing.UP;

		int f = __FDtoUseable2DMath(facing);
		int s = __FDtoUseable2DMath(side);
		return __FDfromUseable2DMath(Math.abs(s-f) % 4);
	}


	/**
	 * Piston-style rotations
	 */
	public static void initSixDirBlock(World world, int x, int y, int z,
									   EntityLivingBase placer) {
		if(world.isRemote) return;
		//If the player is not looking very far up or down, treat as four-dir.
		if (MathHelper.abs(placer.rotationPitch) < 60) {
			initFourDirBlock(world, x, y, z, placer);
		}
		else { //The player is looking up or down.
			if (placer.rotationPitch > 0) {
				world.setBlockMetadataWithNotify(x, y, z, 1, 1|2); //Player is looking down, block should face up
			}
			else {
				world.setBlockMetadataWithNotify(x, y, z, 0, 1|2); //Player is looking up, block should face down
			}
		}
	}
	public static EnumFacing getTranslatedSidePStyle(EnumFacing side, EnumFacing facing) {
		//Facing is presumed to be the direction faced by the "front" of a block, and the "front" dir in the block's space
		//is South.
		//UP, NORTH, SOUTH, WEST, EAST
		//I'm just gonna use a table here because I have no idea how to do axis-aligned 3D rotation, sorry senpai
		if(facing == EnumFacing.UP) {
			if(side == EnumFacing.UP) return EnumFacing.SOUTH;
			if(side == EnumFacing.DOWN) return EnumFacing.NORTH;
			if(side == EnumFacing.EAST) return EnumFacing.EAST;
			if(side == EnumFacing.WEST) return EnumFacing.WEST;
			if(side == EnumFacing.NORTH) return EnumFacing.DOWN;
			if(side == EnumFacing.SOUTH) return EnumFacing.UP;
			return EnumFacing.SOUTH;
		}
		else if(facing == EnumFacing.DOWN) {
			if(side == EnumFacing.UP) return EnumFacing.NORTH;
			if(side == EnumFacing.DOWN) return EnumFacing.SOUTH;
			if(side == EnumFacing.EAST) return EnumFacing.EAST;
			if(side == EnumFacing.WEST) return EnumFacing.WEST;
			if(side == EnumFacing.NORTH) return EnumFacing.UP;
			if(side == EnumFacing.SOUTH) return EnumFacing.DOWN;
			return EnumFacing.SOUTH;
		}
		else {
			return getTranslatedSideFStyle(side, facing);
		}
	}

}
