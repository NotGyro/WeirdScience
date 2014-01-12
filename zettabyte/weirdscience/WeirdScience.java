package zettabyte.weirdscience;


import static java.lang.System.out;
import zettabyte.weirdscience.block.*;
import zettabyte.weirdscience.client.gui.WeirdScienceGUIHandler;
import zettabyte.weirdscience.item.MelonPan;
import zettabyte.weirdscience.tileentity.TileEntityPhosphateEngine;
//Basic Forge stuff.
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.item.ItemPotion;
//Import our own stuff.

@Mod(modid="WeirdScience", name="Weird Science", version="0.0.0")
@NetworkMod(clientSideRequired=true)
public class WeirdScience {
    @Instance(value = "WeirdScience")
    public static WeirdScience instance;
    @SidedProxy(clientSide="zettabyte.weirdscience.client.ClientProxy", serverSide="zettabyte.weirdscience.CommonProxy")
    public static CommonProxy proxy;
    
    
    public static CreativeTabs tabWeirdScience = new CreativeTabWeirdScience(CreativeTabs.getNextID(), "tabWeirdScience");
    
    //TODO: Must find a better way to handle our own block registry.
    public static BlockPhosphateEngine phosphateEngine;
    
    public static Item melonPan;

    int idPhosphateEngine = 0;
    boolean enablePhosphateEngine = true;
    

    int idMelonPan = 0;
    boolean enableMelonPan = true;
    
    @EventHandler // used in 1.6.2
    public void preInit(FMLPreInitializationEvent event) {
    	Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        //Enable/disable components of the mod.
        enablePhosphateEngine = config.get("Features", "Enable phosphate engine", true).getBoolean(true);
        enableMelonPan = config.get("Features", "Enable melonpan", true, "If you disable Melonpan you're a horrible person.").getBoolean(true);
        //Manage misc. settings
        
        //Manage worldgen.
        
        //Retrieve block IDs
        //int TestID = config.get(Configuration.CATEGORY_BLOCK, "Single-block generator ID", 1014).getInt();
        idPhosphateEngine = config.getBlock("Phosphate engine ID", 3500, "In a later refactor, this will just be one sub-block of the machine block ID.").getInt();
        idMelonPan = config.getItem("Melonpan item ID", 4949).getInt();
    
    	if(idPhosphateEngine != 0) {
	    	phosphateEngine = (BlockPhosphateEngine)new BlockPhosphateEngine(idPhosphateEngine, Material.iron)
	        .setHardness(2.0F).setStepSound(Block.soundAnvilFootstep)
	        .setUnlocalizedName("phosphateEngine");
	    	phosphateEngine.setTextureName("weirdscience:retardcube");
	    	phosphateEngine.setCreativeTab(tabWeirdScience);

	    	int peEnergyPerTick = config.get("Phosphate Engine", "Max RF output per tick", 50).getInt();
	    	int peEnergyPerDirt = config.get("Phosphate Engine", "RF generated per dirt burned", 20).getInt();
	    	int maxStorage = config.get("Phosphate Engine", "Max internal storage of Phosphate Engine", 80).getInt();

	    	phosphateEngine.setCapacity(maxStorage);
	    	phosphateEngine.setEfficiency(peEnergyPerDirt);
	    	phosphateEngine.setOutputRate(peEnergyPerTick);
    	}

        config.save();
    }
   
    @EventHandler
    public void load(FMLInitializationEvent event) {
    	LanguageRegistry.instance().addStringLocalization("itemGroup.tabWeirdScience", "en_US", "Weird Science");
    	
    	if(idPhosphateEngine != 0) {
	        MinecraftForge.setBlockHarvestLevel(phosphateEngine, "pickaxe", 0);
            LanguageRegistry.addName(phosphateEngine, "Phosphate Engine");
	        GameRegistry.registerBlock(phosphateEngine, "phosphateEngine");
	        GameRegistry.registerTileEntity(TileEntityPhosphateEngine.class, "PhosphateEngine");
    	}
    	NetworkRegistry.instance().registerGuiHandler(this, new WeirdScienceGUIHandler());
        proxy.registerRenderers();
        
        // Register the all-consuming Melonpan
        melonPan = new MelonPan(idMelonPan);
        LanguageRegistry.addName(melonPan, "Melonpan");
        GameRegistry.addShapelessRecipe(new ItemStack(melonPan,MelonPan.craftCount), MelonPan.recipe);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

}
