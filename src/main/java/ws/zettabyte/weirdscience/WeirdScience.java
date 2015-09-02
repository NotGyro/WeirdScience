package ws.zettabyte.weirdscience;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.MinecraftForge;
import ws.zettabyte.weirdscience.block.BlockSkullOverride;
import ws.zettabyte.weirdscience.fluid.FluidSmog;
import ws.zettabyte.weirdscience.gas.BlockGas;
import ws.zettabyte.weirdscience.gas.BlockGasExplosive;
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
@Mod(modid = WeirdScience.modid, name = WeirdScience.name, version = WeirdScience.version, dependencies = WeirdScience.dependencies)
public class WeirdScience {
	public static final String modid = "WeirdScience";
	public static final String name = "Weird Science";
	public static final String version = "Weird Science";
	public static final String dependencies = "";
	
    @Instance("WeirdScience")
    public static WeirdScience instance;
    
    @SidedProxy(clientSide="ws.zettabyte.weirdscience.client.ClientProxy", serverSide="ws.zettabyte.weirdscience.CommonProxy")
    public static CommonProxy proxy;
    
    //The logger for the mod. Should this not be static? Since it's Minecraft, it's unlikely that there will be threading issues.
    public static final Logger logger = Logger.getLogger("WeirdScience");
    
    public static CreativeTabs tabWeirdScience = new CreativeTabWeirdScience(CreativeTabs.getNextID(), "tabWeirdScience");
    
    public static ArrayList<String> sounds;// = CongealedBloodBlock.sounds;
    
    public static Configuration config;

    public static FluidSmog fluidSmog;
    public static BlockGasExplosive blockSmog;    
    
	//Important things to note: Values read from config and passed around don't reach their destination serverside unless
	//they are null. Weird.
	
	public WeirdScience() {
		instance = this;
        //logger.setParent((Logger) FMLLog.getLogger());
		logger.setLevel(Level.ALL);
	}
	
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
    	//logger.info("Testing.");
    	LanguageRegistry.instance().addStringLocalization("itemGroup.tabWeirdScience", "en_US", "Weird Science");    	
    	//NetworkRegistry.registerGuiHandler(this, new WeirdScienceGUIHandler());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
    	fluidSmog = (FluidSmog) new FluidSmog("Smog");
    	blockSmog = (BlockGasExplosive) new BlockGasExplosive(fluidSmog) {
            @SideOnly(Side.CLIENT)
            @Override
            public void registerBlockIcons(IIconRegister register) {
            	this.blockIcon = register.registerIcon("weirdscience:smog");
                
                fluidSmog.setIcons(this.blockIcon);
            }
    	};
    	
    	blockSmog.entitiesInteract = true;
    	blockSmog.isReactive = true;
    	blockSmog.explosionThreshhold = 6;
    	
    	blockSmog.setBlockTextureName("WeirdScience:smog");
    	
    	//blockSmog.setEntitiesInteract(true);
    	//TODO: Add actual acid.
    	//gasSmogBlock.setBlockAcid(fluidAcidBlock);
    	
    	//gasSmogBlock.setExplosionThreshhold(8);
    	
    	blockSmog.setHardness(0.5f);
        
        blockSmog.setBlockName("blockSmog");
        fluidSmog.setUnlocalizedName("smog");
	    GameRegistry.registerBlock(blockSmog, "blockSmog");
    	
    	
        config.save();
        
        proxy.registerRenderers();
        proxy.registerSound();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	blockSmog.setCreativeTab(tabWeirdScience);
    }
}
