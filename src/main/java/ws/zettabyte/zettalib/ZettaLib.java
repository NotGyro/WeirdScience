package ws.zettabyte.zettalib;

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
import ws.zettabyte.zettalib.client.gui.widgets.WidgetAmountBar;
import ws.zettabyte.zettalib.initutils.ICreativeTabInfo;
import ws.zettabyte.zettalib.network.MessageTileIntComponent;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

//Packet handler is just a dummy as of this point.
@Mod(modid = ZettaLib.modid, name = ZettaLib.name, version = ZettaLib.version, dependencies = ZettaLib.dependencies)
public class ZettaLib {
	public static final String modid = "ZettaLib";
	public static final String name = "ZettaLib";
	public static final String version = "0.0.1";
	public static final String dependencies = "";
	
    @Instance("ZettaLib")
    public static ZettaLib instance;
    
    @SidedProxy(clientSide="ws.zettabyte.zettalib.client.ClientProxy", serverSide="ws.zettabyte.zettalib.CommonProxy")
    public static CommonProxy proxy;
    
    //The logger for the mod. Should this not be static? Since it's Minecraft, it's unlikely that there will be threading issues.
    public static final Logger logger = Logger.getLogger("ZettaLib");
    
    public static boolean enableStick = false;
    
    //Used for GUI packets and etc 
    public static SimpleNetworkWrapper network;
    
	public ZettaLib() {
		instance = this;
		logger.setLevel(Level.ALL);
	}
	
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	network = NetworkRegistry.INSTANCE.newSimpleChannel(this.modid);
    	network.registerMessage(MessageTileIntComponent.Handler.class, MessageTileIntComponent.class, MessageTileIntComponent.packetID, Side.SERVER);
	    Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

        /*WidgetAmountBar.forceDisableInterpolate = ! (
        		config.getBoolean("Enable GUI bar interpolation", "Client", true, "Allow GUI bars, such as fluid tanks and energy meters, to try to smooth out their own change over time. Disabling this may decrease lag inside of GUIs.")
        		);*/
        enableStick = config.getBoolean("Enable debug stick", "Dev", false, "Enable a stick that prints information about tiles to the console.");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
    	if(enableStick) {
    	ItemDebugStick stick = new ItemDebugStick();
    	stick.setUnlocalizedName("itemDebugStick");
    	stick.setTextureName("stick");
	    GameRegistry.registerItem(stick, "itemDebugStick");
	    stick.setCreativeTab(CreativeTabs.tabMisc);
    	}
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }
}
