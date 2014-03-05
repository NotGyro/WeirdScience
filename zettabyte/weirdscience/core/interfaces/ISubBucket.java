package zettabyte.weirdscience.core.interfaces;

import net.minecraft.block.Block;
import net.minecraftforge.fluids.Fluid;

public interface ISubBucket extends ISubItem {
	Fluid getFluid();
	void setFluid(Fluid fluid);

	Block getContained();
	int getContainedMeta();
	void setContained(Block toContain);
	void setContainedMeta(int setTo);
}
