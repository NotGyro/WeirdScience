package zettabyte.weirdscience;


import static java.lang.System.out;
import zettabyte.weirdscience.block.*;
import zettabyte.weirdscience.client.gui.WeirdScienceGUIHandler;
import zettabyte.weirdscience.fluid.BlockGasBase;
import zettabyte.weirdscience.item.MelonPan;
import zettabyte.weirdscience.tileentity.TileEntityBloodDonation;
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
import cpw.mods.fml.relauncher.SideOnly;
import mekanism.common.PacketHandler;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraft.item.ItemPotion;
//Import our own stuff.
import net.minecraft.util.Icon;

@Mod(modid="WeirdScience", name="Weird Science", version="0.0.0")
@NetworkMod(channels = {"WS"}, clientSideRequired = true, serverSideRequired = false)
public class WeirdScience {
    @Instance(value = "WeirdScience")
    public static WeirdScience instance;
    @SidedProxy(clientSide="zettabyte.weirdscience.client.ClientProxy", serverSide="zettabyte.weirdscience.CommonProxy")
    public static CommonProxy proxy;
    
    
    public static CreativeTabs tabWeirdScience = new CreativeTabWeirdScience(CreativeTabs.getNextID(), "tabWeirdScience");
    
    //TODO: Must find a better way to handle our own block registry.
    public static BlockPhosphateEngine phosphateEngine;
    public static BlockBloodDonation bloodDonation;
    
    public static Item melonPan;

    int idPhosphateEngine = 0;
    boolean enablePhosphateEngine = true;

    int idMelonPan = 0;
    boolean enableMelonPan = true;
    
    int idBloodDonation = 0;
    boolean enableBloodDonation = true;
    
    int idBucket = 0;
    
    public int fluidBloodID;
    public Fluid fluidBlood;
    public BlockFluidClassic fluidBloodBlock;
    
    public int idGasSmog;
    public Fluid fluidSmog;
    public BlockGasBase gasSmogBlock;
    
    @EventHandler // used in 1.6.2
    public void preInit(FMLPreInitializationEvent event) {
    	Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        //Enable/disable components of the mod.
        enablePhosphateEngine = config.get("Features", "Enable phosphate engine", true).getBoolean(true);
        enableBloodDonation = config.get("Features", "Enable blood donation machine", true).getBoolean(true);
        enableMelonPan = config.get("Features", "Enable melonpan", true, "If you disable Melonpan you're a horrible person.").getBoolean(true);
        //Manage misc. settings
        
        //Manage worldgen.
        
        //Retrieve block IDs
        //int TestID = config.get(Configuration.CATEGORY_BLOCK, "Single-block generator ID", 1014).getInt();
        idPhosphateEngine = config.getBlock("Phosphate engine ID", 3500, "In a later refactor, this will just be one sub-block of the machine block ID.").getInt();
        idMelonPan = config.getItem("Melonpan item ID", 4949).getInt();
        idBloodDonation = config.getBlock("Blood donation machine ID", 3501).getInt();
        idBucket = config.getItem("Weird Science fluid bucket ID", 4950).getInt();
        idGasSmog = config.getBlock("Smog block ID", 3600).getInt();

        //Initialize Phosphate Engine if it isn't hard-disabled.
    	if(idPhosphateEngine != 0) {
	    	phosphateEngine = (BlockPhosphateEngine)new BlockPhosphateEngine(idPhosphateEngine, Material.iron)
	        .setHardness(2.0F).setStepSound(Block.soundAnvilFootstep)
	        .setUnlocalizedName("phosphateEngine");
	    	phosphateEngine.setTextureName("weirdscience:retardcube");
	    	phosphateEngine.setCreativeTab(tabWeirdScience);

	    	int peEnergyPerTick = config.get("Phosphate Engine", "Max RF output per tick", 50).getInt();
	    	int peEnergyPerDirt = config.get("Phosphate Engine", "RF generated per dirt burned", 1).getInt();
	    	int maxStorage = config.get("Phosphate Engine", "Max internal storage of Phosphate Engine", 80).getInt();

	    	phosphateEngine.setCapacity(maxStorage);
	    	phosphateEngine.setEfficiency(peEnergyPerDirt);
	    	phosphateEngine.setOutputRate(peEnergyPerTick);
    	}
    	

    	//TODO: Config for this. Dunno how it works with liquids.
    	fluidBloodID = 1000;
    	fluidBlood = new Fluid("Blood").setBlockID(fluidBloodID);
        FluidRegistry.registerFluid(fluidBlood);
    	fluidBloodBlock = new BlockFluidClassic(fluidBloodID, fluidBlood, Material.water) {
    		//Things I didn't know Java could do.

            @SideOnly(Side.CLIENT)
            protected Icon stillIcon;
            @SideOnly(Side.CLIENT)
            protected Icon flowingIcon;
            
            @SideOnly(Side.CLIENT)
            @Override
            public void registerIcons(IconRegister register) {
                stillIcon = register.registerIcon("weirdscience:bloodStill");
                flowingIcon = register.registerIcon("weirdscience:bloodStill");
                
                fluidBlood.setIcons(stillIcon);
            }
            @Override
            public Icon getIcon(int side, int meta) {
                    return (side == 0 || side == 1)? stillIcon : flowingIcon;
            }
    	};
    	fluidBloodBlock.setCreativeTab(tabWeirdScience);
    	
    	if(idBloodDonation != 0) {
    		bloodDonation = (BlockBloodDonation)new BlockBloodDonation(idBloodDonation, Material.rock)
	        .setHardness(2.0F).setStepSound(Block.soundGlassFootstep)
	        .setUnlocalizedName("bloodDonationStation");
	    	bloodDonation.setCreativeTab(tabWeirdScience);

	    	int mbPerDonation = config.get("Blood", "Milibuckets per donation", 500).getInt();
	    	int dmgPerDonation = config.get("Blood", "Damage per donation", 2).getInt();
	    	int maxStorage = config.get("Blood", "Blood Donation Station max storage in milibuckets", 2000).getInt();

	    	bloodDonation.setDonationAmt(mbPerDonation);
	    	bloodDonation.setDamagePer(dmgPerDonation);
	    	bloodDonation.setStorageCap(maxStorage);
	    	bloodDonation.setFluid(fluidBlood);
    	}

    	fluidSmog = new Fluid("Smog").setBlockID(idGasSmog);
        FluidRegistry.registerFluid(fluidSmog);

        //TODO get good at understanding how "icon" differs from "texture" in this engine.
    	if(idGasSmog != 0) {
	    	gasSmogBlock = (BlockGasBase) new BlockGasBase(idGasSmog, fluidSmog, Material.snow){
	    		//Things I didn't know Java could do.
	            @SideOnly(Side.CLIENT)
	            @Override
	            public void registerIcons(IconRegister register) {
	            	this.blockIcon = register.registerIcon("weirdscience:retardcube");
	                
	                fluidSmog.setIcons(this.blockIcon);
	            }
	            @Override
	            public Icon getIcon(int side, int meta) {
	                    return this.blockIcon;
	            }
	    	};
	    	gasSmogBlock.setTextureName("weirdscience:retardcube");
	    	gasSmogBlock.setCreativeTab(tabWeirdScience);
	    	
	    	if(idPhosphateEngine != 0) {
	    		phosphateEngine.setWaste(gasSmogBlock);
	    	}
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

    	if(idBloodDonation != 0) {
		    MinecraftForge.setBlockHarvestLevel(bloodDonation, "pickaxe", 0);
	        LanguageRegistry.addName(bloodDonation, "Blood Donation Station");
		    GameRegistry.registerBlock(bloodDonation, "bloodDonationStation");
		    GameRegistry.registerTileEntity(TileEntityBloodDonation.class, "BloodDonation");
    	}
    	if(idGasSmog != 0) {
            gasSmogBlock.setUnlocalizedName("gasSmog");
            fluidSmog.setUnlocalizedName("gasSmog");
            GameRegistry.registerBlock(fluidBloodBlock, "Smog");
	        LanguageRegistry.addName(gasSmogBlock, "Smog");
		    GameRegistry.registerBlock(gasSmogBlock, "gasSmog");
    	}
	        
    	NetworkRegistry.instance().registerGuiHandler(this, new WeirdScienceGUIHandler());
        proxy.registerRenderers();
        
        // Register the all-consuming Melonpan
        melonPan = new MelonPan(idMelonPan);
        LanguageRegistry.addName(melonPan, "Melonpan");
        if(enableMelonPan) {
        	GameRegistry.addShapelessRecipe(new ItemStack(melonPan,MelonPan.craftCount), MelonPan.recipe);
        }
        
        GameRegistry.registerBlock(fluidBloodBlock, "Blood");
        LanguageRegistry.addName(fluidBloodBlock, "Blood");
        fluidBlood.setUnlocalizedName("Blood");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

}
