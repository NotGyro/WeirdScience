package ws.zettabyte.zettalib.block;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockGeneric extends Block {
	
	public String harvestType = "pickaxe";
	public int harvestLevel = 1;
	protected ItemStack itemDropped;
	protected int droppedRandomBonus;
	protected int quantityDropped;
	
	public BlockGeneric(Material material) {
		super(material);
	}

	
    public Material getMaterial() {
        return this.blockMaterial;
    }
    
    public void setMaterial(Material m) {
    	//Deep dark voodoo. If you get a security exception, here it is. I'm sorry, I did it all for the greater good.
    	Field field;
		try {
			//Get the field of the block class.
			field = Block.class.getField("blockMaterial");
	        field.setAccessible(true);
	
	        //Modify the field to not be final.
	        Field modifiersField = Field.class.getDeclaredField("modifiers");
	        modifiersField.setAccessible(true);
	        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
	
	        field.set(this, m);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
    }
	@Override
	public int quantityDropped(Random random) {
		// TODO Auto-generated method stub
		return this.quantityDroppedWithBonus(0, random);
	}	
	
	@Override
	public Item getItemDropped(int meta, Random random, int par3) {
		if(this.itemDropped != null) {
			return itemDropped.getItem();
		}
		else {
			return super.getItemDropped(meta, random, par3);
		}
	}


	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
			int metadata, int fortune) {
		// TODO Auto-generated method stub
		return super.getDrops(world, x, y, z, metadata, fortune);
	}


	@Override
	public int quantityDroppedWithBonus(int bonus, Random random) {
		if(this.itemDropped != null) {
			return itemDropped.stackSize + random.nextInt(bonus + droppedRandomBonus);
		}
		else {
			return super.quantityDroppedWithBonus(bonus+ droppedRandomBonus, random);
		}
	}
	@Override
	public int quantityDropped(int meta, int fortune, Random random) {
		return this.quantityDroppedWithBonus(fortune, random);
	}
	@Override
    public int damageDropped(int par1)
    {
		if(this.itemDropped != null) {
			return itemDropped.getItemDamage();
		}
		else {
			return super.damageDropped(par1);
		}
    }
	public ItemStack getItemDropped() {
		return itemDropped.copy();
	}
	public void setItemDropped(ItemStack itemDropped) {
		this.itemDropped = itemDropped;
	}
	public int getDroppedRandomBonus() {
		return droppedRandomBonus;
	}
	public void setDroppedRandomBonus(int droppedRandomBonus) {
		this.droppedRandomBonus = droppedRandomBonus;
	}

}