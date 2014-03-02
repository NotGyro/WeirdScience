package zettabyte.weirdscience.core.recipe;

import net.minecraft.item.ItemStack;

public interface IWorkbenchRecipe {
	//Get the result of this crafting recipe.
	ItemStack getResult();
	//Returns the recipe in the standard format.
	Object[] get();
	//If not, it's shaped.
	boolean isShapeless();
	boolean usesOredict();
}
