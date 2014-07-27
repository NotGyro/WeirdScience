package ws.zettabyte.weirdscience.core.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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
		this(r, rec, shapeless, true);
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
		return shapeless;
	}

	@Override
	public boolean usesOredict() {
		return oredicted;
	}
	
	public void setOredicted(boolean o) {
		oredicted = o;
	}

	public void setShapeless(boolean s) {
		shapeless = s;
	}
	
	//Dummy functionality:-----------------
    @Override
    public boolean matches (InventoryCrafting inventorycrafting, World world)
    {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public ItemStack getCraftingResult (InventoryCrafting inventorycrafting)
    {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public int getRecipeSize ()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public ItemStack getRecipeOutput ()
    {
        // TODO Auto-generated method stub
        return null;
    }
    //--------------------------------------
}
