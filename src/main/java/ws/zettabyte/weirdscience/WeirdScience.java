package ws.zettabyte.weirdscience;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import ws.zettabyte.weirdscience.block.BlockSkullOverride;
import ws.zettabyte.weirdscience.fluid.BlockAcid;
import ws.zettabyte.weirdscience.fluid.FluidAcid;
import ws.zettabyte.weirdscience.gas.BlockGas;
import ws.zettabyte.weirdscience.gas.BlockGasFlammable;
import ws.zettabyte.weirdscience.gas.FluidSmog;
import ws.zettabyte.weirdscience.machine.BlockCatalyticEngine;
import ws.zettabyte.weirdscience.machine.testheat.BlockHeatTest;
import ws.zettabyte.zettalib.BucketEventManager;
import ws.zettabyte.zettalib.block.BlockGeneric;
import ws.zettabyte.zettalib.initutils.Conf;
import ws.zettabyte.zettalib.initutils.ConfAnnotationParser;
import ws.zettabyte.zettalib.initutils.Configgable;
import ws.zettabyte.zettalib.initutils.InitUtils;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

//Packet handler is just a dummy as of this point.
@Configgable(section="test")
@Mod(modid = WeirdScience.modid, name = WeirdScience.name, version = WeirdScience.version, dependencies = WeirdScience.dependencies)
public class WeirdScience {
	public static final String modid = "WeirdScience";
	public static final String name = "Weird Science";
	public static final String version = "Weird Science";
	public static final String dependencies = "required-after:ZettaLib;";
	
    @Instance("WeirdScience")
    public static WeirdScience instance;
    
    @SidedProxy(clientSide="ws.zettabyte.weirdscience.client.ClientProxy", serverSide="ws.zettabyte.weirdscience.CommonProxy")
    public static CommonProxy proxy;
    
    protected static InitUtils iu;
    
    //The logger for the mod. Should this not be static? Since it's Minecraft, it's unlikely that there will be threading issues.
    public static final Logger logger = Logger.getLogger("WeirdScience");
    
    public static CreativeTabs tabWeirdScience = new CreativeTabWeirdScience(CreativeTabs.getNextID(), "tabWeirdScience");
    
    public static ArrayList<String> sounds;// = CongealedBloodBlock.sounds;
    
    public static Configuration config;

    public static FluidSmog fluidSmog;
    public static BlockGasFlammable blockSmog;
    
    public static FluidAcid fluidAcid;
    public static BlockAcid blockAcid;
    
    public static ItemBucket itemAcidBucket; //TODO: Eat through iron buckets, need non-reactive gold buckets.
    
    public static BlockGeneric blockRust;
    
    public static BlockCatalyticEngine blockCEngine;
    
    public static BlockHeatTest blockHeatTest;
    
    @Conf(name="test", comment="Beep boop.", def="9")
    public static int testConf = 0;
	//Important things to note: Values read from config and passed around don't reach their destination serverside unless
	//they are null. Weird.
	
	public WeirdScience() {
		instance = this;
        //logger.setParent((Logger) FMLLog.getLogger());
		logger.setLevel(Level.ALL);
	}
	BucketEventManager bucketMan = new BucketEventManager(); //My favorite superhero
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	//Do strange hacks.
    	//Block.blocksList[Block.skull.blockID] = null;
    	//Block.blocksList[Block.skull.blockID] = (new BlockSkullOverride(Block.skull.blockID))
    	//		.setHardness(1.0F).setStepSound(Block.soundStoneFootstep)
    	//		.setUnlocalizedName("skull").setTextureName("skull");
    	//Block.skull = Block.blocksList[Block.skull.blockID];
    	
    	//Get on with things.
    	config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        
        iu = new InitUtils();
        iu.config = config;
        iu.log = logger;
        iu.tab = tabWeirdScience;
        iu.modid = "weirdscience";
        
        ConfAnnotationParser anno = new ConfAnnotationParser(config);
        try {
			anno.parse(this.getClass());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        logger.warning("Important news: " + Integer.toString(this.testConf));
    	//logger.info("Testing.");
    	LanguageRegistry.instance().addStringLocalization("itemGroup.tabWeirdScience", "en_US", "Weird Science");    	
    	//NetworkRegistry.registerGuiHandler(this, new WeirdScienceGUIHandler());
    	
    	MinecraftForge.EVENT_BUS.register(bucketMan);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
    	fluidSmog = (FluidSmog) new FluidSmog("smog");
        fluidSmog.setUnlocalizedName("smog");
        FluidRegistry.registerFluid(fluidSmog);
        
	    fluidAcid = new FluidAcid("acid");
	    fluidAcid.setUnlocalizedName("acid");
        FluidRegistry.registerFluid(fluidAcid);
	   
        
    	blockSmog = (BlockGasFlammable) new BlockGasFlammable(fluidSmog);
    	blockSmog.entitiesInteract = true;
    	blockSmog.isReactive = true;
    	blockSmog.explosionThreshhold = 6;
        iu.initBlockConfig(blockSmog, "Smog").setHardness(0.5f);
        
    	fluidSmog.setBlock(blockSmog);
	    
    	
	    blockAcid = new BlockAcid(fluidAcid);
    	blockAcid.entitiesInteract = true;
    	blockAcid.isReactive = true;
        iu.initBlockConfig(blockAcid, "Acid").setBlockTextureName("weirdscience:placeholderacid");
        
    	fluidAcid.setBlock(blockAcid);

        blockRust = new BlockGeneric(Material.iron);
        iu.initBlockConfig(blockRust, "Rust").setHarvestLevel("pickaxe",0);
        blockRust.setHardness(0.6F);
        
        blockHeatTest = new BlockHeatTest(Material.iron);
        iu.initBlockConfig(blockHeatTest, "HeatTest").setHarvestLevel("pickaxe",0);
        blockHeatTest.setHardness(0.6F);
    	
	    itemAcidBucket = new ItemBucket(blockAcid);
	    FluidContainerRegistry.registerFluidContainer(fluidAcid, new ItemStack(itemAcidBucket), new ItemStack(Items.bucket));
	    iu.initItemConfig(itemAcidBucket, "Acid Bucket");
        
        Item ingotAluminum = new Item();
        OreDictionary.registerOre("ingotAluminum", ingotAluminum);
        OreDictionary.registerOre("ingotAluminium", ingotAluminum);
        iu.initItemConfig(ingotAluminum, "Aluminum Ingot");
        

        Item dustAluminum = new Item();
        OreDictionary.registerOre("dustAluminum", dustAluminum);
        OreDictionary.registerOre("dustAluminium", dustAluminum);
        iu.initItemConfig(dustAluminum, "Aluminum Dust");

        Item itemAshes = new Item();
        OreDictionary.registerOre("ashes", itemAshes);
        iu.initItemConfig(itemAshes, "Ashes");

        Item itemRust = new Item();
        iu.initItemConfig(itemRust, "Rust").setTextureName("weirdscience:rustPile");

        blockRust.setItemDropped(new ItemStack(itemRust, 6, 0));
        blockRust.setDroppedRandomBonus(3);

        blockCEngine = new BlockCatalyticEngine(Material.iron);
        iu.initBlockConfig(blockCEngine, "Catalytic Engine");
        
	    
        config.save();
        
        proxy.registerRenderers();
        proxy.registerSound();
        
        NetworkRegistry.INSTANCE.registerGuiHandler(this, instance.proxy);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	itemAcidBucket.setCreativeTab(tabWeirdScience);
    	bucketMan.addRecipe(blockAcid, new ItemStack(itemAcidBucket));
        fluidSmog.setIcons(blockSmog.getIcon(0, 0));
        fluidAcid.setIcons(blockAcid.getIcon(0, 0));
    }
}
