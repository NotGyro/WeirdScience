package zettabyte.weirdscience.core.recipe;

import net.minecraft.item.ItemStack;

public class SimpleRecipe implements IWorkbenchRecipe {

	protected ItemStack result;
	protected Object[] recipe;
	
	protected boolean shapeless;
	protected boolean oredicted;

	public SimpleRecipe(ItemStack r, Object[] rec, boolean s, boolean o) {
		result = r;
		recipe = rec;
		shapeless = s;
		oredicted = o;
	}
	public SimpleRecipe(ItemStack r, Object[] rec, boolean shapeless) {
		this(r, rec, false, true);
	}
	public SimpleRecipe(ItemStack r, Object[] rec) {
		this(r, rec, false);
	}

	@Override
	public ItemStack getResult() {
		return result;
	}

	@Override
	public Object[] get() {
		return recipe;
	}

	@Override
	public boolean isShapeless() {
		return false;
	}

	@Override
	public boolean usesOredict() {
		return false;
	}
	
	public void setOredicted(boolean o) {
		oredicted = o;
	}

	public void setShapeless(boolean s) {
		shapeless = s;
	}
}
