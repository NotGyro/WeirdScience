package ws.zettabyte.weirdscience.core.recipe;

import java.util.ArrayList;

import net.minecraft.item.crafting.IRecipe;

public interface IRecipeProvider {
	ArrayList<IRecipe> getRecipes();
}
