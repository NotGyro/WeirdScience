package ws.zettabyte.weirdscience.gas;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class MaterialGas extends Material {
	public static final MaterialGas instance = new MaterialGas();
	
	public MaterialGas() {
		super(MapColor.airColor);
	}

	@Override
	public boolean isLiquid() {
		return false;
	}

	@Override
	public boolean isSolid() {
		return false;
	}

	@Override
	public boolean getCanBlockGrass() {
		return super.getCanBlockGrass();
	}

	@Override
	public boolean blocksMovement() {
		return false;
	}

	@Override
	public boolean getCanBurn() {
		return true;
	}

	@Override
	public boolean isReplaceable() {
		return true;
	}

	@Override
	public boolean isOpaque() {
		return false;
	}

	@Override
	public boolean isToolNotRequired() {
		return true;
	}

	@Override
	public int getMaterialMobility() {
		// TODO Auto-generated method stub
		return 0;
	}

}
