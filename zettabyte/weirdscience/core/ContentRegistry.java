package zettabyte.weirdscience.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import zettabyte.weirdscience.core.baseclasses.ItemBucketBase;
import zettabyte.weirdscience.core.interfaces.IConfiggable;
import zettabyte.weirdscience.core.interfaces.IDeferredInit;
import zettabyte.weirdscience.core.interfaces.IRegistrable;
import zettabyte.weirdscience.core.interfaces.ISoundProvider;
import zettabyte.weirdscience.core.interfaces.IWeirdScienceBlock;
import zettabyte.weirdscience.core.interfaces.IWeirdScienceItem;
import zettabyte.weirdscience.core.recipe.IRecipeProvider;
import zettabyte.weirdscience.core.recipe.IWorkbenchRecipe;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * Intended to wrap and hide all of the "tell Forge this object exists" sort of thing,
 * including outgoing inter-mod interaction.
 * 
 * Helps allow properties of blocks, items, etc... to be set declaratively in the class,
 * rather than procedurally in some extremely bloated initialization method.
 * 
 * An auxiliary usage, since virtually all Weird Science content passes through this class'
 * initialization methods, is to store and retrieve content class instances by name.
 * 
 * Author: Gyro
 */

public class ContentRegistry {
	//My favorite superhero.
	public BucketEventManager bucketMan;

	private HashMap<String, Object> registry;
	private ArrayList<Item> itemsToRegister;
	private ArrayList<Block> blocksToRegister;
	private ArrayList<Fluid> fluidsToRegister;
	private ArrayList<TileEntity> tileentitiesToRegister;
	
	private ArrayList<IDeferredInit> initToDo;
	ArrayList<IWorkbenchRecipe> recipesToRegister;
	
	private Configuration config;
	
	public Logger logger;
	
	private CreativeTabs cTab;
	
	public static final String modID = "WeirdScience";
	
	public ArrayList<String> soundNames = new ArrayList<String>(24);


	public ContentRegistry(Configuration setConfig, Logger setLogger, CreativeTabs setTab) {
		config = setConfig;
		logger = setLogger;
		cTab = setTab;
		
		bucketMan = new BucketEventManager();
		
		//Give it a large size hint.
		registry = new HashMap<String, Object>(128);
		itemsToRegister = new ArrayList<Item>(64);
		blocksToRegister = new ArrayList<Block>(64);
		fluidsToRegister = new ArrayList<Fluid>(16);
		tileentitiesToRegister = new ArrayList<TileEntity>(32);
		initToDo = new ArrayList<IDeferredInit>(64);
		recipesToRegister = new ArrayList<IWorkbenchRecipe>(128);
	}

	//Gets anything registered with this.
	public Object getObject(String str) {
		return registry.get(str);
	}
	
	
	public void GeneralRegister(Object reg) { 
		if(reg instanceof IDeferredInit) {
			initToDo.add((IDeferredInit)reg);
		}
		if(reg instanceof IConfiggable) {
			DoConfig((IConfiggable)reg);
		}
		//Don't just silently break if we're overwriting things.
		if(reg instanceof IRegistrable) {
			RegisterRegistrable((IRegistrable)reg);
		}
	}

	public boolean RegisterRegistrable(IRegistrable reg) {
		//Don't just silently break if we're overwriting things.
		if(registry.containsKey(reg.getGameRegistryName())) {
			logger.severe("Duplicate Weird Science registry entry: " + reg.getGameRegistryName());
			return false;
		}
		else {
			registry.put(reg.getGameRegistryName(), reg);
			return true;
		}
	}
	
	public void RegisterSounds(ISoundProvider prov) {
		soundNames.addAll(prov.getSounds());
	}
	
	public void OverrideRegister(IRegistrable reg) {
		registry.put(reg.getGameRegistryName(), reg);
	}
	public void DoConfig(IConfiggable obj) {
		obj.doConfig(config, this);
	}
	//Automatically calls RegisterRegistrable if the fluid is a registrable.
	public boolean RegisterFluid(Fluid fluid) {
		GeneralRegister(fluid);
		//fluidsToRegister.add(fluid);

		FluidRegistry.registerFluid(fluid);
		return true;
	}
	
	//Automatically calls RegisterRegistrable if the item is a registrable and RegisterSounds if ISoundProvider.
	public boolean RegisterItem(Item item) {
		GeneralRegister(item);
		if(item instanceof ISoundProvider) {
			RegisterSounds((ISoundProvider)item);
		}
		/* Set it up so that we'll handle any recipe classes for the item provided by the item.
		 * NOTE: These classes must remain valid until finalization. These pointers to IRecipeProvider
		 * classes are not used immediately, but instead recipe registration is deferred.
		 */
		if(item instanceof IRecipeProvider) {
			ArrayList<IWorkbenchRecipe> rp = ((IRecipeProvider)item).getRecipes();
			for(int i = 0; i < rp.size(); ++i) {
				RegisterRecipe(rp.get(i));
			}
		}
		/*
		 * Fluid Container / bucket stuff
		 */
		if(item instanceof ItemBucketBase) {
			ItemBucketBase bucket = (ItemBucketBase)item;
			if(bucket.getFluid() != null) {
				FluidContainerRegistry.registerFluidContainer(bucket.getFluid(), new ItemStack(item), new ItemStack(Item.bucketEmpty));
	        }
	        bucketMan.addRecipe(bucket.getContained(), new ItemStack(bucket, 1));
		}
		
		itemsToRegister.add(item);
		return true;
	}
	
	//Automatically calls RegisterRegistrable if the block is a registrable and RegisterSounds if ISoundProvider.
	public boolean RegisterBlock(Block block) {
		GeneralRegister(block);
		if(block instanceof ISoundProvider) {
			RegisterSounds((ISoundProvider)block);
		}
		if(block instanceof ITileEntityProvider) {
			//Check to see if this provides us with associated tile entities. 
			ITileEntityProvider ourBlock = (ITileEntityProvider)block;
			TileEntity addition = ourBlock.createNewTileEntity(null);
			if(addition != null) {
				//Do configuration for configgable tile entities here.
				GeneralRegister(addition);
				tileentitiesToRegister.add(addition);
			}
		}
		/* Set it up so that we'll handle any recipe classes for the block provided by the block.
		 * NOTE: These classes must remain valid until finalization. These pointers to IRecipeProvider
		 * classes are not used immediately, but instead recipe registration is deferred.
		 */
		if(block instanceof IRecipeProvider) {
			ArrayList<IWorkbenchRecipe> rp = ((IRecipeProvider)block).getRecipes();
			for(int i = 0; i < rp.size(); ++i) {
				RegisterRecipe(rp.get(i));
			}
		}
		blocksToRegister.add(block);
		return true;
	}

	//Automatically calls RegisterRegistrable if the block is a registrable.
	public boolean RegisterRecipe(IWorkbenchRecipe recipe) {
		GeneralRegister(recipe);
		recipesToRegister.add(recipe);
		return true;
	}
	//Tell the deferred init users that we're all set.
	public void DeferredInit() {
		for(int i = 0; i < initToDo.size(); ++i) {
			initToDo.get(i).DeferredInit(this);
		}

		initToDo = null;
	}
	
	//Init the blocks that we've got to register after all of the config is done.
	protected void FinalizeBlocks() {
		for(int i = 0; i < blocksToRegister.size(); ++i) {
			Block b = blocksToRegister.get(i);
			if(b instanceof IRegistrable) {
				IRegistrable reg = (IRegistrable)b;
				LanguageRegistry.addName(b, reg.getEnglishName());
				GameRegistry.registerBlock(b, ItemBlock.class, reg.getGameRegistryName());//, modID);
			}
			if(b instanceof IWeirdScienceBlock) {
				IWeirdScienceBlock wsb = (IWeirdScienceBlock)b;
				if(wsb.InCreativeTab()) {
					b.setCreativeTab(cTab);
				}
				//Get and set harvest levels for each metadata of the block.
				for(int j = 0; j <= 15; ++j) {
					MinecraftForge.setBlockHarvestLevel(b, j, wsb.getHarvestType(j), wsb.getHarvestLevel(j));
				}
			}
		}
	}

	//Init the items that we have after all configuration is done.
	protected void FinalizeItems() {
		for(int i = 0; i < itemsToRegister.size(); ++i) {
			Item item = itemsToRegister.get(i);
			if(item instanceof IRegistrable) {
				IRegistrable reg = (IRegistrable)item;
				LanguageRegistry.addName(item, reg.getEnglishName());
				GameRegistry.registerItem(item, reg.getGameRegistryName());//, modID);
			}
			if(item instanceof IWeirdScienceItem) {
				IWeirdScienceItem wsi = (IWeirdScienceItem)item;
				if(wsi.InCreativeTab()) {
					item.setCreativeTab(cTab);
				}
			}
		}
	}
	
	//Init the items that we have after all configuration is done.
	protected void FinalizeFluids() {
		//This doesn't work the way we thought it did.
		/*for(int i = 0; i < fluidsToRegister.size(); ++i) {
			Fluid fluid = fluidsToRegister.get(i);
		}*/
	}
	
	//Init the recipes that we have after all configuration is done.
	protected void FinalizeRecipes() {
		for(int i = 0; i < recipesToRegister.size(); ++i) {
			IWorkbenchRecipe recipe = recipesToRegister.get(i);
			if(recipe.usesOredict()) {
				if(recipe.isShapeless()) {
					GameRegistry.addShapedRecipe(recipe.getResult(), recipe.get());
				}
				else {
					GameRegistry.addShapelessRecipe(recipe.getResult(), recipe.get());
				}
			}
			else {
				if(recipe.isShapeless()) {
					GameRegistry.addRecipe(new ShapedOreRecipe(recipe.getResult(), recipe.get()));
				}
				else {
					GameRegistry.addRecipe(new ShapelessOreRecipe(recipe.getResult(), recipe.get()));
				}
			}
		}
	}
	protected void FinalizeTileEntities() {
		for(int i = 0; i < tileentitiesToRegister.size(); ++i) {
			TileEntity te = tileentitiesToRegister.get(i);
			if(te instanceof IRegistrable) {
				GameRegistry.registerTileEntity(te.getClass(), ((IRegistrable)te).getGameRegistryName());
			}
		}
	}
	//Registers everything we've been given so far with 
	public void FinalizeContent() {
		FinalizeBlocks();
		FinalizeItems();
		FinalizeRecipes();
		
		FinalizeTileEntities();
		
		//This is where you can tell I'm used to C++.
		itemsToRegister = null;
		blocksToRegister = null;
		fluidsToRegister = null;
		tileentitiesToRegister = null;
		recipesToRegister = null;
	}
}
