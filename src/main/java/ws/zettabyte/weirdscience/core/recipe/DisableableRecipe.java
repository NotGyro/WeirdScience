package ws.zettabyte.weirdscience.core.recipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import ws.zettabyte.weirdscience.core.ContentRegistry;
import ws.zettabyte.weirdscience.core.interfaces.IConfiggable;
import ws.zettabyte.weirdscience.core.interfaces.IRegistrable;

public class DisableableRecipe extends SimpleRecipe implements IConfiggable,
		IRegistrable {

	//protected String 
	protected boolean enabled = true;
	protected String itemName = "Unknown Item";
	
	//The real magic happens in this one.
	public DisableableRecipe(Item item, Object[] rec, boolean s, boolean o) {
		this(new ItemStack(item, 1), rec, s, o);
		if(item instanceof IRegistrable) {
			itemName = ((IRegistrable) item).getEnglishName();
		}
	}
	
	public DisableableRecipe(ItemStack r, Object[] rec, boolean s, boolean o) {
		super(r, rec, s, o);
		// TODO Auto-generated constructor stub
	}

	public DisableableRecipe(ItemStack r, Object[] rec, boolean shapeless) {
		super(r, rec, shapeless);
		// TODO Auto-generated constructor stub
	}

	public DisableableRecipe(ItemStack r, Object[] rec) {
		super(r, rec);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getEnglishName() {
		// TODO Auto-generated method stub
		return "Recipe for " + itemName;
	}

	@Override
	public String getGameRegistryName() {
		return "simplerecipe" + itemName.replace(" ", "");
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void doConfig(Configuration config, ContentRegistry cr) {
		enabled = config.get("Recipe", "Enable " + itemName + " recipe", enabled).getBoolean(enabled);
	}

}
