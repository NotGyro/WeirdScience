package ws.zettabyte.zettalib.inventory;

import net.minecraft.item.ItemStack;

public class SlotOutput extends ItemSlot {

	public SlotOutput(int num) {
		super(num);
	}
	public SlotOutput(int num, String n) {
		super(num, n);
	}

	@Override
	public ItemStack acceptInput(ItemStack s) {
		return s;
	}
	@Override
	public boolean canInput(ItemStack s) {
		return false;
	}
	
}
