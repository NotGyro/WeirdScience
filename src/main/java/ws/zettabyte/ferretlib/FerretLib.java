package ws.zettabyte.ferretlib;

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
import ws.zettabyte.weirdscience.gas.BlockGasExplosive;
import ws.zettabyte.weirdscience.gas.FluidSmog;
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
@Mod(modid = FerretLib.modid, name = FerretLib.name, version = FerretLib.version, dependencies = FerretLib.dependencies)
public class FerretLib {
	public static final String modid = "FerretLib";
	public static final String name = "Ferret Lib";
	public static final String version = "Ferret Lib";
	public static final String dependencies = "";
	
    @Instance("FerretLib")
    public static FerretLib instance;
    
    @SidedProxy(clientSide="ws.zettabyte.ferretlib.client.ClientProxy", serverSide="ws.zettabyte.ferretlib.CommonProxy")
    public static CommonProxy proxy;
    
    //The logger for the mod. Should this not be static? Since it's Minecraft, it's unlikely that there will be threading issues.
    public static final Logger logger = Logger.getLogger("FerretLib");
    
	public FerretLib() {
		instance = this;
		logger.setLevel(Level.ALL);
	}
	
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }
}
