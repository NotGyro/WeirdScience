package ws.zettabyte.weirdscience.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import org.apache.commons.lang3.tuple.ImmutablePair;

import ws.zettabyte.weirdscience.core.baseclasses.ItemBucketWS;
import ws.zettabyte.weirdscience.core.chemistry.IReactionReceiver;
import ws.zettabyte.weirdscience.core.chemistry.IReactionSpec;
import ws.zettabyte.weirdscience.core.interfaces.IConfiggable;
import ws.zettabyte.weirdscience.core.interfaces.IDeferredInit;
import ws.zettabyte.weirdscience.core.interfaces.IRegistrable;
import ws.zettabyte.weirdscience.core.interfaces.ISoundProvider;
import ws.zettabyte.weirdscience.core.interfaces.ISubBucket;
import ws.zettabyte.weirdscience.core.interfaces.ISubItem;
import ws.zettabyte.weirdscience.core.interfaces.IWeirdScienceBlock;
import ws.zettabyte.weirdscience.core.interfaces.IWeirdScienceItem;
import ws.zettabyte.weirdscience.core.recipe.IRecipeProvider;
import ws.zettabyte.weirdscience.core.recipe.IWorkbenchRecipe;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
* Intended to wrap and hide all of the "tell Forge this object exists" sort of thing,
* including outgoing inter-mod interaction.
*
* Helps allow properties of blocks, items, etc... to be set declaratively in the class,
* rather than procedurally in some extremely bloated initialization method.
*
* Author: Gyro
*/

public class ContentRegistry implements IFuelHandler
{
    //My favorite superhero.
    public BucketEventManager bucketMan;
    
    private ArrayList<Item> itemsToRegister;
    private ArrayList<Block> blocksToRegister;
    private ArrayList<Fluid> fluidsToRegister;
    private ArrayList<TileEntity> tileentitiesToRegister;

    private ArrayList<IDeferredInit> initToDo;
    ArrayList<IRecipe> recipesToRegister;

    private ArrayList<IReactionReceiver> reactants;

    private Configuration config;

    public Logger logger;

    private CreativeTabs cTab;

    public static final String modID = "WeirdScience";

    public ArrayList<String> soundNames = new ArrayList<String>(24);

    //For Vanilla furnaces. (ItemID, DamageValue) to burn time.
    private HashMap<ImmutablePair<Item, Integer>, Integer> fuelDataSpecific = new HashMap<ImmutablePair<Item, Integer>, Integer>(10);
    //For Vanilla furnaces. Item ID to burn time, does not care about damage value.
    private HashMap<Item, Integer> fuelDataSimple = new HashMap<Item, Integer>(10);

    public ContentRegistry(Configuration setConfig, Logger setLogger, CreativeTabs setTab)
    {
        config = setConfig;
        logger = setLogger;
        cTab = setTab;

        bucketMan = new BucketEventManager();

        //Give it a large size hint.
        itemsToRegister = new ArrayList<Item>(64);
        blocksToRegister = new ArrayList<Block>(64);
        fluidsToRegister = new ArrayList<Fluid>(16);
        tileentitiesToRegister = new ArrayList<TileEntity>(32);
        initToDo = new ArrayList<IDeferredInit>(64);
        recipesToRegister = new ArrayList<IRecipe>(128);
        reactants = new ArrayList<IReactionReceiver>(8);
    }


    public void GeneralRegister (Object reg)
    {
        if (reg instanceof IDeferredInit)
        {
            initToDo.add((IDeferredInit) reg);
        }
        if (reg instanceof IConfiggable)
        {
            DoConfig((IConfiggable) reg);
        }
        //Don't just silently break if we're overwriting things.
        if (reg instanceof IRegistrable)
        {
            RegisterRegistrable((IRegistrable) reg);
        }
        if (reg instanceof IReactionReceiver)
        {
            RegisterReactionReceiver((IReactionReceiver) reg);
        }
    }

    public boolean RegisterRegistrable (IRegistrable reg)
    {
        //Don't just silently break if we're overwriting things.
        //if (registry.containsKey(reg.getGameRegistryName()))
        //{
        //    logger.severe("Duplicate Weird Science registry entry: " + reg.getGameRegistryName());
        //    return false;
        //}
        //else
        //{
        //    registry.put(reg.getGameRegistryName(), reg);
            return true;
        //}
    }

    public void RegisterSounds (ISoundProvider prov)
    {
        soundNames.addAll(prov.getSounds());
    }

    public void OverrideRegister (IRegistrable reg)
    {
        //registry.put(reg.getGameRegistryName(), reg);
    }

    public void DoConfig (IConfiggable obj)
    {
        obj.doConfig(config, this);
    }

    //Automatically calls RegisterRegistrable if the fluid is a registrable.
    public boolean RegisterFluid (Fluid fluid)
    {
        GeneralRegister(fluid);
        //fluidsToRegister.add(fluid);

        FluidRegistry.registerFluid(fluid);
        return true;
    }

    //Automatically calls RegisterRegistrable if the item is a registrable and RegisterSounds if ISoundProvider.
    public boolean RegisterItem (Item item)
    {
        GeneralRegister(item);
        if (item instanceof ISoundProvider)
        {
            RegisterSounds((ISoundProvider) item);
        }
        /* Set it up so that we'll handle any recipe classes for the item provided by the item.
        * NOTE: These classes must remain valid until finalization. These pointers to IRecipeProvider
        * classes are not used immediately, but instead recipe registration is deferred.
        */
        if (item instanceof IRecipeProvider)
        {
            ArrayList<IRecipe> rp = ((IRecipeProvider) item).getRecipes();
            for (int i = 0; i < rp.size(); ++i)
            {
                RegisterRecipe(rp.get(i));
            }
        }
        /*
        * Fluid Container / bucket stuff
        */
        if (item instanceof ItemBucketWS)
        {
            ItemBucketWS bucket = (ItemBucketWS) item;
            ISubBucket sb = null;
            ArrayList<ISubItem> subItems = bucket.getSubItems();
            if (subItems != null)
            {
                for (int i = 0; i < subItems.size(); i++)
                {
                    sb = (ISubBucket) subItems.get(i);
                    if (sb.getFluid() != null)
                    {
                        FluidContainerRegistry.registerFluidContainer(sb.getFluid(), new ItemStack(item, 1, sb.getAssociatedMeta()), new ItemStack(Items.bucket));
                    }
                    bucketMan.addRecipe(sb.getContained(), sb.getContainedMeta(), new ItemStack(bucket, 1, sb.getAssociatedMeta()));
                }
            }
        }

        itemsToRegister.add(item);
        return true;
    }

    //Automatically calls RegisterRegistrable if the block is a registrable and RegisterSounds if ISoundProvider.
    public boolean RegisterBlock (Block block)
    {
        GeneralRegister(block);
        if (block instanceof ISoundProvider)
        {
            RegisterSounds((ISoundProvider) block);
        }
        if (block instanceof ITileEntityProvider)
        {
            //Check to see if this provides us with associated tile entities.
            ITileEntityProvider ourBlock = (ITileEntityProvider) block;
            TileEntity addition = ourBlock.createNewTileEntity(null, 0);
            if (addition != null)
            {
                //Do configuration for configgable tile entities here.
                GeneralRegister(addition);
                tileentitiesToRegister.add(addition);
            }
        }
        /* Set it up so that we'll handle any recipe classes for the block provided by the block.
        * NOTE: These classes must remain valid until finalization. These pointers to IRecipeProvider
        * classes are not used immediately, but instead recipe registration is deferred.
        */
        if (block instanceof IRecipeProvider)
        {
            ArrayList<IRecipe> rp = ((IRecipeProvider) block).getRecipes();
            for (int i = 0; i < rp.size(); ++i)
            {
                RegisterRecipe(rp.get(i));
            }
        }
        blocksToRegister.add(block);
        return true;
    }

    //Automatically calls RegisterRegistrable if the block is a registrable.
    public boolean RegisterRecipe (IRecipe recipe)
    {
        GeneralRegister(recipe);
        recipesToRegister.add(recipe);
        return true;
    }

    //Tell the deferred init users that we're all set.
    public void DeferredInit ()
    {
        for (int i = 0; i < initToDo.size(); ++i)
        {
            initToDo.get(i).DeferredInit(this);
        }

        initToDo = null;
        GameRegistry.registerFuelHandler(this);
    }

    //Init the blocks that we've got to register after all of the config is done.
    protected void FinalizeBlocks ()
    {
        for (int i = 0; i < blocksToRegister.size(); ++i)
        {
            Block b = blocksToRegister.get(i);
            if (b instanceof IRegistrable)
            {
                IRegistrable reg = (IRegistrable) b;
                //LanguageRegistry.(b, reg.getEnglishName());
                GameRegistry.registerBlock(b, ItemBlock.class, reg.getGameRegistryName());//, modID);
            }
            if (b instanceof IWeirdScienceBlock)
            {
                IWeirdScienceBlock wsb = (IWeirdScienceBlock) b;
                //Do per-metadata things.
                if (wsb.InCreativeTab())
                {
                    b.setCreativeTab(cTab);
                }
                for (int j = 0; j <= 15; ++j)
                {
                    //MinecraftForge.setBlockHarvestLevel(b, j, wsb.getHarvestType(j), wsb.getHarvestLevel(j));
                }

                //ArrayList<ISubBlock> subBlocks = wsb.getSubBlocks();
                //if(subBlocks != null) {
                // for(ISubBlock sb : subBlocks) {
                // //Per-subblock operations
                // }
                //}
            }
        }
    }

    //Init the items that we have after all configuration is done.
    protected void FinalizeItems ()
    {
        for (int i = 0; i < itemsToRegister.size(); ++i)
        {
            Item item = itemsToRegister.get(i);
            if (item instanceof IRegistrable)
            {
                IRegistrable reg = (IRegistrable) item;
                LanguageRegistry.addName(item, reg.getEnglishName());
                GameRegistry.registerItem(item, reg.getGameRegistryName());//, modID);
            }
            if (item instanceof IWeirdScienceItem)
            {
                IWeirdScienceItem wsi = (IWeirdScienceItem) item;
                if (wsi.InCreativeTab())
                {
                    item.setCreativeTab(cTab);
                }
                ArrayList<ISubItem> subItems = wsi.getSubItems();
                if (subItems != null)
                {
                    for (ISubItem si : subItems)
                    {
                        //Per-subblock operations
                        GeneralRegister(si);
                        LanguageRegistry.addName(new ItemStack(item, 1, si.getAssociatedMeta()), si.getEnglishName());
                        //Fuel stuff.
                        if (si.getFurnaceFuelValue() > 0)
                        {
                            fuelDataSpecific.put(new ImmutablePair<Item, Integer>(item, si.getAssociatedMeta()), si.getFurnaceFuelValue());
                        }
                    }
                }
                //Fuel stuff for non-sub-item-users.
                else if (wsi.getFurnaceFuelValue() > 0)
                {
                    fuelDataSimple.put(item, wsi.getFurnaceFuelValue());
                }
            }

        }
    }

    //Init the items that we have after all configuration is done.
    protected void FinalizeFluids ()
    {
        //This doesn't work the way I thought it did.
        /*for(int i = 0; i < fluidsToRegister.size(); ++i) {
        Fluid fluid = fluidsToRegister.get(i);
        }*/
    }

    //Init the recipes that we have after all configuration is done.
    protected void FinalizeRecipes ()
    {
        for (int i = 0; i < recipesToRegister.size(); ++i)
        {
            if (recipesToRegister.get(i) instanceof IWorkbenchRecipe)
            {
                IWorkbenchRecipe recipe = (IWorkbenchRecipe) recipesToRegister.get(i);
                if (recipe.usesOredict())
                {
                    if (!recipe.isShapeless())
                    {
                        GameRegistry.addShapedRecipe(recipe.getResult(), recipe.get());
                    }
                    else
                    {
                        GameRegistry.addShapelessRecipe(recipe.getResult(), recipe.get());
                    }
                }
                else
                {
                    if (!recipe.isShapeless())
                    {
                        GameRegistry.addRecipe(new ShapedOreRecipe(recipe.getResult(), recipe.get()));
                    }
                    else
                    {
                        GameRegistry.addRecipe(new ShapelessOreRecipe(recipe.getResult(), recipe.get()));
                    }
                }
            }
            else
            {
                GameRegistry.addRecipe(recipesToRegister.get(i));
            }
        }
    }

    protected void FinalizeTileEntities ()
    {
        for (int i = 0; i < tileentitiesToRegister.size(); ++i)
        {
            TileEntity te = tileentitiesToRegister.get(i);
            if (te instanceof IRegistrable)
            {
                GameRegistry.registerTileEntity(te.getClass(), ((IRegistrable) te).getGameRegistryName());
            }
        }
    }

    //Registers everything we've been given so far with
    public void FinalizeContent ()
    {
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
        reactants = null;
    }

    //Chemistry stuff goes here:
    public void RegisterReactionReceiver (IReactionReceiver rec)
    {
        reactants.add(rec);
    }

    public void RegisterReaction (IReactionSpec reaction)
    {
        for (IReactionReceiver rec : reactants)
        {
            rec.registerReaction(reaction);
        }
    }

    @Override
    public int getBurnTime (ItemStack fuel)
    {
        if (fuelDataSimple.containsKey(fuel))
        {
            return fuelDataSimple.get(fuel);
        }
        ImmutablePair<Integer, Integer> itemDef = new ImmutablePair(fuel, fuel.getItemDamage());
        if (fuelDataSpecific.containsKey(itemDef))
        {
            return fuelDataSpecific.get(itemDef);
        }
        return 0;
    }
}