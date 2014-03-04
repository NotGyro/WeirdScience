package zettabyte.weirdscience.core.chemistry;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import zettabyte.weirdscience.core.baseclasses.ItemBase;

//TODO
//The class by which we make things by mixing them with water
//(which obviously itself isn't a reactive).
public class ReactiveItem extends ItemBase implements IReactionReceiver {

	protected ArrayList<IReactionSpec> Reactions = new ArrayList<IReactionSpec>(12);

	@Override
	public boolean registerReaction(IReactionSpec react) {
		if(react.getSolute() instanceof ItemStack) {
			if(((ItemStack)react.getSolute()).getUnlocalizedName().contentEquals(this.getUnlocalizedName((ItemStack)react.getSolute()))) {
				Reactions.add(react);
				return true;
			}
		}
		else if(react.getSolute() instanceof Item) {
			if(((Item)react.getSolute()).getUnlocalizedName().contentEquals(this.getUnlocalizedName())) {
				Reactions.add(react);
				return true;
			}
		}
		return false;
	}
	

    /**
     * Determines if this Item has a special entity for when they are in the world.
     * Is called when a EntityItem is spawned in the world, if true and Item#createCustomEntity
     * returns non null, the EntityItem will be destroyed and the new Entity will be added to the world.
     *
     * @param stack The current item stack
     * @return True of the item has a custom entity, If true, Item#createCustomEntity will be called
     */
    public boolean hasCustomEntity(ItemStack stack)
    {
        return false;
    }

    /**
     * This function should return a new entity to replace the dropped item.
     * Returning null here will not kill the EntityItem and will leave it to function normally.
     * Called when the item it placed in a world.
     *
     * @param world The world object
     * @param location The EntityItem object, useful for getting the position of the entity
     * @param itemstack The current item stack
     * @return A new Entity object to spawn or null
     */
    public Entity createEntity(World world, Entity location, ItemStack itemstack)
    {
        return null;
    }
    
	public ReactiveItem(Configuration config, String name, int defaultID) {
		super(config, name, defaultID);
		// TODO Auto-generated constructor stub
	}

	public ReactiveItem(Configuration config, String name) {
		super(config, name);
		// TODO Auto-generated constructor stub
	}

	public ReactiveItem(int id) {
		super(id);
		// TODO Auto-generated constructor stub
	}


}
