package ws.zettabyte.weirdscience;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import ws.zettabyte.weirdscience.block.BlockSkullOverride;
import ws.zettabyte.weirdscience.client.gui.WeirdScienceGUIHandler;
import ws.zettabyte.zettalib.ContentRegistry;
import cpw.mods.fml.common.FMLLog;
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

//Packet handler is just a dummy as of this point.
@Mod(modid = WeirdScience.modid, name = "Weird Science", version = "0.0.0", dependencies = "required-after:ZettaLib")
@NetworkMod(channels = {"WS"}, clientSideRequired = true, serverSideRequired = false, packetHandler = ws.zettabyte.weirdscience.network.WeirdPacketHandler.class)
public class WeirdScience {
	public static final String modid = "weirdscience";
	
    @Instance("WeirdScience")
    public static WeirdScience instance;
    
    @SidedProxy(clientSide="ws.zettabyte.weirdscience.client.ClientProxy", serverSide="ws.zettabyte.weirdscience.CommonProxy")
    public static CommonProxy proxy;
    
    //The logger for the mod. Should this not be static? Since it's Minecraft, it's unlikely that there will be threading issues.
    public static final Logger logger = Logger.getLogger("WeirdScience");
    
    public static CreativeTabs tabWeirdScience = new CreativeTabWeirdScience(CreativeTabs.getNextID(), "tabWeirdScience");
    
    public static ArrayList<String> sounds;// = CongealedBloodBlock.sounds;
    
    public static Configuration config;
    
    public static ContentRegistry weirdRegistry;
    
	
	//Important things to note: Values read from config and passed around don't reach their destination serverside unless
	//they are null. Weird.
	
	public WeirdScience() {
		instance = this;
        logger.setParent(FMLLog.getLogger());
		logger.setLevel(Level.ALL);
	}
	
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	//Do strange hacks.
    	Block.blocksList[Block.skull.blockID] = null;
    	Block.blocksList[Block.skull.blockID] = (new BlockSkullOverride(Block.skull.blockID))
    			.setHardness(1.0F).setStepSound(Block.soundStoneFootstep)
    			.setUnlocalizedName("skull").setTextureName("skull");
    	//Block.skull = Block.blocksList[Block.skull.blockID];
    	
    	//Get on with things.
    	config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        weirdRegistry = new ContentRegistry(config, logger, tabWeirdScience);
        MinecraftForge.EVENT_BUS.register(weirdRegistry.bucketMan);
        WeirdScienceContent.RegisterContent(config, weirdRegistry, event);
    	//logger.info("Testing.");
    	LanguageRegistry.instance().addStringLocalization("itemGroup.tabWeirdScience", "en_US", "Weird Science");    	
    	NetworkRegistry.instance().registerGuiHandler(this, new WeirdScienceGUIHandler());
    }
   
    @EventHandler
    public void load(FMLInitializationEvent event) {
    	weirdRegistry.FinalizeContent(); //Do Forge Registry things.
        config.save();
        
        proxy.registerRenderers();
        this.sounds = weirdRegistry.soundNames; //This line MUST come before proxy.registerSound().
        proxy.registerSound();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	weirdRegistry.DeferredInit();
    }
}
