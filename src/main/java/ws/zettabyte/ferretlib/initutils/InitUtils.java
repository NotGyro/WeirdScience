package ws.zettabyte.ferretlib.initutils;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

import java.util.logging.Level;
import java.util.logging.Logger;

import ws.zettabyte.weirdscience.CreativeTabWeirdScience;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * A number of convenience functions to do common bits of Minecraft object initialization.
 * Intended to reduce boilerplate and copy-and-paste errors.
 * @author Samuel "Gyro" Cutlip 
 */
public final class InitUtils {
	
	public String modid; //Where to grab textures and such from.
	public Logger log;
	public Configuration config;
    public CreativeTabs tab;

    /**
	 * Registers a block with GameRegistry, sets its unlocalized name, calls .setBlockTextureName()
	 * with a default value derived from our name.
	 * 
	 * @param block The block to initialize.
	 * @param name An English or whatever your native language is name for the block.
	 * @return The block we're initializing. Guaranteed never to be null: If something went wrong,
	 * an exception will be thrown instead.
	 */
	public Block initBlock(Block block, String name) {
		String simpleName = name.replace(" ", "");
		//A default unlocalized name.
		block.setBlockName("block" + simpleName);
		block.setBlockTextureName(modid + ":" + simpleName);
	    GameRegistry.registerBlock(block, "block" + simpleName);
	    if(tab != null) {
	    	if(block instanceof ICreativeTabInfo) {
	    		if(((ICreativeTabInfo)block).isInCreativeTab()) {
		    		block.setCreativeTab(tab);
	    		}
	    	}
		    else {
	    		block.setCreativeTab(tab);
		    }
	    }
		return block;
	}
	
	/**
	 * The same as initBlock(Block block, String name), but attempts to run our block
	 * through config annotation parsing.
	 * 
	 * Registers a block with GameRegistry, sets its unlocalized name, calls .setBlockTextureName()
	 * with a default value derived from our name.
	 * 
	 * @param block The block to initialize.
	 * @param name An English or whatever your native language is name for the block.
	 * Used to derive a bunch of stuff like unlocalized names and a default texture.
	 * @return The block we're initializing. Guaranteed never to be null: If something went wrong,
	 * an exception will be thrown instead.
	 */
	public Block initBlockConfig(Block block, String name) {
		if(config == null) return initBlock(block, name);
		//Otherwise...
		try {
			(new ConfAnnotationParser(config)).parse(block.getClass());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initBlock(block, name);
		return block;
	}
    /**
	 * Registers an item with GameRegistry, sets its unlocalized name, calls .setTextureName()
	 * with a default value derived from our name.
	 * 
	 * TODO 
	 * 
	 * @param block The block to initialize.
	 * @param name An English or whatever your native language is name for the block.
	 * @return The block we're initializing. Guaranteed never to be null: If something went wrong,
	 * an exception will be thrown instead.
	 */
	public Item initItem(Item item, String name) {
		String simpleName = name.replace(" ", "");
		//A default unlocalized name.
		item.setUnlocalizedName("item" + simpleName);
		item.setTextureName(modid + ":" + simpleName);
	    GameRegistry.registerItem(item, "item" + simpleName);
	    if(tab != null) {
	    	if(item instanceof ICreativeTabInfo) {
	    		if(((ICreativeTabInfo)item).isInCreativeTab()) {
	    			item.setCreativeTab(tab);
	    		}
	    	}
		    else {
		    	item.setCreativeTab(tab);
		    }
	    }
		return item;
	}
	
	/**
	 * The same as initBlock(Block block, String name), but attempts to run our block
	 * through config annotation parsing.
	 * 
	 * Registers a block with GameRegistry, sets its unlocalized name, calls .setBlockTextureName()
	 * with a default value derived from our name (the word "block" is prepended, no spaces).
	 * 
	 * @param block The block to initialize.
	 * @param name An English or whatever your native language is name for the block.
	 * Used to derive a bunch of stuff like unlocalized names and a default texture.
	 * @return The block we're initializing. Guaranteed never to be null: If something went wrong,
	 * an exception will be thrown instead.
	 */
	public Item initItemConfig(Item item, String name) {
		if(config == null) return initItem(item, name);
		//Otherwise...
		try {
			(new ConfAnnotationParser(config)).parse(item.getClass());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initItem(item, name);
		return item;
	}
}

/*
 * You have no idea how many times I've done something like 
 * GameRegistry.register(blockSmog, "blockSmog");
 * GameRegistry.register(blockAcid, "blockSmog");
 */