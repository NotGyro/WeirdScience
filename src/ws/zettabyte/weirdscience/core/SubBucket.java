package zettabyte.weirdscience.core;

import net.minecraft.block.Block;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.IFluidBlock;
import zettabyte.weirdscience.core.interfaces.ISubBucket;

public class SubBucket extends SubItem implements ISubBucket {
	Block contained;
	Fluid containedFluid;
	int containedMeta = 0;

	@Override
	public void setFluid(Fluid fluid) {
		containedFluid = fluid;
	}
	@Override
	public Fluid getFluid() {
		return containedFluid;
	}

	@Override
	public Block getContained() {
		return contained;
	}

	@Override
	public void setContained(Block toContain) {
		contained = toContain;
	}
	
	public SubBucket() {
		super();
	}
	
	public SubBucket(String engName, String textureName) {
		super(engName, textureName);
	}

	public SubBucket(String engName, String textureName, Fluid f) {
		this(engName, textureName);
		containedFluid = f;
		contained = Block.blocksList[f.getBlockID()];
	}
	public SubBucket(String engName, String textureName, Block b) {
		this(engName, textureName);
		contained = b;
		if(b instanceof IFluidBlock) {
			containedFluid = ((IFluidBlock)b).getFluid();
		}
	}
	public SubBucket(String engName, String textureName, Block b, int m) {
		this(engName, textureName, b);
		containedMeta = m;
	}
	@Override
	public int getContainedMeta() {
		return containedMeta;
	}
	@Override
	public void setContainedMeta(int setTo) {
		containedMeta = setTo;
	}

}
