package zettabyte.weirdscience;


import static java.lang.System.out;
import zettabyte.weirdscience.block.*;
import zettabyte.weirdscience.client.gui.WeirdScienceGUIHandler;
import zettabyte.weirdscience.fluid.BlockFluidClassicWS;
import zettabyte.weirdscience.fluid.BlockGasBase;
import zettabyte.weirdscience.fluid.BlockGasExplosive;
import zettabyte.weirdscience.fluid.BlockGasSmog;
import zettabyte.weirdscience.fluid.FluidAcid;
import zettabyte.weirdscience.fluid.FluidSmog;
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

    public Fluid fluidBlood;
    public Fluid fluidAcid;
    public BlockFluidClassic fluidBloodBlock;
    public BlockFluidClassicWS fluidAcidBlock;
    
    public int idGasSmog;
    public FluidSmog fluidSmog;
    public BlockGasSmog gasSmogBlock;
	private int idFluidBlood;
	private int idFluidAcid;
    
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
        idFluidBlood = config.getBlock("Blood block ID", 3601).getInt();
        idFluidAcid = config.getBlock("Acid block ID", 3602).getInt();

        //Initialize Phosphate Engine if it isn't hard-disabled.
    	if(idPhosphateEngine != 0) {
	    	phosphateEngine = (BlockPhosphateEngine)new BlockPhosphateEngine(idPhosphateEngine, Material.iron){
	            public void registerIcons(IconRegister register) {
			    	sidesIcon = register.registerIcon("weirdscience:genericmachine1");
			    	topandbottomIcon = register.registerIcon("weirdscience:genericmachine");
	            }
	    	};
	    	phosphateEngine.setHardness(2.0F).setStepSound(Block.soundAnvilFootstep).setUnlocalizedName("phosphateEngine");
	    	phosphateEngine.setTextureName("weirdscience:genericmachine");
	    	phosphateEngine.setCreativeTab(tabWeirdScience);

	    	int peEnergyPerTick = config.get("Phosphate Engine", "Max RF output per tick", 50).getInt();
	    	int peEnergyPerDirt = config.get("Phosphate Engine", "RF generated per dirt burned", 1).getInt();
	    	int maxStorage = config.get("Phosphate Engine", "Max internal storage of Phosphate Engine", 80).getInt();

	    	phosphateEngine.setCapacity(maxStorage);
	    	phosphateEngine.setEfficiency(peEnergyPerDirt);
	    	phosphateEngine.setOutputRate(peEnergyPerTick);
    	}
    	
    	fluidBlood = new Fluid("Blood").setBlockID(idFluidBlood);
        FluidRegistry.registerFluid(fluidBlood);
    	fluidBloodBlock = new BlockFluidClassic(idFluidBlood, fluidBlood, Material.water) {
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

    	fluidAcid = new FluidAcid("Acid").setBlockID(idFluidAcid);
        FluidRegistry.registerFluid(fluidAcid);
    	if(idFluidAcid != 0) {
	    	fluidAcidBlock = (BlockFluidClassicWS) new BlockFluidClassicWS(idFluidAcid, fluidAcid, Material.water){
	    		//Things I didn't know Java could do.
	            @SideOnly(Side.CLIENT)
	            @Override
	            public void registerIcons(IconRegister register) {
	            	this.blockIcon = register.registerIcon("weirdscience:placeholderacid");
	                
	                fluidAcid.setIcons(this.blockIcon);
	            }
	            @Override
	            public Icon getIcon(int side, int meta) {
	                    return this.blockIcon;
	            }
	    	};
	    	fluidAcidBlock.addDisplacementEntry(Block.waterStill.blockID, false);
	    	fluidAcidBlock.addDisplacementEntry(Block.waterMoving.blockID, false);
	    	fluidAcidBlock.setEntitiesInteract(true);
	    	fluidAcidBlock.setTextureName("weirdscience:placeholderacid");
	    	fluidAcidBlock.setCreativeTab(tabWeirdScience);
    	}

    	fluidSmog = (FluidSmog) new FluidSmog("Smog").setBlockID(idGasSmog);
        FluidRegistry.registerFluid(fluidSmog);

        //TODO get good at understanding how "icon" differs from "texture" in this engine.
    	if(idGasSmog != 0) {
	    	gasSmogBlock = (BlockGasSmog) new BlockGasSmog(idGasSmog, fluidSmog, Material.air){
	    		//Things I didn't know Java could do.
	            @SideOnly(Side.CLIENT)
	            @Override
	            public void registerIcons(IconRegister register) {
	            	this.blockIcon = register.registerIcon("weirdscience:smog");
	                
	                fluidSmog.setIcons(this.blockIcon);
	            }
	            @Override
	            public Icon getIcon(int side, int meta) {
	                    return this.blockIcon;
	            }
	    	};
	    	gasSmogBlock.setEntitiesInteract(true);
	    	gasSmogBlock.setTextureName("weirdscience:smog");
	    	gasSmogBlock.setCreativeTab(tabWeirdScience);
	    	//TODO: Add actual acid.
	    	gasSmogBlock.setBlockAcid(fluidAcidBlock);
	    	
	    	gasSmogBlock.setExplosionThreshhold(8);
	    	
	    	gasSmogBlock.setHardness(0.5f);
	    
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
	        LanguageRegistry.addName(gasSmogBlock, "Smog");
		    GameRegistry.registerBlock(gasSmogBlock, "gasSmog");
    	}

    	if(idFluidAcid != 0) {
    		fluidAcidBlock.setUnlocalizedName("fluidSmog");
            GameRegistry.registerBlock(fluidAcidBlock, "Acid");
            LanguageRegistry.addName(fluidAcidBlock, "Acid");
            fluidAcid.setUnlocalizedName("Acid");
    	}
	        
    	NetworkRegistry.instance().registerGuiHandler(this, new WeirdScienceGUIHandler());
        proxy.registerRenderers();
        
        // Register the all-consuming Melonpan
        melonPan = new MelonPan(idMelonPan);
        LanguageRegistry.addName(melonPan, "Melonpan");
        if(enableMelonPan) {
        	GameRegistry.addShapelessRecipe(new ItemStack(melonPan,MelonPan.craftCount), MelonPan.recipe);
        }
    	if((idPhosphateEngine != 0) && (idGasSmog != 0)) {
    		phosphateEngine.setWaste(gasSmogBlock);
    	}

        GameRegistry.registerBlock(fluidBloodBlock, "Blood");
        LanguageRegistry.addName(fluidBloodBlock, "Blood");
        fluidBlood.setUnlocalizedName("Blood");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

}
